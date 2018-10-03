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
//    var  parsingImageUrl = false
//    var parsingMp3Url = false
    var parsingdateTime = false
    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if ("item" == qName) {
            currentMediaItem = MediaItemData("","","","","")
        } else if ("title" == qName) {
            parsingTitle = true
        } else if("description"==qName){
            parsingDescription = true;

        }else if("pubDate"==qName){
            parsingdateTime = true;

        }
        else if (localName=="thumbnail") {
            val thumbnail = attributes?.getValue("url")
            currentMediaItem.imageUrl = thumbnail!!

        }  else if (localName == "content") {
            val mp3Link = attributes?.getValue("url")
            currentMediaItem.mp3Url = mp3Link.toString()
        }




    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if ("item".equals(qName, ignoreCase = true)) {
            mediaItemList.add(currentMediaItem)
//            currentMediaItem = null
        } else if ("title".equals(qName, ignoreCase = true)) {
            parsingTitle = false
        } else if ("description".equals(qName, ignoreCase = true)) {
            parsingDescription = false
        } else if ("pubDate".equals(qName, ignoreCase = true)) {
            parsingdateTime = false

        }
    }

    // Characters method fills current RssItem object with data when title and link tag content is being processed
    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
        if (parsingTitle) {
                currentMediaItem.title = String(ch, start, length)

        } else if (parsingDescription) {
                // currentItem.setLink(new String(ch, start, length));
                currentMediaItem.description = String(ch, start, length)
                Log.e("description", String(ch, start, length))

        } else if (parsingdateTime) {
                currentMediaItem.dateTime = String(ch,start,length)

        }
    }
}