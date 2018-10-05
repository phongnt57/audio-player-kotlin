package com.pntstudio.buzz.tedaudio.rssparse


import com.pntstudio.buzz.tedaudio.model.MediaItemData
import org.xml.sax.InputSource
import java.io.StringReader
import java.net.URL
import java.util.*
import javax.xml.parsers.SAXParserFactory


/**
 * Class reads RSS data.
 * @author ITCuties
 */
class RssReader
/**
 * We set this URL with the constructor
 */
(// Our class has an attribute which represents RSS Feed URL
        private val rssUrlContent: String) {
    /**
     * Get RSS items. This method will be called to get the parsing process result.
     * @return
     */
    // At first we need to get an SAX Parser Factory object
    //source.setEncoding("utf-8");
    //source.setEncoding("ISO-8859-1");
    val items: ArrayList<MediaItemData>?
        @Throws(Exception::class)
        get() {
            try {
//                val url = URL(rssUrl)
                val factory = SAXParserFactory.newInstance()
                val parser = factory.newSAXParser()
                val reader = parser.xmlReader
                val handler = RssParseHandler()
                reader.contentHandler = handler
//                val source = InputSource(url.openStream())
                val inputSource = InputSource(StringReader(rssUrlContent))
                reader.parse(inputSource)
                return handler.mediaItemList
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null

        }
}

