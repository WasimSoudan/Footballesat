package com.example.footballesat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.SimpleDateFormat
import java.util.*

class Matchclass {
    protected var match_id: Int = 0
    protected var homeTema: String = ""
    protected var awayTeam: String = ""
    protected var time: String = ""
    protected var id_home_team: Int = 0
    protected var id_away_Team: Int = 0
    protected var img_home_Team: String = ""
    protected var img_away_Team: String = ""
    protected var stadium: String = ""
    protected var capacity: Int = 0
    protected var Leagname: String = ""
    protected var status:Boolean=false
    protected var postponed:Boolean=false
    protected var ft_score:String=""
    protected var minute:Int=0

    constructor(
        match_id: Int,
        Leagname: String,
        homeTema: String,
        awayTeam: String,
        time: String,
        id_home_team: Int,
        id_away_Team: Int,
        img_home_Team: String,
        img_away_Team: String,
        stadium: String,
        capacity: Int,
        status:Boolean,
        postponed:Boolean,
        ft_score:String,
        minute:Int
    ) {
        this.match_id = match_id
        this.homeTema = homeTema
        this.awayTeam = awayTeam
        this.time = time
        this.id_home_team = id_home_team
        this.id_away_Team = id_away_Team
        this.img_home_Team = img_home_Team
        this.img_away_Team = img_away_Team
        this.stadium = stadium
        this.capacity = capacity
        this.Leagname = Leagname
        this.status=status
        this.postponed=postponed
        this.ft_score=ft_score
        this.minute=minute
    }
    fun getId_Home():Int{return this.id_home_team}
    fun get_Status():Boolean{return this.status}
    fun get_Ft_score():String{return this.ft_score}
    fun get_postponed():Boolean{return this.postponed}
    fun gethometeam(): String {
        val name=formatnameteam(this.homeTema)
        return name
    }
    fun getID_match():Int{return this.match_id}
    fun get_minute():Int{return this.minute}

    fun getAwayteam(): String {
        val name=formatnameteam(this.awayTeam)
        return name
    }

    fun getleag_name(): String {
        return this.Leagname
    }

    fun getstadiumName(): String {
        return this.stadium
    }

    fun getcapacity(): Int {
        return this.capacity
    }

    fun getLogoHome(): String {
        return this.img_home_Team
    }

    fun getLogoAway(): String {
        return this.img_away_Team
    }

    fun getTimehour(): String {
        val df: SimpleDateFormat = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH)
        df.setTimeZone(TimeZone.getTimeZone("UTC"))
        val date: Date = df.parse(time)
        df.setTimeZone(TimeZone.getDefault())
        val formattedDate: String = df.format(date)
        var tem = formattedDate.split(" ")
        var tem2 = tem[1].split(":")
        var hour: Int = Integer.parseInt(tem2[0])
        if (hour > 12) {
            hour = hour - 12
            return hour.toString() + ":" + tem2[1] + " PM"
        } else {
            return hour.toString() + ":" + tem2[1] + "AM"
        }
    }

    fun getdate(): String {
        var tem = time.split(" ")
        return tem[0]
    }

    fun formatnameteam(name:String):String{
        val name2=name.split(" ")
        var templet:String=""
        for(i in 0..name2.size-1) {
            if (!name2[i].contains("FC",true)) {
                    if(!name2[i].contains("CF",true)) {
                        templet += name2[i] + " "
                    }
            }
        }
        if(templet.length>15){
            val tem =templet.split(" ")
            if(tem[0].length<5){
                return tem[0]+" "+tem[1]
            }
            if(tem[0].equals("Manchester")){
                return tem[1]
            }
           return tem[0]
        }

        return templet
    }


    constructor()

}
class MatchAdapter: RecyclerView.Adapter<MatchAdapter.MyViewHolder>  {
    protected lateinit var ct: Context
    protected lateinit var array: Array<Matchclass>
    constructor(ct: Context, array: Matchclass){
        this.ct=ct
        this.array= arrayOf(array)
    }
    constructor(ct: Context, array2: Array<Matchclass>, s:String){
        this.ct=ct
        this.array= array2
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_home_team: ImageView =itemView.findViewById(R.id.img_home_team)
        var img_away_team: ImageView =itemView.findViewById(R.id.img_away_team)
        var tv_home_team : TextView =itemView.findViewById(R.id.tv_home_team)
        var tv_date_score: TextView =itemView.findViewById(R.id.tv_date_score)
        var tv_time_score: TextView =itemView.findViewById(R.id.tv_time_score)
        var tv_away_team: TextView =itemView.findViewById(R.id.tv_away_team)
        var view_time:View=itemView.findViewById(R.id.view_time)
        var view_Date:View=itemView.findViewById(R.id.view_Date)
        var con_scorer:ConstraintLayout=itemView.findViewById(R.id.table_con_scorer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchAdapter.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.match, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            var home_team:String=array[position].gethometeam()
            var away_team:String=array[position].getAwayteam()
            holder.tv_home_team.text=home_team
            holder.tv_away_team.text=away_team
            Glide.with(ct)
                .load(array[position].getLogoHome())
                .override(120, 120)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_home_team)
            Glide.with(ct)
                .load(array[position].getLogoAway())
                .override(120, 120)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_away_team)
            if(array[position].get_Status()) {
                holder.tv_time_score.text=array[position].get_Ft_score()
                holder.tv_date_score.text="Finish"
                holder.view_Date.visibility=View.GONE
                holder.view_time.visibility=View.GONE
            }else if(array[position].get_minute()>0) {
                holder.tv_time_score.text=array[position].get_Ft_score()
                holder.tv_date_score.text=array[position].get_minute().toString()+" minute"
            } else{
                holder.tv_date_score.text = array[position].getdate()
                holder.tv_time_score.text = array[position].getTimehour()
                if(array[position].get_postponed()){
                    holder.view_Date.visibility=View.VISIBLE
                    holder.view_time.visibility=View.VISIBLE
                }
            }
          /*  if(array[position].get_postponed()){
                holder.view_Date.visibility=View.VISIBLE
                holder.view_time.visibility=View.VISIBLE
                holder.tv_date_score.text = array[position].getdate()
                holder.tv_time_score.text = array[position].getTimehour()
            }*/

            holder.con_scorer.setOnClickListener {
                val Intent : Intent = Intent(ct,MatchDay::class.java)
                Intent.putExtra("id_match",array[position].getID_match())
                ct.startActivity(Intent)
            }

        }catch (ex:Exception){
            Toast.makeText(ct,ex.toString(), Toast.LENGTH_LONG).show()
        }

    }
    override fun getItemCount(): Int {
        return array.size
    }


}