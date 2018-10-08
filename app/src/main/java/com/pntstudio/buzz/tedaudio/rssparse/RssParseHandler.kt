package com.pntstudio.buzz.tedaudio.rssparse

import android.util.Log
import com.pntstudio.buzz.tedaudio.model.MediaItemData
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

/**
 * Created by admin on 10/2/18.
 */
class RssParseHandler : DefaultHandler() {
    var mediaItemList = arrayListOf<MediaItemData>()
    lateinit var currentMediaItem: MediaItemData;
    var parsingTitle = false
    var parsingDescription = false
    var parsingItem = false
    var parsingOrginLink = false
//    var  parsingImageUrl = false
//    var parsingMp3Url = false
    var parsingdateTime = false
    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if ("item" == qName) {
            currentMediaItem = MediaItemData("","","","","","")
            parsingItem = true
        } else if ("title" == qName) {
            parsingTitle = true
        } else if("description"==qName){
            parsingDescription = true;
        }else if("pubDate"==qName){
            parsingdateTime = true;

        }else if("feedburner:origLink"==qName){
            parsingOrginLink = true
        }
        else if (localName=="thumbnail" && parsingItem) {
            val thumbnail = attributes?.getValue("url")
            currentMediaItem.imageUrl = thumbnail!!

        }  else if (localName == "content" && parsingItem) {
            val mp3Link = attributes?.getValue("url")
            currentMediaItem.mp3Url = mp3Link.toString()
        }

    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if ("item".equals(qName, ignoreCase = true)) {
            mediaItemList.add(currentMediaItem)
            parsingItem = false
//            currentMediaItem = null
        } else if ("title".equals(qName, ignoreCase = true)) {
            parsingTitle = false
        } else if ("description".equals(qName, ignoreCase = true)) {
            parsingDescription = false
        } else if ("pubDate".equals(qName, ignoreCase = true)) {
            parsingdateTime = false

        }else if("feedburner:origLink".equals(qName,ignoreCase = true)){
            parsingOrginLink = false
        }
    }

    // Characters method fills current RssItem object with data when title and link tag content is being processed
    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
        if (parsingTitle && parsingItem) {
                currentMediaItem.title = String(ch, start, length)

        } else if (parsingDescription && parsingItem) {
                // currentItem.setLink(new String(ch, start, length));
                currentMediaItem.description = String(ch, start, length)
                Log.e("description", String(ch, start, length))

        } else if (parsingdateTime && parsingItem) {
                currentMediaItem.dateTime = String(ch,start,length)

        }else if(parsingOrginLink && parsingItem){
            currentMediaItem.originLink = String(ch,start,length)
        }
    }
}