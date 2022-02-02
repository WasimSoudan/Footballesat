package com.example.footballesat

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FavoriteTeamAndLeague  {
    lateinit var id_user: String
    lateinit var id_taem: HashMap<Int, String>
    lateinit var id_leag:HashMap<Int,String>
    lateinit var id_Season:HashMap<Int,Int>
        constructor(id_user: String, id_taem: HashMap<Int, String>,id_leag:HashMap<Int,String>) {
        this.id_user = id_user
        this.id_taem = id_taem
        this.id_leag=id_leag
        id_Season= HashMap()

    }

    constructor(
        id_user: String,
        id_taem: HashMap<Int, String>,
        id_leag: HashMap<Int, String>,
        id_Season: HashMap<Int, Int>
    ) {
        this.id_user = id_user
        this.id_taem = id_taem
        this.id_leag = id_leag
        this.id_Season = id_Season
    }
    constructor()

    fun getTeams():HashMap<Int,String>{return this.id_taem}

    fun addTeam(id:Int, name: String?){
        id_taem.put(id, name.toString())
    }
    fun removeTeam(id:Int){
        id_taem.remove(id)
    }
    fun addleag(id:Int, name: String?){
        id_leag.put(id, name.toString())
    }
    fun removeleag(id:Int){
        id_leag.remove(id)
    }
    fun addSeason(id:Int, ids: Int){
        id_Season.put(id, ids)
    }
    fun removeSeason(id:Int){
        id_Season.remove(id)
    }
    fun printAll():String {
        var tem: String = "$id_user\nTeam\n"
        for (key in id_taem.keys){
            tem += "key $key = ${id_taem[key]}\n"
        }
        tem+="league\n"
        for (key in id_leag.keys){
            tem +="key $key =${id_leag[key]}\n"
        }
         tem+="Season\n"
        for (key in id_Season.keys){
            tem +="key $key =${id_Season[key]}\n"
        }
        return tem
    }
    fun chakeSeason():String{
        if(id_Season.isEmpty()){
          return "emty "
        }
        return " Not emty"
    }
    fun setHasMAp(h:HashMap<Int,Int>){
        this.id_Season=h
    }
    fun printId_Season():String{
       var tem:String="Season\n"
       for (key in id_Season.keys){
           tem +="key $key =${id_Season[key]}\n"
       }
        return tem
    }
    fun getfirstkey(): Int {
        var Leag_ID:Int=0
        for (key in id_Season.keys){
            Leag_ID=key
            break
        }
        var id_s:Int=0
         id_s = id_Season[Leag_ID]!!
        val numbers: IntArray = intArrayOf(id_s,Leag_ID)
       return id_s
    }
    fun getSeasonByID(L:String):IntArray{
        var Leag_ID:Int=0
        for (key in id_leag.keys){
            if (id_leag.keys.equals(L)){
                Leag_ID=key
                break
            }
        }
        var id_s:Int=0
        for (key in id_Season.keys){
            if (id_Season.keys.equals(Leag_ID)){
                id_s= id_Season[key]!!
                break
            }
        }
        return intArrayOf(id_s,Leag_ID)
    }

    fun getnumberleag():Int{
       return this.id_leag.size
    }
    fun getsize():Int{
        return this.id_taem.size+this.id_leag.size
    }
    fun getnameTeam():ArrayList<String>{
       var list:ArrayList<String>
       list=ArrayList()
        for(Key in id_taem.keys){
            list.add(id_taem[Key]!!)
        }
        return list

    }
    fun getIDLageOnly():ArrayList<Int>{
        var list= ArrayList<Int>()
        list= ArrayList()
        for (key in id_leag.keys){
            list.add(key)
        }
        return list
    }
    fun getIDTeamOnly():ArrayList<Int>{
        var list= ArrayList<Int>()
        list= ArrayList()
        for (key in id_taem.keys){
            list.add(key)
        }
        return list
    }

    fun test():ArrayList<HashMap<Int,String>>{
        var list:ArrayList<HashMap<Int,String>> = ArrayList()
        for(key in id_leag.keys){
          for(key2 in id_Season.keys){
              if(key==key2){
                 val q= hashMapOf<Int,String>()
                  q.put(id_Season[key2]!!,id_leag[key]!!)
                  list.add(q)
              }
          }
        }

        return list
    }
    fun test2():ArrayList<HashMap<Int,String>>{
        val list :ArrayList<HashMap<Int,String>> = ArrayList()
        for(key in id_taem.keys){
            val q= hashMapOf<Int,String>()
            q.put(key,id_taem[key]!!)
            list.add(q)
        }
        return list
    }

    fun get_id_team():ArrayList<Int>{
        var return_id_team:Array<Int> = Array(this.id_taem.size){0}
        var list :ArrayList<Int> =ArrayList()
        for(key in id_taem.keys){
            list.add(key)
        }
        //return_id_team= list.toArray() as Array<Int>
        return list
    }

    fun get_id_season():ArrayList<Int>{
        var return_id_season:ArrayList<Int> =ArrayList()
            for(key in id_Season.keys){
                return_id_season.add(id_Season[key]!!)
            }
        return return_id_season

    }
    fun get_idcounter_sesan():HashMap<Int,Int>{
           var x:HashMap<Int,Int>
           = hashMapOf(42 to 1980,46 to 2022,62 to 2100,113 to 2029,48 to 2020)
            return x
    }
    fun getid_leag():ArrayList<Int>{
        var list :ArrayList<Int> = ArrayList()
        for(key in this.id_leag.keys){
            list.add(key)
        }
        for(key in this.id_taem.keys){
            list.add(key)
        }

        return list
    }

    fun getKey(id :Int): Int {
        return id
    }


}
