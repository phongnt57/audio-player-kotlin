package com.pntstudio.buzz.tedaudio.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager

import com.pntstudio.buzz.tedaudio.R
import kotlinx.android.synthetic.main.fragment_meia_list.*
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.view.*
import com.pntstudio.buzz.tedaudio.DetailActivity
import com.pntstudio.buzz.tedaudio.MainActivity
import com.pntstudio.buzz.tedaudio.helps.PLAYPOS
import com.pntstudio.buzz.tedaudio.viewmodel.MediaListFragmentViewModel
import com.pntstudio.buzz.tedaudio.model.MediaItemAdapter
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView






/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MediaListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MediaListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaListFragment : Fragment(), MediaItemAdapter.OnClickItem {
    override fun onClick(item: MediaItemData,position:Int) {
        val intent = Intent(activity,DetailActivity::class.java)
        intent.putExtra("list",viewmodel.getMediaList().value)
        intent.putExtra("detail",item)
        intent.putExtra(PLAYPOS,position)
        startActivity(intent)
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private lateinit var mediaAdapter: MediaItemAdapter
    private  lateinit var viewmodel: MediaListFragmentViewModel

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meia_list, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        toolbar.setTitle("Audio")
        val linearLayoutMamanger = LinearLayoutManager(activity)

        up_button.hide()
        up_button.setOnClickListener { linearLayoutMamanger.smoothScrollToPosition(mediaRv, null, 0);
        }
        mediaRv.setHasFixedSize(true);
        mediaRv.setLayoutManager(linearLayoutMamanger);
        mediaRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && up_button.isShown())
                    up_button.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE &&linearLayoutMamanger.findFirstCompletelyVisibleItemPosition() > 3) {
                    up_button.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        viewmodel = ViewModelProviders.of(activity!!).get(MediaListFragmentViewModel::class.java)
        viewmodel.heroes.observe(activity!!, object : Observer<ArrayList<MediaItemData>> {
            override fun onChanged(t: ArrayList<MediaItemData>?) {
                mediaAdapter = MediaItemAdapter(context!!, t!!,t,this@MediaListFragment,-1)
                mediaRv.adapter = mediaAdapter
            }


        })
        viewmodel.getTextSearch().observe(activity!!,object : Observer<String> {
            override fun onChanged(t: String?) {
                mediaAdapter.filter.filter(t)
            }
        })
    }





    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
//        activity!!.getMenuInflater().inflate(R.menu.menu_dashboard, menu);


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MediaListFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): MediaListFragment {
            val fragment = MediaListFragment()
//            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putString(ARG_PARAM2, param2)
//            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
