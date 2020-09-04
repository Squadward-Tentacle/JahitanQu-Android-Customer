package com.example.jahitanqu_customer.common.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.jahitanqu_customer.JahitanQu

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
class AccountHelper(context: Context) {

    val KEY_TOKEN = "pref.KeyToken"
    val KEY_EMAIL = "pref.KeyEmail"
    val KEY_FIRSTNAME = "pref.KeyFirstname"
    val KEY_LASTNAME = "pref.KeyLastname"
    val KEY_PHOTO_URL = "pref.KeyPhotoUrl"
    val KEY_PHONE_NUMBER = "pref.KeyNumber"

    val prefs : SharedPreferences = context.getSharedPreferences("myPref",0)

    var keyToken: String?
        get() = prefs.getString(KEY_TOKEN, "")
        set(token) = prefs.edit().putString(KEY_TOKEN, token).apply()

    var keyEmail:String?
        get() = prefs.getString(KEY_EMAIL,"")
        set(value) = prefs.edit().putString(KEY_EMAIL,value).apply()

    var keyFirstname:String?
        get() = prefs.getString(KEY_FIRSTNAME,"")
        set(value) = prefs.edit().putString(KEY_FIRSTNAME,value).apply()

    var keyPhotoUrl:String?
        get() = prefs.getString(KEY_PHOTO_URL,"")
        set(value) = prefs.edit().putString(KEY_PHOTO_URL,value).apply()

    var keyPhoneNumber:String?
        get() = prefs.getString(KEY_PHONE_NUMBER,"")
        set(value) = prefs.edit().putString(KEY_PHONE_NUMBER,value).apply()

    fun clear(){
        prefs.edit().clear().apply()
    }
}