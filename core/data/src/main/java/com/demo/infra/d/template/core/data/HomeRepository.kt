package com.demo.infra.d.template.core.data

import com.demo.infra.d.template.core.model.HomeItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val homeItems: Flow<List<HomeItem>>
}
