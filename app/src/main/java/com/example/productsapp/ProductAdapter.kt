package com.example.productsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import model.Product

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productItem: CardView = itemView.findViewById(R.id.cvProductItem)
        var productImage: ImageView = itemView.findViewById(R.id.ivThumbnail)
        var productName: TextView = itemView.findViewById(R.id.tvTitle)
        val productDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = products[position].title
        holder.productDescription.text = products[position].description
    }
}