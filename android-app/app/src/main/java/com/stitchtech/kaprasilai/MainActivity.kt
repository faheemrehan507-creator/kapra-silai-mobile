package com.stitchtech.kaprasilai

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    private fun readAssetHtml(): String {
        val candidates = listOf("kapra_silai_app.html", "index.html")
        for (name in candidates) {
            try {
                val text = assets.open(name).bufferedReader(Charsets.UTF_8).use { it.readText() }
                if (text.isNotBlank()) return text
            } catch (_: Exception) {}
        }
        return ""
    }

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
        s.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        val assetHtml = readAssetHtml()

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                if (request?.isForMainFrame == true && assetHtml.isNotEmpty()) {
                    view?.loadDataWithBaseURL(
                        "https://app.kaprasilai.pk/",
                        assetHtml,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            }
        }
        webView.webChromeClient = WebChromeClient()

        if (assetHtml.isNotEmpty()) {
            webView.loadDataWithBaseURL(
                "https://app.kaprasilai.pk/",
                assetHtml,
                "text/html",
                "UTF-8",
                null
            )
        } else {
            webView.loadUrl("https://faheemrehan507-creator.github.io/kapra-silai-mobile/")
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
