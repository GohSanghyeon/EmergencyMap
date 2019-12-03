package com.example.emergencymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_location_list.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class LocationList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)
        StrictMode.enableDefaults()
        var initem = false
        var inAddr = false
        var inLat = false
        var inLongi = false
        var buildAddress:String? = null
        var wgs84Lat:String? = null
        var wgs84Long:String? = null
        try
        {
            val urlstr = "http://apis.data.go.kr/B552657/AEDInfoInqireService/getAedFullDown?&pageNo=1&numOfRows=10&ServiceKey=" + "Rw5fnd4aIV%2FYmq31RakSWT7yW4UKspC%2FBEMy%2Bro8DUsn3%2Fw4qTA8kXBOXBlvnBGUhx1MiIcUm%2BIdOYB0iPQk%2BA%3D%3D"
            val url = URL(urlstr)
            val parserCreator = XmlPullParserFactory.newInstance()
            val parser = parserCreator.newPullParser()
            parser.setInput(url.openStream(), null)
            var parserEvent = parser.eventType
            println("파싱시작합니다.")
            while (parserEvent != XmlPullParser.END_DOCUMENT)
            {
                when (parserEvent) {
                    XmlPullParser.START_TAG -> {
                        if (parser.getName().equals("buildAddress"))
                        {
                            inAddr = true
                        }
                        if (parser.getName().equals("wgs84Lat"))
                        {
                            inLat = true
                        }
                        if (parser.getName().equals("wgs84Lon"))
                        {
                            inLongi = true
                        }
                        if (parser.getName().equals("message"))
                        { //message 태그를 만나면 에러 출력
                            result.setText("에러")
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                    }
                    XmlPullParser.TEXT -> {
                        if (inAddr)
                        {
                            buildAddress = parser.getText()
                            inAddr = false
                        }
                        if (inLat)
                        {
                            wgs84Lat = parser.getText()
                            inLat = false
                        }
                        if (inLongi)
                        {
                            wgs84Long = parser.getText()
                            inLongi = false
                        }
                    }
                    XmlPullParser.END_TAG -> if (parser.getName().equals("item"))
                    {
                        result.text = "\n 주소 : " + buildAddress + "\n 위도 : " + wgs84Lat + "\n 경도 : " + wgs84Long + "\t" // 이 부분 고침
                        initem = false
                    }
                }
                parserEvent = parser.next()
            }
        }
        catch (e:Exception) {
            result.setText("에러")
        }



    }
}
