package com.example.jahitanqu_customer.views.authentication

/**
 * Created by Maulana Ibrahim on 03/September/2020
 * Email maulibrahim19@gmail.com
 */
class AuthContract {

    interface login{
        fun onSuccess()
        fun onFailure()
    }

    interface register{
        fun onSuccess()
        fun onFailure()
    }
}