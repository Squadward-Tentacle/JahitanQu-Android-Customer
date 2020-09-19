package com.example.jahitanqu_customer.common.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson


/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
class AccountHelper(context: Context) {

    val KEY_TOKEN = "pref.KeyToken"
    val KEY_EMAIL = "pref.KeyEmail"
    val KEY_FIRSTNAME = "pref.KeyFirstname"
    val KEY_LASTNAME = "pref.KeyLastname"
    val KEY_IDCUSTOMER = "pref.KeyIdCustomer"
    val KEY_PHOTO_URL = "pref.KeyPhotoUrl"
    val KEY_PHONE_NUMBER = "pref.KeyNumber"
    val KEY_ADDRESS_NAME = "pref.KeyAddressName"
    val KEY_LATITUDE = "pref.KeyLatitude"
    val KEY_LONGITUDE = "pref.KeyLongitude"
    val KEY_CUSTOMER = "pref.KeyCustomer"
    val KEY_CUSTOMER_FCM = "pref.keyCustomerFcm"
    val KEY_SUB_DISTRICT = "pref.KeySubDistrict"
    val KEY_CITY ="pref.KeyCity"
    val KEY_POSTAL_CODE = "pref.KeyPostalCode"


    val prefs: SharedPreferences = context.getSharedPreferences("myPref", 0)

    val prefsFcmToken : SharedPreferences = context.getSharedPreferences("myPrefFcmToken",0)

    var keyToken: String?
        get() = prefs.getString(KEY_TOKEN, "")
        set(token) = prefs.edit().putString(KEY_TOKEN, token).apply()

    var keyEmail: String?
        get() = prefs.getString(KEY_EMAIL, "")
        set(value) = prefs.edit().putString(KEY_EMAIL, value).apply()

    var keyFirstname: String?
        get() = prefs.getString(KEY_FIRSTNAME, "")
        set(value) = prefs.edit().putString(KEY_FIRSTNAME, value).apply()

    var keyLastName: String?
        get() = prefs.getString(KEY_LASTNAME, "")
        set(value) = prefs.edit().putString(KEY_LASTNAME, value).apply()

    var keyPhotoUrl: String?
        get() = prefs.getString(KEY_PHOTO_URL, "")
        set(value) = prefs.edit().putString(KEY_PHOTO_URL, value).apply()

    var keyPhoneNumber: String?
        get() = prefs.getString(KEY_PHONE_NUMBER, "")
        set(value) = prefs.edit().putString(KEY_PHONE_NUMBER, value).apply()

    var keyIdCustomer: String?
        get() = prefs.getString(KEY_IDCUSTOMER, "")
        set(value) = prefs.edit().putString(KEY_IDCUSTOMER, value).apply()

    var keyAddressName: String?
        get() = prefs.getString(KEY_ADDRESS_NAME, "")
        set(value) = prefs.edit().putString(KEY_ADDRESS_NAME, value).apply()

    var keyLatitude: Float?
        get() = prefs.getFloat(KEY_LATITUDE, 0.0F)
        set(value) = prefs.edit().putFloat(KEY_LATITUDE, value!!).apply()

    var keyLongitude: Float?
        get() = prefs.getFloat(KEY_LONGITUDE, 0.0F)
        set(value) = prefs.edit().putFloat(KEY_LONGITUDE, value!!).apply()

    var keyCity:String?
        get() = prefs.getString(KEY_CITY, "")
        set(value) = prefs.edit().putString(KEY_CITY, value!!).apply()

    var keySubDistrict:String?
        get() = prefs.getString(KEY_SUB_DISTRICT, "")
        set(value) = prefs.edit().putString(KEY_SUB_DISTRICT, value!!).apply()

    var keyPostalCode:String?
        get() = prefs.getString(KEY_POSTAL_CODE, "")
        set(value) = prefs.edit().putString(KEY_POSTAL_CODE, value!!).apply()

    fun clear() {
        prefs.edit().clear().apply()
    }

    var keyCustomerFcm: String?
        get() = prefsFcmToken.getString(KEY_CUSTOMER_FCM, "")
        set(value) = prefsFcmToken.edit().putString(KEY_CUSTOMER_FCM, value!!).apply()

    fun clearFcmToken(){
        prefsFcmToken.edit().clear().apply()
    }

    fun setCustomerPref(customer: Any) {
        val gson = Gson()
        val json = gson.toJson(customer)
        prefs.edit().putString(KEY_CUSTOMER, json).apply()
    }
}