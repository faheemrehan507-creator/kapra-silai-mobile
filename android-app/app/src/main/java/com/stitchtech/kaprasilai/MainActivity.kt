package com.stitchtech.kaprasilai

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)

        val s: WebSettings = webView.settings
        s.javaScriptEnabled = true
        s.domStorageEnabled = true
        s.databaseEnabled = true
        s.allowFileAccess = true
        s.allowContentAccess = true
        s.loadWithOverviewMode = true
        s.useWideViewPort = true

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        // Read HTML directly from APK assets stream and render via HTTPS base URL
        // This completely prevents net::ERR_FILE_NOT_FOUND on Android 11-15!
        val htmlContent = try {
            assets.open("kapra_silai_app.html").bufferedReader(Charsets.UTF_8).use { it.readText() }
        } catch (e: Exception) {
            try {
                assets.open("index.html").bufferedReader(Charsets.UTF_8).use { it.readText() }
            } catch (e2: Exception) {
                ""
            }
        }

        if (htmlContent.isNotEmpty()) {
            webView.loadDataWithBaseURL(
                "https://app.kaprasilai.pk/",
                htmlContent,
                "text/html",
                "UTF-8",
                null
            )
        } else {
            webView.loadUrl("file:///android_asset/kapra_silai_app.html")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (this::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
