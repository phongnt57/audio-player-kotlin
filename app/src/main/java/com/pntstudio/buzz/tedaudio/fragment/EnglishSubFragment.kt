package com.pntstudio.buzz.tedaudio.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.uamp.viewmodels.MediaItemFragmentViewModel

import com.pntstudio.buzz.tedaudio.R
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import kotlinx.android.synthetic.main.fragment_english_sub.*
import org.jsoup.Jsoup
import android.text.method.ScrollingMovementMethod



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EnglishSubFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EnglishSubFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnglishSubFragment : Fragment() {
    private  lateinit var viewmodel: MediaItemFragmentViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            mParam1 = arguments!!.getString(ARG_PARAM1)
//            mParam2 = arguments!!.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_english_sub, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProviders.of(activity!!).get(MediaItemFragmentViewModel::class.java)
        transcript_tv.setMovementMethod(ScrollingMovementMethod())


        viewmodel.getSelectedMedia().observe(this,object : Observer<MediaItemData>{
            override fun onChanged(t: MediaItemData?) {
                val link = t!!.originLink
                val trancscriptLink = link!!.replace("?rss", "/transcript?transcript")
                transcript_tv.text = ""
                val threadJSoup = Thread(Runnable {
                    try {
                        Jsoup.connect(trancscriptLink).get().run {
                            Log.e("title",title())
                            var trancript = ""
                            select("div.Grid__cell > p").forEachIndexed { index, element ->
//                                Log.e("elemt",element.text())
//                                Log.e("index","".plus(index))
                                trancript =  trancript + element.text()+"\n"+"\n"



                            }
                            activity!!.runOnUiThread(Runnable {
                                transcript_tv.text = trancript
                            })


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })

                threadJSoup.start()


            }

        })

    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
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

        fun newInstance(): EnglishSubFragment {
            val fragment = EnglishSubFragment()

            return fragment
        }
    }
}// Required empty public constructor
