package com.example.newsapp

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.xml.sax.DTDHandler

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdaptar: NewsListAdaptar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycleView.layoutManager=LinearLayoutManager(this)
        fetchdata()
        mAdaptar=NewsListAdaptar( this)
        recycleView.adapter=mAdaptar

    }
    private fun fetchdata() {
        val url="https://newsapi.org/v2/top-headlines?country=in&apiKey=f017da8de21e4c53b098cf84bcd25dbe"
        val JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val newsJsonArray=it.getJSONArray("articles")
                val newsArray= ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject=newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdaptar.updateNews(newsArray)
            },
            Response.ErrorListener {
            })

        MySingleton.getInstance(this).addToRequestQueue(JsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val  builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

}