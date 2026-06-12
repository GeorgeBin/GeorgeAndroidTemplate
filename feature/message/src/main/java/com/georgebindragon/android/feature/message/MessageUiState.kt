package com.georgebindragon.android.feature.message

data class MessageUiState(
    val query: String = "",
    val messages: List<MessageItem> = DefaultMessages,
) {
    val filteredMessages: List<MessageItem>
        get() {
            val keyword = query.trim()
            val filtered = if (keyword.isEmpty()) {
                messages
            } else {
                messages.filter { message ->
                    message.sender.contains(keyword, ignoreCase = true) ||
                        message.title.contains(keyword, ignoreCase = true) ||
                        message.summary.contains(keyword, ignoreCase = true)
                }
            }

            return filtered.sortedWith(
                compareByDescending<MessageItem> { it.pinned }
                    .thenBy { it.read }
                    .thenByDescending { it.time },
            )
        }
}

val DefaultMessages: List<MessageItem> = listOf(
    MessageItem(
        id = "system-update",
        sender = "系统通知",
        title = "版本更新提醒",
        summary = "新版本已准备完成，建议在空闲时完成升级。",
        time = "09:42",
        read = false,
        pinned = true,
    ),
    MessageItem(
        id = "permission",
        sender = "权限助手",
        title = "权限检查完成",
        summary = "通知、存储和定位权限状态已经同步。",
        time = "09:10",
    ),
    MessageItem(
        id = "workbench",
        sender = "工作台",
        title = "待办任务变更",
        summary = "设备巡检任务有新的处理记录。",
        time = "昨天",
        read = true,
    ),
    MessageItem(
        id = "security",
        sender = "安全中心",
        title = "登录状态确认",
        summary = "检测到一次新的登录，如非本人操作请及时处理。",
        time = "周二",
    ),
    MessageItem(
        id = "service",
        sender = "服务消息",
        title = "后台服务运行正常",
        summary = "核心服务最近 24 小时未发现异常。",
        time = "周一",
        read = true,
    ),
)
