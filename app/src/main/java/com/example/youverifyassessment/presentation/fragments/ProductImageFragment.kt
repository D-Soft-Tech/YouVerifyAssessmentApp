package com.example.youverifyassessment.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentProductImageBinding

class ProductImageFragment(
    private val image: String
) : Fragment() {

    private lateinit var binding: FragmentProductImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_image, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            productImageUrl = image
            lifecycleOwner = viewLifecycleOwner
        }
    }
}