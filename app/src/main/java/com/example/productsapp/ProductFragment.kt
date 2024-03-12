package com.example.productsapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.productsapp.databinding.FragmentProductBinding
import com.squareup.picasso.Picasso
import model.Product

class ProductFragment : Fragment() {

    private var product: Product? = null

    private val binding: FragmentProductBinding by lazy {
        FragmentProductBinding.inflate(layoutInflater)
    }

    private fun bind(product: Product, imageView: ImageView) = with(binding) {
        tvFragmentDescription.text = product.description
        tvProductTitle.text = product.title
        tvBrand.text = product.brand
        tvPriceCount.text = product.price.toString()
        tvRatingCount.text = product.rating.toString()
        Picasso.get().load(product.thumbnail).into(imageView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_PRODUCT, Product::class.java)
        } else {
            arguments?.getParcelable(ARG_PRODUCT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product?.let { product ->
            bind(product, view.findViewById(R.id.ivProductImage))
        }

        val backButton = view.findViewById<Button>(R.id.btnBackToList)

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}