import com.nativebuild.Build

fun main(args: Array<String>) {
    var run = false
    var destExists = false

    println("Running Kotlin/Native Builder v${com.nativebuild.VERSION}")
    println("Current Kotlin version: ${KotlinVersion.CURRENT}\n")
    println("Type 'info' at any input prompt to show more information about this utility.")
    println("Type 'license' at any input prompt to show this product's license.")

    if (!Build.nativeDestDir.exists()) {
        println("A new version of Kotlin/Native should be available!")
        println("Do you want to install?")
        println("- Yes")
        println("- No")
        println("- Info")
        println("- License\n")
        val answer : String = readLine()!!
        if (answer == "Y" || answer == "y") run = true
        // todo add 'info' option

    } else {
        println("You are up-to-date. Do you want to force re-install anyway?")
        println("- Yes")
        println("- No")
        println("- Info")
        println("- License\n")
        val answer : String = readLine()!!
        destExists = true
        if (answer == "Y" || answer == "y") run = true
    }

    if (run) {
        if (!com.nativebuild.TESTING) {
            println("\n--- These steps usually take a while, please wait. ---\n")

            Build.downloadZip()

            // deleting previous installation if exists
            if (destExists) Build.removeOldInstallation()

            // extracts .zip file to folder
            Build.extractZip()

            // deletes .zip file
            Build.deleteZip()

            // adding .jar file to C:\kotlin-native
            if (Build.jarPath != Build.jarFile) Build.addJarToPath()

            // creates bat file if it doesn't exist and returns true
            Build.appendToBatFile()

            /* adding to path */

            Build.removeOldVersionPaths()

            // adds new kotlin-native variable to path
            Build.addNewVersionToPath()

            // adding C:\kotlin-native to path
            Build.addBaseToPath()

            // ending statements
            println("\nIf a previous installation exists, it will not have been uninstalled, but it may have been removed from user path.")
            println("If you want to run native-build again, run 'native-build' in the command line (this will only work after restart)")
        }
    }
}
