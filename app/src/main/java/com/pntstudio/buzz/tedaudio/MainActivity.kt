package com.pntstudio.buzz.tedaudio

import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import com.pntstudio.buzz.tedaudio.fragment.MediaListFragment
import com.pntstudio.buzz.tedaudio.viewmodel.MediaListFragmentViewModel
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), MediaListFragment.OnFragmentInteractionListener {
    private  lateinit var viewmodel: MediaListFragmentViewModel


    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragment = MediaListFragment.Companion.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
//                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
//            R.id.navigation_notifications -> {
////                message.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = MediaListFragment.Companion.newInstance()
        addFragment(fragment)
        viewmodel = ViewModelProviders.of(this).get(MediaListFragmentViewModel::class.java)



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


        return true;

    }

    /**
     * add/replace fragment in container [framelayout]
     */
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
                .addToBackStack(fragment.javaClass.getSimpleName())
                .commit()
    }
}
