package com.georgebindragon.android.core.data

import com.georgebindragon.android.core.model.HomeItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val homeItems: Flow<List<HomeItem>>
}
