# Goal Progress

更新时间：2026-06-11

## 1. 当前 goal 内容

当前 goal 来源：`docs/codex_Goal.md`

目标是在不破坏现有工程可运行性的前提下，将 GeorgeAndroidTemplate 逐步升级为模块化 Android 模板工程：

- 可配置启动流程
- 可插拔页面
- 可插拔业务模块
- 基础能力 SDK
- 可配置底部 Tab 主页面
- 可选系统特权能力

目标架构方向：

- Single Activity
- Jetpack Compose
- Navigation Compose
- Hilt
- Repository
- UDF / MVI-like UI State
- Gradle Convention Plugins
- `base / core / feature / app` 分层
- `AppConfig` 驱动功能启用与页面显示

执行原则：

- 每个阶段工程可以编译
- 不破坏现有页面和已有设置能力
- 每次只做一个清晰目标
- 每个阶段完成后执行必要验证
- 每个阶段完成后进行 Git 提交
- 不主动 push

## 2. 已完成内容

已提交：

- `35a1476 feat: add configurable navigation foundation`
- `f7ed72c feat: add startup coordinator skeleton`
- `a5ac4bd feat: add privacy agreement gate`
- `fc31a98 feat: add permission overview gate`

已完成的阶段性内容：

- 新增 `base:common` 基础模型骨架：
  - `AppResult`
  - `AppError`
  - `UiText`
  - `CoroutineDispatchers`
  - `safeCall`
  - `LoadableState`
  - `Initializable`
  - `Destroyable`
  - `ComponentState`
- 新增 `core:appconfig` 配置中心骨架：
  - `AppConfig`
  - `PrivacyFeatureConfig`
  - `PermissionFeatureConfig`
  - `AuthFeatureConfig`
  - `MainFeatureConfig`
  - `SettingsFeatureConfig`
  - `BusinessFeatureConfig`
  - `TabConfig`
  - `AppConfigProvider`
  - `DefaultAppConfigProvider`
- 新增并完善 `core:permission` 权限能力：
  - `AppPermission`
  - `AppPermissionDeclaration`
  - `PermissionRequestTiming`
  - `PermissionChecker`
  - `AndroidPermissionChecker`
  - `PermissionIntentFactory`
  - `AndroidPermissionIntentFactory`
  - `PermissionRepository`
  - `DataStorePermissionRepository`
  - `PermissionState`
  - `PermissionGateState`
- 新增 `core:navigation` 路由骨架：
  - `RootRoute`
  - `StartupRoute`
  - `MainRoute`
  - `TabDestination`
  - `TopLevelDestination`
  - `NavigationExtensions`
- 引入 Navigation Compose 依赖。
- 新增 `feature:main` 主页面壳骨架：
  - `MainShellRoute`
  - `MainShellScreen`
  - `MainTabState`
  - `MainNavigation`
- Home 和 Settings feature 增加各自导航注册函数：
  - `homeScreen`
  - `settingsScreen`
- `app` 从手写 Home / Settings 枚举切屏迁移为 Root/Main 两层 NavHost。
- 主页面底部 Tab 由 `AppConfig.main.tabs` 驱动，当前默认支持 Home 和 Settings。
- 新增 `core:startup` 启动编排骨架：
  - `StartupDestination`
  - `StartupCoordinator`
  - `DefaultStartupCoordinator`
- RootNavHost 默认先进入 Startup，再由 `StartupCoordinator` 跳转 Main。
- 预留 Privacy / Permission / Login 占位路由。
- 新增 `core:privacy`：
  - `PrivacyState`
  - `PrivacyAcceptSource`
  - `PrivacyRepository`
  - `DataStorePrivacyRepository`
- 新增 `feature:privacy`：
  - `PrivacyRoute`
  - `PrivacyScreen`
  - `PrivacyViewModel`
  - `PrivacyUiState`
  - `PrivacyNavigation`
- `StartupCoordinator` 已根据 `AppConfig.privacy` 和 `PrivacyRepository.shouldShowPrivacy(...)` 判断进入 Privacy 或 Main。
- 首次启动会进入隐私协议页。
- 同意后记录隐私协议版本、用户协议版本、同意时间和同意来源。
- 不同意会调用 app 层退出回调。
- 隐私协议版本或用户协议版本更新后会重新展示隐私协议页。
- 新增 `feature:permission`：
  - `PermissionOverviewRoute`
  - `PermissionRequestRoute`
  - `PermissionOverviewScreen`
  - `PermissionRequestScreen`
  - `PermissionViewModel`
  - `PermissionNavigation`
- `AppConfig.permission` 支持总览版本、权限声明、必要/可选权限和跳过策略配置。
- `StartupCoordinator` 已在隐私 gate 之后，根据权限配置和状态决定是否进入权限总览。
- 权限总览页支持展示权限用途、必要/可选状态。
- 权限申请页支持普通运行时权限申请、特殊权限系统设置跳转、状态刷新和可选权限跳过。
- 设置页新增权限管理入口，可再次进入权限总览。
- 新增 `core:auth`：
  - `AuthRepository`
  - `AuthState`
  - `SessionState`
  - `DataStoreAuthRepository`
- 新增 `feature:auth`：
  - `LoginRoute`
  - `LoginScreen`
  - `LoginViewModel`
  - `LoginUiState`
  - `AuthNavigation`
- `StartupCoordinator` 已在隐私和权限 gate 之后，根据 `AppConfig.auth` 和登录状态决定进入 Login 或 Main。
- 登录页支持账号密码登录、登录中状态、错误提示、游客模式配置和登录成功进入 Main。
- 设置页新增退出登录入口，退出后回到 Startup 流程重新解析。
- 更新 `docs/architecture.md`，同步当前模块状态和下一步演进说明。

## 3. 未完成内容

目标文档中仍未完成或仅有占位的内容：

- 设置页根据 `SettingsFeatureConfig` 动态显示/隐藏入口
- Hilt 接入与逐步替换 `AppDependencies`
- `core:time`
- `core:timer`
- `core:scheduler`
- `core:boot`
- `core:service`
- `core:update` 和 `feature:update`
- `base:shell`
- `base:media`
- `base:network-tool`
- `core:feedback`
- `core:notification`
- `core:system` 和 `feature:system-debug`
- `core:lifecycle`
- 后续 UI 能力完善，包括通用列表、状态页、DialogHost、ToastBridge、跑马灯文字等
- 真实业务模块插件化示例
- Hilt、Repository、UDF/MVI-like 状态流的全面落地

## 4. 已修改文件

本轮已提交涉及文件：

```text
app/build.gradle.kts
app/src/main/java/com/georgebindragon/android/app/AppDependencies.kt
app/src/main/java/com/georgebindragon/android/app/StartupRoute.kt
app/src/main/java/com/georgebindragon/android/app/TemplateApp.kt
base/common/build.gradle.kts
base/common/src/main/AndroidManifest.xml
base/common/src/main/java/com/georgebindragon/android/base/common/AppError.kt
base/common/src/main/java/com/georgebindragon/android/base/common/AppResult.kt
base/common/src/main/java/com/georgebindragon/android/base/common/ComponentLifecycle.kt
base/common/src/main/java/com/georgebindragon/android/base/common/CoroutineDispatchers.kt
base/common/src/main/java/com/georgebindragon/android/base/common/LoadableState.kt
base/common/src/main/java/com/georgebindragon/android/base/common/SafeCall.kt
base/common/src/main/java/com/georgebindragon/android/base/common/UiText.kt
base/common/src/test/java/com/georgebindragon/android/base/common/SafeCallTest.kt
core/appconfig/build.gradle.kts
core/appconfig/src/main/AndroidManifest.xml
core/appconfig/src/main/java/com/georgebindragon/android/core/appconfig/AppConfig.kt
core/appconfig/src/main/java/com/georgebindragon/android/core/appconfig/AppConfigProvider.kt
core/appconfig/src/test/java/com/georgebindragon/android/core/appconfig/DefaultAppConfigProviderTest.kt
core/navigation/build.gradle.kts
core/navigation/src/main/AndroidManifest.xml
core/navigation/src/main/java/com/georgebindragon/android/core/navigation/AppRoute.kt
core/navigation/src/main/java/com/georgebindragon/android/core/navigation/NavigationExtensions.kt
core/navigation/src/main/java/com/georgebindragon/android/core/navigation/TabDestination.kt
core/navigation/src/main/java/com/georgebindragon/android/core/navigation/TopLevelDestination.kt
core/permission/build.gradle.kts
core/permission/src/main/AndroidManifest.xml
core/permission/src/main/java/com/georgebindragon/android/core/permission/AppPermission.kt
core/permission/src/main/java/com/georgebindragon/android/core/permission/AppPermissionDeclaration.kt
core/startup/build.gradle.kts
core/startup/src/main/AndroidManifest.xml
core/startup/src/main/java/com/georgebindragon/android/core/startup/DefaultStartupCoordinator.kt
core/startup/src/main/java/com/georgebindragon/android/core/startup/StartupCoordinator.kt
core/startup/src/main/java/com/georgebindragon/android/core/startup/StartupDestination.kt
core/startup/src/test/java/com/georgebindragon/android/core/startup/DefaultStartupCoordinatorTest.kt
docs/architecture.md
feature/home/build.gradle.kts
feature/home/src/main/java/com/georgebindragon/android/feature/home/HomeNavigation.kt
feature/main/build.gradle.kts
feature/main/src/main/AndroidManifest.xml
feature/main/src/main/java/com/georgebindragon/android/feature/main/MainNavigation.kt
feature/main/src/main/java/com/georgebindragon/android/feature/main/MainShellRoute.kt
feature/main/src/main/java/com/georgebindragon/android/feature/main/MainShellScreen.kt
feature/main/src/main/java/com/georgebindragon/android/feature/main/MainTabState.kt
core/privacy/build.gradle.kts
core/privacy/src/main/AndroidManifest.xml
core/privacy/src/main/java/com/georgebindragon/android/core/privacy/DataStorePrivacyRepository.kt
core/privacy/src/main/java/com/georgebindragon/android/core/privacy/PrivacyRepository.kt
core/privacy/src/main/java/com/georgebindragon/android/core/privacy/PrivacyState.kt
core/privacy/src/test/java/com/georgebindragon/android/core/privacy/DataStorePrivacyRepositoryTest.kt
feature/privacy/build.gradle.kts
feature/privacy/src/main/AndroidManifest.xml
feature/privacy/src/main/java/com/georgebindragon/android/feature/privacy/PrivacyNavigation.kt
feature/privacy/src/main/java/com/georgebindragon/android/feature/privacy/PrivacyRoute.kt
feature/privacy/src/main/java/com/georgebindragon/android/feature/privacy/PrivacyScreen.kt
feature/privacy/src/main/java/com/georgebindragon/android/feature/privacy/PrivacyUiState.kt
feature/privacy/src/main/java/com/georgebindragon/android/feature/privacy/PrivacyViewModel.kt
feature/settings/build.gradle.kts
feature/settings/src/main/java/com/georgebindragon/android/feature/settings/SettingsNavigation.kt
gradle/libs.versions.toml
settings.gradle.kts
```

本文件为暂停记录：

```text
docs/GOAL_PROGRESS.md
```

## 5. 已运行的验证命令及结果

已通过：

```bash
./gradlew projects --no-daemon
```

结果：成功，Gradle 能识别新增模块。

已通过：

```bash
./gradlew :base:common:testDebugUnitTest :core:appconfig:testDebugUnitTest assembleDebug --no-daemon
```

结果：成功。

已通过：

```bash
./gradlew testDebugUnitTest lintDebug --no-daemon
```

结果：成功。

已通过：

```bash
./gradlew :core:startup:testDebugUnitTest assembleDebug --no-daemon
```

结果：成功。

再次已通过：

```bash
./gradlew testDebugUnitTest lintDebug --no-daemon
```

结果：成功。

恢复执行后新增验证，已通过：

```bash
./gradlew :core:privacy:testDebugUnitTest :core:startup:testDebugUnitTest assembleDebug --no-daemon
```

结果：成功。

恢复执行后再次全量验证，已通过：

```bash
./gradlew testDebugUnitTest lintDebug --no-daemon
```

结果：成功。

提交前格式检查，已通过：

```bash
git diff --check
```

结果：成功，无空白错误。

阶段验收构建，已通过：

```bash
./gradlew assembleDebug --no-daemon
```

结果：成功。

权限阶段核心验证，已通过：

```bash
./gradlew :core:permission:testDebugUnitTest :core:startup:testDebugUnitTest :core:appconfig:testDebugUnitTest :feature:permission:compileDebugKotlin :app:compileDebugKotlin --no-daemon
```

结果：成功。

权限阶段验收构建，已通过：

```bash
./gradlew assembleDebug --no-daemon
```

结果：成功。

权限阶段全量单测和 lint，已通过：

```bash
./gradlew testDebugUnitTest lintDebug --no-daemon
```

结果：成功。

登录阶段核心验证，已通过：

```bash
./gradlew :core:auth:testDebugUnitTest :core:startup:testDebugUnitTest :feature:auth:compileDebugKotlin :app:compileDebugKotlin --no-daemon
```

结果：成功。

登录阶段验收构建，已通过：

```bash
./gradlew assembleDebug --no-daemon
```

结果：成功。

登录阶段全量单测和 lint，已通过：

```bash
./gradlew testDebugUnitTest lintDebug --no-daemon
```

结果：成功。

备注：lint 过程中出现过 `:base:io` 作为 Java library 外部依赖未被 Android lint 分析的提示，但未导致失败。

## 6. 当前阻塞点

无技术阻塞。

当前 goal 未完成，不应标记为 complete。

## 7. 下次恢复后第一步应该做什么

下次恢复后第一步：

```bash
git status
git branch --show-current
```

确认工作区状态后，从设置页配置化阶段继续：

- 让设置页根据 `SettingsFeatureConfig` 动态显示或隐藏语言、主题、字号、方向、专家模式、权限、隐私、关于、退出登录、重启等入口。
- 保持 SettingsScreen 不直接操作 DataStore 或语言底层 API。
- 继续沿用现有 Repository / UseCase 回调边界。

开始改动前应重新检查 `docs/codex_Goal.md` 中阶段 10 的要求，并确认当前代码仍以登录 gate 提交之后的状态为基础。
