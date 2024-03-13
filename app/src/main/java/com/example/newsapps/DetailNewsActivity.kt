package com.example.newsapps

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.esmaeel.shareitlib.SharableItem
import com.esmaeel.shareitlib.Share
import com.example.newsapps.databinding.ActivityDetailNewsBinding
import com.example.newsapps.view.Constant
import com.example.newsapps.view.ViewBindingExt
import com.example.newsapps.view.ViewBindingExt.viewBinding

class DetailNewsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDetailNewsBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {

        val imageNews = intent.getStringExtra(Constant.NEWS_IMAGE)
        val titleNews = intent.getStringExtra(Constant.NEWS_TITLE)
        val contentNews = intent.getStringExtra(Constant.NEWS_CONTENT)

        GlideApp.with(this@DetailNewsActivity)
            .load(imageNews)
            .into(binding.newsImage)
        binding.titleNews.text = titleNews

        val content: Spanned
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content = Html.fromHtml(contentNews, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            content = Html.fromHtml(contentNews)
        }
        binding.txtContent.text = content


        binding.btnShare.setOnClickListener {
            Share.with(context = this@DetailNewsActivity).item(
                SharableItem(
                    pictureUrl = imageNews!!,
                    data = titleNews!!,
                    shareAppLink = false,
                    downloadOurAppMessage = "Find us here"
                ),

                onStart = {
                    Log.e(TAG, "Sharing Started.")
                },

                onFinish = { isSuccessful: Boolean, errorMessage: String ->
                    if (isSuccessful)
                        Log.e(TAG, "Successfully shared")
                    else
                        Log.e(TAG, "error happened : $errorMessage")

                }
            )
        }
    }

}