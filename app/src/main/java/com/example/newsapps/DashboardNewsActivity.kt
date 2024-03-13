package com.example.newsapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapps.view.ViewBindingExt.viewBinding
import com.example.newsapps.databinding.ActivityDashboardNewsBinding
import com.example.newsapps.view.Constant
import com.example.newsapps.view.Resource
import com.example.newsapps.view.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

class DashboardNewsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDashboardNewsBinding::inflate)
    private val viewModel: NewsViewModel by viewModels()
    private var mAdapterNews by Delegates.notNull<NewsListAdapter>()
    private var mTitle: String? = null
    private var mContent: String? = null
    private var mImage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        getLiveData()
    }

    private fun initView() = with(binding) {

        viewModel.getAllNews()

        mAdapterNews = NewsListAdapter(this@DashboardNewsActivity)

        rvNews.apply {
            setHasFixedSize(false)
            layoutManager =
                LinearLayoutManager(this@DashboardNewsActivity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapterNews
        }

        newsImage.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constant.NEWS_TITLE, mTitle)
            bundle.putString(Constant.NEWS_CONTENT, mContent)
            bundle.putString(Constant.NEWS_IMAGE, mImage)

            val intent = Intent(this@DashboardNewsActivity, DetailNewsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            lifecycleScope.launchWhenStarted {
                allNews.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            mAdapterNews.submitList(it.data)

                            val shuffledData = it.data?.shuffled()
                            if (!shuffledData.isNullOrEmpty()) {
                                val randomData = shuffledData.random()
                                showNewsRandom(randomData.image.toString(), randomData.title.toString())
                                mTitle = randomData.title.toString()
                                mContent = randomData.content.toString()
                                mImage = randomData.image.toString()
                            }
                        }

                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                            ToastUtils.showMessage(
                                this@DashboardNewsActivity,
                                it.message.toString()
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showNewsRandom(url: String, title: String){
        GlideApp.with(this)
            .load(url)
            .into(binding.newsImage)

        binding.titleNewsRandom.text = title
    }

}