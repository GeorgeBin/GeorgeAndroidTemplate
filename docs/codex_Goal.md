# GeorgeAndroidTemplate 架构升级 Goal 实施文档

> 适用对象：Codex Goal 模式  
> 适用工程：GeorgeAndroidTemplate  
> 目标：在不破坏现有工程可运行性的前提下，将当前 Android 模板工程逐步升级为“可配置启动流程 + 可插拔页面 + 可插拔业务模块 + 基础能力 SDK”的模块化模板工程。

---

## 0. 执行原则

### 0.1 总原则

本次任务不是一次性完成所有功能实现，而是按照阶段逐步完成架构落地。

每个阶段必须满足：

1. 工程可以编译。
2. 不破坏现有页面和已有设置能力。
3. 每次只做一个清晰目标。
4. 每个阶段完成后执行必要验证。
5. 每个阶段完成后进行 Git 提交。
6. 不在同一次提交中混合无关改动。
7. 不提交密钥、证书、local.properties、IDE 临时文件、构建产物。
8. 不主动 push 到远程仓库，除非用户明确要求。

### 0.2 Git 操作要求

开始前：

```bash
git status
git branch --show-current
```

如果当前工作区已有未提交改动：

1. 先查看改动内容。
2. 判断是否属于用户已有改动。
3. 不要覆盖用户已有改动。
4. 必要时先询问用户，或将当前任务限制在不冲突的文件内。

每个阶段完成后：

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
```

如果某个命令因为当前工程环境或依赖问题无法执行，需要记录原因，并尽量执行可用的替代命令，例如：

```bash
./gradlew :app:assembleDebug
./gradlew test
```

提交前：

```bash
git diff --stat
git diff
git status
```

提交格式：

```bash
git add <本阶段相关文件>
git commit -m "feat: xxx"
```

推荐提交信息：

```text
chore: add architecture goal docs
feat: add app config module skeleton
feat: add navigation architecture skeleton
feat: add startup coordinator skeleton
feat: add configurable main shell
feat: add privacy gate skeleton
feat: add permission gate skeleton
feat: add auth gate skeleton
feat: add common result and error models
feat: add time capability interfaces
feat: add timer and scheduler interfaces
feat: add feedback and notification skeleton
feat: add system capability skeleton
```

---

## 1. 最终架构目标

GeorgeAndroidTemplate 最终应成为：

```text
Android App 模板工程
+ 基础能力 SDK
+ 可配置启动流程
+ 可插拔页面
+ 可插拔业务模块
+ 可配置底部 Tab 主页面
+ 可选系统特权能力
```

最终技术架构：

```text
Single Activity
Jetpack Compose
Navigation Compose
Hilt
Repository
UDF / MVI-like UI State
Gradle Convention Plugins
base / core / feature / app 分层
AppConfig 驱动功能启用与页面显示
```

---

## 2. 分层规则

### 2.1 app 层

`app` 只负责应用组装。

允许放：

```text
Application
MainActivity
RootNavHost
TemplateApp
TemplateAppState
全局 Scaffold
全局 SnackbarHost
全局 DialogHost
Splash 入口
全局主题包裹
```

禁止放：

```text
Repository 具体实现
网络请求
数据库操作
业务规则
具体业务页面 UI
系统特权能力具体实现
```

### 2.2 base 层

`base` 放纯基础能力，尽量不依赖 Android 业务语义。

允许：

```text
Result / Error 基础模型
日志基础接口
时间格式化
IO 工具
Shell 执行基础封装
PCM/WAV 文件工具
Ping 工具
安全工具
```

禁止：

```text
页面
业务逻辑
Feature 配置
App 启动流程
具体 Android 页面跳转
```

### 2.3 core 层

`core` 放跨 feature 复用的 App 能力。

允许：

```text
AppConfig
StartupCoordinator
Navigation Route
Privacy 状态
Permission 状态
Auth 状态
Settings
Locale
Lifecycle
Time
Timer
Scheduler
Boot
Foreground Service
Update
Feedback
Notification
System Capability
Repository
Network
Database
Datastore
Storage
DesignSystem
UI 通用组件
```

禁止：

```text
具体业务页面
直接依赖 feature
直接依赖 app
```

### 2.4 feature 层

`feature` 放页面和业务功能闭环。

标准结构：

```text
feature/xxx
├─ XxxRoute.kt
├─ XxxScreen.kt
├─ XxxViewModel.kt
├─ XxxUiState.kt
├─ XxxUiEvent.kt
├─ XxxUiEffect.kt
├─ XxxNavigation.kt
└─ component/
```

规则：

```text
Route 负责连接 ViewModel 和 Screen
Screen 只负责展示
ViewModel 负责状态和事件
Navigation.kt 负责注册导航目的地
component 放 feature 私有组件
```

---

## 3. 目标模块结构

最终建议模块：

```text
app

base
├─ common
├─ time
├─ io
├─ log
├─ crash
├─ security
├─ shell
├─ media
└─ network-tool

core
├─ appconfig
├─ startup
├─ navigation
├─ privacy
├─ permission
├─ auth
├─ settings
├─ locale
├─ lifecycle
├─ time
├─ timer
├─ scheduler
├─ boot
├─ service
├─ update
├─ feedback
├─ notification
├─ system
├─ data
├─ network
├─ database
├─ datastore
├─ storage
├─ model
├─ domain
├─ designsystem
├─ ui
├─ adaptive
└─ testing

feature
├─ privacy
├─ permission
├─ auth
├─ main
├─ settings
├─ home
├─ diagnostics
├─ update
├─ time-debug
├─ system-debug
└─ business-xxx
```

注意：

1. 不要一次性实现所有模块的完整功能。
2. 可以先创建空模块和接口。
3. 每个模块必须有清晰职责。
4. 不要创建巨大的 `utils` 模块。

---

## 4. 依赖方向

最终依赖方向：

```text
app
 ↓
feature
 ↓
core
 ↓
base
```

允许：

```text
app -> feature
app -> core
app -> base
feature -> core
feature -> base
core -> base
```

禁止：

```text
base -> core
base -> feature
base -> app
core -> feature
core -> app
feature -> app
feature:home -> feature:settings
feature:main 直接写死 HomeScreen
ViewModel -> Retrofit API
ViewModel -> Room Dao
Composable -> Repository
Composable -> DataStore
```

---

## 5. 启动流程目标

Splash 后不直接进入首页，而是进入启动流程编排。

目标流程：

```text
System Splash
  ↓
StartupRoute / StartupCoordinator
  ↓
隐私协议检查
  ↓
权限总览 / 权限申请
  ↓
登录检查
  ↓
MainShell 主页面
```

判断顺序：

```text
1. 是否需要展示隐私协议？
   - 第一次启动
   - 隐私协议版本更新
   - 用户协议版本更新

2. 是否需要展示权限总览？
   - 第一次启动
   - 新版本新增关键权限
   - 配置要求强制展示

3. 是否需要申请必要权限？
   - 必要权限未授予

4. 是否需要登录？
   - 当前 App 要求登录
   - token/session 不存在
   - token/session 过期

5. 进入主页面
```

核心模型建议：

```kotlin
sealed interface StartupDestination {
    data object Privacy : StartupDestination
    data object PermissionOverview : StartupDestination
    data object Login : StartupDestination
    data object Main : StartupDestination
}
```

核心接口建议：

```kotlin
interface StartupCoordinator {
    suspend fun resolveDestination(): StartupDestination
}
```

---

## 6. AppConfig 目标

新增 `core:appconfig`。

职责：

```text
描述当前 App 包含哪些功能
描述启动流程是否启用
描述是否需要隐私协议
描述是否需要权限页
描述是否需要登录
描述主页面有哪些 Tab
描述设置页显示哪些入口
描述业务模块是否启用
```

核心模型建议：

```kotlin
data class AppConfig(
    val privacy: PrivacyFeatureConfig,
    val permission: PermissionFeatureConfig,
    val auth: AuthFeatureConfig,
    val main: MainFeatureConfig,
    val settings: SettingsFeatureConfig,
    val business: Map<String, BusinessFeatureConfig>,
)
```

```kotlin
data class PrivacyFeatureConfig(
    val enabled: Boolean = true,
    val privacyVersion: Int = 1,
    val userAgreementVersion: Int = 1,
)
```

```kotlin
data class PermissionFeatureConfig(
    val enabled: Boolean = true,
    val showOverviewOnFirstLaunch: Boolean = true,
)
```

```kotlin
data class AuthFeatureConfig(
    val enabled: Boolean = true,
    val required: Boolean = true,
    val allowGuestMode: Boolean = false,
)
```

```kotlin
data class MainFeatureConfig(
    val enabled: Boolean = true,
    val tabs: List<TabConfig>,
)
```

```kotlin
data class SettingsFeatureConfig(
    val enabled: Boolean = true,
    val showLanguage: Boolean = true,
    val showTheme: Boolean = true,
    val showAppScale: Boolean = true,
    val showPageOrientation: Boolean = true,
    val showExpertMode: Boolean = true,
    val showPermission: Boolean = true,
    val showPrivacy: Boolean = true,
    val showAbout: Boolean = true,
    val showLogout: Boolean = true,
)
```

```kotlin
data class BusinessFeatureConfig(
    val featureKey: String,
    val enabled: Boolean,
    val requiresLogin: Boolean = false,
    val requiredPermissions: List<AppPermission> = emptyList(),
)
```

---

## 7. Navigation 目标

当前手写页面切换应逐步替换为 Navigation Compose。

目标路由层级：

```text
RootNavHost
├─ StartupGraph
│  ├─ PrivacyRoute
│  ├─ PermissionOverviewRoute
│  ├─ PermissionRequestRoute
│  └─ LoginRoute
│
└─ MainGraph
   └─ MainShellRoute
      ├─ Tab: Home
      ├─ Tab: Message / Workbench / Business
      ├─ Tab: Settings
      └─ 其他可配置 Tab
```

新增 `core:navigation`：

```text
core:navigation
├─ AppRoute.kt
├─ RootRoute.kt
├─ StartupRoute.kt
├─ MainRoute.kt
├─ TabDestination.kt
├─ TopLevelDestination.kt
└─ NavigationExtensions.kt
```

每个 feature 提供自己的导航注册函数：

```kotlin
fun NavGraphBuilder.xxxScreen(...) {
    composable<XxxRoute> {
        XxxRoute(...)
    }
}
```

---

## 8. MainShell 目标

新增 `feature:main`。

职责：

```text
底部 Tab 主壳
根据 AppConfig 渲染 Tab
支持 2~5 个 Tab
默认推荐 4 个 Tab
Tab 顺序可配置
Tab 是否显示可配置
Tab 内容可替换
```

Tab 模型建议：

```kotlin
data class TabConfig(
    val route: String,
    val title: UiText,
    val icon: AppIcon,
    val order: Int,
    val visible: Boolean = true,
    val requiresLogin: Boolean = false,
    val requiredPermissions: List<AppPermission> = emptyList(),
)
```

默认 Tab 示例：

```text
首页 Home
消息 Message
工作台 Workbench
设置 Settings
```

但具体 App 可以配置为：

```text
对讲
联系人
记录
设置
```

或：

```text
设备
视频老化
日志
设置
```

---

## 9. 隐私协议目标

新增：

```text
core:privacy
feature:privacy
```

`core:privacy` 职责：

```text
保存用户协议版本
保存隐私协议版本
判断是否需要重新展示
记录同意时间
记录同意来源
提供 PrivacyRepository
```

`feature:privacy` 职责：

```text
隐私政策页面
用户协议页面
协议更新说明
同意 / 不同意
不同意退出 App
```

核心接口建议：

```kotlin
interface PrivacyRepository {
    suspend fun shouldShowPrivacy(): Boolean
    suspend fun acceptPrivacy(
        privacyVersion: Int,
        userAgreementVersion: Int,
    ): AppResult<Unit>
}
```

---

## 10. 权限目标

新增：

```text
core:permission
feature:permission
```

支持：

```text
普通运行时权限
特殊权限
权限总览
权限申请
权限状态检查
权限用途说明
跳转系统设置
```

权限类型建议：

```kotlin
sealed interface AppPermission {
    data class RuntimePermission(
        val androidPermission: String,
    ) : AppPermission

    data object Notification : AppPermission
    data object Overlay : AppPermission
    data object Accessibility : AppPermission
    data object IgnoreBatteryOptimization : AppPermission
    data object InstallUnknownApps : AppPermission
    data object ExactAlarm : AppPermission
    data object UsageStats : AppPermission
    data object WriteSettings : AppPermission
}
```

权限声明建议：

```kotlin
data class AppPermissionDeclaration(
    val permission: AppPermission,
    val title: UiText,
    val description: UiText,
    val required: Boolean,
    val requestTiming: PermissionRequestTiming,
)
```

申请时机：

```kotlin
enum class PermissionRequestTiming {
    OnStartup,
    BeforeLogin,
    AfterLogin,
    OnFeatureUse,
    ManualOnly,
}
```

---

## 11. 登录目标

新增：

```text
core:auth
feature:auth
```

`core:auth` 职责：

```text
登录态
Token / Session
账号信息
登录 / 登出
登录过期
游客模式
```

`feature:auth` 职责：

```text
登录页面
登录表单
登录中状态
登录错误提示
登录成功跳转
```

核心接口建议：

```kotlin
interface AuthRepository {
    fun observeAuthState(): StateFlow<AuthState>
    suspend fun isLoggedIn(): Boolean
    suspend fun login(account: String, password: String): AppResult<Unit>
    suspend fun logout(): AppResult<Unit>
}
```

---

## 12. 设置目标

设置页应支持配置显示：

```text
语言设置
主题设置
字号设置
页面方向
专家模式
权限管理
隐私协议入口
关于 App
退出登录
重启 App
```

要求：

```text
设置页不直接操作 DataStore
设置页不直接操作 AppCompatDelegate
设置页通过 ViewModel -> Repository / UseCase 操作
```

---

## 13. 时间能力目标

新增：

```text
core:time
```

保留并完善：

```text
base:time
```

功能：

```text
获取已开机时长
获取系统时间
获取校准后时间
接收时分秒变化
接收时间变化
接收日期变化
接收时区变化
NTP 时间校准
HTTP 时间校准
私有接口时间校准
维护校准偏移量
```

核心接口建议：

```kotlin
interface AppTimeRepository {
    fun observeTimeState(): StateFlow<AppTimeState>
    fun currentSystemTimeMillis(): Long
    fun currentElapsedRealtimeMillis(): Long
    fun currentCalibratedTimeMillis(): Long
    suspend fun calibrate(strategy: TimeCalibrationStrategy): AppResult<TimeCalibrationResult>
}
```

---

## 14. 定时器与调度目标

新增：

```text
core:timer
core:scheduler
```

`core:timer`：进程内共享定时器。

需求：

```text
同一时长使用同一个实例
有观察者时启动
无观察者时取消
支持不同时间提供者
```

接口建议：

```kotlin
interface SharedTimerManager {
    fun ticker(intervalMillis: Long): Flow<TimerTick>
}
```

`core:scheduler`：系统级定时任务。

支持：

```text
AlarmManager 间隔定时
AlarmManager 指定时间点定时
一次性定时
重复定时
取消定时
开机后恢复
```

接口建议：

```kotlin
interface AppScheduler {
    fun scheduleOnce(request: ScheduleRequest): AppResult<Unit>
    fun scheduleRepeating(request: RepeatingScheduleRequest): AppResult<Unit>
    fun cancel(scheduleId: String): AppResult<Unit>
}
```

---

## 15. 常用功能目标

新增：

```text
core:boot
core:service
core:update
```

### 15.1 开机启动

`core:boot` 支持：

```text
BOOT_COMPLETED
QUICKBOOT_POWERON
厂商开机广播
恢复 AlarmManager 任务
记录启动来源
```

### 15.2 前台服务

`core:service` 支持：

```text
前台服务启动 / 停止
前台服务通知
服务状态监听
服务原因记录
```

### 15.3 在线升级

`core:update` + `feature:update` 支持：

```text
检查版本
下载 APK
校验 APK
升级弹窗
下载进度
安装 APK
强制升级
可选升级
```

---

## 16. 常用工具目标

新增：

```text
base:shell
base:media
base:network-tool
core:feedback
core:notification
```

### 16.1 Shell

```kotlin
interface ShellExecutor {
    suspend fun execute(command: String): ShellResult
}
```

### 16.2 保存流到文件

放在：

```text
base:io
core:storage
```

### 16.3 保存 PCM 为 WAV

放在：

```text
base:media
```

```kotlin
object WavWriter {
    fun writePcmAsWav(
        pcmFile: File,
        wavFile: File,
        sampleRate: Int,
        channelCount: Int,
        bitsPerSample: Int,
    ): AppResult<File>
}
```

### 16.4 Ping 工具

放在：

```text
base:network-tool
```

支持：

```text
ShellPingTool
SocketPingTool
HttpPingTool
```

### 16.5 提示、弹窗、Toast、通知

放在：

```text
core:feedback
core:notification
```

---

## 17. 系统特权能力目标

新增：

```text
core:system
feature:system-debug
```

必须隔离系统特权能力。

支持：

```text
APK 静默安装
自动授予普通权限
自动授予特殊权限
自动打开无障碍
自动加入省电白名单
Root 命令执行
系统签名 API 调用
```

能力检测模型建议：

```kotlin
data class SystemCapabilities(
    val isSystemApp: Boolean,
    val hasPlatformSignature: Boolean,
    val hasRoot: Boolean,
    val canSilentInstall: Boolean,
    val canGrantPermission: Boolean,
    val canModifyAccessibility: Boolean,
    val canModifyBatteryOptimization: Boolean,
)
```

规则：

```text
普通 App 不应依赖系统特权实现
所有系统能力调用前必须做 capability 检查
没有权限时返回明确错误
不要让系统特权能力污染普通模块
```

---

## 18. 生命周期目标

新增：

```text
core:lifecycle
```

支持：

```text
应用前台状态监听
应用后台状态监听
Activity 数量监听
进程生命周期监听
App 启动来源记录
```

接口建议：

```kotlin
interface AppLifecycleObserver {
    fun observeAppForegroundState(): StateFlow<AppForegroundState>
    fun observeAppStartSource(): StateFlow<AppStartSource>
}
```

---

## 19. UI 能力目标

继续使用并完善：

```text
core:designsystem
core:ui
core:adaptive
```

### 19.1 core:designsystem

放：

```text
颜色
主题
Typography
Shape
Spacing
Elevation
Icon
动画规范
尺寸规范
```

### 19.2 core:ui

放：

```text
GroupList
TopBar
Tab
Pager
列表 + 搜索框
树状 + 搜索框
吸顶列表
吸顶标题栏
跑马灯文字
TipHost
DialogHost
ToastBridge
Empty / Error / Loading 页面
StatusPage
FormPage
DetailPage
```

### 19.3 跑马灯文字

支持：

```text
自动跑
获取焦点才跑
文字溢出才跑
循环次数
速度配置
延迟配置
```

---

## 20. 实施阶段

### 阶段 1：架构文档与空模块骨架

目标：

```text
建立架构文档
新增必要空模块
确保 settings.gradle.kts 中模块结构稳定
不实现复杂功能
```

建议新增：

```text
base:common
base:shell
base:media
base:network-tool

core:appconfig
core:startup
core:navigation
core:privacy
core:permission
core:auth
core:lifecycle
core:time
core:timer
core:scheduler
core:boot
core:service
core:update
core:feedback
core:notification
core:system
core:domain
core:testing

feature:privacy
feature:permission
feature:auth
feature:main
feature:update
feature:diagnostics
feature:time-debug
feature:system-debug
```

验收：

```bash
./gradlew projects
./gradlew assembleDebug
```

提交：

```bash
git add .
git commit -m "chore: add architecture module skeleton"
```

---

### 阶段 2：base:common 基础模型

目标：

```text
提供全工程共用的 Result、Error、UiText、Dispatcher 基础模型
```

实现：

```text
AppResult<T>
AppError
UiText
CoroutineDispatchers
safeCall
LoadableState
Initializable
Destroyable
ComponentState
```

验收：

```bash
./gradlew :base:common:testDebugUnitTest
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add common result and state models"
```

---

### 阶段 3：AppConfig 配置中心

目标：

```text
新增 AppConfig 模型
提供默认配置
后续启动流程、Tab、页面显示全部由 AppConfig 驱动
```

实现：

```text
AppConfig
PrivacyFeatureConfig
PermissionFeatureConfig
AuthFeatureConfig
MainFeatureConfig
SettingsFeatureConfig
BusinessFeatureConfig
TabConfig
AppConfigProvider
DefaultAppConfigProvider
```

验收：

```bash
./gradlew :core:appconfig:testDebugUnitTest
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add app config foundation"
```

---

### 阶段 4：Navigation Compose 架构骨架

目标：

```text
引入 Navigation Compose
新增 core:navigation
建立 RootNavHost、StartupGraph、MainGraph 的基础结构
暂时可以保留现有页面内容
```

实现：

```text
添加 navigation-compose 依赖
RootRoute
StartupRoute
MainRoute
TabDestination
TopLevelDestination
TemplateAppState
TemplateNavHost
```

注意：

```text
不要一次性重写所有页面
先保证现有 Home / Settings 可以通过 NavHost 打开
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add navigation compose foundation"
```

---

### 阶段 5：MainShell 可配置底部 Tab

目标：

```text
新增 feature:main
实现根据 AppConfig 渲染底部 Tab
支持 Tab 数量、顺序、显示隐藏配置
```

实现：

```text
MainShellRoute
MainShellScreen
MainShellViewModel
MainTabState
MainNavigation
```

默认先支持：

```text
Home
Settings
```

然后扩展为默认四 Tab：

```text
Home
Message Placeholder
Workbench Placeholder
Settings
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add configurable main shell tabs"
```

---

### 阶段 6：StartupCoordinator 启动编排

目标：

```text
Splash 后进入 StartupRoute
StartupCoordinator 根据状态决定进入 Privacy、Permission、Login、Main
```

实现：

```text
StartupDestination
StartupCoordinator
DefaultStartupCoordinator
StartupRoute
StartupViewModel
StartupNavigation
```

初始阶段可以用假实现：

```text
默认直接进入 Main
通过 AppConfig 开关模拟进入 Privacy / Permission / Login
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add startup coordinator"
```

---

### 阶段 7：隐私协议 Gate

目标：

```text
实现首次启动和协议版本更新时显示隐私协议页
```

实现：

```text
core:privacy
PrivacyRepository
DataStorePrivacyRepository
PrivacyState

feature:privacy
PrivacyRoute
PrivacyScreen
PrivacyViewModel
PrivacyNavigation
```

要求：

```text
同意后记录版本
不同意退出 App
协议版本更新后重新展示
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add privacy agreement gate"
```

---

### 阶段 8：权限总览与申请 Gate

目标：

```text
实现权限总览页面和权限申请能力
```

实现：

```text
core:permission
AppPermission
AppPermissionDeclaration
PermissionRepository
PermissionChecker
PermissionIntentFactory
PermissionRequestTiming

feature:permission
PermissionOverviewRoute
PermissionRequestRoute
PermissionScreen
PermissionViewModel
PermissionNavigation
```

要求：

```text
支持普通权限
支持特殊权限跳转
支持必要权限和可选权限
支持跳过策略
支持从设置页再次进入权限页面
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add permission overview gate"
```

---

### 阶段 9：登录 Gate

目标：

```text
实现可选登录模块
```

实现：

```text
core:auth
AuthRepository
AuthState
SessionState

feature:auth
LoginRoute
LoginScreen
LoginViewModel
AuthNavigation
```

要求：

```text
AppConfig 可关闭登录
支持登录态检查
支持退出登录
支持登录成功进入 Main
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add optional auth gate"
```

---

### 阶段 10：设置页配置化

目标：

```text
设置页根据 SettingsFeatureConfig 显示或隐藏设置项
语言和主题通过 Repository / UseCase 管理
```

实现：

```text
语言设置
主题设置
字号设置
方向设置
专家模式
权限入口
隐私协议入口
关于页
退出登录
重启 App
```

要求：

```text
不要让 SettingsScreen 直接操作 DataStore
不要让 SettingsScreen 直接操作语言底层 API
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: make settings page configurable"
```

---

### 阶段 11：Hilt 接入

目标：

```text
逐步替换 AppDependencies
使用 Hilt 管理 Repository、UseCase、ViewModel
```

实现：

```text
@HiltAndroidApp
@AndroidEntryPoint
@HiltViewModel
RepositoryModule
DataStoreModule
SettingsModule
PrivacyModule
PermissionModule
AuthModule
```

要求：

```text
可以分多次提交
不要一次性删除 AppDependencies
先迁移新模块
再迁移旧 settings / locale / input
最后删除 AppDependencies
```

验收：

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

提交：

```bash
git commit -m "feat: integrate hilt dependency injection"
```

---

### 阶段 12：时间能力接口

目标：

```text
建立 core:time 时间能力接口和基础实现
```

实现：

```text
AppTimeRepository
AppTimeState
TimeCalibrationStrategy
TimeCalibrationResult
TimeChangeObserver
SystemTimeProvider
CalibratedTimeProvider
```

先实现：

```text
系统时间
开机时长
时区
日期
时分秒 StateFlow
```

后续再实现：

```text
NTP
HTTP
私有接口
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add app time capability interfaces"
```

---

### 阶段 13：共享定时器与 AlarmManager 调度接口

目标：

```text
建立 core:timer 和 core:scheduler
```

实现：

```text
SharedTimerManager
TimerTick
TimerSource
CoroutineTimerSource

AppScheduler
ScheduleRequest
RepeatingScheduleRequest
ScheduleTime
AlarmManagerScheduler
```

要求：

```text
相同时长共享同一个 ticker
无观察者时取消
AlarmManager 支持指定时间点和间隔定时
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add timer and scheduler foundations"
```

---

### 阶段 14：常用功能接口

目标：

```text
建立 boot、service、update、lifecycle 能力接口
```

实现：

```text
core:lifecycle
AppLifecycleObserver
AppForegroundState
AppStartSource

core:boot
BootReceiver
BootStartHandler

core:service
ForegroundServiceController
ForegroundServiceState

core:update
UpdateChecker
UpdateDownloader
UpdateInstaller
UpdateState
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add common runtime capability interfaces"
```

---

### 阶段 15：常用工具接口

目标：

```text
建立 shell、media、network-tool、feedback、notification 能力
```

实现：

```text
base:shell
ShellExecutor
ShellResult

base:media
WavWriter

base:network-tool
PingTool
PingResult

core:feedback
FeedbackMessage
FeedbackManager

core:notification
NotificationChannelManager
AppNotificationManager
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add common tool capability interfaces"
```

---

### 阶段 16：系统特权能力骨架

目标：

```text
建立 core:system，但不强行实现所有系统特权操作
```

实现：

```text
SystemCapabilities
SystemCapabilityManager
SystemPermissionManager
SilentInstallManager
RootShellExecutor
PrivilegedSystemExecutor
```

要求：

```text
普通 App 下必须安全返回不可用
所有能力必须先 capability check
不要让普通业务模块直接依赖系统特权实现
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add system capability skeleton"
```

---

### 阶段 17：UI 组件完善

目标：

```text
完善 core:ui 中常用控件和页面模板
```

实现：

```text
GroupList
TemplateTopBar
TemplateTab
TemplatePager
SearchListPage
SearchTreePage
StickyHeaderList
MarqueeText
FocusableMarqueeText
StatusPage
FormPage
EmptyErrorLoading
```

要求：

```text
不要和具体业务耦合
优先服务 settings、permission、privacy、diagnostics 页面
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add common compose ui components"
```

---

### 阶段 18：诊断与调试页面

目标：

```text
新增可选调试页面，用于验证模板能力
```

实现：

```text
feature:diagnostics
feature:time-debug
feature:system-debug
```

页面：

```text
时间状态页面
权限状态页面
启动来源页面
前后台状态页面
系统能力检测页面
Shell / Ping 测试页面
```

要求：

```text
这些 feature 必须可以从 app 中包含或不包含
不要作为正式业务强依赖
```

验收：

```bash
./gradlew assembleDebug
```

提交：

```bash
git commit -m "feat: add optional diagnostics pages"
```

---

## 21. 最终验收标准

全部阶段完成后，工程应满足：

```text
1. App 启动流程可配置。
2. 隐私协议页面可启用或禁用。
3. 隐私协议版本更新后可重新展示。
4. 权限总览页面可启用或禁用。
5. 支持普通权限和特殊权限。
6. 登录页面可启用或禁用。
7. 主页面 Tab 可配置数量、顺序、显示隐藏。
8. 设置页可配置显示语言、主题、权限、隐私、关于等入口。
9. 业务模块可以通过 Gradle 依赖包含或不包含。
10. 基础能力模块边界清晰，没有巨大的 utils 包。
11. 系统特权能力被隔离。
12. 工程可以 assembleDebug。
13. 单元测试可执行。
14. Lint 可执行，或记录无法执行的原因。
15. 每个阶段都有清晰 Git 提交。
```

最终验证：

```bash
./gradlew clean
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
```

最终检查：

```bash
git status
git log --oneline -20
```

---

## 22. 禁止事项

执行过程中禁止：

```text
1. 不要一次性重写整个工程。
2. 不要删除已有功能后再重建，除非确有必要。
3. 不要创建巨大 utils 模块。
4. 不要让 feature 之间直接依赖。
5. 不要让 core 依赖 feature。
6. 不要让 ViewModel 直接访问 Retrofit、Dao、DataStore。
7. 不要让 Composable 直接操作 Repository。
8. 不要将系统签名、Root、静默安装能力散落在普通模块。
9. 不要提交构建产物。
10. 不要提交密钥、证书、local.properties。
11. 不要主动 push。
12. 不要在没有验证的情况下提交。
```

---

## 23. 推荐 Codex 执行策略

请按以下方式执行：

```text
1. 先阅读当前工程结构。
2. 对比本文件中的目标架构。
3. 从阶段 1 开始执行。
4. 每次只实现一个阶段。
5. 每个阶段完成后运行验证命令。
6. 验证通过后提交 Git。
7. 如果验证不通过，优先修复本阶段引入的问题。
8. 如果失败原因来自历史问题或环境问题，记录原因，并继续保持本阶段改动最小化。
9. 每个阶段结束时输出：
   - 完成内容
   - 修改文件
   - 验证结果
   - Git 提交 hash
   - 下一阶段建议
```

