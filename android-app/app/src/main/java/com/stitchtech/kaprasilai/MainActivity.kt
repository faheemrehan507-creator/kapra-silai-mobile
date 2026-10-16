package com.stitchtech.kaprasilai

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayInputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    private fun openAppAssetStream(): InputStream {
        val candidates = listOf("kapra_silai_app.html", "index.html")
        for (name in candidates) {
            try {
                return assets.open(name)
            } catch (_: Exception) {}
        }
        val fallback = """
            <!DOCTYPE html><html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
            <meta http-equiv="refresh" content="0;url=https://faheemrehan507-creator.github.io/kapra-silai-mobile/"></head>
            <body style="background:#0f172a;color:#fff;font-family:sans-serif;padding:24px;text-align:center;">
            <h3>Loading Kapra Silai Cloud...</h3>
            <p><a href="https://faheemrehan507-creator.github.io/kapra-silai-mobile/" style="color:#fbbf24;">Tap here if not redirected</a></p>
            </body></html>
        """.trimIndent()
        return ByteArrayInputStream(fallback.toByteArray(Charsets.UTF_8))
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

        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                val url = request?.url?.toString() ?: return null
                if (url.startsWith("https://appassets.kaprasilai.pk/")) {
                    return WebResourceResponse(
                        "text/html",
                        "UTF-8",
                        200,
                        "OK",
                        mapOf("Access-Control-Allow-Origin" to "*", "Cache-Control" to "no-store"),
                        openAppAssetStream()
                    )
                }
                return super.shouldInterceptRequest(view, request)
            }
        }
        webView.webChromeClient = WebChromeClient()

        // Load via virtual HTTPS asset origin — zero file:// errors, zero IPC string limits!
        webView.loadUrl("https://appassets.kaprasilai.pk/index.html")
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
