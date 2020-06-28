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
 * Contains functions that allow you to create a standardised
 * menu screen with a query and responses, and the ability to
 * see history of all queries and responses (given the same
 * object).
 *
 *
 * @author Joshua Kent
 */
class Menu() {

    private var lastInput: String? = null //input
    private var lastShow: Pair<String?, Array<String>>? = null // [query, options]
    private var lastSetMenu: Pair<String?, Array<String>>? = null // [query, options]
    private var lastPrompt: Pair<Pair<String?, Array<String>>, String?>? = null // [[query, options], input]

    private var getInputHistory: MutableList<String?> = mutableListOf() // [input, ...]
    private var showHistory: MutableList<Pair<String?, Array<String>>?> = mutableListOf() // [[query, options], ...]
    private var setMenuHistory: MutableList<Pair<String?, Array<String>>?> = mutableListOf() // [[query, options], ...]
    private var promptHistory: MutableList<Pair<Pair<String?, Array<String>>, String?>?> = mutableListOf() // [[[query, options], input], ...]

    /**
     * Asks the user a question, displays options, waits for input then returns their
     * input.
     *
     * The query, options and input are added to the prompt history.
     *
     * @param query What will be asked to the user.
     * @param options An array of what the options will be for the user to choose.
     * @return The user's input, (if the user did not enter anything and just
     * clicked enter, `null` will be returned).
     * @author Joshua Kent
     */
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

    /**
     * Shows whatever was last set with `setMenu()`
     *
     * The query and answer is added to the last show history.
     *
     * @return If the function was successful, `true` is returned.
     * If not (such as nothing being ever set with the `setMenu` function,
     * then `false` is returned.
     * @author Joshua Kent
     */
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

    /**
     * Asks for the user's input.
     *
     * @return If the user inputted something, it is returned.
     * If they only pressed enter, `null` is returned.
     * @author Joshua Kent
     */
    fun getInput(): String? {
        print(": ")
        lastInput = readLine()!!
        if (lastInput == "") lastInput = null

        getInputHistory.add(lastInput)

        return lastInput
    }

    /**
     * Sets a new menu's query and options to be shown with `show()`.
     *
     * The menu is added to the set menu history.
     *
     * @param query What will be asked to the user.
     * @param options An array of what the options will be for the user to choose.
     * @author Joshua Kent
     */
    fun setMenu(query: String?, options: Array<String>) {
        lastSetMenu = Pair(query, options)
        setMenuHistory.add(lastSetMenu)
    }


    /**
     * @return What was last inputted with `getInput()` OR `prompt()`.
     * @author Joshua Kent
     */
    fun lastInput() = lastInput

    /**
     * @return What was last shown with `show()`.
     * @author Joshua Kent
     */
    fun lastShow() = lastShow

    /**
     * @return What menu was last set with `setMenu()`.
     * @author Joshua Kent
     */
    fun lastSetMenu() = lastSetMenu

    /**
     * @return Returns what was last asked, the options and the user's input with `prompt()`,
     * as `((query, options), input)`.
     * @author Joshua Kent
     */
    fun lastPrompt() = lastPrompt


    /**
     * @return Everything that the user has inputted with `getInput()` OR `prompt()`.
     * @author Joshua Kent
     */
    fun getInputHistory() = getInputHistory

    /**
     * @return Every menu that has been displayed with `show()`.
     * @author Joshua Kent
     */
    fun showHistory() = showHistory

    /**
     * @return Every menu that has been set with `setMenu()`.
     * @author Joshua Kent
     */
    fun setMenuHistory() = setMenuHistory

    /**
     * @return The query, options and input from every `prompt()`.
     * @author Joshua Kent
     */
    fun promptHistory() = promptHistory


    /**
     * Clears the input history (from `getInput()` OR `prompt()`).
     *
     * @author Joshua Kent
     */
    fun clearGetInputHistory() { getInputHistory = mutableListOf() }

    /**
     * Clears the show history (from `show()`).
     *
     * @author Joshua Kent
     */
    fun clearShowHistory() { showHistory = mutableListOf() }

    /**
     * Clears the set menu history (from `setMenu()`).
     *
     * @author Joshua Kent
     */
    fun clearSetMenuHistory() { setMenuHistory = mutableListOf() }

    /**
     * Clears the prompt history (from `prompt()`).
     *
     * @author Joshua Kent
     */
    fun clearPromptHistory() { promptHistory = mutableListOf() }

}