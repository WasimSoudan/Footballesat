package com.example.footballesat.onboarding

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.collections.get as get1

class FragmentAdapter(list:ArrayList<HashMap<Int,String>>, fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val list =list
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val id_sesan =list[position].keys.toString().replace("]","")
            .replace("[","").toInt()
        return matchFragment(id_sesan)
    }

}
