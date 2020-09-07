package com.example.jahitanqu_customer.presentation.views.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.TailorViewModel
import com.example.jahitanqu_customer.presentation.views.main.home.adapter.RecycleTailorAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment(),View.OnClickListener {

    lateinit var navController: NavController

    lateinit var recycleTailorAdapter: RecycleTailorAdapter

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
        btnLogout.setOnClickListener(this)
        rvTopTailor.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        tailorViewModel.getTopRatedTailor(1)
        tailorViewModel.tailorTopRatedList.observe(viewLifecycleOwner, Observer { it ->
            recycleTailorAdapter = RecycleTailorAdapter(it)
            rvTopTailor.adapter = recycleTailorAdapter
        })
    }

    override fun onClick(p0: View?) {
        when(p0){
            btnLogout ->{
                firebaseAuth.signOut()
                prefs.clear()
                navController.navigate(R.id.toLoginFragment)
            }
        }
    }


}
