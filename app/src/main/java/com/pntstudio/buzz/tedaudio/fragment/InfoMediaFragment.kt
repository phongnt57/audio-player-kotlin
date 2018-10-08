package com.pntstudio.buzz.tedaudio.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.uamp.viewmodels.MediaItemFragmentViewModel

import com.pntstudio.buzz.tedaudio.R
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import kotlinx.android.synthetic.main.fragment_info_media.*
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration





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
    private var fetch: Fetch? = null


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
        val fetchConfiguration = FetchConfiguration.Builder(activity!!)
                .setDownloadConcurrentLimit(3)
                .build()
        fetch = Fetch.getInstance(fetchConfiguration)


        download_img.setOnClickListener { downLoadFile() }
    }

    private fun downLoadFile() {
        val request = Request(viewmodel.getSelectedMedia().value!!.mp3Url!!, "")
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL
        fetch!!.enqueue(request, error(){


        })

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }



    companion object {

        // TODO: Rename and change types and number of parameters
        fun newInstance(): InfoMediaFragment {
            val fragment = InfoMediaFragment()

            return fragment
        }
    }
}// Required empty public constructor

