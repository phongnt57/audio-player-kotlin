package com.pntstudio.buzz.tedaudio.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.pntstudio.buzz.tedaudio.DetailActivity
import com.pntstudio.buzz.tedaudio.R
import com.pntstudio.buzz.tedaudio.helps.PLAYPOS
import com.pntstudio.buzz.tedaudio.helps.config
import com.pntstudio.buzz.tedaudio.model.MediaItemAdapter
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import com.pntstudio.buzz.tedaudio.services.MusicService
import com.pntstudio.buzz.tedaudio.viewmodel.MediaListFragmentViewModel
import kotlinx.android.synthetic.main.fragment_meia_list.*


/**
 * A simple [Fragment] subclass.
 * Use the [MediaListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadListFragment : Fragment(), MediaItemAdapter.OnClickItem {
    override fun onClick(item: MediaItemData, position: Int) {
        if(context!!.config.isMyServiceRunning(MusicService::class.java,activity!!)){
            if(viewmodel.getCurrentPlaying().value!=null){
                if(!viewmodel.getCurrentPlaying().value!!.isOffline){
                    val myService = Intent(activity, MusicService::class.java)
                    activity!!.stopService(myService)

                }
            }

        }

        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra("list", viewmodel.getDownloadList().value)
        intent.putExtra("detail", item)
        intent.putExtra(PLAYPOS, position)
        startActivity(intent)
    }



    private lateinit var mediaAdapter: MediaItemAdapter
    private lateinit var viewmodel: MediaListFragmentViewModel

//    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_down_list, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        toolbar.setTitle("Audio")
        val linearLayoutMamanger = LinearLayoutManager(activity)

        up_button.hide()
        up_button.setOnClickListener {
            linearLayoutMamanger.smoothScrollToPosition(mediaRv, null, 0);
        }
        mediaRv.setHasFixedSize(true);
        mediaRv.setLayoutManager(linearLayoutMamanger);
        mediaRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && up_button.isShown())
                    up_button.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE && linearLayoutMamanger.findFirstCompletelyVisibleItemPosition() > 3) {
                    up_button.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        viewmodel = ViewModelProviders.of(activity!!).get(MediaListFragmentViewModel::class.java)
        viewmodel.downloads.observe(activity!!, object : Observer<ArrayList<MediaItemData>> {
            override fun onChanged(t: ArrayList<MediaItemData>?) {
                mediaAdapter = MediaItemAdapter(context!!, t!!, t, this@DownloadListFragment, -1)
                mediaRv.adapter = mediaAdapter
            }


        })
        viewmodel.getTextSearch().observe(activity!!, object : Observer<String> {
            override fun onChanged(t: String?) {
                mediaAdapter.filter.filter(t)
            }
        })
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
//        if (mListener != null) {
//            mListener!!.onFragmentInteraction(uri)
//        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            mListener = context
//        } else {
//            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
//        mListener = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
//        activity!!.getMenuInflater().inflate(R.menu.menu_dashboard, menu);


    }


    companion object {

        // TODO: Rename and change types and number of parameters
        fun newInstance(): DownloadListFragment {
            val fragment = DownloadListFragment()
            return fragment
        }
    }
}
