package com.georgebindragon.android.feature.home

import com.georgebindragon.android.core.data.HomeRepository
import com.georgebindragon.android.core.model.HomeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiStateEmitsRepositoryItems() = runTest(testDispatcher) {
        val items = listOf(HomeItem(id = "1", title = "Title", body = "Body"))
        val viewModel = HomeViewModel(
            repository = object : HomeRepository {
                override val homeItems = flowOf(items)
            },
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        assertEquals(HomeUiState.Success(items), viewModel.uiState.value)
    }
}
