package com.izer.testwebsocket

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences.Editor
import android.util.JsonReader
import android.util.Log
import io.ktor.utils.io.errors.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

class WebSocketEcho(val activity: Activity): WebSocketListener() {
    private val userDataSet : String =  "UserAutData";
    private val uuid : String = "UserId"
    private val currMsg : String? = null
    override fun onOpen(webSocket: WebSocket, response: Response) {
        val sharedPref = activity.getSharedPreferences(userDataSet, Context.MODE_PRIVATE)
        val hasVisited:Boolean = sharedPref.getBoolean("hasVisited", false)
        if (!hasVisited) {

            val json1 = JSONObject()
            val json2 = JSONObject()
            json2.put("name", "test")
            json2.put("pic", "default")
            json1.put("com", "newUser")
            json1.put("user", json2)

            val a = json1.toString()
            webSocket.send(a)
        } else {
            val json1 = JSONObject()
            val json2 = JSONObject()
            json2.put("uuid", sharedPref.getString(uuid, null))
            json1.put("com", "existingUser")
            json1.put("user", json2)
            val a = json1.toString()
            webSocket.send(a)
        }

    }
    private fun getUUid() {
       // uuid = text.toString()
    }
    override fun onMessage(webSocket: WebSocket, text: String) {

        doParse(text)
        output("Receiving : " + text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        output("Receiving bytes : " + bytes!!.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket!!.close(NORMAL_CLOSURE_STATUS, null)
        output("Closing : $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        output("Error : " + t.message)
    }

    companion object {
        private val NORMAL_CLOSURE_STATUS = 1000
    }

    private fun output(txt: String) {
        //val json_contact:JSONObject = JSONObject(txt)
        //json_contact.
        Log.v("WSS", txt)
    }
    private fun doParse(text:String) {
        val sharedPref = activity.getSharedPreferences(userDataSet, Context.MODE_PRIVATE)
        val hasVisited:Boolean = sharedPref.getBoolean("hasVisited", false)

        if (!hasVisited) {

            val reader = JsonReader(text.reader())
            reader.beginObject();
                while (reader.hasNext()) {
                    try {
                        val name = reader.nextName()
                        if (name.equals("uuid")) {
                            val e: Editor = sharedPref.edit()
                            e.putBoolean("hasVisited", true)
                            e.apply()
                            e.putString(uuid, reader.nextString())
                            e.apply()
                            output(uuid)
                        } else {
                            reader.skipValue()
                        }
                    } catch (e:IOException) {
                        output(e.toString())
                    }
                }

        }
    }
}

