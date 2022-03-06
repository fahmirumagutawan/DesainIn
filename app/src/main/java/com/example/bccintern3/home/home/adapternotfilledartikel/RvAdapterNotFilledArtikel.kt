package com.example.bccintern3.home.home.adapternotfilledartikel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bccintern3.invisiblefunction.DbReference
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RvAdapterNotFilledArtikel(private var parentView: View,
                                private var mainFlManager: FragmentManager,
                                private var navbar:BottomNavigationView,
                                private var activity: AppCompatActivity)
    :RecyclerView.Adapter<RvAdapterNotFilledArtikelViewHolder>()
{
    private lateinit var arrAtribut:ArrayList<ArrayList<String>>
    private lateinit var dbRef: DbReference
    private var maxCount=2

    fun init(){
        arrAtribut = ArrayList()
        arrAtribut.add(ArrayList())
        arrAtribut.add(ArrayList())
        arrAtribut.add(ArrayList())
        arrAtribut.add(ArrayList())
        arrAtribut.add(ArrayList())
        arrAtribut.add(ArrayList())
        dbRef = DbReference()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapterNotFilledArtikelViewHolder {
        init()
        return RvAdapterNotFilledArtikelViewHolder(LayoutInflater.from(parent.context)
            ,parent
            ,parentView
            ,mainFlManager
            ,navbar
            ,activity)
    }
    override fun onBindViewHolder(holder: RvAdapterNotFilledArtikelViewHolder, position: Int) {
        val ref = dbRef.refArticle()
        val position = position
        var j=0
        while(j<arrAtribut.size){
            arrAtribut.get(j).clear()
            j++
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                /**iterasi tahun**/
                for (i: DataSnapshot in snapshot.children) {
                    /**iterasi bulan**/
                    for (j: DataSnapshot in i.children) {
                        /**iterasi tanggal**/
                        for (k: DataSnapshot in j.children) {
                            /**baru disini iterasi child**/
                            for (disini: DataSnapshot in k.children) {
                                arrAtribut.get(0).add(disini.child("image").getValue().toString())
                                arrAtribut.get(1).add(disini.child("judul").getValue().toString())
                                arrAtribut.get(2).add(disini.child("hari").getValue().toString())
                                arrAtribut.get(3).add(disini.child("bulan").getValue().toString())
                                arrAtribut.get(4).add(disini.child("tahun").getValue().toString())
                                arrAtribut.get(5).add(disini.child("id").getValue().toString())

                                if (arrAtribut.get(0).size == maxCount) break
                            }
                            if (arrAtribut.get(0).size == maxCount) break
                        }
                        if (arrAtribut.get(0).size == maxCount) break
                    }
                    if (arrAtribut.get(0).size == maxCount) break
                }
                holder.bind(
                    arrAtribut.get(0).get(position),
                    arrAtribut.get(1).get(position),
                    arrAtribut.get(2).get(position),
                    arrAtribut.get(3).get(position),
                    arrAtribut.get(4).get(position),
                    arrAtribut.get(5).get(position)
                )

                ref.removeEventListener(this)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun getItemCount(): Int {
        return maxCount
    }
}