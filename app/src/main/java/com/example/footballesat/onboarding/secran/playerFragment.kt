package com.example.footballesat.onboarding.secran

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.footballesat.R

class playerFragment : Fragment() {
    private val args:playerFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_player, container, false)
        val id =args.id
        val nameteam=args.teamname
        val id_sesan=args.idSesean
        val shirt =args.shirt
        val leag_name=args.leagName
        val fragment_player_close: TextView =view.findViewById(R.id.fragment_player_close)
        val player_name: TextView =view.findViewById(R.id.fragment_player_name)
        val player_team: TextView =view.findViewById(R.id.fragment_player_team)
        val player_age: TextView =view.findViewById(R.id.fragment_player_age)
        val player_country: TextView =view.findViewById(R.id.fragment_player_country)
        val player_shirt: TextView =view.findViewById(R.id.fragment_player_shirt)
        val player_name_leag: TextView =view.findViewById(R.id.fragment_player_name_leag)
        val player_match: TextView =view.findViewById(R.id.fragment_player_match)
        val player_home: TextView =view.findViewById(R.id.fragment_player_home)
        val player_away: TextView =view.findViewById(R.id.fragment_player_away)
        val player_year: TextView =view.findViewById(R.id.fragment_player_year)
        val player_weight: TextView =view.findViewById(R.id.fragment_player_weight)
        val player_height: TextView =view.findViewById(R.id.fragment_player_height)
        val player_pl_matches: TextView =view.findViewById(R.id.fragment_player_pl_matches)
        val player_minutes: TextView =view.findViewById(R.id.fragment_player_minutes)
        val player_penalties: TextView =view.findViewById(R.id.fragment_player_penalties)
        val frameLayoutleagstatic: FrameLayout =view.findViewById(R.id.frameLayoutleagstatic)
        val myQueue = Volley.newRequestQueue(context)
        player_team.text=nameteam
        player_shirt.text=shirt.toString()
        player_name_leag.text=leag_name
        val url:String="https://app.sportdataapi.com/api/v1/soccer/players/$id?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val j= response.getJSONObject("data")
                val id =j.getInt("player_id")
                //Toast.makeText(context,id.toString(),Toast.LENGTH_LONG).show()
                player_name.text=j.getString("firstname")+" "+j.getString("lastname")
                if(!j.isNull("age")){player_age.text=j.getInt("age").toString()}else{player_age.text="null"}
                if(!j.isNull("country")){player_country.text =j.getJSONObject("country").getString("name")}else{player_country.text="null"}
                if(!j.isNull("birthday")){player_year.text=j.getString("birthday")}else{player_year.text="null"}
                if(!j.isNull("weight")){player_weight.text=j.getInt("weight").toString()}else{player_weight.text="null"}
                if(!j.isNull("height")){player_height.text =j.getInt("height").toString()}else{player_height.text="null"}

            } catch (ex: Exception) {
                Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
            }

        }, { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

        })
        myQueue.add(request)
        val url2:String="https://app.sportdataapi.com/api/v1/soccer/topscorers?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9&season_id=$id_sesan"
        val request2: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url2, null, { response ->
            try {
                val jdata=response.getJSONArray("data")
                var t :Boolean=true
                for(i in 0..jdata.length()-1){
                    val j =jdata.getJSONObject(i)
                    if(id ==j.getJSONObject("player").getInt("player_id")){
                        player_match.text=j.getJSONObject("goals").getInt("overall").toString()
                        player_home.text=j.getJSONObject("goals").getInt("home").toString()
                        player_away.text=j.getJSONObject("goals").getInt("away").toString()
                        if(!j.isNull("matches_played")){player_pl_matches.text=j.getInt("matches_played").toString()}
                        if(!j.isNull("minutes_played")){player_minutes.text=j.getInt("minutes_played").toString()}
                        if(!j.isNull("penalties")){player_penalties.text=j.getInt("penalties").toString()}else{player_penalties.text="0"}
                        t=false
                        break
                    }
                }
                if(t){
                    frameLayoutleagstatic.visibility=View.GONE
                }

            } catch (ex: Exception) {
                Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
            }
        }, { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        })

        myQueue.add(request2)

        fragment_player_close.setOnClickListener(){
            getActivity()?.onBackPressed()
        }


        return view
    }


    }
