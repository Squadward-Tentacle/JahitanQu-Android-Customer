package com.example.jahitanqu_customer.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Maulana Ibrahim on 08/September/2020
 * Email maulibrahim19@gmail.com
 */
data class MetaData(
    val page: Int,
    @SerializedName("page_count")
    val pageCount: Int,
    @SerializedName("per_page")
    val perPage: Int
)