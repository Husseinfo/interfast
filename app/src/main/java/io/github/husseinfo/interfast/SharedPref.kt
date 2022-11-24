package io.github.husseinfo.interfast

import android.content.Context

const val APP_INTRO_FLAG = "APP_INTRO_FLAG"

fun saveFirstRun(context: Context) {
    val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putBoolean(APP_INTRO_FLAG, true)
    editor.apply()
}

fun isFirstRun(context: Context): Boolean {
    val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    return !sharedPref.getBoolean(APP_INTRO_FLAG, false)
}
