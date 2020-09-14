package com.example.jahitanqu_customer.presentation.views.main.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.BaseContract
import com.example.jahitanqu_customer.model.FcmToken
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.AuthViewModel
import com.example.jahitanqu_customer.presentation.viewmodel.TailorViewModel
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTopRatedTailorAdapter
import com.example.jahitanqu_customer.presentation.views.main.home.slider.SliderAdapter
import com.example.jahitanqu_customer.presentation.views.main.home.slider.SliderItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject
import kotlin.math.abs

class HomeFragment : Fragment(), View.OnClickListener, BaseContract {

    lateinit var navController: NavController

    lateinit var recycleTopRatedTailorAdapter: RecycleTopRatedTailorAdapter

    lateinit var handler: Handler

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var tailorViewModel: TailorViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        handler = Handler()
        btnViewAll.setOnClickListener(this)
        tailorViewModel.getTopRatedTailor()
        rvTopTailor.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycleTopRatedTailorAdapter = RecycleTopRatedTailorAdapter(listOf())
        rvTopTailor.adapter = recycleTopRatedTailorAdapter
        tailorViewModel.tailorTopRatedList.observe(viewLifecycleOwner, Observer { it ->
            recycleTopRatedTailorAdapter = RecycleTopRatedTailorAdapter(it)
            recycleTopRatedTailorAdapter.baseContract = this
            recycleTopRatedTailorAdapter.showShimmer =false
            rvTopTailor.adapter = recycleTopRatedTailorAdapter
        })
        createImageSlider()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnViewAll -> {
                navController.navigate(R.id.toTailorListFragment)
            }
        }
    }

    override fun itemClickListener(id: String) {
        val bundle = bundleOf("idTailor" to id)
        navController.navigate(R.id.toTailorDetailFragment, bundle)
    }

    private fun createImageSlider() {
        var listImage = mutableListOf(
            SliderItem("https://firebasestorage.googleapis.com/v0/b/jahitanqu-customer.appspot.com/o/banner.png?alt=media&token=e310c61e-e895-424c-9801-9cd7c33a930c"),
            SliderItem("https://firebasestorage.googleapis.com/v0/b/jahitanqu-customer.appspot.com/o/banner1.png?alt=media&token=5f25198b-a4d7-40a8-9664-c660959c4148"),
            SliderItem("https://firebasestorage.googleapis.com/v0/b/jahitanqu-customer.appspot.com/o/banner2.png?alt=media&token=32a758b3-9942-48db-84ff-4a9ca03a634b"),
            SliderItem("https://firebasestorage.googleapis.com/v0/b/jahitanqu-customer.appspot.com/o/banner3.png?alt=media&token=55153f82-fd54-44e5-90f6-577252525dbb")
        )
        viewPagerImageSlider.adapter = SliderAdapter(listImage, viewPagerImageSlider)
        viewPagerImageSlider.clipToPadding = false
        viewPagerImageSlider.offscreenPageLimit = 1
        viewPagerImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransform = CompositePageTransformer()
        compositePageTransform.addTransformer(MarginPageTransformer(40))
        viewPagerImageSlider.setPageTransformer(compositePageTransform)
        viewPagerImageSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(sliderRunnable)
                handler.postDelayed(sliderRunnable, 3000)
            }
        })
    }

    val sliderRunnable = Runnable() {
        run {
            viewPagerImageSlider.currentItem = viewPagerImageSlider.currentItem + 1
        }
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(sliderRunnable,3000)
    }

}
