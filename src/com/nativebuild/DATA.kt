package com.nativebuild

const val VERSION = "1.0.0rc5-PRE1"  // TODO: for future update, to check for new major releases on GitHub
object LastUpdated {
    const val TIME = "17:00"
    const val DATE = "27 June 2020"
    const val FULL = "$TIME $DATE"
}
const val FULLVERSION = "$VERSION ${LastUpdated.FULL}"
const val AUTHOR = "Joshua Kent"
const val TESTING = false // enable to stop building when testing