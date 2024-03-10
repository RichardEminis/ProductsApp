package com.example.productsapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var loadMoreButton: Button
    private var skip = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvProducts)

        fetchProducts()

        loadMoreButton = findViewById(R.id.show_list_button)
        loadMoreButton.setOnClickListener {
            skip += ITEMS_PER_PAGE
            fetchProducts()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadMoreButton.visibility = View.VISIBLE
                } else {
                    loadMoreButton.visibility = View.GONE
                }
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchProducts() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            val response = apiService.getProducts(skip, ITEMS_PER_PAGE)
            val products = response.products

            if (::productAdapter.isInitialized) {
                productAdapter.addProducts(products)
            } else {
                productAdapter = ProductAdapter(products)
                recyclerView.adapter = productAdapter
            }
        }
    }
}