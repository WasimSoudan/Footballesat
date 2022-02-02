package com.example.footballesat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MatchDay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_day)
        var id_match = intent.getIntExtra("id_match", 0)
        val sharedPreferences: SharedPreferences =getSharedPreferences("matchstatic",Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? =sharedPreferences.edit()
        editor?.apply {
            putInt("id_match",id_match)
            apply()
        }
}
}
