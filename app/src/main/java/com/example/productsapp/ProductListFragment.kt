package com.example.productsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.productsapp.databinding.FragmentProductListBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.ApiService
import model.Product
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductListFragment : Fragment() {

    private val binding: FragmentProductListBinding by lazy {
        FragmentProductListBinding.inflate(layoutInflater)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var loadMoreButton: Button
    private var skip = 0
    private var products: MutableList<Product> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvProducts

        fetchProducts()

        loadMoreButton = binding.showListButton
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

        productListAdapter = ProductListAdapter(products)
        recyclerView.adapter = productListAdapter

        productListAdapter.setOnItemClickListener(object : ProductListAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                openProductFragment(product)
            }
        })
    }

    private fun openProductFragment(product: Product) {
        val fragment = ProductFragment()
        val args = Bundle().apply {
            putParcelable(ARG_PRODUCT, product)
        }
        fragment.arguments = args

        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, fragment)
            .addToBackStack(null)
            .commit()
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
            products = response.products

            if (::productListAdapter.isInitialized) {
                productListAdapter.addProducts(products)
            } else {
                productListAdapter = ProductListAdapter(products)
                recyclerView.adapter = productListAdapter
            }
        }
    }
}