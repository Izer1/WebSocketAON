package com.izer.testwebsocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.coroutines.*
import okhttp3.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// this provide to make 'Open ws connection'


        //btn.setOnClickListener {

        //}

       /* GlobalScope.launch(Dispatchers.IO) {

            val client = OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                //.sslSocketFactory() - ? нужно ли его указывать дополнительно
                .build()
            val request = Request.Builder()
                .url("wss://master.ageofnerds.net:42069") // 'wss' - для защищенного канала "ws://10.0.2.2:42069"
                .build()
            val wsListener = WebSocketEcho (this@MainActivity)
            val webSocket = client.newWebSocket(request, wsListener)
        }*/
    }
    suspend fun doWork() = coroutineScope {
        val btn: Button = findViewById(R.id.buttonConnect)
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            //.sslSocketFactory() - ? нужно ли его указывать дополнительно
            .build()
        val request = Request.Builder()
            .url("wss://master.ageofnerds.net:42069") // 'wss' - для защищенного канала "ws://10.0.2.2:42069"
            .build()
        val wsListener = WebSocketEcho (this@MainActivity)

        val webSocket = client.newWebSocket(request, wsListener)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
