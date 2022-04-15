package cs.mad.pantree.Activities

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import cs.mad.pantree.R

class WebViewActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)

        //bare bones webview acitivity to allow user to read recipe in app
        val i = intent
        val url = i.getStringExtra("url")
        if (url != null) {
            Log.d("url", url)
        }

        val webView = findViewById<WebView>(R.id.webView)
        if (url != null) {
            Log.d("url", url)
        }
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}