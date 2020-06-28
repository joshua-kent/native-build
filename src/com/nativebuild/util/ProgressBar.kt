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
    for (file in directory.listFiles()) {
        if (file.isFile) {
            length += file.length()
        } else { // if it is a directory
            length += sizeOfDirectory(file) // test that directory's size then add it
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
 * @return 0 if the progress bar successfully ran
 * @author Joshua Kent
 */
fun progressBar(process: Process, filePath: String, maxValue: Long,
                barLength: Int = 64, updateWait: Long = 500, reverse: Boolean = false): Int {

    class ProgressBarException(message: String): Exception(message)

    print("[" + ".".repeat(barLength) + "] 0%")

    val file = File(filePath)

    // main progress bar logic
    while (process.isAlive) {
        // get file length depending on if it is a file or directory
        var currentFileSize = if (file.exists()) {
            if (file.isFile) { file.length() } else { sizeOfDirectory(file) }
        } else { 0 }

        // display progress
        if (currentFileSize >= maxValue) {
            if (!reverse) {
                print("\r[" + "#".repeat(barLength) + "] 100%")
            } else {
                print("\r[" + ".".repeat(barLength) + "] 0%")
            }
        } else {
            var amountOfHashes = (currentFileSize.toFloat() / maxValue.toFloat() * barLength).toInt()
            var amountOfStops = barLength - amountOfHashes
            var percentDone = (currentFileSize.toFloat() / maxValue.toFloat() * 100).toInt()
            if (!reverse) {
                print("\r[" + "#".repeat(amountOfHashes) + ".".repeat(amountOfStops) + "] $percentDone%")
            } else {
                print("\r[" + "#".repeat(amountOfStops) + ".".repeat(amountOfHashes) + "] ${100 - percentDone}%")
            }
        }
        Thread.sleep(updateWait)
    }
    print("\r")
    return 0
}