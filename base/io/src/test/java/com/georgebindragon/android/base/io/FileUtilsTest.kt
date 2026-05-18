package com.georgebindragon.android.base.io

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.io.FileAlreadyExistsException

class FileUtilsTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun ensureDirectoryCreatesDirectory() {
        val directory = File(temporaryFolder.root, "nested/dir")

        val result = FileUtils.ensureDirectory(directory)

        assertEquals(directory, result)
        assertTrue(directory.isDirectory)
    }

    @Test
    fun ensureParentDirectoryCreatesParentDirectory() {
        val file = File(temporaryFolder.root, "nested/file.txt")

        val result = FileUtils.ensureParentDirectory(file)

        assertEquals(file, result)
        assertTrue(file.parentFile!!.isDirectory)
        assertFalse(file.exists())
    }

    @Test
    fun ensureFileCreatesParentDirectoryAndFile() {
        val file = File(temporaryFolder.root, "nested/file.txt")

        val result = FileUtils.ensureFile(file)

        assertEquals(file, result)
        assertTrue(file.isFile)
    }

    @Test
    fun copyOverwritesByDefault() {
        val source = temporaryFolder.newFile("source.txt")
        val target = temporaryFolder.newFile("target.txt")
        source.writeText("new")
        target.writeText("old")

        val result = FileUtils.copy(source, target)

        assertEquals(target, result)
        assertEquals("new", target.readText())
    }

    @Test(expected = FileAlreadyExistsException::class)
    fun copyCanRejectExistingTarget() {
        val source = temporaryFolder.newFile("source.txt")
        val target = temporaryFolder.newFile("target.txt")

        FileUtils.copy(source, target, overwrite = false)
    }

    @Test
    fun deleteDoesNotDeleteNonEmptyDirectoryRecursively() {
        val directory = temporaryFolder.newFolder("dir")
        File(directory, "child.txt").writeText("child")

        assertFalse(FileUtils.delete(directory))
        assertTrue(directory.exists())
    }

    @Test
    fun deleteRecursivelyDeletesDirectoryTree() {
        val directory = temporaryFolder.newFolder("dir")
        File(directory, "child.txt").writeText("child")

        assertTrue(FileUtils.deleteRecursively(directory))
        assertFalse(directory.exists())
    }
}
