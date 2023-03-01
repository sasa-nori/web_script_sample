package com.example.htmlexample

import android.annotation.SuppressLint
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewAssetLoader.AssetsPathHandler
import androidx.webkit.WebViewClientCompat
import com.example.htmlexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        WebView.setWebContentsDebuggingEnabled(true)

        binding.mainWebView.apply {
            webChromeClient = chromeClient
            webViewClient = webClient

            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true

            val url = "https://appassets.androidplatform.net/assets/web_assets/index.html"
            loadUrl(url)
        }
    }

    private val assetsHandler by lazy {
        AssetsPathHandler(this)
    }
    private val assetLoader by lazy {
        WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", assetsHandler)
            .build()
    }

    private val webClient = object : WebViewClientCompat() {
        @RequiresApi(21)
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            if (request.url.schemeSpecificPart.startsWith("//appassets.androidplatform.net/")) {
                request.requestHeaders["Access-Control-Allow-Origin"] = "*";
            }
            //   request.requestHeaders["Content-type"] = "text/javascript"

            val interceptedWebRequest = assetLoader.shouldInterceptRequest(request.url)
            interceptedWebRequest?.let {
                if (request.url.toString().endsWith(".js", true)) {
                    it.mimeType = "text/javascript"
                }
            }
            return interceptedWebRequest
        }

        override fun shouldInterceptRequest(
            view: WebView,
            url: String
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(Uri.parse(url))
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceErrorCompat
        ) {
            super.onReceivedError(view, request, error)
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: WebResourceResponse
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            return false
        }
    }

    private val chromeClient = object : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            return super.onConsoleMessage(consoleMessage)
        }

        override fun onJsBeforeUnload(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return false
        }

        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return false
        }

        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return false
        }

        override fun onJsPrompt(
            view: WebView?,
            url: String?,
            message: String?,
            defaultValue: String?,
            result: JsPromptResult?
        ): Boolean {
            return false
        }

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            return false
        }
    }
}