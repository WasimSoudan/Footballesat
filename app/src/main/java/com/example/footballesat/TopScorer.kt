package com.example.footballesat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class TopScorer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_scorer)
        var id_sesan:Int=intent.getIntExtra("id_sesan",0)
        var name_Leag:String?=intent.getStringExtra("name_leag")
        var top_scorere_fram:FrameLayout=findViewById(R.id.top_scorere_fram)
        var tv_top_score_leag_name:TextView=findViewById(R.id.tv_top_score_leag_name)
        var top_scorer_img_leag:ImageView=findViewById(R.id.top_scorer_img_leag)
        tv_top_score_leag_name.text=name_Leag
        if (name_Leag.equals("Premier League")){
           top_scorere_fram.setBackgroundColor(getResources().getColor(R.color.P))
            top_scorer_img_leag.setImageResource(R.drawable.premier_league)
        }else if(name_Leag.equals("Laliga")) {
            top_scorere_fram.setBackgroundColor(getResources().getColor(R.color.L))
            top_scorer_img_leag.setImageResource(R.drawable.laliga)
        }else if(name_Leag.equals("Serie_A")){
            top_scorere_fram.setBackgroundColor(getResources().getColor(R.color.S))
            top_scorer_img_leag.setImageResource(R.drawable.serie_a)
        }else if(name_Leag.equals("Ligue 1")){
            top_scorere_fram.setBackgroundColor(getResources().getColor(R.color.L1))
            top_scorer_img_leag.setImageResource(R.drawable.ligue1)
        }else if(name_Leag.equals("Bundesliga")){
            top_scorere_fram.setBackgroundColor(getResources().getColor(R.color.B))
            top_scorer_img_leag.setImageResource(R.drawable.bundesliga)
        }
        val r1: RecyclerView =findViewById(R.id.r_top_score)
        val myQueue = Volley.newRequestQueue(this)
        var url:String="https://app.sportdataapi.com/api/v1/soccer/topscorers?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9&season_id=$id_sesan"
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val jarray=response.getJSONArray("data")
                var list:Array<topScore> = Array(jarray.length()){topScore()}
                for (i in 0..list.size-1){
                    val j=jarray.getJSONObject(i)
                    val pos=j.getInt("pos")
                    val player_id=j.getJSONObject("player").getInt("player_id")
                    val player_name=j.getJSONObject("player").getString("player_name")
                    val overall=j.getJSONObject("goals").getInt("overall")
                    val home=j.getJSONObject("goals").getInt("home")
                    val away=j.getJSONObject("goals").getInt("away")
                    list[i]=topScore(player_id,player_name,pos,overall,home,away)
                }
                val Myadapter: TopScoreAdapter = TopScoreAdapter(this, list)
                r1.layoutManager = LinearLayoutManager(this)
                r1.adapter = Myadapter
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }, { e ->
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        })

        myQueue.add(request)
    }
}



class topScore{
    protected var id_Player:Int = 0
    protected var name:String=""
    protected var pos:Int = 0
    protected var overall:Int = 0
    protected var home:Int = 0
    protected var away:Int = 0
    constructor()
    constructor(id_Player: Int, name: String, pos: Int, overall: Int, home: Int, away: Int) {
        this.id_Player = id_Player
        this.name = name
        this.pos = pos
        this.overall = overall
        this.home = home
        this.away = away
    }
    fun get_name():String{return name}
    fun get_pos():Int{return pos}
    fun get_overall():Int{return overall}
    fun get_home():Int{return home}
    fun get_away():Int{return away}
}


class TopScoreAdapter: RecyclerView.Adapter<TopScoreAdapter.MyViewHolder>  {
    protected lateinit var ct: Context
    protected lateinit var list: Array<topScore>
    constructor(ct: Context, list:Array<topScore>){
        this.ct=ct
        this.list= list
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card_secor_name_player: TextView =itemView.findViewById(R.id.card_secor_name_player)
        var card_scorer_all: TextView =itemView.findViewById(R.id.card_scorer_all)
        var card_scorer_home: TextView =itemView.findViewById(R.id.card_scorer_home)
        var card_scorer_away: TextView =itemView.findViewById(R.id.card_scorer_away)
        var card_secor_pos: TextView =itemView.findViewById(R.id.card_secor_pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopScoreAdapter.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.scorer_card, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var name=list[position].get_name().split(" ")
        if(name.size>2){
            holder.card_secor_name_player.text=name[0]+" "+name[name.size-1]
        }else{
            holder.card_secor_name_player.text=list[position].get_name()
        }
        holder.card_secor_pos.text=list[position].get_pos().toString()
        holder.card_scorer_all.text=list[position].get_overall().toString()
        holder.card_scorer_home.text=list[position].get_home().toString()
        holder.card_scorer_away.text=list[position].get_away().toString()
    }
    override fun getItemCount(): Int {
        return list.size
    }


}