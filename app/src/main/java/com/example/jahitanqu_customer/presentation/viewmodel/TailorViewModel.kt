package com.example.jahitanqu_customer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.jahitanqu_customer.data.repository.TailorRepository
import com.example.jahitanqu_customer.model.Tailor
import javax.inject.Inject

/**
 * Created by Maulana Ibrahim on 07/September/2020
 * Email maulibrahim19@gmail.com
 */
class TailorViewModel @Inject constructor(private val tailorRepository: TailorRepository):ViewModel() {

    var tailorTopRatedList:LiveData<List<Tailor>> = tailorRepository.tailorTopRatedList
    var tailorList:LiveData<List<Tailor>> = tailorRepository.tailorList

    fun getTopRatedTailor(){
        tailorRepository.getTopRatedTailor()
    }

    fun getTailor(page:Int){
        tailorRepository.getTailor(page)
    }
}