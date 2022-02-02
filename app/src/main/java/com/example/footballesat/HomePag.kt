package com.example.footballesat

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.footballesat.onboarding.FragmentAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home_pag.*
import kotlinx.android.synthetic.main.activity_home_pag.view.*


class HomePag : AppCompatActivity() {
   protected lateinit var f:FavoriteTeamAndLeague
    protected lateinit var tabLayout: TabLayout
    protected lateinit var viewPager: ViewPager2
    protected lateinit var fragmentAdapter: FragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pag)
        val sharedPreferences: SharedPreferences =getSharedPreferences("share", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("save")){
            val gson: Gson = Gson()
            val s=sharedPreferences.getString("save","")
            f= gson.fromJson(s, FavoriteTeamAndLeague::class.java)
        }
        if (sharedPreferences.contains("save1")){
            val gson: Gson = Gson()
            val s=sharedPreferences.getString("save","")
            f= gson.fromJson(s, FavoriteTeamAndLeague::class.java)
        }
        var fr: FragmentContainerView=findViewById(R.id.fragmentContainerView2_homepage)
      // Toast.makeText(this,fr.id.toString(),Toast.LENGTH_LONG).show()
        var drawerLayout: DrawerLayout =findViewById(R.id.drawerLayout)
        findViewById<ImageView>(R.id.imageView).setOnClickListener(){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        var name_fragmant:String="HomeFragment"
        val navigatioView: NavigationView =findViewById(R.id.navigatioView)

        navigatioView.setNavigationItemSelectedListener {
            val sharedPreferences1: SharedPreferences =getSharedPreferences("share1", Context.MODE_PRIVATE)
            var tem:String=""
            if (sharedPreferences1.contains("selected_Fragment")){
                 tem=sharedPreferences1.getString("selected_Fragment","").toString()
            }
            if(tem.isNotEmpty()){
                name_fragmant=tem
                tem=""
                sharedPreferences1.edit().clear().commit()
            }
            if(it.itemId==R.id.setting){
                if(name_fragmant.equals("HomeFragment")) {
                    findNavController(R.id.fragmentContainerView2_homepage).navigate(R.id.action_homePage_Fragment_to_setting_Fragment)
                }else if(name_fragmant.equals("FavoriteFragmant")){
                    findNavController(R.id.fragmentContainerView2_homepage).navigate(R.id.action_favorite_Fragment_to_setting_Fragment)
                }
                name_fragmant="SettingFragment"
                drawerLayout.closeDrawer(GravityCompat.START)

            }else if(it.itemId==R.id.menu_fav){
                if(name_fragmant.equals("HomeFragment")) {
                    findNavController(R.id.fragmentContainerView2_homepage).navigate(R.id.action_homePage_Fragment_to_favorite_Fragment)
                }else if(name_fragmant.equals("SettingFragment")){
                    findNavController(R.id.fragmentContainerView2_homepage).navigate(R.id.action_setting_Fragment_to_favorite_Fragment)
                }
                name_fragmant="FavoriteFragmant"
                drawerLayout.closeDrawer(GravityCompat.START)
            }else if(it.itemId==R.id.menu_lan){
                Toast.makeText(applicationContext,"language",Toast.LENGTH_LONG).show()
            }else if(it.itemId==R.id.mene_Home){
                if(name_fragmant.equals("FavoriteFragmant")){
                    findNavController(R.id.fragmentContainerView2_homepage).navigate(R.id.action_favorite_Fragment_to_homePage_Fragment)
                }else if(name_fragmant.equals("SettingFragment")){
                    findNavController(R.id.fragmentContainerView2_homepage).navigate(R.id.action_setting_Fragment_to_homePage_Fragment)
                }
                name_fragmant="HomeFragment"
                drawerLayout.closeDrawer(GravityCompat.START)
            }else if(it.itemId==R.id.men_logout){
                Firebase.auth.signOut()
                sharedPreferences.edit().clear().commit()
                val sharedPref =getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
                val editor=sharedPref.edit()
                editor.putBoolean("finach",false)
                editor.apply()
                moveTaskToBack(true)
                finish()
            }
            true
        }
    }
    override fun onBackPressed(){
        moveTaskToBack(true)
        finish()
    }

    }

