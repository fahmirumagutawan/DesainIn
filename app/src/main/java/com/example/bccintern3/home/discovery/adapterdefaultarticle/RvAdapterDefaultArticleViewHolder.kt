package com.example.bccintern3.home.discovery.adapterdefaultarticle

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bccintern2.picasso.RoundCornerRect
import com.example.bccintern3.R
import com.example.bccintern3.home.discovery.DiscoveryFragmentDetailArtikel
import com.example.bccintern3.invisiblefunction.DbReference
import com.example.bccintern3.invisiblefunction.LoadFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class RvAdapterDefaultArticleViewHolder(inflater: LayoutInflater
                                        , parent:ViewGroup
                                        , parentView: View
                                        , mainFlManager:FragmentManager
                                        , private val activity: AppCompatActivity
                                        , private val navbar:BottomNavigationView)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.home_discoveryfragment_default_artikelborder,parent,false))
{
    private var imageView: ImageView
    private var titleTv:TextView
    private var penulisTv:TextView
    private var tanggalTv:TextView
    private var clickArea:LinearLayout
    private var parentView = parentView
    private var mainFlManager = mainFlManager
    private var loadFrag:LoadFragment
    private lateinit var hari:String
    private lateinit var bulan:String
    private lateinit var tahun:String
    private lateinit var id:String

    init {
        clickArea = itemView.findViewById(R.id.discoveryfragment_default_artikelborder_clickarea)
        imageView = itemView.findViewById(R.id.discoveryfragment_default_artikelborder_iv)
        titleTv = itemView.findViewById(R.id.discoveryfragment_default_artikelborder_titletv)
        penulisTv = itemView.findViewById(R.id.discoveryfragment_default_artikelborder_penulistv)
        tanggalTv = itemView.findViewById(R.id.discoveryfragment_default_artikelborder_tanggaltv)
        loadFrag = LoadFragment()
        runClickListener()
    }
    fun bind(url:String,
             judul:String,
             penulis:String,
             tanggal:String,
             hari:String,
             bulan:String,
             tahun:String,
             id:String){

        this.hari=hari
        this.bulan=bulan
        this.tahun=tahun
        this.id=id
        val width = parentView.width
        val height = width/3

        /**set gambar**/
        Picasso
            .get()
            .load(url)
            .transform(RoundCornerRect(30f,0f,0f,0f,0f))
            .resize(width,height)
            .centerCrop()
            .into(imageView)

        /**set judul**/
        titleTv.setText(judul)

        /**set penulis**/
        penulisTv.setText(penulis)

        /**set tanggal**/
        tanggalTv.setText(tanggal)
    }
    fun runClickListener(){
        clickArea.setOnClickListener {
            Handler().postDelayed({
                loadFrag.transfer(
                    mainFlManager
                    ,R.id.homeactivity_flmanager
                    ,DiscoveryFragmentDetailArtikel(hari, bulan, tahun, id, mainFlManager,navbar,activity))
            },1000)
        }
    }
}