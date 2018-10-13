package com.pntstudio.buzz.tedaudio

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.SeekBar
import com.example.android.uamp.viewmodels.MediaItemFragmentViewModel
import com.pntstudio.buzz.tedaudio.fragment.DetailMediaListFragment
import com.pntstudio.buzz.tedaudio.fragment.EnglishSubFragment
import com.pntstudio.buzz.tedaudio.fragment.InfoMediaFragment
import com.pntstudio.buzz.tedaudio.helps.*
import com.pntstudio.buzz.tedaudio.model.Events
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import com.pntstudio.buzz.tedaudio.services.MusicService
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.layout_media_controller.*
import java.util.*


class DetailActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var bus: Bus
    lateinit var model: MediaItemFragmentViewModel
    lateinit var alarm: AlarmManager
    lateinit var pendingIntent: PendingIntent
    var timeInterval = 0L
    var selectedIndexTime = 0;
    var isExistMedia = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        alarm = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntent = PendingIntent.getBroadcast(applicationContext, 0,
                Intent().setAction("STOP_TEST_SERVICE"), PendingIntent.FLAG_UPDATE_CURRENT)

        var listMedia = ArrayList<MediaItemData>()
        listMedia = intent.getSerializableExtra("list") as ArrayList<MediaItemData>
        val detail = intent.getSerializableExtra("detail") as MediaItemData
        val position = listMedia.indexOf(detail)
        isExistMedia  = intent.getBooleanExtra("isExistMedia",false)
        setSupportActionBar(tool_bar)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter


        model = ViewModelProviders.of(this).get(MediaItemFragmentViewModel::class.java!!)
        model.loadMediaItems(listMedia)
        model.setSelectedMedia(detail)
        model.setSelectNumber(position)

        bus = BusProvider.instance
        bus.register(this)
        initSeekbarChangeListener()
        shuffle_btn.setOnClickListener { toggleShuffle() }
        previous_btn.setOnClickListener { sendIntent(PREVIOUS) }
        play_pause_btn.setOnClickListener { sendIntent(PLAYPAUSE) }
        next_btn.setOnClickListener {
            sendIntent(NEXT)
        }
        repeat_btn.setOnClickListener { toggleSongRepetition() }
        song_progress_current.setOnClickListener { sendIntent(SKIP_BACKWARD) }
        song_progress_max.setOnClickListener { sendIntent(SKIP_FORWARD) }
        if(!isExistMedia) {
            initSericePlayer()
        }else{
            val handler = Handler()
            handler.postDelayed(Runnable {
                //Do something after 500ms
                bus.post(Events.EmptyObject())

            }, 500)

        }
        tabDots.setupWithViewPager(container, true)
        container.offscreenPageLimit = 3
        showStatusPlayer();

    }

    private fun toggleShuffle() {
        val isShuffleEnabled = !config.isShuffleEnabled
        config.isShuffleEnabled = isShuffleEnabled
        if (isShuffleEnabled)
            shuffle_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_shuffle_blue))
        else
            shuffle_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_shuffle))


    }

    private fun toggleSongRepetition() {
        val repeatSong = !config.repeatSong
        config.repeatSong = repeatSong
        if(repeatSong) repeat_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_repeat_blue))
        else  repeat_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_repeat))

    }
    private fun showStatusPlayer(){
        val isShuffleEnabled = config.isShuffleEnabled
        if (isShuffleEnabled)
            shuffle_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_shuffle_blue))
        else
            shuffle_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_shuffle))

        val repeatSong = config.repeatSong
        if(repeatSong) repeat_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_repeat_blue))
        else  repeat_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_repeat))


    }

    private fun initSeekbarChangeListener() {
        song_progressbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val duration = song_progressbar.max.getFormattedDuration()
                val formattedProgress = progress.getFormattedDuration()
                song_progress_current.text = formattedProgress
                song_progress_max.text = duration
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Intent(this@DetailActivity, MusicService::class.java).apply {
                    putExtra(PROGRESS, seekBar.progress)
                    action = SET_PROGRESS
                    startService(this)
                }
            }
        })
    }

    private fun initSericePlayer() {

        Intent(this, MusicService::class.java).apply {
            putExtra(SONG_POS, model.getSelecrNUmber().value)
            putExtra("list", model.getMediaList().value)
            putExtra("detail", model.getSelectedMedia().value)
//            putExtra(PLAYPOS, position)
            action = INIT
            startService(this)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detail, menu)
        if (config.alarm_turn_off)
            menu.getItem(0).icon = ContextCompat.getDrawable(this, R.drawable.ic_alarm_clock_selected)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        val id = item.itemId
        if (id == R.id.action_alarm) {
            if (!config.alarm_turn_off) {
                // not set alarm yet
                val singleChoiceItems = resources.getStringArray(R.array.dialog_alarm_str)
                val itemSelected = 0
                AlertDialog.Builder(this)
                        .setTitle("Select time to turn off ")
                        .setSingleChoiceItems(singleChoiceItems, itemSelected, DialogInterface.OnClickListener
                        { dialogInterface, selectedIndex -> setIndexAlarmTime(selectedIndex) })
                        .setPositiveButton("Ok" ){dialog, which -> setStopServiceAlarm(item) }
                        .setNegativeButton("Cancel", null)
                        .show()
            } else {
                // already set alarm
                AlertDialog.Builder(this)
                        .setTitle("Cancel alarm turn off")
                        .setMessage("Audio will turn off in " + convertLongToTime(timeInterval))
                        .setPositiveButton("Yes") { dialog, which -> cancelAlarm(item) }
                        .setNegativeButton("No") { dialog, which -> Log.d("MainActivity", "Aborting mission...") }
                        .show()


            }

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun cancelAlarm(item: MenuItem) {
        config.alarm_turn_off = false
        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_alarm_clock)
        alarm.cancel(pendingIntent)
    }

    fun setIndexAlarmTime(index: Int){
        selectedIndexTime = index;
    }

    fun setStopServiceAlarm( item: MenuItem) {
        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_alarm_clock_selected)
        config.alarm_turn_off = true
        timeInterval = TIME_MAP.get(selectedIndexTime)!! * 60 * 1000L + System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(timeInterval)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        } else {
            alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        }
    }


    @Subscribe
    fun songChangedEvent(event: Events.SongChanged) {
        Log.e("change", "----");
        val song = event.song
        if (song != null) {
            model.setSelectedMedia(song)
            tool_bar.setTitle(song.title)
            tool_bar.title  = song.title




//            this@DetailActivity.runOnUiThread(java.lang.Runnable {
//
//            })
        }

    }

    @Subscribe
    fun songChangeDuration(event: Events.DurationUpdate) {
        val duration = event.duration
        song_progressbar.max = duration / 1000

    }

    @Subscribe
    fun songStateChanged(event: Events.SongStateChanged) {
        val isPlaying = event.isPlaying
        play_pause_btn.setImageDrawable(resources.getDrawable(if (isPlaying) R.drawable.ic_pause_button else R.drawable.ic_play_button))
    }


    @Subscribe
    fun progressUpdated(event: Events.ProgressUpdated) {
        val progress = event.progress
        song_progressbar.progress = progress
    }

    override fun onDestroy() {
        super.onDestroy()
        bus.unregister(this)

    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) return DetailMediaListFragment.newInstance()
            else if (position == 1) return InfoMediaFragment.newInstance()
            else return EnglishSubFragment.newInstance()
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_detail, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments!!.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
