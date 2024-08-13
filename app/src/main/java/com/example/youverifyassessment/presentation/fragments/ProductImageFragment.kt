package com.example.youverifyassessment.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentProductImageBinding

class ProductImageFragment(
    val image: String
) : Fragment() {

    private var _binding: FragmentProductImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productImageIV.setImageResource(R.drawable.img_cleanser)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}