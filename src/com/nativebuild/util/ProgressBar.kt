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
import com.nativebuild.util.misc.*

/**
 * Creates a progress bar based on several parameters.
 *
 * @param filePath The path of the file that is is progressing
 * @param maxValue The value that the file should reach in bytes
 * @param barLength The length of the bar. Maximum length is 128 (default: 64)
 * @param updateWait How long to wait between updates in milliseconds (default: 500)
 * @param reverse Reverses the progress bar (mainly used for when deleting something) (default: false)
 * @param measuredIn What units the progress bar should be measured in. Valid values:
 * 'B', 'KB', 'MB', 'GB', null. To omit the units, set `maxValue` in bytes and
 * set this variable to `null`. (default: null)
 * @return 0 if the progress bar successfully ran
 * @author Joshua Kent
 */
fun progressBar(filePath: String, maxValue: Long,
                barLength: Int = 64, updateWait: Long = 500, reverse: Boolean = false,
                measuredIn: String? = null): Int {

    class ProgressBarException(message: String): NativeBuildException(message)

    if (barLength > 100) { throw ProgressBarException("Maximum bar length (100) exceeded.") }

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

    /* main progress bar logic */


    // get file length depending on if it is a file or directory
    var currentFileSize = if (file.exists()) {
        if (file.isFile) { file.length() } else { sizeOfDirectory(file) }
    } else { 0 }

    // add units to end (ie. [134KB/5928KB])
    when (measuredIn?.toUpperCase()) {
        "KB" -> currentFileSize /= 1024
        "MB" -> currentFileSize /= 1048576
        "GB" -> currentFileSize /= 1073741824
        !in arrayOf(null, "B") -> throw ProgressBarException(
                "Unknown 'measuredIn' value (not 'B', 'KB', 'MB', 'GB', or null).")
    }
    val endUnits = if (measuredIn != null) {
        if (!reverse) {
            " ($currentFileSize${measuredIn.toUpperCase()}/$MAXVALUE${measuredIn.toUpperCase()})"
        } else {
            " (${MAXVALUE - currentFileSize}${measuredIn.toUpperCase()}/$MAXVALUE${measuredIn.toUpperCase()})"
        }
    } else { "" }

    /* display progress */

    val amountOfHashes = (currentFileSize.toFloat() / MAXVALUE.toFloat() * barLength).toInt()
    val amountOfStops = barLength - amountOfHashes
    val percentDone = (currentFileSize.toFloat() / MAXVALUE.toFloat() * 100).toInt()

    var barOutput = ""
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


    // remove excess based on largest amount the progress bar could be at 100% based on settings
    var excessNum = barLength + 7
    if (measuredIn != null) { excessNum += 7 + (measuredIn.length + MAXVALUE.toInt()) * 2 }


    print(" ".repeat(excessNum) + "\r$barOutput\r")
    Thread.sleep(updateWait)

    return 0
}