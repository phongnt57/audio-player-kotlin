/*
 * Copyright 2018 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.uamp.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.example.android.uamp.EMPTY_PLAYBACK_STATE

import com.example.android.uamp.MediaSessionConnection
import com.example.android.uamp.NOTHING_PLAYING

import com.example.android.uamp.media.extensions.id
import com.example.android.uamp.media.extensions.isPlayEnabled
import com.example.android.uamp.media.extensions.isPlaying
import com.pntstudio.buzz.tedaudio.R
import com.pntstudio.buzz.tedaudio.model.MediaItemData

/**
 * [ViewModel] for [MediaItemFragment].
 */
public class MediaItemFragmentViewModel : ViewModel() {
    private var mediaList: MutableLiveData<ArrayList<MediaItemData>> = MutableLiveData()
    private var selectedArticle = MutableLiveData<MediaItemData>()
    private var selectNumber = MutableLiveData<Int>()
    fun getSelectedMedia(): MutableLiveData<MediaItemData> {
        return selectedArticle
    }
    fun setSelectNumber(number: Int){
        selectNumber.value = number
    }
    fun getSelecrNUmber():MutableLiveData<Int> {
        return selectNumber
    }


    fun setSelectedMedia(article: MediaItemData) {
        selectedArticle.setValue(article)
    }

    fun getMediaList(): MutableLiveData<ArrayList<MediaItemData>> {
        return mediaList
    }

    fun loadMediaItems(list:ArrayList<MediaItemData>) {
        // fetch media here
        mediaList.value = list


    }


}