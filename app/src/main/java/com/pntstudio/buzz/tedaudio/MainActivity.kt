package com.pntstudio.buzz.tedaudio

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.pntstudio.buzz.tedaudio.fragment.DownloadListFragment
import com.pntstudio.buzz.tedaudio.fragment.MediaListFragment
import com.pntstudio.buzz.tedaudio.helps.BusProvider
import com.pntstudio.buzz.tedaudio.helps.PERMISSION_WRITE_STORAGE
import com.pntstudio.buzz.tedaudio.helps.hasPermission
import com.pntstudio.buzz.tedaudio.model.Events
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import com.pntstudio.buzz.tedaudio.viewmodel.MediaListFragmentViewModel
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MediaListFragment.OnFragmentInteractionListener {
    private lateinit var viewmodel: MediaListFragmentViewModel
    lateinit var bus: Bus
    lateinit var currentPlaying: MediaItemData
//    val mediaListfragment = MediaListFragment.Companion.newInstance()
//    val downloadListfragment = DownloadListFragment.Companion.newInstance()
//    var currentTab = 0


    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                content.currentItem = 0
//                if(currentTab==1) {
//                    addFragment(mediaListfragment)
//                    currentTab = 0
//                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                content.currentItem = 1
//                if(currentTab==0) {
//                    addFragment(downloadListfragment)
//                    currentTab = 1
//                }
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        val fragment = MediaListFragment.Companion.newInstance()
//        addFragment(fragment)
        val adapterViewPager = DemoFragmentAdapter(supportFragmentManager)
        content.adapter = adapterViewPager
        content.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
            }
        })

        viewmodel = ViewModelProviders.of(this).get(MediaListFragmentViewModel::class.java)
        playing_layout.visibility = View.GONE
        bus = BusProvider.instance
        bus.register(this)
        bus.post(Events.EmptyObject())
        playing_layout.setOnClickListener { showDetailActivity() }


    }

    private fun showDetailActivity() {
        val intent = Intent(this, DetailActivity::class.java)
        if (!currentPlaying.isOffline)
            intent.putExtra("list", viewmodel.getMediaList().value)
        else intent.putExtra("list", viewmodel.getDownloadList().value)
        intent.putExtra("detail", currentPlaying)
        intent.putExtra("isExistMedia", true)
        startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        val searchItem = menu!!.findItem(R.id.action_search)
        val searchView = searchItem.getActionView() as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Toast like print
//                UserFeedback.show("SearchOnQueryTextSubmit: " + query)
//                if (!searchView.isIconified) {
//                    searchView.isIconified = true
//                }
//                searchView.onActionViewCollapsed()
                viewmodel.setTextSearch(query)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                viewmodel.setTextSearch(s)
                return false
            }
        })

        if(!hasPermission(PERMISSION_WRITE_STORAGE)){
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_WRITE_STORAGE)

        }




        return true;

    }

    override fun onDestroy() {
        super.onDestroy()
        bus.unregister(this)

    }

    @Subscribe
    fun songChangedEvent(event: Events.SongChanged) {
        Log.e("change", "----");
        currentPlaying = event.song!!
        playing_layout.visibility = View.VISIBLE
        playing_song_tv.setText(currentPlaying.title)
        playing_song_tv.setSelected(true)


    }

    /**
     * add/replace fragment in container [framelayout]
     */
    private fun addFragment(fragment: Fragment) {
//        supportFragmentManager
//                .beginTransaction()
//                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
//                .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
//                .addToBackStack(fragment.javaClass.getSimpleName())
//                .commit()
    }

    inner class DemoFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> return MediaListFragment.Companion.newInstance()
                1 -> return DownloadListFragment.Companion.newInstance()
                else -> return null
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }


    }
}
