package com.example.jahitanqu_customer.presentation.views.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahitanqu_customer.JahitanQu

import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.model.Tailor
import com.example.jahitanqu_customer.presentation.viewmodel.TailorViewModel
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleCommentAdapter
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTailorAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_tailor_detail.*
import kotlinx.android.synthetic.main.fragment_tailor_list.*
import javax.inject.Inject

class TailorDetailFragment : Fragment() {

    @Inject
    lateinit var tailorViewModel: TailorViewModel

    lateinit var recycleCommentAdapter: RecycleCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tailor_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idTailor = arguments?.getString("idTailor")
        pbLoading.visibility = View.VISIBLE
        rvRatingAndReview.layoutManager = LinearLayoutManager(context)
        tailorViewModel.getTailorById(idTailor!!)
        tailorViewModel.tailor.observe(viewLifecycleOwner, Observer {
            pbLoading.visibility = View.GONE
            if (it.avatarImageUrl.isNotEmpty()){
                Picasso.get()
                    .load(it.avatarImageUrl)
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_photo)
                    .into(ivTailor)
            }
            tvTailorName.text = "${it.firstname} ${it.lastname}"
            tvTailorAddress.text = it.address.addressName
            rbRatingTailor.rating = it.rating.toFloat()
            tvTailorDescription.text = it.description
            recycleCommentAdapter = RecycleCommentAdapter(it.comment)
            rvRatingAndReview.adapter = recycleCommentAdapter
        })
    }
}
