package com.example.footballesat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.widget.CheckBox
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
import com.google.gson.Gson
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TeamInfo : AppCompatActivity() {
    protected lateinit var f:FavoriteTeamAndLeague
    protected lateinit var tv_name:TextView
    protected lateinit var tv_city:TextView
    protected lateinit var tv_name_leag:TextView
    protected lateinit var tv_name_stadium:TextView
    protected lateinit var tv_capacity:TextView
    protected lateinit var img_team:ImageView
    protected lateinit var team_info_img_back:ImageView
    protected lateinit var img_leag_logo:ImageView
    protected lateinit var ch_folow: CheckBox
    protected lateinit var sharedPreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_info)
        var id_team = intent.getIntExtra("id_team", 0)
        var name_team = intent.getStringExtra("name")
        var sesan_id = intent.getIntExtra("sesan_id", 0)
        sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("save")) {
            val gson: Gson = Gson()
            val s = sharedPreferences.getString("save", "")
            f = gson.fromJson(s, FavoriteTeamAndLeague::class.java)
        }
        var b: Boolean = false
        tv_name = findViewById(R.id.tv_name_team)
        tv_name.text=name_team
        tv_city = findViewById(R.id.tv_city)
        img_team = findViewById(R.id.img_team)
        team_info_img_back = findViewById(R.id.team_info_img_back)
        ch_folow = findViewById(R.id.ch_follow)
        tv_name_leag = findViewById(R.id.tv_name_leag)
        tv_name_stadium = findViewById(R.id.tv_name_stadium)
        tv_capacity = findViewById(R.id.tv_capacity)
        img_leag_logo =findViewById(R.id.img_leag_logo)
        var r1: RecyclerView = findViewById(R.id.r1_team_info)
        var r2: RecyclerView = findViewById(R.id.r2_team_info)
        var img_logo_stadium: ImageView = findViewById(R.id.img_logo_stadium)
        img_logo_stadium.setImageResource(R.drawable.stadium)
        var img_leag_logo: ImageView = findViewById(R.id.img_leag_logo)
        img_leag_logo.setImageResource(R.drawable.ic_launcher_foreground)
        var myQueue = Volley.newRequestQueue(this)
        try {
            var h: HashMap<Int, String> = f.getTeams()
            for (key in h.keys) {
                if (key == id_team) {
                    ch_folow.isChecked = true
                    b = true
                    break
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
         val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
         val date: String = df.format(Calendar.getInstance().time)
        var url2="https://app.sportdataapi.com/api/v1/soccer/teams/$id_team?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
        val request2: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url2, null, { response ->
            try {
                val j =response.getJSONObject("data")
                tv_name.text=j.getString("name").toString()
                tv_city.text=j.getJSONObject("country").getString("name")
                Glide.with(this)
                    .load(j.getString("logo"))
                    .override(120, 120)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_team)

            } catch (ex: Exception) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }, { e ->
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        })
        myQueue.add(request2)
        var url="https://app.sportdataapi.com/api/v1/soccer/matches?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9&season_id=$sesan_id&date_from=$date"
        //  var i:Int=0
        var arrayofMatch:Array<Matchclass> = Array(12){Matchclass()}
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val jaray=response.getJSONArray("data")
                var counter:Int=0
                for(i in 0..jaray.length()-1 ){
                    val j =jaray.getJSONObject(i)
                    if(counter<12) {
                        if (j.getJSONObject("home_team").getString("name").equals(name_team) ||
                            j.getJSONObject("away_team").getString("name").equals(name_team)
                        ) {
                            val match_id: Int = j.getInt("match_id")
                            var minute:Int=0
                            if(!j.isNull("minute")) {
                                minute = j.getInt("minute")
                            }
                            val time: String = j.getString("match_start")
                            val home_team :String=j.getJSONObject("home_team").getString("name")
                            val away_team :String=j.getJSONObject("away_team").getString("name")
                            val home_team_id :Int=j.getJSONObject("home_team").getInt("team_id")
                            val away_team_id :Int=j.getJSONObject("away_team").getInt("team_id")
                            val home_team_logo :String=j.getJSONObject("home_team").getString("logo")
                            val away_team_logo :String=j.getJSONObject("away_team").getString("logo")
                            val legname:String =j.getJSONObject("group").getString("group_name")
                            var stadium:String=""
                            var capacity:Int=0
                            if(!j.isNull("venue")){
                                stadium=j.getJSONObject("venue").getString("name")
                                capacity=j.getJSONObject("venue").getInt("capacity")
                            }
                            var status:Boolean=false
                            var postponed:Boolean=false
                            var ft_score:String =""
                            if (j.getString("status").equals("finished")) {
                                status=true
                            }
                            if (j.getString("status").equals("postponed")) {
                                postponed=true
                            }
                            if (j.getString("status").equals("inplay")) {
                                if(minute<45){
                                    ft_score=j.getJSONObject("stats").getString("ht_score")
                                }else {
                                    ft_score=j.getJSONObject("stats").getString("ft_score")
                                }
                            } else {
                                ft_score="0-0"
                            }
                            arrayofMatch[counter]=Matchclass(match_id,legname,home_team,away_team,time
                                , home_team_id,away_team_id,home_team_logo,away_team_logo,stadium,capacity, status, postponed, ft_score, minute)
                            counter++
                        }

                    }else{
                        for (k in 0..arrayofMatch.size-1){
                            if(arrayofMatch[k].getId_Home()==id_team){
                                Toast.makeText(this,arrayofMatch[k].gethometeam(),Toast.LENGTH_LONG).show()
                                tv_name_leag.text =arrayofMatch[k].getleag_name()
                                tv_name_stadium.text=arrayofMatch[k].getstadiumName()
                                tv_capacity.text=arrayofMatch[k].getcapacity().toString()
                                setImage(arrayofMatch[k].getleag_name())
                                break
                            }
                        }
                        var arrayonegame:Matchclass=arrayofMatch[0]
                        var arraygame:Array<Matchclass> = Array(11){Matchclass()}
                        for (i in 1..arrayofMatch.size-1){
                            arraygame[i-1]=arrayofMatch[i]
                        }
                        val Myadapter: MatchAdapter = MatchAdapter(this, arrayonegame)
                        r1.layoutManager = LinearLayoutManager(this)
                        r1.adapter = Myadapter
                        val Myadapter2: MatchAdapter = MatchAdapter(this, arraygame ,"")
                        r2.layoutManager = LinearLayoutManager(this)
                        r2.adapter = Myadapter2
                        break
                    }

                }
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
                tv_name_leag.text=ex.message
            }
        }, { e ->
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            tv_name_leag.text=e.message
        })
        myQueue.add(request)
        ch_folow.setOnClickListener() {
            if (b == true) {
                // h.remove(id_team)
                f.removeTeam(id_team)
                b = false
                Toast.makeText(this, "remove", Toast.LENGTH_LONG).show()
            } else {
                //   h.put(id_team,name_team)
                f.addTeam(id_team, name_team)
                b = true
                Toast.makeText(this, "add", Toast.LENGTH_LONG).show()
            }
            Toast.makeText(this,f.printAll(),Toast.LENGTH_LONG).show()
        }
        team_info_img_back.setOnClickListener(){
            save()
        }
    }

    override fun onBackPressed() {
      //  super.onBackPressed()
        save()
    }

    fun save (){
        val editor: SharedPreferences.Editor? =sharedPreferences.edit()
        editor?.apply {
            var gson = Gson()
            var json:String =gson.toJson(f)
            putString("save",json)
            // tv.text=x.getname()+" "+x.getage()
            apply()
        }
        startActivity(Intent(this,HomePag::class.java))
        finish()
    }
    fun setImage(name:String){
        if(name.equals("Premier League")){
            img_leag_logo.setImageResource(R.drawable.premier_league)
        }else if(name.equals("LaLiga")){
            img_leag_logo.setImageResource(R.drawable.laliga)
        }else if(name.equals("Serie A")){
            img_leag_logo.setImageResource(R.drawable.serie_a)
        }else if(name.equals("Ligue 1")){
            img_leag_logo.setImageResource(R.drawable.bundesliga)
        }else if(name.equals("Bundesliga")){
            img_leag_logo.setImageResource(R.drawable.ligue1)
        }
    }
}
