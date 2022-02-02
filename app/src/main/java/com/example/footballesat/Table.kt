package com.example.footballesat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class Table : AppCompatActivity() {
    protected lateinit var img_table : ImageView
    protected lateinit var tv_name_leage:TextView
    protected lateinit var btn_Satting:Button
    protected lateinit var btn_News:Button
    protected lateinit var btn_Home:Button
    protected lateinit var btn_top_score:Button
    protected lateinit var fram:FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)
        val sesan_id= intent.getIntExtra("SeasonandLeag",0)
        var myQueue = Volley.newRequestQueue(this)
        var pb: ProgressBar =findViewById(R.id.progressBar)
        btn_News=findViewById(R.id.id_news2)
        btn_Satting=findViewById(R.id.id_setting2)
        btn_Home=findViewById(R.id.id_matches2)
        btn_top_score=findViewById(R.id.id_top_score)
        img_table=findViewById(R.id.imageTable)
        tv_name_leage=findViewById(R.id.tv_name_Leag)
        fram=findViewById(R.id.fram)
        var name_leag:String=""
        if (sesan_id==1980){
            write("Premier League",R.color.P,R.color.white,R.drawable.logo_premier_league,R.color.white)
            name_leag="Premier League"
        }else if(sesan_id==2029) {
            write("Laliga",R.color.L,R.color.white,R.drawable.laliga,R.color.white)
            name_leag="Laliga"

        }else if(sesan_id==2100){
            write("Serie_A",R.color.S,R.color.white,R.drawable.serie_a,R.color.white)
            name_leag="Serie_A"
        }else if(sesan_id==2022){
            write("Ligue 1",R.color.L1,R.color.white,R.drawable.ligue1,R.color.white)
            name_leag="Ligue 1"
        }else if(sesan_id==2020){
            write("Bundesliga",R.color.B,R.color.white,R.drawable.bundesliga,R.color.white)
            name_leag="Bundesliga"
        }
        pb.visibility = View.VISIBLE
        var array: Array<TeanInLeag>
        var arrayIdTeam:IntArray
        var r1: RecyclerView =findViewById(R.id.r1)
        var u: String = "https://app.sportdataapi.com/api/v1/soccer/standings?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9&season_id="+sesan_id
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, u, null, { response ->
            try {
                var j = response.getJSONObject("data")
                var jarray = j.getJSONArray("standings")
                array = Array(jarray.length()) { TeanInLeag() }
                arrayIdTeam= IntArray(jarray.length())
                for (i in 0..jarray.length() -1) {
                    var x = jarray.getJSONObject(i)
                    var team_id: Int = x.getInt("team_id")
                    var position: Int = x.getInt("position")
                    var point: Int = x.getInt("points")
                    var overall = x.getJSONObject("overall")
                    var gp: Int = overall.getInt("games_played")
                    var w: Int = overall.getInt("won")
                    var d: Int = overall.getInt("draw")
                    var l: Int = overall.getInt("lost")
                    var name_team: String = ""
                    var img:String=""
                    arrayIdTeam[i]=team_id
                    array[i] = TeanInLeag(name_team,img, team_id, position, point, gp, w, d, l)
                }
                val myt:Table_Search= Table_Search(this,array,arrayIdTeam)
                myt.start()
                myt.join()
                var arrayt:Array<TeanInLeag>
                arrayt=myt.getarray()
                val Myadapter: AdapterTable = AdapterTable(this,arrayt,sesan_id)
                r1.layoutManager = LinearLayoutManager(this)
                r1.adapter = Myadapter
                pb.visibility = View.GONE
    }   catch (ex: Exception) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        }
    }, { e ->
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
    })

    myQueue.add(request)
        btn_Home.setOnClickListener(){
            startActivity(Intent(this,HomePag::class.java))
            finish()
        }
        btn_top_score.setOnClickListener(){
            startActivity(Intent(this,TopScorer::class.java)
                .putExtra("id_sesan",sesan_id)
                .putExtra("name_leag",name_leag))
        }
}
 fun write(s:String,color: Int,color2:Int ,photo:Int,texColor:Int){
         img_table.setImageResource(photo)
         tv_name_leage.setText(s)
         fram.setBackgroundColor(resources.getColor(color))
         btn_News.setBackgroundColor(resources.getColor(color))
         btn_Satting.setBackgroundColor(resources.getColor(color))
         btn_Home.setBackgroundColor(resources.getColor(color))
         btn_top_score.setBackgroundColor(resources.getColor(color2))
         btn_News.setTextColor(resources.getColor(texColor))
         btn_Satting.setTextColor(getResources().getColor(texColor))
         btn_Home.setTextColor(getResources().getColor(texColor))
 }
}