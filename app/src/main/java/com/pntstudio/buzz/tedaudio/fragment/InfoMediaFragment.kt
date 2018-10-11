package com.pntstudio.buzz.tedaudio.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.android.uamp.viewmodels.MediaItemFragmentViewModel

import com.pntstudio.buzz.tedaudio.R
import com.pntstudio.buzz.tedaudio.model.Download
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import com.pntstudio.buzz.tedaudio.services.DownloadService

import kotlinx.android.synthetic.main.fragment_info_media.*
import kotlinx.android.synthetic.main.fragment_english_sub.*
import org.jsoup.Jsoup

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InfoMediaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InfoMediaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoMediaFragment : Fragment() {
    private  lateinit var viewmodel: MediaItemFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProviders.of(activity!!).get(MediaItemFragmentViewModel::class.java)

        viewmodel.getSelectedMedia().observe(this,object : Observer<MediaItemData> {
            override fun onChanged(t: MediaItemData?) {
                Glide.with(activity!!)
                        .load(t!!.imageUrl)
                        .into(cover_img)
                title_tv.setText(t.description)

            }

        })



        download_img.setOnClickListener { downLoadFile() }

    }

    private fun downLoadFile() {
        val download = Download(viewmodel.getSelectedMedia().value!!.title!!,viewmodel.getSelectedMedia().value!!.mp3Url!!,"",0,0)
        val intent = Intent(activity, DownloadService::class.java)
        intent.putExtra("download",download)
        activity!!.startService(intent)


    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }



    companion object {

        fun newInstance(): InfoMediaFragment {
            val fragment = InfoMediaFragment()

            return fragment
        }
    }
}// Required empty public constructor

