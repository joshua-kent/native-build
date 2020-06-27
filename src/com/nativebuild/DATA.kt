package com.nativebuild

const val VERSION = "1.0.0rc5-PRE3"  // TODO: for future update, to check for new major releases on GitHub
object LastUpdated {
    const val TIME = "21:43"
    const val DATE = "27 June 2020"
    const val FULL = "$TIME $DATE"
}
const val FULLVERSION = "$VERSION ${LastUpdated.FULL}"
const val AUTHOR = "Joshua Kent"
const val TESTING = false // enable to stop building when testing
const val LICENSE = """""" // TODO: Add license