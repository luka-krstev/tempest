package com.elfak.tempest.common

object Validate {
    fun isEmail(text: String): Boolean {
        return text.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    fun isLengthBetween(text: String, lower: Int, upper: Int): Boolean {
        return text.length in (lower)..<upper
    }

    fun isPhoneNumber(text: String): Boolean {
        return text.isNotEmpty() && android.util.Patterns.PHONE.matcher(text).matches();
    }
}