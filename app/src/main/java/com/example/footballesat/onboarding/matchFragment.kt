package com.example.footballesat.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.footballesat.MatchAdapter
import com.example.footballesat.Matchclass
import com.example.footballesat.R
import kotlinx.android.synthetic.main.fragment_match.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class matchFragment (x:Int?): Fragment() {
    val x=x
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_match, container, false)
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: String = df.format(Calendar.getInstance().time)
        var myQueue = Volley.newRequestQueue(context)

        var list:Array<Matchclass>
        // Toast.makeText(context, x.toString(), Toast.LENGTH_SHORT).show()
        var url:String="https://app.sportdataapi.com/api/v1/soccer/matches?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9&season_id=$x&date_from=$date"
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val jaray=response.getJSONArray("data")
                var length:Int=jaray.length()
                if(length>jaray.length()) {
                    length = jaray.length()
                }
                list= Array(length){ Matchclass() }
                for(i in 0..length-1){
                    val index=jaray.getJSONObject(i)
                    var status:Boolean=false
                    var ft_score:String="0-0"
                    var minute:Int=0
                    var postponed:Boolean=false
                    if(index.getString("status").equals("finished")){
                        status=true
                        postponed=false
                        ft_score=index.getJSONObject("stats").getString("ft_score")
                    }else if(index.getString("status").equals("postponed")){
                        postponed=true
                    }else if(index.getString("status").equals("inplay")){
                        minute=index.getInt("minute")
                        if(minute<45){
                            ft_score=index.getJSONObject("stats").getString("ht_score")
                        }else{
                            ft_score=index.getJSONObject("stats").getString("ft_score")
                        }
                    }
                    val match_id=index.getInt("match_id")
                    val home_team=index.getJSONObject("home_team").getString("name")
                    val away_team=index.getJSONObject("away_team").getString("name")
                    val match_start=index.getString("match_start")
                    val id_home_team=index.getJSONObject("home_team").getInt("team_id")
                    val id_away_Team=index.getJSONObject("away_team").getInt("team_id")
                    val img_home_Team=index.getJSONObject("home_team").getString("logo")
                    val img_away_Team=index.getJSONObject("away_team").getString("logo")
                    var stadium:String=""
                    var capacity:Int=0
                    if(index.isNull("venue")) {
                    }else{
                        stadium = index.getJSONObject("venue").getString("name")
                        capacity= index.getJSONObject("venue").getInt("capacity")
                    }
                    var leag_name:String=index.getJSONObject("group").getString("group_name")
                    list[i]= Matchclass(match_id,leag_name,home_team,away_team,
                        match_start,id_home_team,id_away_Team,
                        img_home_Team,img_away_Team,stadium, capacity,status,postponed,ft_score, minute)
                }
                val Myadapter2: MatchAdapter = MatchAdapter(requireContext(), list ,"")
                view.r1.layoutManager = LinearLayoutManager(context)
                view.r1.adapter = Myadapter2
            } catch (ex: Exception) {
                Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
                view.tv_show_pr.text=ex.message
                view.tv_show_pr.visibility=View.VISIBLE
            }
        }, { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            view.tv_show_pr.text=e.message
            view.tv_show_pr.visibility=View.VISIBLE
        })

        myQueue.add(request)
        return view
    }



}