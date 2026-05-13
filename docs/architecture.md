# Android Template Architecture

本文档定义本模板工程的模块分层、依赖方向和代码放置规则。目标是让工程能按 Now in Android 的模块化思路演进，同时保留本模板对基础能力、系统能力和 App 级共享能力的边界设计。

## 分层目标

工程按 `app`、`feature`、`core`、`base` 四层组织：

- `base`：最底层、跨模块、低业务语义的基础能力。优先保持纯 Kotlin 或低 Android 依赖，不依赖 Android UI，不承载 App 业务语义。
- `core`：App 级共享能力。可以包含 Android 能力、UI 基础能力、数据基础设施、网络基础设施、App 级状态和跨 feature 的稳定抽象。
- `feature`：具体页面、业务流程和用户可见功能入口。feature 可以组合 core 能力，但不沉淀通用基础设施。
- `app`：尽量薄，只负责绑定整个代码库的组装层，包括 `Application`、`MainActivity`、顶层导航、依赖组装和启动入口。

## 依赖规则

默认依赖方向：

```text
app -> feature -> core -> base
```

约束规则：

- `base` 不能依赖 `core`、`feature`、`app`。
- `core` 不能依赖 `feature`、`app`。
- `feature` 不能依赖 `app`，也不直接依赖其他 feature。
- 同层模块默认不互相依赖；如果多个同层模块需要共享能力，优先下沉到更低层。
- 跨 feature 跳转由 `app` 的顶层导航协调，或通过 `core` 中的抽象状态和协议间接协作。
- 模块命名应表达职责，不表达实现细节；实现可以替换，模块边界应稳定。

## 模块职责

### app

`app` 是最终应用的装配层，应保持薄且直接：

- `Application`：初始化全局依赖、进程级能力和必要的启动任务。
- `MainActivity`：承接 Android 启动入口、edge-to-edge、顶层 Compose 容器。
- `NavHost` / 顶层导航：绑定 feature 入口，处理跨 feature 路由。
- 依赖组装：在没有 DI 框架时负责创建根依赖；引入 Hilt 后负责安装根组件。
- 全局启用入口：决定某个 core 能力或 feature 是否真正启用。

`app` 不应放具体业务页面、通用 UI 组件、数据仓库实现或工具类。

### base

`base` 放最底层的基础能力，适合被任何模块复用：

- `base:io`：纯 Kotlin 文件、路径、流处理工具。不依赖 Android `Context`，不决定 App 目录策略。
- `base:log`：日志基础能力，包括打印到 logcat、保存到文件、日志文件路径、文件命名、单文件大小、总日志大小等策略。日志的 UI 展示或 web 查看入口可以在更高层包装，底层写入和查询能力放在 base。
- `base:error`：错误类型、错误包装、错误格式化等低业务语义能力。
- `base:common`：纯 Kotlin 通用工具，例如时间、集合、字符串、纯 Kotlin 防抖器。
- `base:coroutine`：协程调度器、Flow 工具、协程错误处理等通用能力。
- `base:json`：JSON 序列化工具和基础适配。
- `base:system`：系统工具类。只放系统签名、系统应用、Root 权限或特殊设备环境下才能使用的能力，并在 API 命名和文档中明确权限前置条件。

`base` 应避免出现业务名词、页面概念、主题样式和 App 设置状态。

### core

`core` 放 App 级共享能力，是 feature 的主要依赖来源：

- `core:model`：跨层共享的数据模型。
- `core:data`：业务 Repository、数据聚合、具体业务 API、DTO 和数据来源协调。可以组合 `core:network`、`core:database`、`core:datastore`、`core:storage` 等底层数据来源。
- `core:network`：网络基础设施聚合模块。当前在模块内按协议分包，例如 `http`、后续可扩展 `tcp`、`udp`；复杂后再拆成 Gradle 子模块。
- `core:database`：数据库、DAO、实体和迁移。
- `core:datastore`：KV、Preferences、Proto DataStore 和轻量状态持久化。
- `core:storage`：Android 文件、目录、缓存、导入导出和下载文件。依赖 `base:io` 组合纯 Kotlin 文件工具和 Android `Context` 目录能力。
- `core:permissions`：权限检查、权限状态抽象、权限请求协议。
- `core:designsystem`：主题、颜色、字体、间距、组件尺寸、内容缩放、系统栏视觉策略等设计系统。
- `core:ui`：通用 UI 组件、Modifier、View 扩展、状态栏、焦点 UI。
- `core:settings`：用户设置状态，例如主题、语言、缩放、专家模式。
- `core:locale`：语言切换、Locale 解析和应用级语言状态。
- `core:adaptive`：竖屏、横屏、平板、折叠屏等窗口和设备形态判断。
- `core:input`：按键、遥控器、D-Pad、键盘输入抽象。
- `core:localserver`：本地服务、调试服务或本机通信能力。

`core` 可以包含 Android API 和 Compose 基础能力，但不能包含具体页面流程。只要一个能力描述的是“怎么展示某个具体页面”或“用户完成某个业务流程”，就应放到 `feature`。

### feature

`feature` 放具体用户功能：

- `feature:home`：首页页面和首页业务流程。
- `feature:settings`：设置页面本身，包括设置项列表、设置详情页和设置交互流程。
- 其他 feature 按用户可见的功能边界拆分。

feature 内部可以包含 screen、route、ViewModel、UI state 和该功能私有的 mapper。多个 feature 需要共享的组件、状态或模型，不能复制到每个 feature；应沉淀到 `core` 或 `base`。

## 放置决策表

| 内容 | 放置位置 | 原则 |
| --- | --- | --- |
| 打印日志、文件日志滚动策略 | `base:log` | 低业务语义，跨模块复用 |
| 日志查看页面 | `feature:*` 或调试 feature | 是具体页面和用户流程 |
| 通用错误包装 | `base:error` | 不依赖 App 语义 |
| 纯 Kotlin 文件、路径、流处理工具 | `base:io` | 不依赖 Android `Context`，不决定 App 目录策略 |
| Repository、业务 API、DTO 和数据聚合 | `core:data` | App 级共享数据能力，组合底层数据来源 |
| Retrofit、OkHttp、JSON 和 HTTP 拦截器 | `core:network` 的 `http` 包 | HTTP 基础设施，不包含业务接口 |
| TCP/UDP 连接、协议编解码和连接管理 | `core:network` 的 `tcp` / `udp` 包 | 有真实实现时再创建分包，复杂后再拆子模块 |
| 数据库和迁移 | `core:database` | App 级基础设施 |
| KV、Preferences、Proto DataStore、轻量状态持久化 | `core:datastore` | 持久化介质能力，不承载业务 Repository |
| Android 文件、目录、缓存、导入导出、下载文件 | `core:storage` | Android 文件存储基础设施，可组合 `base:io` |
| 主题、颜色、字体、间距、组件尺寸、系统栏视觉策略 | `core:designsystem` | 全 App 统一设计语言；字体、间距和组件尺寸统一受 AppScale 控制 |
| 通用 Compose 组件和 Modifier | `core:ui` | 多 feature 复用的 UI 能力 |
| 主题、语言、缩放、专家模式状态 | `core:settings` | UI 配置状态是 App 级共享状态 |
| 设置页面 | `feature:settings` | 页面和交互流程属于 feature |
| 设置是否启用、入口放在哪里 | `app` | 顶层组装和导航决策 |
| 语言切换能力 | `core:locale` | App 级共享能力 |
| 平板、横竖屏、折叠屏、WindowSizeClass 判断 | `core:adaptive` | App 级适配判断；具体页面布局放各 `feature` |
| 按键、遥控器、D-Pad 抽象 | `core:input` | 跨页面输入协议 |
| 系统签名或 Root 才能调用的工具 | `base:system` | 底层能力，但必须明确权限边界 |

## 持久化分层

持久化能力拆成“介质基础设施”和“业务数据仓库”两层：

| 层级 | 模块 | 职责 |
| --- | --- | --- |
| 纯 Kotlin 文件工具 | `base:io` | 文件、路径、流处理等 Context-free 工具 |
| KV 和轻量状态 | `core:datastore` | Preferences DataStore、Proto DataStore、轻量状态持久化 |
| 结构化数据库 | `core:database` | Room / SQLite、Entity、Dao、Migration |
| Android 文件存储 | `core:storage` | App 目录、缓存、导入导出、下载文件和 Android 文件策略 |
| 业务数据仓库 | `core:data` | Repository、业务 API、DTO、数据聚合，组合 network/database/datastore/storage |

调用边界：

- `feature:*` 只调用业务 Repository 或稳定的 core 抽象，不直接操作数据库、KV 或 Android 文件目录。
- `core:data` 负责把业务语义映射到底层数据来源，可组合 `core:network`、`core:database`、`core:datastore` 和 `core:storage`。
- `core:database`、`core:datastore`、`core:storage` 不放业务 Repository，不直接表达页面流程。
- `app` 只负责初始化、依赖注入和最终装配；不沉淀具体持久化 API 或业务仓库实现。
- 对外交付的 `sdk:*` 模块可以在 SDK 内部维护自己的 persistence 子层，避免把 SDK 的存储细节泄漏到宿主 App 的 `core:*` 边界。

KV 能力统一收敛到 `core:datastore`。历史上的 App 层 KV 入口不再保留；业务设置等持久化能力由对应 core 模块通过明确接口消费。

## UI 与设置分层

UI 相关能力按三类拆分：

- UI 能力放 `core:ui` 和 `core:designsystem`。
- UI 配置状态放 `core:settings`。
- UI 设置页面放 `feature:settings`。
- 真正启用入口、顶层导航和全局绑定放 `app`。

示例：主题切换能力的职责分配：

- `core:designsystem` 定义主题、颜色、字体、尺寸和内容缩放如何作用于 UI。
- `core:settings` 定义当前主题模式、AppScale、语言、缩放、专家模式等状态；当前主题和 AppScale 选择为运行期内存态，后续需要时再接持久化协议。
- `feature:settings` 展示设置页面，允许用户修改主题和 AppScale。
- `app` 读取设置状态，在顶层 `TemplateTheme` 或导航入口中真正启用。

## 生命周期状态

App 级状态按生命周期语义分为三类：

- `App`：应用进程级状态，例如主题、语言、全局配置、初始化完成状态。
- `Operator`：操作者环境状态，例如当前输入方式、权限状态、设备形态、调试开关。
- `User`：用户身份和偏好状态，例如用户设置、专家模式、个性化配置。

状态归属原则：

- 跨 feature 使用的状态放 `core`。
- 只服务单个页面流程的临时状态放对应 `feature`。
- 决定全局是否启用某能力的状态由 `app` 消费和绑定，但状态定义不放在 `app`。

## 当前工程状态与后续演进

当前已存在模块：

- `:app`：应用入口、顶层壳和 feature 组装。
- `:base:io`：纯 Kotlin 文件工具模块骨架。
- `:base:log`：日志基础模块起点。
- `:core:model`：共享模型。
- `:core:adaptive`：竖屏、横屏、折叠屏和 WindowSizeClass 响应式判断能力。
- `:core:data`：数据仓库抽象和当前静态数据实现。
- `:core:database`：Room / SQLite、Entity、Dao 和 Migration 边界骨架。
- `:core:datastore`：KV、Preferences、Proto DataStore 和轻量状态持久化边界。
- `:core:designsystem`：Compose 主题和设计系统起点。
- `:core:network`：网络基础设施聚合模块，当前 HTTP 能力放在 `http` 包下。
- `:core:settings`：用户设置状态起点，当前主题和 AppScale 选择为运行期内存态。
- `:core:storage`：Android 文件、目录、缓存、导入导出和下载文件边界骨架。
- `:feature:home`：首页 feature。
- `:feature:settings`：设置页面起点，当前提供主题切换。

建议后续按需求逐步补充具体实现，不提前创建业务 API 或示例实现：

- 需要通用 UI 组件时，新增 `:core:ui`。
- 主题和 AppScale 选择由 `:core:settings` 基于 `:core:datastore` 持久化。
- 需要语言切换能力时，新增 `:core:locale`。
- 具体页面布局策略由各 feature 自己实现，`core:adaptive` 只提供响应式判断能力。
- 需要 D-Pad、遥控器、键盘抽象时，新增 `:core:input`。
- 需要系统签名、系统应用或 Root 能力时，新增 `:base:system`，并为每个 API 标明权限前置条件和失败行为。
- `:core:database` 先不启用 Room 插件和依赖，等出现真实数据库 schema、DAO 或迁移需求时再接入。

`core:network` 演进规则：

- 当前保持单一 `:core:network` Gradle 模块，内部按协议分包：`http`、`tcp`、`udp`。
- `http` 放 Retrofit、OkHttp、kotlinx.serialization converter、HTTP 拦截器等能力。
- `tcp`、`udp` 只在出现真实 socket、协议编解码或连接管理需求时创建，不提前放空包。
- 任一协议能力变复杂、依赖变重或需要独立测试/发布时，再拆成 `:core:network:http`、`:core:network:tcp`、`:core:network:udp` 等子模块。
