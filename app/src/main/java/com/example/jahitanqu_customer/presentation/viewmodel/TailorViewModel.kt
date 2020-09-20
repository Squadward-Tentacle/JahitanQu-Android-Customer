package com.example.jahitanqu_customer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.jahitanqu_customer.data.factory.TailorDataSource
import com.example.jahitanqu_customer.data.factory.TailorDataSourceFactory
import com.example.jahitanqu_customer.data.repository.TailorRepository
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.Tailor
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */
class TailorViewModel @Inject constructor(
    private val tailorRepository: TailorRepository,
    tailorDataSourceFactory: TailorDataSourceFactory
) : ViewModel() {

    val tailorPagedList: LiveData<PagedList<Tailor>>
    val tailor:LiveData<Tailor>
    private val  liveDataSource: LiveData<TailorDataSource>
    val showShimmer:LiveData<Boolean>

    init {
        liveDataSource = tailorDataSourceFactory.tailorLiveDataSource
        tailor = tailorRepository.tailor
        val config = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(6).build()
        tailorPagedList = LivePagedListBuilder(tailorDataSourceFactory, config).build()
        showShimmer = tailorDataSourceFactory.showShimmer
    }

    val tailorTopRatedList: LiveData<List<Tailor>> = tailorRepository.tailorTopRatedList
    val tailorNearbyList:LiveData<List<Tailor>> = tailorRepository.tailorNearbyList


    fun getTopRatedTailor() {
        tailorRepository.getTopRatedTailor()
    }

    fun getTailorById(idTailor:String){
        tailorRepository.getTailorById(idTailor)
    }

    fun getNearbyTailor(address: Address){
        tailorRepository.getNearbyTailor(address)
    }
}