package com.example.footballesat.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.footballesat.R
import com.example.footballesat.onboarding.secran.firstScreen
import com.example.footballesat.onboarding.secran.secandScreen
import com.example.footballesat.onboarding.secran.therdScreen
import kotlinx.android.synthetic.main.fragment_view_pagerfragment.view.*


class ViewPagerfragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pagerfragment, container, false)
        val fragmentlist = arrayListOf<Fragment>(firstScreen(),secandScreen(),therdScreen())
        val adapter=ViewPagerAdapater(fragmentlist,requireActivity().supportFragmentManager,lifecycle)
        view.viewPager.adapter=adapter

        return view

    }
}
