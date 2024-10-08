package com.example.bloodbank.utils

import android.os.SystemClock

class GenerateID {
    companion object {
        fun generate(): String {
            val code: Long = SystemClock.uptimeMillis()
            return code.toString()
        }
    }
}