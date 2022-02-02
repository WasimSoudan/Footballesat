package com.example.footballesat

import android.app.Notification.EXTRA_PEOPLE
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.footballesat.TeamAdapter2.MyViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_favorite.*

class favorite : AppCompatActivity() {
    private var id_team: IntArray = intArrayOf(6402, 2522, 2523, 2892, 2524, 6404, 6406, 4066, 4794, 4810, 4798, 3651, 2509)
    private var id_leag: IntArray = intArrayOf(237, 538, 392, 301, 314)
    private var leag_logo: IntArray = intArrayOf(
        R.drawable.premier_league,
        R.drawable.laliga,
        R.drawable.serie_a,
        R.drawable.ligue1,
        R.drawable.bundesliga
    )
    private lateinit var name_Leage: Array<String>
    private lateinit var id_user :String
    var h_team: HashMap<Int, String> = HashMap<Int, String>()
    var h_leag: HashMap<Int, String> = HashMap<Int, String>()
    var h_sesan: HashMap<Int, Int> = HashMap<Int, Int>()
    lateinit var bu: Button
    lateinit var teamstr:Array<String>
    lateinit var f:FavoriteTeamAndLeague
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        id_user = intent.getStringExtra("id_user").toString()
        name_Leage = resources.getStringArray(R.array.leag)
        bu = findViewById(R.id.bt_go)
        var array_team: Array<teamclass> = Array(id_team.size) { teamclass() }
        val myQueue = Volley.newRequestQueue(this)
        for (i in 0..id_team.size - 1) {
            var url: String =
                "https://app.sportdataapi.com/api/v1/soccer/teams/" + id_team[i] + "?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
            val request: JsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        var j = response.getJSONObject("data")
                        var id: Int = j.getInt("team_id")
                        var logo = j.getString("logo")
                        var name_team = j.getString("name")
                        array_team[i] = teamclass(id, name_team, logo)
                    } catch (ex: Exception) {
                        Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
                    }
                    val TeamAdapter: TeamAdapter2 = TeamAdapter2(this, array_team)
                    r1.layoutManager = LinearLayoutManager(this)
                    r1.adapter = TeamAdapter
                    h_team=TeamAdapter.get_hTeam()
                },
                Response.ErrorListener { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                })
            myQueue.add(request)
            var list_leag: Array<leagclass>
            list_leag = Array(id_leag.size) { leagclass() }
            for (i in 0..list_leag.size - 1) {
                list_leag[i] = leagclass(id_leag[i], name_Leage[i], leag_logo[i])
            }
            val LeagAdapter: LeagAdapter = LeagAdapter(this, list_leag)
            r2.layoutManager = LinearLayoutManager(this)
            r2.adapter = LeagAdapter
            bu.setOnClickListener() {
                h_leag=LeagAdapter.getHasMapa()
                h_sesan=LeagAdapter.getHasMapsesan()
                f= FavoriteTeamAndLeague(id_user,h_team,h_leag,h_sesan)
                Toast.makeText(this,f.printAll(),Toast.LENGTH_LONG).show()
                 save(f)

            }
        }

    }
    fun save(f:FavoriteTeamAndLeague){
        val sharedPreferences: SharedPreferences =getSharedPreferences("share",Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? =sharedPreferences.edit()
        editor?.apply {
            var gson = Gson()
            var json:String =gson.toJson(f)
            putString("save",json)
            apply()
        }
        Toast.makeText(this,"Data save",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomePag::class.java))
    }
}

class teamclass{
    protected var id_team:Int=0
    protected var name_Team: String=""
    protected var logo:String=""

    constructor(id_team: Int, name_Team: String, logo: String) {
        this.id_team = id_team
        this.name_Team = name_Team
        this.logo = logo
    }
    constructor()
    fun get_id_team():Int{return this.id_team}
    fun get_name_Team():String{return this.name_Team}
    fun get_logo():String{return this.logo}
}

class leagclass{
    protected var id_leag:Int=0
    protected var name_leag:String=""
    protected var logo:Int=0
    constructor()
    constructor(id_leag: Int, name_leag: String, logo: Int) {
        this.id_leag = id_leag
        this.name_leag = name_leag
        this.logo = logo
    }
    fun get_id_leag():Int{return this.id_leag}
    fun get_name_leag():String{return this.name_leag}
    fun get_logo():Int{return this.logo}


}

class TeamAdapter2 : RecyclerView.Adapter<MyViewHolder>{
    protected  var ct: Context
    protected var List:Array<teamclass>
    protected  var h: HashMap<Int, String> = HashMap<Int, String>()
    constructor(ct: Context, array: Array<teamclass>){
        this.ct=ct
        this.List=array

    }
    fun get_hTeam():HashMap<Int,String>{return this.h}

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tx: TextView = itemView.findViewById(R.id.row_favorit_name_tv)
        var img: ImageView = itemView.findViewById(R.id.row_favorit_img)
        var row_favorit_switch:Switch=itemView.findViewById(R.id.row_favorit_switch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.myrow, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return List.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            holder.tx.text=List[position].get_name_Team()
            Glide.with(ct)
                .load(List[position].get_logo())
                .override(120, 120)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img)
             holder.row_favorit_switch.setOnClickListener() {
                if (holder.row_favorit_switch.isChecked) {
                    h.put(List[position].get_id_team(),List[position].get_name_Team())
                    Toast.makeText(ct, "add",Toast.LENGTH_SHORT).show()
                }else{
                    h.remove(List[position].get_id_team())
                    Toast.makeText(ct, "remove", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (ex: Exception) {
            Toast.makeText(ct,ex.message, Toast.LENGTH_SHORT).show()

        }
    }


}


class LeagAdapter: RecyclerView.Adapter<LeagAdapter.MyViewHolder> {
    protected var ct: Context
    protected var list:Array<leagclass>
    protected var h: HashMap<Int, String> = HashMap<Int, String>()
    protected var h_sesan: HashMap<Int, Int> = HashMap<Int, Int>()
    constructor(ct: Context, list:Array<leagclass>) {
        this.ct = ct
        this.list=list
    }

    fun getHasMapa(): HashMap<Int, String> {
        return h
    }
    fun getHasMapsesan(): HashMap<Int, Int> {
        return h_sesan
    }


    fun savevalue(poss: Int) {
        h.put(list[poss].get_id_leag(), list[poss].get_name_leag())
        getSesan(list[poss].get_id_leag())
    }

    private fun getSesan(leag:Int) {
        val myQueue = Volley.newRequestQueue(ct)
        var x: Int = 0
        var h_id_season: HashMap<Int, Int> = HashMap()
        var url: String =
            "https://app.sportdataapi.com/api/v1/soccer/seasons?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9&league_id=$leag"
        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    var jaray = response.getJSONArray("data")
                    x = jaray.getJSONObject(jaray.length() - 1).getInt("season_id")
                    h_sesan.put(leag, x)
                    //Toast.makeText(ct,x.toString(),Toast.LENGTH_SHORT).show()
                    //  Toast.makeText(ct,"added sesan $x",Toast.LENGTH_LONG).show()
                } catch (ex: Exception) {
                    Toast.makeText(ct, ex.message, Toast.LENGTH_SHORT).show()

                }
            },
            Response.ErrorListener { e ->
                Toast.makeText(ct, e.message, Toast.LENGTH_SHORT).show()

            })
        myQueue.add(request)


    }
    fun removevalue(poss: Int) {
        h.remove(list[poss].get_id_leag())
        h_sesan.remove(list[poss].get_id_leag())
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var myText: TextView = itemView.findViewById(R.id.row_favorit_name_tv)
        var imagephoto: ImageView = itemView.findViewById(R.id.row_favorit_img)
        var row_favorit_switch:Switch=itemView.findViewById(R.id.row_favorit_switch)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagAdapter.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.myrow, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeagAdapter.MyViewHolder, position: Int) {
        try {
            holder.myText.text=list[position].get_name_leag()
            holder.imagephoto.setImageResource(list[position].get_logo())
            holder.row_favorit_switch.setOnClickListener {
                if (holder.row_favorit_switch.isChecked){
                    //Toast.makeText(ct,list[position].get_name_leag(),Toast.LENGTH_SHORT).show()
                    savevalue(position)
                }else{
                    //Toast.makeText(ct,list[position].get_id_leag().toString(),Toast.LENGTH_SHORT).show()
                    removevalue(position)
                }
            }


        }catch (ex:Exception){
            Toast.makeText(ct,ex.message,Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}