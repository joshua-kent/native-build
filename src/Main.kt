import com.nativebuild.Build
import com.nativebuild.Menu
import com.nativebuild.Info
import com.nativebuild.License


fun main(args: Array<String>) {

    val mainMenu = Menu()
    var run = false
    var destExists = false

    println("Running Kotlin/Native Builder v${com.nativebuild.VERSION}")
    println("Current Kotlin version: ${KotlinVersion.CURRENT}\n")
    println("Type 'info' at any input prompt to show more information about this utility.")
    println("Type 'license' at any input prompt to show this product's license.")

    if (!Build.nativeDestDir.exists()) {
        val answer = mainMenu.prompt(
                "A new version of Kotlin/Native should be available!",
                arrayOf("Yes", "No", "Info", "License")
        )

        when (answer?.toLowerCase()) {
            in arrayOf("y", "yes") -> run = true
            in arrayOf("n", "no") -> System.exit(0)
            "info" -> System.exit(0) // TODO
            "license" -> System.exit(0) // TODO
            else -> System.exit(0) // TODO
        }
    } else {
        val answer = mainMenu.prompt(
                "You are up-to-date. Do you want to force re-install anyway?",
                arrayOf("Yes", "No", "Info", "License")
        )

        when (answer?.toLowerCase()) {
            in arrayOf("y", "yes") -> run = true
            in arrayOf("n", "no") -> System.exit(0)
            "info" -> System.exit(0) // TODO
            "license" -> System.exit(0) // TODO
            else -> System.exit(0) // TODO
        }

        destExists = true
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
