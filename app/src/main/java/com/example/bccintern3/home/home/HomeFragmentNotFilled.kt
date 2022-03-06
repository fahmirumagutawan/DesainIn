package com.example.bccintern3.home.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.bccintern3.R
import com.example.bccintern3.home.discovery.DiscoveryFragmentListArtikel
import com.example.bccintern3.home.discovery.DiscoveryFragmentListKategori
import com.example.bccintern3.home.discovery.vp_adapterdetailkategori.VpAdapterDetailKategori
import com.example.bccintern3.home.home.adapternotfilledartikel.RvAdapterNotFilledArtikel
import com.example.bccintern3.home.home.adapternotfilledkategori.RvAdapterNotFilledKategori
import com.example.bccintern3.home.home.vp_adapternotfilledbanner.VpAdapterNotFilledBanner
import com.example.bccintern3.invisiblefunction.BackHandler
import com.example.bccintern3.invisiblefunction.LoadFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class HomeFragmentNotFilled(private var flManager: FragmentManager,
                            private var activity:AppCompatActivity,
                            private var thisContext: Context,
                            private var navbar:BottomNavigationView):Fragment(R.layout.home_homefragment_notfilled) {

    private lateinit var artikelAdapter:RvAdapterNotFilledArtikel
    private lateinit var kategoriAdapter:RvAdapterNotFilledKategori
    private lateinit var vpAdapter:VpAdapterNotFilledBanner
    private lateinit var kategoriRv:RecyclerView
    private lateinit var artikelRv:RecyclerView
    private lateinit var desainerRv:RecyclerView
    private lateinit var bannerVp:ViewPager2
    private lateinit var desainerLainTv:TextView
    private lateinit var artikelLainTv:TextView
    private lateinit var kategoriLainTv:TextView
    private lateinit var loadFrag:LoadFragment
    private lateinit var storageRef:StorageReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        setKategoriView(view)
        setArtikelView(view)
        loadViewPager(view)
        runTouchListener()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        runBackHandler(context)
    }
    fun init(view: View){
        kategoriRv = view.findViewById(R.id.homefragment_notfill_kategori_rv)
        artikelRv = view.findViewById(R.id.homefragment_notfill_artikel_rv)
        desainerRv = view.findViewById(R.id.homefragment_notfill_desainer_rv)
        bannerVp = view.findViewById(R.id.homefragment_notfill_banner_vp)
        desainerLainTv = view.findViewById(R.id.homefragment_notfill_rekomenlain_tv)
        artikelLainTv = view.findViewById(R.id.homefragment_notfill_artikellain_tv)
        kategoriLainTv = view.findViewById(R.id.homefragment_notfill_kategorilain_tv)
        loadFrag = LoadFragment()
        storageRef = FirebaseStorage.getInstance().getReference()
    }
    fun runTouchListener(){
        artikelLainTv.setOnClickListener {
            Handler().postDelayed({
                navbar.menu.getItem(0).setChecked(false)
                navbar.menu.getItem(1).setChecked(true)
                loadFrag.transfer(flManager,R.id.homeactivity_flmanager,DiscoveryFragmentListArtikel(flManager,thisContext,navbar,activity))
            },1000)
        }
        kategoriLainTv.setOnClickListener {
            Handler().postDelayed({
                navbar.menu.getItem(0).setChecked(false)
                navbar.menu.getItem(1).setChecked(true)
                loadFrag.transfer(flManager,R.id.homeactivity_flmanager,DiscoveryFragmentListKategori(flManager,thisContext,navbar,activity))
            },1000)
        }
    }
    fun setKategoriView(view: View){
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                kategoriAdapter = RvAdapterNotFilledKategori(view,flManager,thisContext,navbar,activity)
                val gridLayoutManager = GridLayoutManager(thisContext,3)
                kategoriRv.layoutManager = gridLayoutManager
                kategoriRv.adapter = kategoriAdapter
            }

        })
    }
    fun setArtikelView(view: View){
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                artikelAdapter = RvAdapterNotFilledArtikel(view,flManager,navbar,activity)
                val gridLayoutManager = GridLayoutManager(thisContext,2,GridLayoutManager.VERTICAL,false)
                artikelRv.layoutManager = gridLayoutManager
                artikelRv.adapter = artikelAdapter
            }
        })
    }
    fun loadViewPager(view:View){
        val ref = storageRef.child("home_banner")

        ref.listAll().addOnSuccessListener {
            val url = ArrayList<String>()
            var param=0
            for(i: StorageReference in it.items){
                val size = it.items.size
                i.downloadUrl.addOnSuccessListener {
                    url.add(it.toString())
                    param++
                    if(param==size){
                        vpAdapter = VpAdapterNotFilledBanner(url,view,flManager, navbar, activity)
                        bannerVp.adapter = vpAdapter
                    }
                }
            }
        }
    }
    private fun runBackHandler(context: Context){
        val back = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val handler = context as BackHandler
                val time = System.currentTimeMillis()
                val toast = Toast.makeText(thisContext,"Tekan kembali lagi untuk keluar",Toast.LENGTH_SHORT)

                if(time+1500>System.currentTimeMillis()){
                    handler.exit(activity)
                    toast.cancel()
                }
                else{
                    toast.show()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,back)
    }
}