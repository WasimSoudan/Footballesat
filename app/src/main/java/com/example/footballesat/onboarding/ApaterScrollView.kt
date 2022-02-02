package com.example.footballesat.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.footballesat.*
import com.google.gson.Gson

class ApaterScrollView: RecyclerView.Adapter<ApaterScrollView.MyViewHolder> {
    protected var ct:Context?
    protected var listofscroll:ArrayList<TameAndLeagScrollView>
    protected var h :HashMap<Int,Int>
    constructor(ct:Context?,list:ArrayList<TameAndLeagScrollView>,h:HashMap<Int,Int>){
        this.ct=ct
        this.listofscroll=list
        this.h=h
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scroll_card_card_img: ImageView =itemView.findViewById(R.id.scroll_card_card_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApaterScrollView.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.scroll_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApaterScrollView.MyViewHolder, position: Int) {
        //holder.scroll_card_card_tv.text = listofscroll[position].get_name()
        //Toast.makeText(ct,listofscroll[position].get_name(),Toast.LENGTH_LONG).show()
        if (listofscroll[position].get_img2() == 0) {
            Glide.with(ct!!)
                .load(listofscroll[position].get_img())
                .override(60, 60)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.scroll_card_card_img)
        } else {
            holder.scroll_card_card_img.setImageResource(listofscroll[position].get_img2())
        }
        holder.scroll_card_card_img.setOnClickListener{
            clike(listofscroll[position].get_name(),listofscroll[position].get_id())
        }

    }
    fun clike(name:String, id:Int){
        var id2:Int=0
        if (id==1980){
            id2=id
        }else if(id==2029) {
            id2=id
        }else if(id==2100){
            id2=id
        }else if(id==2022){
            id2=id
        }else if(id==2020){
            id2=id
        }
        if(id2!=0) {
            val Intent: Intent = Intent(ct, Table::class.java)
            Intent.putExtra("SeasonandLeag", id2)
            ct!!.startActivity(Intent)
        }else{
            var id_counter:Int=0
            var myQueue = Volley.newRequestQueue(ct)
            var url: String = "https://app.sportdataapi.com/api/v1/soccer/teams/"+id+"?apikey=f32263e0-46f1-11ec-904c-dd9898d919b9"
            val request2: JsonObjectRequest =
                JsonObjectRequest(Request.Method.GET, url, null, { response ->
                    try {
                        var tem=response.getJSONObject("data").getJSONObject("country").getInt("country_id")
                        for(key in h.keys){
                            if(key ==tem){
                                id_counter=h.get(key)!!
                                Toast.makeText(ct ,id.toString(),Toast.LENGTH_LONG).show()
                                val Intent : Intent = Intent(ct,TeamInfo::class.java)
                                Intent.putExtra("id_team",id)
                                Intent.putExtra("name",name)
                                Intent.putExtra("sesan_id",id_counter)
                                ct!!.startActivity(Intent)
                                break
                            }
                        }

                    } catch (ex: Exception) {
                        Toast.makeText(ct, ex.message, Toast.LENGTH_SHORT).show()
                    }
                    // Toast.makeText(this,id_counter.toString(),Toast.LENGTH_LONG).show()
                }, { e ->
                    Toast.makeText(ct, e.message, Toast.LENGTH_SHORT).show()
                })
            myQueue.add(request2)
        }
    }
    override fun getItemCount(): Int {
       return listofscroll.size
    }

}


class TameAndLeagScrollView{
    protected var id:Int
    protected lateinit var name:String
    protected lateinit var img:String
    protected var img2:Int
    constructor(id:Int,name:String,img:String,img2:Int){
        this.id=id
        this.name=name
        this.img=img
        this.img2=img2
    }
    fun get_name():String{
          if(this.name.isEmpty()){
              return ""
          }
        return this.name

    }
    fun get_img():String{return this.img}
    fun get_img2():Int{return this.img2}
    fun get_id():Int{return this.id}
}
