package com.nativebuild

import java.io.File

object Build {

    // TODO: Add documentation for functions and build object

    // path of main directory
    val nativeDirString = "C:\\kotlin-native"
    // used as a template for installation place
    val nativeDestDirVersionlessString = "${nativeDirString}\\kotlin-native-windows-"
    // same as above, but able to be used as regex string with Powershell
    val nDDVS_ps1 = "C:\\\\kotlin-native\\\\kotlin-native-windows-" //
    // where kotlin native will be installed
    val nativeDestDirString = "${nativeDestDirVersionlessString}${KotlinVersion.CURRENT}"
    // used to check if the current installation is installed
    val nativeDestDir = File(nativeDestDirString)
    // the .jar file to be included to be used by path
    val jarFile = "${nativeDirString}\\native-build.jar"
    // URL to download from
    val targetURL = "https://github.com/JetBrains/kotlin/releases/download/v${KotlinVersion.CURRENT}/kotlin-native-windows-${KotlinVersion.CURRENT}.zip"
    // current path of .jar file
    var jarPath = object {}.javaClass.protectionDomain.codeSource.location.toURI().path
    // path of .bat file in path
    val batFileString = "${nativeDirString}\\native-build.bat"
    // as java file to be manipulated
    val batFile = File(batFileString)
    // current java runtime, used to call start processes
    val javaRuntime : Runtime = Runtime.getRuntime()

    init {
        if (jarPath.startsWith('/')) jarPath = jarPath.drop(1)
        jarPath = jarPath.replace('/', '\\')
    }

    /**
     * Runs a PowerShell command from runtime
     *
     * @param comment What to output while running command
     * @param command What to run (as PowerShell)
     */
    private fun runTemplate(comment: String, command: String) {
        // TODO: add progress bar functionality
        println(comment)
        val commandArr = arrayOf("powershell.exe", "-Command", "\"" + command + "\"")
        val proc = this.javaRuntime.exec(commandArr)
        proc.waitFor()
    }

    /**Downloads .zip file from source.*/
    fun downloadZip() = runTemplate("Downloading from $targetURL...",
        "Invoke-WebRequest \"$targetURL\" -OutFile $nativeDestDirString.zip")

    fun removeOldInstallation() = runTemplate("Deleting previous installation...",
                "Remove-Item $nativeDestDirString -Recurse")

    fun extractZip() = runTemplate("Extracting files from .zip file...",
        "Expand-Archive -LiteralPath '$nativeDestDirString.zip' -DestinationPath $nativeDirString -Force")

    fun deleteZip() = runTemplate("Deleting temporary .zip file...",
        "Remove-Item $nativeDestDirString.zip")

    fun addJarToPath() = runTemplate("Adding native-build.jar to $nativeDirString...",
        "Copy-Item '$jarPath' -Destination $jarFile -Force")

    fun appendToBatFile() {
        val newBatFile = batFile.createNewFile()

        if (newBatFile) {
            println("Creating executable native-build.bat file")
            batFile.appendText("@echo off\njava -jar $jarFile")
        }
    }

    /**
     * Removes pre-existing kotlin-native variables from path.
     * Thanks to Chris Dent's answer here:
     * https://stackoverflow.com/questions/39010405/powershell-how-to-delete-a-path-in-the-path-environment-variable
     */
    fun removeOldVersionPaths() = runTemplate("Removing outdated paths if they exists...",
        "[Environment]::SetEnvironmentVariable('Path', (([Environment]::GetEnvironmentVariable('Path', 'User').Split(';') -NotMatch '$nDDVS_ps1') -Join ';'), 'User')")

    fun addNewVersionToPath() = runTemplate("Adding new Kotlin/Native version to path...",
        "[Environment]::SetEnvironmentVariable('Path', [Environment]::GetEnvironmentVariable('Path', 'User') + ';$nativeDestDirString\\bin', 'User')")

    fun addBaseToPath() = runTemplate("Adding C:\\kotlin-native to path if it isn't already added...",
        "${'$'}path = [Environment]::GetEnvironmentVariable('Path', 'User'); if (!(${'$'}path.Split(';') | Where-Object {${'$'}_ -eq 'C:\\kotlin-native'})) {[Environment]::SetEnvironmentVariable('Path', ${'$'}path + ';C:\\kotlin-native', 'User')}")
}