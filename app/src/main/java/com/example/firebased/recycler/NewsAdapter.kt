package com.example.firebased.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebased.R
import com.example.firebased.model.Info
import com.example.firebased.util.InternetConnectivity
import com.google.android.material.snackbar.Snackbar

class NewsAdapter(
    private val context: Context,
    private val listener: OnNewsClick
) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var news: List<Info> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.news,
                parent,
                false
            )
        return NewsViewHolder(view)

    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = news[position]
        holder.newsSource.text = news.author
        holder.newsTitle.text = news.title
        Glide.with(context).load(news.urlToImage).into(holder.newsImage)

        if (InternetConnectivity.isNetworkAvailable(this.context) == true) {
            holder.itemView.setOnLongClickListener {
                val publishTime: String = news.publishedAt
                val date = publishTime.slice(0..9)
                val time = publishTime.slice(11..18)
                val snackBar: Snackbar =
                    Snackbar.make(it, "News was published on $date at $time", Snackbar.LENGTH_SHORT)
                snackBar.animationMode = Snackbar.ANIMATION_MODE_SLIDE
                snackBar.show()
                return@setOnLongClickListener true
            }
        } else {
            holder.itemView.setOnLongClickListener {
                val snackBar: Snackbar =
                    Snackbar.make(it, "Not connected to internet", Snackbar.LENGTH_SHORT)
                snackBar.animationMode = Snackbar.ANIMATION_MODE_SLIDE
                snackBar.show()
                return@setOnLongClickListener true
            }
        }

    }

    override fun getItemCount(): Int {
        return news.size
    }


    inner class NewsViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val newsSource: TextView = itemView.findViewById(R.id.categorySourceName)
        val newsTitle: TextView = itemView.findViewById(R.id.categoryTitle)
        val newsImage: ImageView = itemView.findViewById(R.id.categoryImage)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(i: View?) {
            val position = adapterPosition
            val pNews = news[position]
            listener.onItemClick(pNews, position)
        }
    }

    fun setNews(aNews: List<Info>) {
        this.news = aNews
        notifyDataSetChanged()
    }

    interface OnNewsClick {
        fun onItemClick(article: Info, position: Int)
    }
}