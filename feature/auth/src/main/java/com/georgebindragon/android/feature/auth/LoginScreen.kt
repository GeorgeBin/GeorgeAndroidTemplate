package com.georgebindragon.android.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.ui.component.FocusableButton

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    allowGuestMode: Boolean,
    onAccountChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGuestClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(
                horizontal = dimensions.screenHorizontalPadding,
                vertical = dimensions.screenVerticalPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
    ) {
        Text(
            text = "登录",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        OutlinedTextField(
            value = uiState.account,
            onValueChange = onAccountChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "账号") },
            singleLine = true,
            enabled = !uiState.loading,
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "密码") },
            singleLine = true,
            enabled = !uiState.loading,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        FocusableButton(
            onClick = onLoginClick,
            enabled = !uiState.loading,
        ) {
            Text(text = if (uiState.loading) "登录中" else "登录")
        }
        if (allowGuestMode) {
            FocusableButton(
                onClick = onGuestClick,
                enabled = !uiState.loading,
            ) {
                Text(text = "游客模式")
            }
        }
    }
}
