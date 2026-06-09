package com.georgebindragon.android.base.common

sealed interface UiText {
    data class Plain(val value: String) : UiText
    data class Resource(val resId: Int, val args: List<Any> = emptyList()) : UiText
}
