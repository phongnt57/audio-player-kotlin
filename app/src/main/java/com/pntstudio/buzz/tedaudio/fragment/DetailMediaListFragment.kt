package com.pntstudio.buzz.tedaudio.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.uamp.viewmodels.MediaItemFragmentViewModel
import com.pntstudio.buzz.tedaudio.R
import com.pntstudio.buzz.tedaudio.helps.PLAYPOS
import com.pntstudio.buzz.tedaudio.helps.SONG_POS
import com.pntstudio.buzz.tedaudio.model.MediaItemAdapter
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import com.pntstudio.buzz.tedaudio.model.MediaItemDetailAdapter
import com.pntstudio.buzz.tedaudio.services.MusicService
import com.pntstudio.buzz.tedaudio.viewmodel.MediaListFragmentViewModel
import kotlinx.android.synthetic.main.fragment_meia_list.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailMediaListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailMediaListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailMediaListFragment : Fragment(), MediaItemDetailAdapter.OnClickItem {
    override fun onClick(item: MediaItemData,position:Int) {
        mediaAdapter.currentItemSelect = position
        mediaAdapter.notifyDataSetChanged()
        songPicked(position)

    }

    private lateinit var mediaAdapter: MediaItemDetailAdapter
    private  lateinit var viewmodel: MediaItemFragmentViewModel

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meia_list_in_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaRv.setHasFixedSize(true);
        mediaRv.setLayoutManager(LinearLayoutManager(activity));
        viewmodel = ViewModelProviders.of(activity!!).get(MediaItemFragmentViewModel::class.java)
        mediaAdapter = MediaItemDetailAdapter(context!!, viewmodel.getMediaList().value!!, this@DetailMediaListFragment, viewmodel.getSelecrNUmber().value!!)
        mediaRv.adapter = mediaAdapter
        viewmodel.getSelectedMedia().observe(this,object :Observer<MediaItemData>{
            override fun onChanged(t: MediaItemData?) {
                val position = viewmodel.getMediaList().value!!.indexOf(t)
                mediaAdapter.currentItemSelect = position
                mediaAdapter.notifyDataSetChanged()

            }


        })

//        viewmodel.heroes.observe(this, object : Observer<ArrayList<MediaItemData>> {
//            override fun onChanged(t: ArrayList<MediaItemData>?) {
//
//            }
//
//
//        })
    }
    private fun songPicked(pos: Int) {
//        setupIconColors()
        Intent(activity, MusicService::class.java).apply {
            putExtra(SONG_POS, pos)
            action = PLAYPOS
            activity!!.startService(this)
        }
    }





    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
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
        mListener = null
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
        fun newInstance(): DetailMediaListFragment {
            val fragment = DetailMediaListFragment()
//            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putString(ARG_PARAM2, param2)
//            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor