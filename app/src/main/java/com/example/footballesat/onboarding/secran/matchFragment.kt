package com.example.footballesat.onboarding.secran

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.footballesat.HomePag
import com.example.footballesat.R
import com.example.footballesat.TeamInfo
import java.text.SimpleDateFormat
import java.util.*


class matchFragment : Fragment() {
    protected lateinit var match_Day_home_img:ImageView
    protected lateinit var match_Day_Away_img:ImageView
    protected lateinit var match_Day_stadium_img:ImageView
    protected lateinit var match_Day_leag_img:ImageView
    protected lateinit var Match_Day_home_tv:TextView
    protected lateinit var match_Day_Away_tv:TextView
    protected lateinit var match_Day_Date_tv:TextView
    protected lateinit var match_Day_Time_tv:TextView
    protected lateinit var match_Day_name_stadium_tv:TextView
    protected lateinit var match_Day_name_leag_tv:TextView
    protected lateinit var match_Day_capacity_tv:TextView
    protected lateinit var Match_Day_Home_formation_tv:TextView
    protected lateinit var match_Day_Away_formation_tv:TextView
    protected lateinit var match_Day_tv_state:TextView
    protected lateinit var match_card_round_tv:TextView
    protected lateinit var match_day_view_Date:View
    protected lateinit var match_day_view_time:View
    protected lateinit var match_Day_r:RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_match2, container, false)
        var match_id:Int =0
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("matchstatic",Context.MODE_PRIVATE)
        if (sharedPreferences.contains("id_match")){
            match_id=sharedPreferences.getInt("id_match",0)
        }
        match_Day_home_img=view.findViewById(R.id.match_Day_home_img)
        match_Day_Away_img=view.findViewById(R.id.match_Day_Away_img)
        match_Day_stadium_img=view.findViewById(R.id.match_Day_stadium_img)
        match_Day_leag_img=view.findViewById(R.id.match_Day_leag_img)
        Match_Day_home_tv=view.findViewById(R.id.Match_Day_home_tv)
        match_Day_Away_tv=view.findViewById(R.id.match_Day_Away_tv)
        match_Day_Date_tv=view.findViewById(R.id.match_Day_Date_tv)
        match_Day_Time_tv=view.findViewById(R.id.match_Day_Time_tv)
        match_Day_name_stadium_tv=view.findViewById(R.id.match_Day_name_stadium_tv)
        match_Day_name_leag_tv=view.findViewById(R.id.match_Day_name_leag_tv)
        match_Day_capacity_tv=view.findViewById(R.id.match_Day_capacity_tv)
        Match_Day_Home_formation_tv=view.findViewById(R.id.match_Day_Home_formation_tv)
        match_Day_Away_formation_tv=view.findViewById(R.id.match_Day_Away_formation_tv)
        match_Day_tv_state=view.findViewById(R.id.match_Day_tv_state)
        match_day_view_Date=view.findViewById(R.id.match_day_view_Date)
        match_day_view_time=view.findViewById(R.id.match_day_view_time)
        match_Day_r=view.findViewById(R.id.match_Day_r)
        match_card_round_tv=view.findViewById(R.id.match_card_round_tv)
        match_Day_stadium_img.setImageResource(R.drawable.stadium)
        val myQueue = Volley.newRequestQueue(context)
        val url:String="https://app.sportdataapi.com/api/v1/soccer/matches/$match_id?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
        var id_team_home:Int=0
        var id_team_away:Int=0
        var id_sesan:Int=0
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val j = response.getJSONObject("data")
                id_sesan = j.getInt("season_id")
                if (j.getString("status").equals("notstarted") || j.getString("status")
                        .equals("postponed")
                ) {
                    var timeanddate: String = j.getString("match_start")
                    var arraytimeandDate = timeanddate.split(" ")
                    match_Day_Date_tv.text = arraytimeandDate[0]
                    var arraytime = arraytimeandDate[1].split(":")
                    val df: SimpleDateFormat =
                        SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH)
                    df.setTimeZone(TimeZone.getTimeZone("UTC"))
                    val date: Date = df.parse(timeanddate)
                    df.setTimeZone(TimeZone.getDefault())
                    val formattedDate: String = df.format(date)
                    var tem = formattedDate.split(" ")
                    var tem2 = tem[1].split(":")
                    var hour: Int = Integer.parseInt(tem2[0])
                    if (hour > 12) {
                        hour = hour - 12
                        match_Day_Time_tv.text = hour.toString() + ":" + tem2[1] + " PM"
                    } else {
                        match_Day_Time_tv.text = hour.toString() + ":" + tem2[1] + "AM"
                    }
                    if (j.getString("status").equals("postponed")) {
                        match_day_view_time.visibility = View.VISIBLE
                        match_day_view_Date.visibility = View.VISIBLE
                    }

                } else {
                    var jstats = j.getJSONObject("stats")
                    if (!jstats.isNull("ft_score")) {
                        match_Day_Time_tv.text = jstats.getString("ft_score")
                    } else {
                        match_Day_Time_tv.text = "0-0"
                    }
                    if (!j.isNull("minute")) {
                        match_Day_Date_tv.text = j.getInt("minute").toString() + "minute"
                    } else {
                        match_Day_Date_tv.text = "0 minute"
                    }
                }
                val j_home_team = j.getJSONObject("home_team")
                Match_Day_home_tv.text = j_home_team.getString("name")
                id_team_home = j_home_team.getInt("team_id")
                Glide.with(this)
                    .load(j_home_team.getString("logo"))
                    .override(100, 100)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(match_Day_home_img)
                val j_away_team = j.getJSONObject("away_team")
                match_Day_Away_tv.text = j_away_team.getString("name")
                id_team_away = j_away_team.getInt("team_id")
                Glide.with(this)
                    .load(j_away_team.getString("logo"))
                    .override(100, 100)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(match_Day_Away_img)
                match_Day_name_leag_tv.text = j.getJSONObject("group").getString("group_name")
                match_card_round_tv.text = j.getJSONObject("round").getString("name")
                if (!j.isNull("venue")) {
                    var x = j.getJSONObject("venue").getString("name")
                    if (x.length > 15) {
                        val y = x.split(" ")
                        if (y[0].length + y[1].length < 20) {
                            var tem = y[0] + " " + y[1] + "\n"
                            for (i in 2..y.size - 1) {
                                tem += y[i]
                            }
                            match_Day_name_stadium_tv.text = tem
                        } else {
                            var tem = y[0] + "\n"
                            for (i in 1..y.size - 1) {
                                tem += y[i]
                            }
                            match_Day_name_stadium_tv.text = tem
                        }
                    } else {
                        match_Day_name_stadium_tv.text = x
                    }
                    match_Day_capacity_tv.text = j.getJSONObject("venue").getString("capacity")

                }

                if (j.getInt("league_id") == 237) {
                    match_Day_leag_img.setImageResource(R.drawable.logo_premier_league)
                } else if (j.getInt("league_id") == 538) {
                    match_Day_leag_img.setImageResource(R.drawable.laliga)
                } else if (j.getInt("league_id") == 392) {
                    match_Day_leag_img.setImageResource(R.drawable.serie_a)
                } else if (j.getInt("league_id") == 301) {
                    match_Day_leag_img.setImageResource(R.drawable.ligue1)
                } else if (j.getInt("league_id") == 314) {
                    match_Day_leag_img.setImageResource(R.drawable.bundesliga)
                }
                var teamLinup_home: Array<playerclass> = Array(11) { playerclass() }
                var teamLinup_away: Array<playerclass> = Array(11) { playerclass() }
                var boo:Boolean=true
                  if (j.isNull("lineups")){
                    match_Day_r.visibility=View.GONE
                    match_Day_tv_state.visibility=View.VISIBLE
                    match_Day_tv_state.text="The Lineups is found"

                }else {
                      val jsonArray_lineups = j.getJSONArray("lineups")
                      if(jsonArray_lineups.length()!=0){
                         Match_Day_Home_formation_tv.text =
                              jsonArray_lineups.getJSONObject(0).getString("formation")
                          match_Day_Away_formation_tv.text =
                              jsonArray_lineups.getJSONObject(1).getString("formation")
                      for (i in 0..jsonArray_lineups.length() - 1) {
                          val jseon = jsonArray_lineups.getJSONObject(i)
                          if (jseon.getInt("team_id") == id_team_home) {
                              val home = jseon.getJSONArray("players")
                              for (j in 0..home.length() - 1) {
                                  val v = home.getJSONObject(j)
                                  var id = v.getInt("player_id")
                                  var name = v.getString("lastname")
                                  var pos = v.getJSONObject("detailed").getString("position")
                                  var number = v.getJSONObject("detailed").getInt("number")
                                  teamLinup_home[j] = playerclass(
                                      id_team_home,
                                      Match_Day_home_tv.text.toString(),
                                      id,
                                      name,
                                      pos,
                                      number,
                                      id_sesan,
                                      match_Day_name_leag_tv.text.toString()
                                  )
                              }
                          } else {
                              val away = jseon.getJSONArray("players")
                              for (j in 0..away.length() - 1) {
                                  val v = away.getJSONObject(j)
                                  var id = v.getInt("player_id")
                                  var name = v.getString("lastname")
                                  var pos = v.getJSONObject("detailed").getString("position")
                                  var number = v.getJSONObject("detailed").getInt("number")
                                  teamLinup_away[j] = playerclass(
                                      id_team_away,
                                      match_Day_Away_tv.text.toString(),
                                      id,
                                      name,
                                      pos,
                                      number,
                                      id_sesan,
                                      match_Day_name_leag_tv.text.toString()
                                  )
                              }
                          }
                      }
                  }else{
                            boo=false
                          match_Day_tv_state.visibility=View.VISIBLE
                          match_Day_tv_state.text="The Lineups isn't found in json Object"
                      }
                }
                    if(boo) {
                        if (!j.isNull("match_events")) {
                            val j_match_events = j.getJSONArray("match_events")
                            for (i in 0..j_match_events.length() - 1) {
                                val index = j_match_events.getJSONObject(i)
                                val type = index.getString("type")
                                if (index.getInt("team_id") == id_team_home) {
                                    for (j in 0..teamLinup_home.size - 1) {
                                        if (teamLinup_home[j].get_idPlayer() == index.getInt("player_id")) {
                                            if (type.equals("yellowcard")) {
                                                teamLinup_home[j].setYallow(true)
                                            }
                                            if (type.equals("redcard")) {
                                                teamLinup_home[j].setRed(true)
                                            }
                                            if (type.equals("yellowredcard")) {
                                                teamLinup_home[j].setRed_Yallow(true)
                                            }
                                            if (type.equals("goal")) {
                                                teamLinup_home[j].setGOAL(1)
                                            }
                                        }

                                    }
                                } else {
                                    for (j in 0..teamLinup_away.size - 1) {
                                        if (teamLinup_away[j].get_idPlayer() == index.getInt("player_id")) {
                                            if (type.equals("yellowcard")) {
                                                teamLinup_away[j].setYallow(true)
                                            }
                                            if (type.equals("redcard")) {
                                                teamLinup_away[j].setRed(true)
                                            }
                                            if (type.equals("yellowredcard")) {
                                                teamLinup_away[j].setRed_Yallow(true)
                                            }
                                            if (type.equals("goal")) {
                                                teamLinup_away[j].setGOAL(1)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        val Myadapter: March_Day_Adapter =
                            March_Day_Adapter(context, teamLinup_home, teamLinup_away)
                        match_Day_r.layoutManager = LinearLayoutManager(context)
                        match_Day_r.adapter = Myadapter
                    }
            //    }
            } catch (ex: Exception) {
                Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
                match_Day_tv_state.visibility=View.VISIBLE
                match_Day_tv_state.text=ex.message
            }
        }, { e ->
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            match_Day_tv_state.visibility=View.VISIBLE
            match_Day_tv_state.text=e.message
        })
        myQueue.add(request)

        match_Day_Away_img.setOnClickListener(){
            startActivity(Intent(context, TeamInfo::class.java)
                .putExtra("id_team",id_team_away)
                .putExtra("name",match_Day_Away_tv.text.toString())
                .putExtra("sesan_id",id_sesan))
        }
        match_Day_home_img.setOnClickListener(){
            startActivity(Intent(context, TeamInfo::class.java)
                .putExtra("id_team",id_team_home)
                .putExtra("name",Match_Day_home_tv.text.toString())
                .putExtra("sesan_id",id_sesan))
        }

         return view
    }

    inner class playerclass{
        protected var id_team:Int=0
        protected var team_name:String=""
        protected var id_player:Int=0
        protected var player_name:String=""
        protected var position :String=""
        protected var number :Int=0
        protected var yallow_card:Boolean = false
        protected var red_yellow_card:Boolean=false
        protected var red_card:Boolean=false
        protected var goal:Int =0
        protected var assist:Int=0
        protected var id_sesan:Int=0
        protected var leagname:String=""
        constructor(id_team:Int,team_name:String,id_player: Int, player_name: String, position: String, number: Int,id_sesan:Int,leagname:String) {
            this.team_name=team_name
            this.id_team=id_team
            this.id_player = id_player
            this.player_name = player_name
            this.position = position
            this.number = number
            this.id_sesan=id_sesan
            this.leagname=leagname
        }
        fun get_leag_name():String{return this.leagname}
        fun get_number():Int{return this.number}
        fun get_name_team():String{return this.team_name}
        fun get_id_sesan():Int{return this.id_sesan}
        fun get_idPlayer():Int{return this.id_player}
        fun get_idTeam():Int{return this.id_team}
        fun get_palyername():String{return this.player_name}
        fun getNameAndPosition():String{
            return this.position+" "+this.player_name
        }
        fun setYallow(b:Boolean){
            this.yallow_card=b
        }
        fun getYallow():Boolean{
            return this.yallow_card
        }
        fun setRed(b:Boolean){
            this.red_card=b
        }
        fun getRed():Boolean{
            return this.red_card
        }
        fun setRed_Yallow(b:Boolean){
            this.red_yellow_card=b
        }
        fun getRed_Yallow():Boolean{
            return this.red_yellow_card
        }

        fun setGOAL(i:Int){
            this.goal=this.goal+i
        }
        fun getGOAL():Int {
            return this.goal
        }
        fun setASSIST(i:Int){
            this.assist+=i
        }
        fun getASSIST():Int{return this.assist}

        constructor()
    }
    inner class March_Day_Adapter: RecyclerView.Adapter<March_Day_Adapter.MyViewHolder> {
        protected  var ct: Context?
        protected lateinit var array_home_team: Array<playerclass>
        protected lateinit var array_away_team: Array<playerclass>
        constructor(ct: Context?, home:Array<playerclass>, away:Array<playerclass> ) {
            this.ct = ct
            this.array_home_team=home
            this.array_away_team=away
        }


        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var card_matchdy_name_home_player: TextView =itemView.findViewById(R.id.card_matchdy_name_home_player)
            var card_matchdy_name_away_player: TextView =itemView.findViewById(R.id.card_matchdy_name_away_player)
            var card_match_day_home_goal: ImageView =itemView.findViewById(R.id.card_match_day_home_goal)
            var card_match_day_home_card: ImageView =itemView.findViewById(R.id.card_match_day_home_card)
            var card_match_day_away_goal: ImageView =itemView.findViewById(R.id.card_match_day_away_goal)
            var card_match_day_away_card: ImageView =itemView.findViewById(R.id.card_match_day_away_card)
            var card_match_day_home_goal_tv: TextView =itemView.findViewById(R.id.card_match_day_home_goal_tv)
            var card_match_day_away_goal_tv: TextView =itemView.findViewById(R.id.card_match_day_away_goal_tv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): March_Day_Adapter.MyViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(ct)
            var view = inflater.inflate(R.layout.match_day_card, parent, false)
            return MyViewHolder(view)
        }


        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            var player_home=array_home_team[position].getNameAndPosition()
            var player_away=array_away_team[position].getNameAndPosition()
            if(player_home.length>18){
                holder.card_matchdy_name_home_player.text=player_home
                holder.card_matchdy_name_home_player.textSize=10f
            }else{
                holder.card_matchdy_name_home_player.text=player_home
            }
            if(player_away.length>18){
                holder.card_matchdy_name_away_player.text=player_away
                holder.card_matchdy_name_away_player.textSize=10f
            }else{
                holder.card_matchdy_name_away_player.text=player_away
            }
            if(array_home_team[position].getYallow()){
                holder.card_match_day_home_card.visibility= View.VISIBLE
                holder.card_match_day_home_card.setImageResource(R.drawable.yellow_card)
            }
            if(array_home_team[position].getRed()){
                holder.card_match_day_home_card.visibility= View.VISIBLE
                holder.card_match_day_home_card.setImageResource(R.drawable.red_card)
            }
            if(array_home_team[position].getRed_Yallow()){
                holder.card_match_day_home_card.visibility= View.VISIBLE
                holder.card_match_day_home_card.setImageResource(R.drawable.yellow_red_card)
            }

            if (array_home_team[position].getGOAL()>0){
                holder.card_match_day_home_goal.visibility= View.VISIBLE
                holder.card_match_day_home_goal.setImageResource(R.drawable.ball)
                if(array_home_team[position].getGOAL()>1) {
                    holder.card_match_day_home_goal_tv.visibility = View.VISIBLE
                    holder.card_match_day_home_goal_tv.setText( "x"+array_home_team[position].getGOAL().toString() )
                }
            }

            if(array_away_team[position].getYallow()){
                holder.card_match_day_away_card.visibility= View.VISIBLE
                holder.card_match_day_away_card.setImageResource(R.drawable.yellow_card)
            }
            if(array_away_team[position].getRed()){
                holder.card_match_day_away_card.visibility= View.VISIBLE
                holder.card_match_day_away_card.setImageResource(R.drawable.red_card)
            }
            if (array_away_team[position].getGOAL()>0){
                holder.card_match_day_away_goal.visibility= View.VISIBLE
                holder.card_match_day_away_goal.setImageResource(R.drawable.ball)
                if(array_away_team[position].getGOAL()>1) {
                    holder.card_match_day_away_goal_tv.visibility= View.VISIBLE
                    holder.card_match_day_away_goal_tv.setText("x"+array_away_team[position].getGOAL().toString())
                }

            }
            if(array_away_team[position].getRed_Yallow()){
                holder.card_match_day_away_card.visibility= View.VISIBLE
                holder.card_match_day_away_card.setImageResource(R.drawable.yellow_red_card)
            }

            holder.card_matchdy_name_home_player.setOnClickListener {
                val action = matchFragmentDirections.actionMatchFragment2ToPlayerFragment(array_home_team[position].get_idPlayer(),array_home_team[position].get_name_team(),array_home_team[position].get_id_sesan(),array_home_team[position].get_number(),array_home_team[position].get_leag_name())
                findNavController().navigate(action)
            }
            holder.card_matchdy_name_away_player.setOnClickListener(){
                val action = matchFragmentDirections.actionMatchFragment2ToPlayerFragment(array_away_team[position].get_idPlayer(),array_away_team[position].get_name_team(),array_away_team[position].get_id_sesan(),array_away_team[position].get_number(),array_away_team[position].get_leag_name())
                findNavController().navigate(action)

            }

        }

        override fun getItemCount(): Int {
            return array_away_team.size
        }

    }
}