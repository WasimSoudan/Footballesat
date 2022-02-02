package com.example.footballesat

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson

class splashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //findNavController().navigate(R.id.action_splashFragment_to_viewPagerfragment)
        Handler().postDelayed({
            if(onBoardingFinished()){
                if(save()){
                    findNavController().navigate(R.id.action_splashFragment_to_homePag)
                }else {
                  //  findNavController().navigate(R.id.action_viewPagerfragment_to_mainActivity)
                    findNavController().navigate(R.id.action_splashFragment_to_viewPagerfragment)
                }
            }else {

                findNavController().navigate(R.id.action_splashFragment_to_viewPagerfragment)
            }

        },1000)
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    private fun onBoardingFinished():Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("finach",false)
    }

    private fun save():Boolean{
        val sharedPref = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        if (sharedPref.contains("save")){
          return true
        }
        return false
    }

}