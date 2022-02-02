package com.example.footballesat.HomePage_Fragmant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.SparseBooleanArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.footballesat.*
import com.example.footballesat.onboarding.TameAndLeagScrollView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_favorite.*


class favorite_Fragment : Fragment() {
    var f :FavoriteTeamAndLeague= FavoriteTeamAndLeague()
    private var id_team: IntArray = intArrayOf(6402, 2522, 2523, 2892, 2524)//, 6404, 6406, 4066, 4794, 4810, 4798, 3651, 2509)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_favorite_, container, false)
        val img :ImageView=view.findViewById(R.id.favorite_fragment_img)
         val myQueue = Volley.newRequestQueue(context)
        var fragment_favorite: fragment_favorite= fragment_favorite()
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("save")){
            val gson: Gson = Gson()
            val s=sharedPreferences.getString("save","")
            f= gson.fromJson(s, FavoriteTeamAndLeague::class.java)
        }
      /*  if (sharedPreferences.contains("save1")){
            val gson: Gson = Gson()
            val s=sharedPreferences.getString("save","")
            f= gson.fromJson(s, FavoriteTeamAndLeague::class.java)
        }*/
        val r1 :RecyclerView=view.findViewById(R.id.framgent_favorite_r)
        val bu :Button=view.findViewById(R.id.framgent_favorite_bu)
        var id_leag: IntArray = intArrayOf(237, 538, 392, 301, 314)
        val name_leag=resources.getStringArray(R.array.leag)
        var leag_logo: IntArray = intArrayOf(
            R.drawable.premier_league,
            R.drawable.laliga,
            R.drawable.serie_a,
            R.drawable.ligue1,
            R.drawable.bundesliga
        )
        var list:ArrayList<favorite_fragment_manger> = ArrayList()
        for(i in 0..id_leag.size-1){
            list.add(favorite_fragment_manger(name_leag[i],id_leag[i],leag_logo[i],""))
        }
        for(j in 0..id_team.size-1) {
            var url: String =
                "https://app.sportdataapi.com/api/v1/soccer/teams/" + id_team[j] + "?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
            val request: JsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        val j = response.getJSONObject("data")
                        val id: Int = j.getInt("team_id")
                        val name: String = j.getString("name")
                        val logo: String = j.getString("logo")
                        list.add(favorite_fragment_manger(name, id, 0, logo))

                    } catch (ex: Exception) {
                        Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
                    }
                    if (j == id_team.size - 1) {
                        fragment_favorite = fragment_favorite(context, f.getid_leag(), list)
                        r1.layoutManager = LinearLayoutManager(context)
                        r1.adapter = fragment_favorite

                    }
                },
                Response.ErrorListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                })
            myQueue.add(request)
        }
        fragment_favorite = fragment_favorite(context, f.getid_leag(), list)
        r1.layoutManager = LinearLayoutManager(context)
        r1.adapter = fragment_favorite
        bu.setOnClickListener() {
            var x = fragment_favorite.getchecked_id()
            var getIDLageOnly = f.getIDLageOnly()
            var getIDTeamOnly = f.getIDTeamOnly()
            for (i in 0..getIDLageOnly.size - 1) {
                if (!x.contains(getIDLageOnly[i])) {
                    if (id_leag.contains(getIDLageOnly[i])) {
                        Toast.makeText(context, getIDLageOnly[i].toString(), Toast.LENGTH_LONG)
                            .show()
                        f.removeleag(getIDLageOnly[i])
                        f.removeSeason(getIDLageOnly[i])
                    }
                }
            }
            for (i in 0..getIDTeamOnly.size - 1) {
                if (!x.contains(getIDTeamOnly[i])) {
                    if (id_team.contains(getIDTeamOnly[i])) {
                        Toast.makeText(context, getIDTeamOnly[i].toString(), Toast.LENGTH_LONG).show()
                        f.removeTeam(getIDTeamOnly[i])
                    }
                }
            }

            for (i in 0..x.size - 1) {
                if (id_leag.contains(x[i])) {
                    if (!getIDLageOnly.contains(x[i])) {
                        //  Toast.makeText(context, x[i].toString()+ name_leag[i].toString(), Toast.LENGTH_LONG).show()
                        var u: String =
                            "https://app.sportdataapi.com/api/v1/soccer/leagues/" + x[i] +
                                    "?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
                        val request: JsonObjectRequest = JsonObjectRequest(
                            Request.Method.GET, u, null,
                            Response.Listener { response ->
                                try {
                                    val j = response.getJSONObject("data")
                                    val name = j.getString("name")
                                    f.addleag(x[i], name)
                                    Toast.makeText(context, name + " add", Toast.LENGTH_LONG).show()
                                } catch (ex: Exception) {
                                    Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
                                }
                                // Toast.makeText(context,f.getKey(x[i]).toString(),Toast.LENGTH_LONG).show()
                                var u2: String =
                                    "https://app.sportdataapi.com/api/v1/soccer/seasons" +
                                            "?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9&league_id=" + x[i]
                                val request2: JsonObjectRequest = JsonObjectRequest(
                                    Request.Method.GET, u2, null,
                                    Response.Listener { response ->
                                        try {
                                            val jarray = response.getJSONArray("data")
                                            val j = jarray.getJSONObject(jarray.length() - 1)
                                            val season_id: Int = j.getInt("season_id")
                                            f.addSeason(x[i], season_id)
                                        } catch (ex: Exception) {
                                            Toast.makeText(context, ex.message, Toast.LENGTH_SHORT)
                                                .show()

                                        }
                                    },
                                    Response.ErrorListener { e ->
                                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
                                            .show()

                                    })
                                myQueue.add(request2)
                            },
                            Response.ErrorListener { e ->
                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

                            })
                        myQueue.add(request)

                    }//end
                } else {
                    if (!getIDTeamOnly.contains(x[i])) {
                        var u: String = "https://app.sportdataapi.com/api/v1/soccer/teams/" + x[i] +
                                "?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
                        val request: JsonObjectRequest = JsonObjectRequest(
                            Request.Method.GET, u, null,
                            Response.Listener { response ->
                                try {
                                    val j = response.getJSONObject("data")
                                    val name = j.getString("name")
                                    f.addTeam(x[i], name)
                                   // Toast.makeText(context, name + " add", Toast.LENGTH_LONG).show()
                                } catch (ex: Exception) {
                                    Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()

                                }

                            },
                            Response.ErrorListener { e ->
                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

                            })
                        myQueue.add(request)
                    }
                }
            }
                Handler().postDelayed({
                    save()
                   findNavController().navigate(R.id.action_favorite_Fragment_to_homePage_Fragment)
                }, 2000)
        }


        img.setOnClickListener {
            save1()
            findNavController().navigate(R.id.action_favorite_Fragment_to_homePage_Fragment)

        }

        return view
    }
    fun save (){
        val editor: SharedPreferences.Editor? =requireActivity().getSharedPreferences("share", Context.MODE_PRIVATE).edit()
        editor?.apply {
            var gson = Gson()
            var json:String =gson.toJson(f)
            putString("save",json)
         Toast.makeText(context,f.printAll(),Toast.LENGTH_LONG).show()
            // tv.text=x.getname()+" "+x.getage()
            apply()
        }

    }
    fun save1 () {
        val editor: SharedPreferences.Editor? =
            requireActivity().getSharedPreferences("share1", Context.MODE_PRIVATE).edit()
        editor?.apply {
            var gson = Gson()
            var json: String = "HomeFragment"
            putString("selected_Fragment", json)
            apply()
        }
    }
    }
    class favorite_fragment_manger{
        protected var name:String
        protected var id:Int
        protected var logo :Int
        protected var logo2:String

        constructor(name: String, id: Int, logo: Int, logo2: String) {
            this.name = name
            this.id = id
            this.logo = logo
            this.logo2 = logo2
        }
        fun get_id():Int{return this.id}
        fun get_name():String{return this.name}
        fun get_logoInt():Int{return this.logo}
        fun get_logoString():String{return this.logo2}
    }





class fragment_favorite: RecyclerView.Adapter<fragment_favorite.MyViewHolder> {
    protected var ct:Context? = null
    protected lateinit var checkedleag:ArrayList<Int>
    protected lateinit var list:ArrayList<favorite_fragment_manger>
    var checkBoxStataArray= SparseBooleanArray()
    constructor(ct:Context?,ch_leag:ArrayList<Int>,list:ArrayList<favorite_fragment_manger>){
        this.ct=ct
        this.checkedleag=ch_leag
        this.list=list
    }
    constructor()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img :ImageView=itemView.findViewById(R.id.card_fragment_favorite_img)
        val tv:TextView=itemView.findViewById(R.id.card_fragment_favorite_tv)
        val ch:CheckBox=itemView.findViewById(R.id.card_fragment_favorite_ch)
        init {
            ch.setOnClickListener {
                if (!checkBoxStataArray.get(adapterPosition,false)){
                    ch.isChecked=true
                    checkBoxStataArray.put(adapterPosition,true)
                }else {
                    ch.isChecked=false
                    checkBoxStataArray.put(adapterPosition,false)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fragment_favorite.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.card_fragment_favorite, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: fragment_favorite.MyViewHolder, position: Int) {
        holder.tv.text=list[position].get_name()
        if(list[position].get_logoInt()!=0){
            holder.img.setImageResource(list[position].get_logoInt())
        }else{
            Glide.with(ct!!)
                .load(list[position].get_logoString())
                .override(120, 120)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img)
        }
        holder.ch.isChecked = checkBoxStataArray.get(position,false)
        if(checkedleag.contains(list[position].get_id())){
            holder.ch.isChecked=true
        }
        holder.ch.setOnClickListener(){
           if(checkedleag.contains(list[position].get_id())){
               checkedleag.remove(list[position].get_id())
           //    Toast.makeText(ct,"reomve",Toast.LENGTH_SHORT).show()
           }else{
               checkedleag.add(list[position].get_id())
             //  Toast.makeText(ct,"add",Toast.LENGTH_SHORT).show()
           }
        }

    }
    fun getchecked_id():ArrayList<Int>{
        return checkedleag
    }


    override fun getItemCount(): Int {
        return list.size
    }

}
