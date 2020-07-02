/*
   Copyright 2020 Joshua Kent

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.nativebuild.util

import com.nativebuild.util.misc.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipFile
import kotlin.concurrent.thread

/**
 * Contains variables and information to do with building Kotlin/Native.
 *
 * @author Joshua Kent
 */
object Build {

    /** The path where Kotlin/Native and this utility will be stored. */
    private const val nativeDirString = "C:/kotlin-native"
    /** Used as a template for where Kotlin/Native will be installed. */
    private const val nativeDestDirVersionlessString = "${nativeDirString}/kotlin-native-windows-"
    /** The same as `nativeDestDirVersionlessString` but compatible with regex.*/
    private const val nDDVS_ps1 = "C:\\\\kotlin-native\\\\kotlin-native-windows-" //
    /** The path where Kotlin/Native will be installed. */
    private val nativeDestDirString = "${nativeDestDirVersionlessString}${KotlinVersion.CURRENT}"
    /** The same as `nativeDestDirString`, but as a `java.io.File` object. */
    val nativeDestDir = File(nativeDestDirString) // not private as used by main()
    /** The zip file where the source is extracted to. */
    private val nativeDestZipString = "$nativeDestDirString.zip"
    /** The same as `nativeDestZipString`, but as a `java.io.File` object. */
    val nativeDestZip = File(nativeDestZipString)
    /** The path where this utility will be installed and visible to path. */
    private const val jarFile = "${nativeDirString}/native-build.jar"
    /** The URL where Kotlin/Native will be downloaded from. */
    private val sourceURLString = "https://github.com/JetBrains/kotlin/releases/download/v${KotlinVersion.CURRENT}/kotlin-native-windows-${KotlinVersion.CURRENT}.zip"
    /** */
    private val sourceURL = URL(sourceURLString)
    /** The current path of the utility, to know whether to copy it into `C:\kotlin-native`. */
    private var jarPath = object {}.javaClass.protectionDomain.codeSource.location.toURI().path
    /** The path of the batch file that allows `native-build` to be executed from the command line. */
    private const val batFileString = "${nativeDirString}/native-build.bat"
    /** The same as `batFileString`, but as a `java.io.File` object. */
    private val batFile = File(batFileString)
    /** Accesses the current Java runtime, which can be used to run processes.*/
    private val javaRuntime = Runtime.getRuntime()

    init {
        if (jarPath.startsWith('/')) jarPath = jarPath.drop(1)
        jarPath = jarPath.replace('/', '\\')
    }

    /**
     * Runs a PowerShell command from runtime (this does not print any output).
     *
     * @param comment What to output while running command
     * @param command What to run (as PowerShell command)
     * @param waitFor Set to `true` to wait for the process to end
     * before continuing. (default: `true`)
     * @author Joshua Kent
     */
    private fun runTemplate(comment: String, command: String, waitFor: Boolean = true): Process {
        println(comment)
        val commandArr = arrayOf("powershell.exe", "-Command", "\"" + command + "\"")
        val proc = this.javaRuntime.exec(commandArr)
        if (waitFor) proc.waitFor()
        val procValue = proc
        if (waitFor) proc.destroy()
        return procValue
    }

    /**
     * Downloads the .zip file from source.
     *
     * Thanks to reflog & Cristian's answer here:
     * https://stackoverflow.com/questions/2983073/how-to-know-the-size-of-a-file-before-downloading-it
     *
     * @author Joshua Kent
     */
    fun downloadZip() {
        val urlConnection = sourceURL.openConnection()
        urlConnection.connect()
        val urlFileSize = urlConnection.contentLengthLong

        // https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
        val rbc: ReadableByteChannel = Channels.newChannel(sourceURL.openStream())
        val fos: FileOutputStream = FileOutputStream(nativeDestZip)

        println("Downloading latest files from $sourceURL")

        var threadDone = false
        thread(true) {
            fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
            // close streams and channels to allow access later
            fos.close()
            rbc.close()
            threadDone = true
        }

        while (!threadDone) { progressBar(nativeDestZipString, urlFileSize, measuredIn = "MB") }
    }

    /**
     * Removes the previous installation of Kotlin/Native (if it is identical to the current version).
     *
     * @author Joshua Kent
     */
    fun removeOldInstallation() {
//        runTemplate("Deleting previous installation...",
//                "Remove-Item $nativeDestDirString -Recurse")
        if (nativeDestDir.exists()) {
            println("Deleting previous installation...")

            val initialSizeOfDir = sizeOfDirectory(nativeDestDir)
            var threadDone = false
            thread(true) {
                val deleteDir = nativeDestDir.deleteRecursively()
                if (!deleteDir) {
                    println("WARNING: The previous installation did not completely delete.")
                }
                threadDone = true
            }

            while (!threadDone) {
                progressBar(nativeDestDirString, initialSizeOfDir, reverse = true, measuredIn = "MB")
            }
        }
    }

    /**
     * Extracts the .zip file that has been downloaded from the source.
     *
     * Thanks to Roman Elizarov's answer here:
     * https://stackoverflow.com/questions/46627357/unzip-a-file-in-kotlin-script-kts
     *
     * @author Joshua Kent
     */
    fun extractZip() {
//        val proc = runTemplate("Extracting files from .zip file...",
//                "Expand-Archive -LiteralPath '$nativeDestDirString.zip' -DestinationPath $nativeDirString -Force",
//        false)

        // get size of zip file if uncompressed
        val zipFile = ZipFile(nativeDestZipString)

        var uncompressedZipSize: Long = 0
        val enum = zipFile.entries()
        try {
            while (enum.hasMoreElements()) {
                val zipEntry = enum.nextElement()
                uncompressedZipSize += zipEntry.size
            }
        } catch (exc: IOException) {
            error(exc)
        }

        var threadDone = false
        thread(true) {
            zipFile.use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    zip.getInputStream(entry).use { input ->
                        val entryDir = File("$nativeDirString/${entry.name}")
                        if (entry.isDirectory) { entryDir.mkdirs() }
                        entryDir.outputStream().use {output ->
                            input.copyTo(output)
                            output.close()
                        }
                        input.close()
                    }
                }
            }
            threadDone = true
        }

        while (!threadDone) { progressBar(nativeDestDirString, uncompressedZipSize, measuredIn = "MB") }

        zipFile.close()
    }

    /**
     * Deletes the .zip file that has been downloaded from the source.
     *
     * @author Joshua Kent
     */
    //fun deleteZip() = runTemplate("Deleting temporary .zip file...",
    //"Remove-Item '$nativeDestDirString.zip' -Force")
    fun deleteZip() {
        println("Deleting temporary .zip file...")
        try {
            nativeDestZip.delete()
        } catch (exc: Throwable) {
            error(exc)
        }
    }

    /**
     * Adds the current utility into path if the current runtime
     * is not running from `C:\kotlin-native\native-build.jar`.
     *
     * @author Joshua Kent
     */
    fun addJarToPath() {
        if (jarPath != jarFile) {
            runTemplate("Adding native-build.jar to $nativeDirString...",
                    "Copy-Item '$jarPath' -Destination $jarFile -Force")
        }
    }

    /**
     * Writes info to `C:\kotlin-native\native-build.bat` to be able to run `native-build` in path.
     *
     * @author Joshua Kent
     */
    fun appendToBatFile() {
        val newBatFile = batFile.createNewFile()

        if (newBatFile) {
            println("Creating executable native-build.bat file")
            batFile.appendText("""
                rem Copyright 2020 Joshua Kent
                rem
                rem Licensed under the Apache License, Version 2.0 (the "License");
                rem you may not use this file except in compliance with the License.
                rem You may obtain a copy of the License at
                rem
                rem     http://www.apache.org/licenses/LICENSE-2.0
                rem
                rem Unless required by applicable law or agreed to in writing, software
                rem distributed under the License is distributed on an "AS IS" BASIS,
                rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                rem See the License for the specific language governing permissions and
                rem limitations under the License.


                @echo off
                java -jar $jarFile
            """.trimIndent())
        }
    }

    /**
     * Removes pre-existing kotlin-native variables from path.
     *
     * Thanks to Chris Dent's answer here:
     * https://stackoverflow.com/questions/39010405/powershell-how-to-delete-a-path-in-the-path-environment-variable
     *
     * @author Joshua Kent
     */
    fun removeOldVersionPaths() = runTemplate("Removing outdated paths if they exist...",
        "[Environment]::SetEnvironmentVariable('Path', (([Environment]::GetEnvironmentVariable('Path', 'User').Split(';') -NotMatch ';$nDDVS_ps1') -Join ';'), 'User')")

    /**
     * Adds the new Kotlin/Native version to path.
     *
     * @author Joshua Kent
     */
    fun addNewVersionToPath() = runTemplate("Adding new Kotlin/Native version to path...",
        "[Environment]::SetEnvironmentVariable('Path', [Environment]::GetEnvironmentVariable('Path', 'User') + ';$nativeDestDirString\\bin', 'User')")

    /**
     * Adds `C:\kotlin-native` to path if it is not already in path.
     *
     * @author Joshua Kent
     */
    fun addBaseToPath() = runTemplate("Adding C:\\kotlin-native to path if it isn't already added...",
        "${'$'}path = [Environment]::GetEnvironmentVariable('Path', 'User'); if (!(${'$'}path.Split(';') | Where-Object {${'$'}_ -eq 'C:\\kotlin-native'})) {[Environment]::SetEnvironmentVariable('Path', ${'$'}path + ';C:\\kotlin-native', 'User')}")

}
