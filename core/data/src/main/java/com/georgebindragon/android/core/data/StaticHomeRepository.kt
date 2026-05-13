package com.georgebindragon.android.core.data

import com.georgebindragon.android.core.model.HomeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class StaticHomeRepository : HomeRepository {
    override val homeItems: Flow<List<HomeItem>> = flowOf(
        List(5) { index ->
            HomeItem(
                id = "template-section-${index + 1}",
                title = "内容模块 ${index + 1}",
                body = "这里预留业务内容展示区域。后续可替换为列表、卡片、表单或其他页面主体内容，当前布局已支持纵向滚动。",
            )
        },
    )
}
