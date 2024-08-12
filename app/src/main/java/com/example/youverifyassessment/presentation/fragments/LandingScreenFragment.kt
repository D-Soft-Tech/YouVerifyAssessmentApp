package com.example.youverifyassessment.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentLandingScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingScreenFragment : Fragment() {
    private lateinit var binding: FragmentLandingScreenBinding
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_landing_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        loginButton.setOnClickListener {
            val action =
                LandingScreenFragmentDirections.actionLandingScreenFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        signUpButton.setOnClickListener {
            val action =
                LandingScreenFragmentDirections.actionLandingScreenFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

    private fun initViews() {
        with(binding) {
            loginButton = loginBtn
            signUpButton = signUpBtn
        }
    }
}