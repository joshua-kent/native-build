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

/**
 * Displays information about the native-build utility,
 * then waits for user input to exit out of the function.
 *
 * @author Joshua Kent
 */
fun displayInfo() {
    println("""
        
        Kotlin/Native Builder
        ---------------------
        
        Kotlin/Native Builder is a non-official utility for easily
        building the Kotlin/Native compiler and setting it up for
        immediate use without hassle.
        
        After you have built for the first time with this utility, you
        will be able to call 'native-build' in the command line to run
        again.
        
        Created by ${com.nativebuild.AUTHOR}.
        Version ${com.nativebuild.FULLVERSION}.
        Source code is available at ${com.nativebuild.REPO}.
        
        For license information, either see the 'LICENSE' file in the
        file containing the source code, or type 'license' in any menu
        screen while running this utility.
    """.trimIndent())

    readLine()
    println("\n----------------------------------------------------------------\n\n")
    com.nativebuild.main(arrayOf("Kotlin"))
}