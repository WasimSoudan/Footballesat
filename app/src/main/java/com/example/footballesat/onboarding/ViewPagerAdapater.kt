package com.example.footballesat.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapater(list:ArrayList<Fragment>,fm :FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fm,lifecycle) {
    private val fragmentlsit =list
    override fun getItemCount(): Int {
       return fragmentlsit.size
    }

    override fun createFragment(position: Int): Fragment {
       return fragmentlsit[position]
    }
}