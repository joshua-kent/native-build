import java.io.File
import java.lang.Process
import java.lang.Runtime

fun main(args : Array<String>) {
    val TESTING = false // enable to stop building when testing new features
    val VERSION = "1.0.0rc1" // TODO: for future update, to check for new major releases on GitHub

    var run = false
    var destExists = false

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
    if (jarPath.startsWith('/')) jarPath = jarPath.drop(1)
    jarPath = jarPath.replace('/', '\\')
    // path of .bat file in path
    val batFileString = "${nativeDirString}\\native-build.bat"
    // as java file to be manipulated
    val batFile = File(batFileString)


    println("Running Kotlin/Native Builder v$VERSION")
    println("Current Kotlin version: ${KotlinVersion.CURRENT}\n")

    if (!nativeDestDir.exists()) {
        println("A new version of Kotlin-Native should be available!")
        println("Do you want to install? (Y/N)")
        val answer : String = readLine()!!
        if (answer == "Y" || answer == "y") run = true

    } else {
        println("You are up-to-date. Do you want to force re-install anyway? (Y/N)")
        val answer : String = readLine()!!
        destExists = true
        if (answer == "Y" || answer == "y") run = true
    }

    if (run) {
        if (!TESTING) {
            println("\n--- These steps usually take a while, please wait. ---\n")

            // downloads .zip file from source
            println("Downloading from ${targetURL}...")
            val p1_c = arrayOf(
                "powershell.exe", "-Command",
                "\"Invoke-WebRequest \"${targetURL}\" -OutFile ${nativeDestDirString}.zip\""
            )
            val p1: Process = Runtime.getRuntime().exec(p1_c)
            p1.waitFor()

            // deleting previous installation if exists
            if (destExists) {
                println("Deleting previous installation...")
                val p2_c = arrayOf("powershell.exe", "-Command", "\"Remove-Item ${nativeDestDirString}\" -Recurse")
                val p2: Process = Runtime.getRuntime().exec(p2_c)
                p2.waitFor()
            }

            // extracts .zip file to folder
            println("Extracting files from .zip file...")
            val p3_c = arrayOf(
                "powershell.exe", "-Command",
                "\"Expand-Archive -LiteralPath '${nativeDestDirString}.zip' -DestinationPath ${nativeDirString}\" -Force"
            )
            val p3: Process = Runtime.getRuntime().exec(p3_c)
            p3.waitFor()

            // deletes .zip file
            println("Deleting temporary .zip file...")
            val p4_c = arrayOf("powershell.exe", "-Command", "\"Remove-Item ${nativeDestDirString}.zip\"")
            val p4: Process = Runtime.getRuntime().exec(p4_c)
            p4.waitFor()

            // adding .jar file to C:\kotlin-native
            if (jarPath != jarFile) {
                println("Adding kotlin-native-build.jar to ${nativeDirString}...")
                val p5_c = arrayOf(
                    "powershell.exe", "-Command",
                    "\"Copy-Item '${jarPath}' -Destination $jarFile -Force\""
                )
                val p5: Process = Runtime.getRuntime().exec(p5_c)
                p5.waitFor()
            }

            /* adding to path */

            println("Removing outdated paths if they exist...")
            /* removes pre-existing kotlin-native variables from path
            Thanks to Chris Dent's answer here:
            https://stackoverflow.com/questions/39010405/powershell-how-to-delete-a-path-in-the-path-environment-variable */
            val p6_c = arrayOf(
                "powershell.exe", "-Command",
                "\"[Environment]::SetEnvironmentVariable('Path', (([Environment]::GetEnvironmentVariable('Path', 'User').Split(';') -NotMatch '${nDDVS_ps1}') -Join ';'), 'User')\""
            )
            val p6: Process = Runtime.getRuntime().exec(p6_c)
            p6.waitFor()

            // adds new kotlin-native variable to path
            println("Adding new kotlin-native version to path...")
            val p7_c = arrayOf(
                "powershell.exe", "-Command",
                "\"[Environment]::SetEnvironmentVariable('Path', [Environment]::GetEnvironmentVariable('Path', 'User')  + ';${nativeDestDirString}\\bin', 'User')\""
            )
            val p7: Process = Runtime.getRuntime().exec(p7_c)
            p7.waitFor()

            // adding C:\kotlin-native to path
            println("Adding C:\\kotlin-native to path if it isn't already added...")
            val p8_c = arrayOf(
                "powershell.exe", "-Command",
                "\"${'$'}path = [Environment]::GetEnvironmentVariable('Path', 'User'); if (!(${'$'}path.Split(';') | Where-Object {${'$'}_ -eq 'C:\\kotlin-native'})) {[Environment]::SetEnvironmentVariable('Path', ${'$'}path + ';C:\\kotlin-native', 'User')}\""
            )
            val p8: Process = Runtime.getRuntime().exec(p8_c)
            p8.waitFor()

            // creates bat file if it doesn't exist and returns true
            val newBatFile = batFile.createNewFile()

            if (newBatFile) {
                println("Creating executable native-build.bat file")
                batFile.appendText("@echo off\njava -jar $jarFile")
            }

            // ending statements
            println("\nIf a previous installation exists, it will not have been uninstalled, but it may have been removed from user path.")
            println("If you want to run native-build again, run 'native-build' in the command line (this will only work after restart)")
        }
    }
}
