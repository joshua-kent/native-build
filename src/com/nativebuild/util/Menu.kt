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

class Menu(query: String? = null, options: Array<String> = arrayOf()) {

    private var lastInput: String? = null //input
    private var lastShow: Pair<String?, Array<String>>? = null // [query, options]
    private var lastSetMenu: Pair<String?, Array<String>>? = null // [query, options]
    private var lastPrompt: Pair<Pair<String?, Array<String>>, String?>? = null // [[query, options], input]

    private var getInputHistory: MutableList<String?> = mutableListOf() // [input, ...]
    private var showHistory: MutableList<Pair<String?, Array<String>>?> = mutableListOf() // [[query, options], ...]
    private var setMenuHistory: MutableList<Pair<String?, Array<String>>?> = mutableListOf() // [[query, options], ...]
    private var promptHistory: MutableList<Pair<Pair<String?, Array<String>>, String?>?> = mutableListOf() // [[[query, options], input], ...]

    fun prompt(query: String?, options: Array<String>): String? {
        if (query != null) println(query + "\n")
        println("Options:")
        for (option in options) println("- $option")
        println()
        print(": ")
        lastInput = readLine()!!

        if (lastInput == "") lastInput = null

        // update variables
        lastPrompt = Pair(Pair(query, options), lastInput)
        promptHistory.add(lastPrompt)

        return lastInput
    }

    fun show(): Boolean {
        val lastSetMenuCopy = lastSetMenu
        if (lastSetMenuCopy != null) {
            val query = lastSetMenuCopy.first
            val options = lastSetMenuCopy.second
            if (query != null) println(query + "\n")
            println("Options:")
            for (option in options) println("- $option")
            println()

            // update variables
            lastShow = Pair(query, options)
            showHistory.add(lastShow)
        } else {
            return false
        }

        return true
    }

    fun getInput(): String? {
        print(": ")
        lastInput = readLine()!!
        if (lastInput == "") lastInput = null

        getInputHistory.add(lastInput)

        return lastInput
    }

    fun setMenu(query: String?, options: Array<String>) {
        lastSetMenu = Pair(query, options)
        setMenuHistory.add(lastSetMenu)
    }


    fun lastInput() = lastInput

    fun lastShow() = lastShow

    fun lastSetMenu() = lastSetMenu

    fun lastPrompt() = lastPrompt


    fun getInputHistory() = getInputHistory

    fun showHistory() = showHistory

    fun setMenuHistory() = setMenuHistory

    fun promptHistory() = promptHistory


    fun clearGetInputHistory() { getInputHistory = mutableListOf() }

    fun clearShowHistory() { showHistory = mutableListOf() }

    fun clearSetMenuHistory() { setMenuHistory = mutableListOf() }

    fun clearPromptHistory() { promptHistory = mutableListOf() }



    init { if (query != null || options.isNotEmpty()) prompt(query, options) }

}