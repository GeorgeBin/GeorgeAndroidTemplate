package com.georgebindragon.android.core.ui.component

import android.view.KeyEvent as AndroidKeyEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.unit.dp
import com.georgebindragon.android.core.designsystem.theme.TemplateTheme
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.ui.focus.ProvideAppInteractionMode
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FocusableSurfaceTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun directionDownMovesFocusToNextFocusableSurface() {
        val firstFocusRequester = FocusRequester()
        composeRule.setFocusableSurfaceContent(firstFocusRequester = firstFocusRequester)

        composeRule.onNodeWithTag(FIRST_TAG).assertIsFocused()
        composeRule.onNodeWithTag(FIRST_TAG).performAndroidKeyPress(AndroidKeyEvent.KEYCODE_DPAD_DOWN)

        composeRule.onNodeWithTag(SECOND_TAG).assertIsFocused()
    }

    @Test
    fun enterAndCenterAndOkTriggerClick() {
        val firstFocusRequester = FocusRequester()
        var clickCount = 0
        composeRule.setFocusableSurfaceContent(
            firstFocusRequester = firstFocusRequester,
            onFirstClick = { clickCount++ },
        )

        composeRule.onNodeWithTag(FIRST_TAG).performAndroidKeyPress(AndroidKeyEvent.KEYCODE_ENTER)
        composeRule.waitForIdle()
        assertEquals(1, clickCount)

        Thread.sleep(550L)
        composeRule.onNodeWithTag(FIRST_TAG).performAndroidKeyPress(AndroidKeyEvent.KEYCODE_DPAD_CENTER)
        composeRule.waitForIdle()
        assertEquals(2, clickCount)

        Thread.sleep(550L)
        composeRule.onNodeWithTag(FIRST_TAG).performAndroidKeyPress(AndroidKeyEvent.KEYCODE_BUTTON_SELECT)
        composeRule.waitForIdle()
        assertEquals(3, clickCount)
    }

    @Test
    fun focusStyleIsVisibleInRemoteModeAndHiddenInTouchMode() {
        val remoteFocusRequester = FocusRequester()
        composeRule.setFocusableSurfaceContent(
            mode = AppInteractionMode.Remote,
            firstFocusRequester = remoteFocusRequester,
        )
        composeRule.onNodeWithTag(FIRST_TAG)
            .assert(SemanticsMatcher.expectValue(TemplateFocusVisibleKey, true))

        val touchFocusRequester = FocusRequester()
        composeRule.setFocusableSurfaceContent(
            mode = AppInteractionMode.Touch,
            firstFocusRequester = touchFocusRequester,
        )
        composeRule.onNodeWithTag(FIRST_TAG)
            .assert(SemanticsMatcher.expectValue(TemplateFocusVisibleKey, false))
    }

    @Test
    fun repeatedConfirmClicksAreDebounced() {
        val firstFocusRequester = FocusRequester()
        var clickCount = 0
        composeRule.setFocusableSurfaceContent(
            firstFocusRequester = firstFocusRequester,
            onFirstClick = { clickCount++ },
        )

        composeRule.onNodeWithTag(FIRST_TAG).performAndroidKeyPress(AndroidKeyEvent.KEYCODE_DPAD_CENTER)
        composeRule.onNodeWithTag(FIRST_TAG).performAndroidKeyPress(AndroidKeyEvent.KEYCODE_DPAD_CENTER)
        composeRule.waitForIdle()
        assertEquals(1, clickCount)

        Thread.sleep(550L)
        composeRule.onNodeWithTag(FIRST_TAG).performAndroidKeyPress(AndroidKeyEvent.KEYCODE_DPAD_CENTER)
        composeRule.waitForIdle()
        assertEquals(2, clickCount)
    }

    private fun androidx.compose.ui.test.SemanticsNodeInteraction.performAndroidKeyPress(
        keyCode: Int,
    ) {
        performKeyPress(
            KeyEvent(AndroidKeyEvent(AndroidKeyEvent.ACTION_DOWN, keyCode)),
        )
    }

    private fun androidx.compose.ui.test.junit4.ComposeContentTestRule.setFocusableSurfaceContent(
        mode: AppInteractionMode = AppInteractionMode.Remote,
        firstFocusRequester: FocusRequester,
        onFirstClick: () -> Unit = {},
        onSecondClick: () -> Unit = {},
    ) {
        setContent {
            FocusableSurfaceTestContent(
                mode = mode,
                firstFocusRequester = firstFocusRequester,
                onFirstClick = onFirstClick,
                onSecondClick = onSecondClick,
            )
        }
    }

    @Composable
    private fun FocusableSurfaceTestContent(
        mode: AppInteractionMode,
        firstFocusRequester: FocusRequester,
        onFirstClick: () -> Unit,
        onSecondClick: () -> Unit,
    ) {
        TemplateTheme {
            ProvideAppInteractionMode(mode = mode) {
                Column {
                    FocusableSurface(
                        onClick = onFirstClick,
                        modifier = Modifier
                            .testTag(FIRST_TAG)
                            .focusRequester(firstFocusRequester),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        Text("First")
                    }
                    FocusableSurface(
                        onClick = onSecondClick,
                        modifier = Modifier.testTag(SECOND_TAG),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        Text("Second")
                    }
                }
                LaunchedEffect(firstFocusRequester) {
                    firstFocusRequester.requestFocus()
                }
            }
        }
    }

    private companion object {
        const val FIRST_TAG = "first"
        const val SECOND_TAG = "second"
    }
}
