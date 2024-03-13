package com.example.newsapps

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapps.databinding.ItemnewsBinding
import com.example.newsapps.view.Constant
import com.example.newsapps.view.Constant.glideRequestOption

class NewsListAdapter (private val activity: FragmentActivity) : ListAdapter<News, NewsListAdapter.Holder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemnewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)

        with(holder.binding) {
            GlideApp.with(activity)
                .load(data.image)
                .apply(glideRequestOption(activity, R.drawable.image_empty))
                .into(imageNews)

            if (data.image == null){
                imageNews.visibility = View.GONE
            }

            textNewsName.text = data.title ?: "-"

            cardNews.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Constant.NEWS_TITLE, data.title.toString())
                bundle.putString(Constant.NEWS_CONTENT, data.content.toString())
                bundle.putString(Constant.NEWS_IMAGE, data.image.toString())

                val intent = Intent(activity, DetailNewsActivity::class.java)
                intent.putExtras(bundle)
                activity.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyDiffCallback : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(val binding: ItemnewsBinding) : RecyclerView.ViewHolder(binding.root)
}