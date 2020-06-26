import com.nativebuild.build.build

fun main(args: Array<String>) {
    val TESTING = false // enable to stop building when testing new features
    val VERSION = "1.0.0rc3" // TODO: for future update, to check for new major releases on GitHub

    var run = false
    var destExists = false

    println("Running Kotlin/Native Builder v$VERSION")
    println("Current Kotlin version: ${KotlinVersion.CURRENT}\n")

    if (!build.nativeDestDir.exists()) {
        println("A new version of Kotlin/Native should be available!")
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

            build.downloadZip()

            // deleting previous installation if exists
            if (destExists) build.removeOldInstallation()

            // extracts .zip file to folder
            build.extractZip()

            // deletes .zip file
            build.deleteZip()

            // adding .jar file to C:\kotlin-native
            if (build.jarPath != build.jarFile) build.addJarToPath()

            // creates bat file if it doesn't exist and returns true
            build.appendToBatFile()

            /* adding to path */

            build.removeOldVersionPaths()

            // adds new kotlin-native variable to path
            build.addNewVersionToPath()

            // adding C:\kotlin-native to path
            build.addBaseToPath()

            // ending statements
            println("\nIf a previous installation exists, it will not have been uninstalled, but it may have been removed from user path.")
            println("If you want to run native-build again, run 'native-build' in the command line (this will only work after restart)")
        }
    }
}
