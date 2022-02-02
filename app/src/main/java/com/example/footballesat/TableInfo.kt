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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class TeanInLeag {
    protected lateinit var name_team: String
    protected var team_id: Int = 0
    protected var position: Int = 0
    protected var points: Int = 0
    protected var games_played: Int = 0
    protected var won: Int = 0
    protected var drow: Int = 0
    protected var lost: Int = 0
    protected var img:String=""
    constructor()
    constructor(name_team: String,img:String, team_id: Int, position: Int, points: Int, games_played: Int, won: Int, drow: Int, lost: Int) {
        this.name_team = name_team
        this.team_id = team_id
        this.position = position
        this.points = points
        this.games_played = games_played
        this.won = won
        this.drow = drow
        this.lost = lost
    }
    fun get_img():String{
        return this.img
    }
    fun set_img(img:String){
        this.img=img
    }
    fun getname_team(): String {
        return name_team
    }

    fun getposition(): Int {
        return position
    }

    fun getpoints(): Int {
        return points
    }

    fun getgames_played(): Int {
        return games_played
    }

    fun getwon(): Int {
        return won
    }

    fun getdrow(): Int {
        return drow
    }

    fun getlost(): Int {
        return lost
    }

    fun getID_team(): Int {
        return team_id
    }

    fun setname(name: String) {
        name_team = name
    }
}

class Table_Search:Thread {
    protected var ct: Context
    protected lateinit var array:Array<TeanInLeag>
    protected lateinit var arrayIdTeam:IntArray
    constructor(ct: Context, array: Array<TeanInLeag>, arrayIdTeam: IntArray){
        this.ct=ct
        this.array=array
        this.arrayIdTeam=arrayIdTeam
    }

    override fun run() {
        var myQueue = Volley.newRequestQueue(ct)
        for (i in 0..array.size - 1) {
            var u: String =
                "https://app.sportdataapi.com/api/v1/soccer/teams/" + arrayIdTeam[i] + "?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
            var name: String = ""
            val request: JsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, u, null,
                Response.Listener { response ->
                    try {
                        var j = response.getJSONObject("data")
                        name = j.getString("name")
                        array[i].setname(name)
                        array[i].set_img(j.getString("logo"))
                    } catch (ex: Exception) {
                        Toast.makeText(ct, ex.message, Toast.LENGTH_SHORT).show()

                    }

                },
                Response.ErrorListener { e ->
                    Toast.makeText(ct, e.message, Toast.LENGTH_SHORT).show()

                })
            myQueue.add(request)
        }
    }

    fun getarray(): Array<TeanInLeag> {
        return array
    }

    fun getarrayInt(): IntArray {
        return arrayIdTeam
    }


}


class AdapterTable: RecyclerView.Adapter<AdapterTable.MyViewHolder> {
    private lateinit var ct:Context
    private lateinit var arrayTabel:Array<TeanInLeag>
    private var sesan_id:Int?
    constructor(ct:Context, arrayTabel: Array<TeanInLeag>, sesan_id: Int?)  {
        this.ct=ct
        this.arrayTabel = arrayTabel
        this.sesan_id=sesan_id
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView =itemView.findViewById(R.id.table_tv_name)
        var tv_poss: TextView =itemView.findViewById(R.id.table_tv_possation)
        var tv_game: TextView =itemView.findViewById(R.id.table_tv_game)
        var tv_win: TextView =itemView.findViewById(R.id.table_tv_win)
        var tv_draw: TextView =itemView.findViewById(R.id.table_tv_draw)
        var tv_lost: TextView =itemView.findViewById(R.id.table_tv_lost)
        var tv_pts: TextView =itemView.findViewById(R.id.table_tv_pts)
        var table_img_team:ImageView=itemView.findViewById(R.id.table_img_team)
        var con : ConstraintLayout =itemView.findViewById(R.id.table_con_scorer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.table_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            Glide.with(ct)
                .load(arrayTabel[position].get_img())
                .override(60, 60)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.table_img_team)

            holder.tv_poss.text=arrayTabel[position].getposition().toString()
            var n:String=arrayTabel[position].getname_team()
            var tem:String=""
            if(n.length>22){
                var x=n.split(" ")
                for ((index,element) in x.withIndex()){
                    if (index==x.size-1){
                        tem+="\n"
                    }
                    tem+="$element "
                }
                holder.tv_name.text=tem
            }else {
                holder.tv_name.text=arrayTabel[position].getname_team()
            }
            holder.tv_game.text=arrayTabel[position].getgames_played().toString()
            holder.tv_win.text=arrayTabel[position].getwon().toString()
            holder.tv_draw.text=arrayTabel[position].getdrow().toString()
            holder.tv_lost.text=arrayTabel[position].getlost().toString()
            holder.tv_pts.text=arrayTabel[position].getpoints().toString()
            holder.con.setOnClickListener(){
                val Intent : Intent = Intent(ct,TeamInfo::class.java)
                Intent.putExtra("id_team",arrayTabel[position].getID_team())
                Intent.putExtra("name",arrayTabel[position].getname_team())
                Intent.putExtra("sesan_id",sesan_id)
                ct.startActivity(Intent)
            }
        }catch (ex:Exception){
            Toast.makeText(ct,ex.message,Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return arrayTabel.size
    }

}
