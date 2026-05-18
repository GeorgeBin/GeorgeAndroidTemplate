package com.georgebindragon.android.base.io

object FileNameUtils {
    private val invalidCharacters = setOf('<', '>', ':', '"', '/', '\\', '|', '?', '*')

    fun sanitize(
        fileName: String,
        replacement: Char = '_',
    ): String {
        val sanitized = fileName
            .trim()
            .map { char ->
                if (char in invalidCharacters || char.isISOControl()) replacement else char
            }
            .joinToString(separator = "")
            .trim(replacement)
            .ifBlank { DEFAULT_FILE_NAME }
        return sanitized
    }

    private const val DEFAULT_FILE_NAME = "untitled"
}
