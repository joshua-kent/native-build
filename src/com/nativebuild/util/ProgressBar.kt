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

import java.io.File
import kotlin.math.pow

/**
 * Gets the size of every item in a directory.
 *
 * Thanks to Tendayi Mawushe's answer here:
 * https://stackoverflow.com/questions/2149785/get-size-of-folder-or-file
 *
 * @param directory The directory to be tested
 * @author Joshua Kent
 */
private fun sizeOfDirectory(directory: File): Long {
    var length = 0L
    if (directory.listFiles() != null) {
        for (file in directory.listFiles()) {
            if (file.isFile) {
                length += file.length()
            } else if (file.isDirectory) { // if it is a directory
                length += sizeOfDirectory(file) // test that directory's size then add it
            }
        }
    }

    return length
}

/**
 * Creates a progress bar based on several parameters.
 *
 * @param process The process of the file (to tell when the download is done)
 * @param filePath The path of the file that is is progressing
 * @param maxValue The value that the file should reach in bytes
 * @param barLength The length of the bar (default: 64)
 * @param updateWait How long to wait between updates in milliseconds (default: 500)
 * @param reverse Reverses the progress bar (mainly used for when deleting something) (default: false)
 * @param unitMeasured What units the progress bar should be measured in. Valid values:
 * 'B', 'KB', 'MB', 'GB', null. To omit the units, set `maxValue` in bytes and
 * set this variable to `null`. (default: null)
 * @return 0 if the progress bar successfully ran
 * @author Joshua Kent
 */
fun progressBar(process: Process, filePath: String, maxValue: Long,
                barLength: Int = 64, updateWait: Long = 500, reverse: Boolean = false,
                measuredIn: String? = null): Int {

    class ProgressBarException(message: String): Exception(message)

    print("[" + ".".repeat(barLength) + "] 0%")

    val file = File(filePath)

    // change max value to correct units
    var MAXVALUE: Long
    if (measuredIn?.toUpperCase() == "KB") {
        MAXVALUE = maxValue / 1024
    } else if (measuredIn?.toUpperCase() == "MB") {
        MAXVALUE = maxValue / 1048576
    } else if (measuredIn?.toUpperCase() == "GB") {
        MAXVALUE = maxValue / 1073741824
    } else { MAXVALUE = maxValue }

    // main progress bar logic
    var lastBarLength = 0
    var barOutput = ""
    var excess = ""
    while (process.isAlive) {
        // get file length depending on if it is a file or directory
        var currentFileSize = if (file.exists()) {
            if (file.isFile) { file.length() } else { sizeOfDirectory(file) }
        } else { 0 }

        // add units to end (ie. [134KB/5928KB])
        var endUnits: String
        when (measuredIn?.toUpperCase()) {
            "KB" -> currentFileSize /= 1024
            "MB" -> currentFileSize /= 1048576
            "GB" -> currentFileSize /= 1073741824
            !in arrayOf(null, "B") -> throw ProgressBarException(
                    "Unknown 'measuredIn' value (not 'B', 'KB', 'MB', 'GB', or null).")
        }
        endUnits = if (measuredIn != null) {
            " ($currentFileSize${measuredIn.toUpperCase()}/$MAXVALUE${measuredIn.toUpperCase()})"
        } else { "" }

        /* display progress */

        var amountOfHashes = (currentFileSize.toFloat() / MAXVALUE.toFloat() * barLength).toInt()
        var amountOfStops = barLength - amountOfHashes
        var percentDone = (currentFileSize.toFloat() / MAXVALUE.toFloat() * 100).toInt()

        lastBarLength = barOutput.length
        if (currentFileSize >= MAXVALUE) {
            if (!reverse) {
                barOutput = "\r[" + "#".repeat(barLength) + "] 100%$endUnits"
            } else {
                barOutput = "\r[" + ".".repeat(barLength) + "] 0%$endUnits"
            }
        } else {
            if (!reverse) {
                barOutput = "\r[" + "#".repeat(amountOfHashes) + ".".repeat(amountOfStops) + "] $percentDone%$endUnits"
            } else {
                barOutput = "\r[" + "#".repeat(amountOfStops) + ".".repeat(amountOfHashes) + "] ${100 - percentDone}%$endUnits"
            }
        }

        // excess is to make sure that the entire stream of the current line gets wiped
        excess = if (barOutput.length < lastBarLength) { " ".repeat(lastBarLength - barOutput.length) } else { "" }
        print("\r$excess" + barOutput)

        Thread.sleep(updateWait)
    }

    print("\r" + " ".repeat(barOutput.length) + "\r")
    //TODO: FIX ZIP FILES BEING UNDELETABLE (LOOK INTO java.io.ZipFile etc)
    return 0
}