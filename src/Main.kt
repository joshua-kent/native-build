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

package com.nativebuild

import com.nativebuild.util.Build
import com.nativebuild.util.Menu
import kotlin.system.exitProcess

/**
 * Displays the start menu for the utility and returns whether to
 * run or not.
 *
 * @param redoing Should be left alone to default (`false`); displays
 * 'invalid output and try again' if `true`. (default: `false`)
 * @author Joshua Kent
 */
fun startMenu(redoing: Boolean = false): Boolean {
    val mainMenu = Menu()
    var run = false
    val answer: String?

    if (!redoing) {
        answer = if (!Build.nativeDestDir.exists()) {
            mainMenu.prompt(
                    "A new version of Kotlin/Native should be available!\nDo you want to update?",
                    arrayOf("Yes", "No", "Info", "License")
            )
        } else {
            mainMenu.prompt(
                    "You are up-to-date. Do you want to force re-install anyway?",
                    arrayOf("Yes", "No", "Info", "License")
            )
        }
    } else {
        println("Invalid input; please try again.")
        answer = mainMenu.getInput()
    }

    when (answer?.toLowerCase()) {
        in arrayOf("y", "yes") -> run = true
        in arrayOf("n", "no") -> exitProcess(0)
        "info" -> com.nativebuild.util.displayInfo()
        "license" -> com.nativebuild.util.displayLicense()
        else -> startMenu(true)
    }

    return run
}

/**
 * The main function where the application is run from.
 *
 * @param args For internal usage to be recognised by Java.
 * @author Joshua Kent
 */
fun main(args: Array<String>) {
    var destExists = Build.nativeDestDir.exists()

    println("Running Kotlin/Native Builder v${com.nativebuild.VERSION}")
    println("Current Kotlin version: ${KotlinVersion.CURRENT}\n")
    println("Type 'info' at any input prompt to show more information about this utility.")
    println("Type 'license' at any input prompt to show this product's license.\n")

    val run = startMenu()

    if (run) {
        if (!com.nativebuild.TESTING) {
            println("\n--- Many of these steps usually take a while, please wait. ---\n")


            Build.downloadZip()

            // deleting previous installation if exists
            if (destExists) Build.removeOldInstallation()

            // extracts .zip file to folder
            Build.extractZip()

            // deletes .zip file
            Build.deleteZip()

            // adding .jar file to C:\kotlin-native
            Build.addJarToPath()

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

    Build.extractZip()
    Build.deleteZip()

    exitProcess(0)
    // to avoid menu -> license -> menu -> license -> yes, completing then returning to first menu screen.
}
