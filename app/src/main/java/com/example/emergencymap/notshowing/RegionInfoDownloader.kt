package com.example.emergencymap.notshowing

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

class RegionInfoDownloader(
    private val context: Context
    , private val doingWithItems: (JSONArray?) -> Unit
) : AsyncTask<Void, Void, JSONArray?>(){
    private var requestURLString = "http://15.164.116.17/itemNum_of_regions.php"

    override fun doInBackground(vararg p0: Void?): JSONArray? {
        var conn: HttpURLConnection? = null

        try{
            conn = URL(requestURLString).openConnection() as HttpURLConnection
            conn.apply{
                requestMethod = "POST"
                this.setRequestProperty("Accept-Charset", "UTF-8")
                this.setRequestProperty("Context_Type",
                    "application/x-www-form-urlencoded;charset=UTF-8")
            }

            if(conn.responseCode == HttpURLConnection.HTTP_OK)
                return downloadItems(conn);
        }
        catch(e: UnknownHostException){ context.toast("연결에 실패하였습니다.") }
        catch(e: Exception){ Log.d("Downloader Exception!","catch...${e.toString()}") }
        finally{ conn?.disconnect() }

        return null
    }

    override fun onPostExecute(result: JSONArray?) {
        super.onPostExecute(result)

        doingWithItems(result)
    }

    private fun downloadItems(conn: HttpURLConnection) : JSONArray?{
        val rawJSONBuffer = StringBuffer()

        val isr = InputStreamReader(conn.inputStream, "UTF-8")
        BufferedReader(isr).forEachLine { rawJSONBuffer.append(it) }

        if(rawJSONBuffer.toString().equals("[]"))
            return null

        try{
            return JSONArray(rawJSONBuffer.toString())
        }catch (e: JSONException){
            Log.d("Download-JSON Making", "Failed! : ${e.stackTrace}")
            throw e
        }
    }
}