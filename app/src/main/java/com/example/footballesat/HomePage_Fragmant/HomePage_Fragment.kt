package com.example.footballesat.HomePage_Fragmant

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.footballesat.FavoriteTeamAndLeague
import com.example.footballesat.R
import com.example.footballesat.onboarding.ApaterScrollView
import com.example.footballesat.onboarding.FragmentAdapter
import com.example.footballesat.onboarding.TameAndLeagScrollView
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_view_pagerfragment.*


class HomePage_Fragment : Fragment() {
    lateinit var f :FavoriteTeamAndLeague
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_home_page_, container, false)
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("save")){
            val gson: Gson = Gson()
            val s=sharedPreferences.getString("save","")
            f= gson.fromJson(s, FavoriteTeamAndLeague::class.java)
        }
        val tabLayout=view.findViewById<TabLayout>(R.id.tab_layout)
        val ac_home_r1: RecyclerView =view.findViewById(R.id.ac_home_r1)
        val viewPager:ViewPager2=view.findViewById(R.id.view_pager2)
        val myQueue = Volley.newRequestQueue(context)
        val id_team=f.get_id_team()
        val id_leag= arrayListOf<Int>(1980,2022,2029,2100,2020)
        var listofscroll:ArrayList<TameAndLeagScrollView> = ArrayList()
        for(i in 0..id_team.size-1) {
            var u: String =
                "https://app.sportdataapi.com/api/v1/soccer/teams/" + id_team[i] + "?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
            val request: JsonObjectRequest =
                JsonObjectRequest(Request.Method.GET, u, null, { response ->
                    try {
                        val j = response.getJSONObject("data")
                        val id = j.getInt("team_id")
                        val name = j.getString("name")
                        val logo = j.getString("logo")
                        listofscroll.add(TameAndLeagScrollView(id, name, logo, 0))
                    } catch (ex: Exception) {
                        Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
                    }
                    if(i ==id_team.size-1) {
                        for(j in 0..id_leag.size-1){
                            var name_leag:String=""
                            var img:Int=0
                            if (id_leag[j]==1980){
                                name_leag="Premier League"
                                img=R.drawable.logo_premier_league
                            }else if(id_leag[j]==2029) {
                                name_leag="Laliga"
                                img=R.drawable.laliga
                            }else if(id_leag[j]==2100){
                                name_leag="Serie_A"
                                img=R.drawable.serie_a
                            }else if(id_leag[j]==2022){
                                name_leag="Ligue 1"
                                img=R.drawable.ligue1
                            }else if(id_leag[j]==2020){
                                name_leag="Bundesliga"
                                img=R.drawable.bundesliga
                            }
                            listofscroll.add(TameAndLeagScrollView(id_leag[j],name_leag,"",img))
                        }
                  }
                    val Myadapter: ApaterScrollView = ApaterScrollView(context, listofscroll,f.get_idcounter_sesan())
                    ac_home_r1.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    ac_home_r1.adapter = Myadapter
                },null)
            myQueue.add(request)

        }
        val fr: FragmentManager =getParentFragmentManager()
        var z:ArrayList<HashMap<Int,String>> = f.test()
       // Toast.makeText(context,z.toString(),Toast.LENGTH_LONG).show()
        //  var team=f.test2()
        // z.addAll(team)
        val fragmentAdapter= FragmentAdapter(z,fr,lifecycle)
        viewPager.adapter=fragmentAdapter
        for (i in 0..z.size-1){
            val r =z[i].keys.toString().replace("]","").replace("[","").toInt()
            tabLayout!!.addTab(tabLayout!!.newTab().setText(z[i].get(r)))
        }

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        return view
    }
}

