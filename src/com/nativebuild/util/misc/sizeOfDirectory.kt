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

package com.nativebuild.util.misc

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
fun sizeOfDirectory(directory: File): Long {
    var length = 0L
    if (directory.exists() && directory.isDirectory) {
        try {
            for (file in directory.listFiles()!!) {
                if (file.isFile) {
                    length += file.length()
                } else if (file.isDirectory) { // if it is a directory
                    length += sizeOfDirectory(file) // test that directory's size then add it
                }
            }
        } catch (exc: java.lang.NullPointerException) {
            return 0L
        }
    }

    return length
}