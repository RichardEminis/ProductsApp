package com.example.productsapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import model.Product

class ProductListAdapter(
    private val products: MutableList<Product>,
    private var itemClickListener: OnItemClickListener? = null
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun addProducts(newProducts: List<Product>) {
        newProducts.forEach {
            products.add(it)
        }
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productItem: CardView = itemView.findViewById(R.id.cvProductItem)
        var productImage: ImageView = itemView.findViewById(R.id.ivThumbnail)
        var productName: TextView = itemView.findViewById(R.id.tvTitle)
        val productDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_product, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = products[position].title
        holder.productDescription.text = products[position].description
        holder.productItem.setOnClickListener {
            itemClickListener?.onItemClick(products[position])
        }
        Picasso.get().load(products[position].thumbnail).into(holder.productImage)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}