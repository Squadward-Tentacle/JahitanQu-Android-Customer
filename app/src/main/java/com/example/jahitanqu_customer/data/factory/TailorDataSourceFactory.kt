package com.example.jahitanqu_customer.data.factory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.jahitanqu_customer.model.Tailor
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */
class TailorDataSourceFactory @Inject constructor(private val tailorDataSource: TailorDataSource) : DataSource.Factory<Int, Tailor>() {

    val tailorLiveDataSource =  MutableLiveData<TailorDataSource>()
    val showShimmer:LiveData<Boolean> = tailorDataSource.showShimmer

    override fun create(): DataSource<Int, Tailor> {
        tailorLiveDataSource.postValue(tailorDataSource)
        return tailorDataSource
    }




}