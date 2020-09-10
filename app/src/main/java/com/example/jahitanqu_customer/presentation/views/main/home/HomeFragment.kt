package com.example.jahitanqu_customer.presentation.views.main.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
import com.example.jahitanqu_customer.prefs
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
        rvTopTailor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tailorViewModel.tailorTopRatedList.observe(viewLifecycleOwner, Observer { it ->
            recycleTopRatedTailorAdapter = RecycleTopRatedTailorAdapter(it)
            recycleTopRatedTailorAdapter.baseContract = this
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

    fun createImageSlider() {
        var listImage = mutableListOf(
            SliderItem("https://www.lampost.co/upload/tukang-jahit-kebanjiran-pemesanan-masker.jpeg"),
            SliderItem("https://cdn2.tstatic.net/manado/foto/bank/images/tukang-jahit_20150716_174612.jpg"),
            SliderItem("https://sukabumiupdate.com/uploads/news/images/770x413/-_200414181151-600.jpg"),
            SliderItem("https://rembun.desa.id/foto_berita/images/jahit_rembun(3)_compress.jpg")
        )
        viewPagerImageSlider.adapter = SliderAdapter(listImage, viewPagerImageSlider)
        viewPagerImageSlider.clipToPadding = false
        viewPagerImageSlider.offscreenPageLimit = 3
        viewPagerImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransform = CompositePageTransformer()
        compositePageTransform.addTransformer(MarginPageTransformer(40))
        compositePageTransform.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r + 0.15f
        }
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
