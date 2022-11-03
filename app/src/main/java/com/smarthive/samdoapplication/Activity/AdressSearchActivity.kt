package com.smarthive.samdoapplication.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.smarthive.samdoapplication.LoadingDialog
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.ActivityAdressSearchBinding


class AdressSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdressSearchBinding
    var errorcheck = 0

    inner class MyJavaScriptInterface(private val mContext: Context) {
        @JavascriptInterface
        fun processDATA(data: String?) {
            val intent = Intent(mContext, SensorPopupActivity::class.java)
            intent.putExtra("data", data)
            setResult(0,intent)
            finish()
        }
    }



    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_adress_search)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.addJavascriptInterface(MyJavaScriptInterface(this), "Android")
        binding.webView.loadUrl("http://smarthive.kr/app/kakao")
        binding.webView.webViewClient = object : WebViewClient() {

            val dialog = LoadingDialog(this@AdressSearchActivity)
//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                super.onPageStarted(view, url, favicon)
//                Toast.makeText(applicationContext, "시작?", Toast.LENGTH_SHORT).show()
//                dialog.show()
//            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                errorcheck = 1
                Toast.makeText(applicationContext, "잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                dialog.dismiss()
                finish()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressBar.visibility = View.GONE
                if (errorcheck == 0) {
                    binding.webView.visibility = View.VISIBLE
                    binding.webView.loadUrl("javascript:sample2_execDaumPostcode();")
                }
            }
        }

        this.supportActionBar?.title = "주소찾기"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}

