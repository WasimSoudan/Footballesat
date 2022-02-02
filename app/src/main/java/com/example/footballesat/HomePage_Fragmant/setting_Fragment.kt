package com.example.footballesat.HomePage_Fragmant

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.footballesat.LeagAdapter
import com.example.footballesat.R
import com.example.footballesat.TeamAdapter2
import com.example.footballesat.leagclass
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.fragment_secand_screen.view.*
import kotlinx.android.synthetic.main.fragment_setting_.*

class setting_Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_setting_, container, false)
        val r_setting_fragmant:RecyclerView=view.findViewById(R.id.r_setting_fragmant)
        val setting_fragment_img:ImageView=view.findViewById(R.id.setting_fragment_img)
        val settingselment:Array<String> =resources.getStringArray(R.array.settingselment)
        val settingdetal:Array<String> =resources.getStringArray(R.array.settingdetal)
        setting_fragment_img.setOnClickListener(){
            save()
            findNavController().navigate(R.id.action_setting_Fragment_to_homePage_Fragment)
        }
        val img_setting:IntArray=
            intArrayOf(R.drawable.ic_baseline_notifications_active_24
                ,R.drawable.ic_baseline_slow_motion_video_24
                ,R.drawable.theme
                ,R.drawable.share
                ,R.drawable.follow
                ,R.drawable.support)
       // Toast.makeText(context,settingselment.size.toString(),Toast.LENGTH_LONG).show()
        val settingAdater: settingAdater = settingAdater(context,img_setting,settingselment,settingdetal)
        r_setting_fragmant.layoutManager = LinearLayoutManager(context)
        r_setting_fragmant.adapter = settingAdater
        return view
    }

    fun save () {
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


class settingAdater: RecyclerView.Adapter<settingAdater.MyViewHolder> {
    protected var ct: Context?
    protected var img:IntArray
    protected lateinit var t1:Array<String>
    protected lateinit var t2:Array<String>
    constructor(ct: Context?,img:IntArray,t1:Array<String>,t2:Array<String> ) {
        this.ct = ct
        this.img=img
        this.t1=t1
        this.t2=t2
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img :ImageView=itemView.findViewById(R.id.card_setting_fragment_im)
        val tv1:TextView=itemView.findViewById(R.id.card_setting_fragment_tv1)
        val tv2:TextView=itemView.findViewById(R.id.card_setting_fragment_tv2)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): settingAdater.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.card_seeting_fragment, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: settingAdater.MyViewHolder, position: Int) {
        try {
            holder.img.setImageResource(img[position])
            holder.tv1.text=t1[position]
            holder.tv2.text=t2[position]
        } catch (ex: Exception) {
            Toast.makeText(ct, ex.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return t1.size
    }
}


