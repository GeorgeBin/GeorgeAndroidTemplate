package com.georgebindragon.android.base.io

import java.io.File
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories

object FileUtils {
    fun ensureDirectory(directory: File): File {
        require(!directory.exists() || directory.isDirectory) {
            "Path exists but is not a directory: ${directory.absolutePath}"
        }
        directory.mkdirs()
        return directory
    }

    fun ensureParentDirectory(file: File): File {
        file.parentFile?.let(::ensureDirectory)
        return file
    }

    fun ensureFile(file: File): File {
        require(!file.exists() || file.isFile) {
            "Path exists but is not a file: ${file.absolutePath}"
        }
        file.toPath().createParentDirectories()
        if (!file.exists()) {
            file.toPath().createFile()
        }
        return file
    }

    fun copy(
        source: File,
        target: File,
        overwrite: Boolean = true,
    ): File {
        require(source.isFile) { "Source is not a file: ${source.absolutePath}" }
        ensureParentDirectory(target)
        source.copyTo(target, overwrite = overwrite)
        return target
    }

    fun delete(file: File): Boolean {
        if (!file.exists()) return true
        return file.delete()
    }

    fun deleteRecursively(file: File): Boolean {
        if (!file.exists()) return true
        return file.deleteRecursively()
    }
}
