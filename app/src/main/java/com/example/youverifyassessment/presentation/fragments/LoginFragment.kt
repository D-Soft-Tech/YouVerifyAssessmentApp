package com.example.youverifyassessment.presentation.fragments

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.youverifyassessment.R
import com.example.youverifyassessment.databinding.FragmentLoginBinding
import com.example.youverifyassessment.domain.DeviceUtilsContract
import com.example.youverifyassessment.presentation.viewModels.AppViewModel
import com.example.youverifyassessment.utils.ModelMapper.toDomain
import com.example.youverifyassessment.utils.UtilsAndExtensions.getLoadingAlertDialog
import com.example.youverifyassessment.utils.UtilsAndExtensions.isValidEmail
import com.example.youverifyassessment.utils.UtilsAndExtensions.runIfConnected
import com.example.youverifyassessment.utils.UtilsAndExtensions.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var loginButton: Button
    private lateinit var signInWithGoogleButton: Button
    private lateinit var emailTiET: TextInputEditText
    private lateinit var passwordTiET: TextInputEditText

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    @Inject
    lateinit var deviceUtilsUseCase: DeviceUtilsContract

    @Inject
    lateinit var fireBaseInstance: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    private val appViewModel: AppViewModel by activityViewModels()
    private var loaderAlertDialog: Dialog? = null

    private val googleSignInRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(it)
                        loaderAlertDialog?.show()
                        loaderAlertDialog?.dismiss()
                        val loggedInUser = task.result
                        signInWithFireBase(loggedInUser)
                    } catch (e: Exception) {
                        e.localizedMessage?.let { it1 -> requireContext().showToast(it1) }
                    }
                }
            } else {
                requireContext().showToast(getString(R.string.failed_try_again))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        loaderAlertDialog = requireContext().getLoadingAlertDialog()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        signInWithGoogleButton.setOnClickListener {
            requireContext().runIfConnected(deviceUtilsUseCase) {
                googleSignInRequestLauncher.launch(googleSignInClient.signInIntent)
            }
        }

        loginButton.setOnClickListener {
            requireContext().runIfConnected(deviceUtilsUseCase) {
                when {
                    emailTiET.text.toString().trim().isEmpty() -> emailTiET.error =
                        getString(R.string.required)

                    !isValidEmail(emailTiET.text.toString().trim()) -> emailTiET.error =
                        getString(R.string.invalid_email)

                    else -> {
                        emailTiET.error = null
                        when {
                            passwordTiET.text.toString().trim().isEmpty() -> passwordTiET.error =
                                getString(R.string.required)

                            passwordTiET.text.toString().trim().length < 4 -> passwordTiET.error =
                                getString(R.string.password_too_short)

                            else -> {
                                loginWithFireBase(
                                    emailTiET.text.toString().trim(),
                                    passwordTiET.text.toString().trim()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun signInWithFireBase(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        fireBaseInstance.signInWithCredential(credential).addOnSuccessListener {
            requireContext().showToast(getString(R.string.logged_in_successfully))
            // save user session
            it?.user?.let { user ->
                appViewModel.saveUser(user.toDomain())
            }
            // Navigate to products fragment
            val action = LoginFragmentDirections.actionLoginFragmentToProductsFragment()
            findNavController().navigate(action)
        }.addOnFailureListener {
            it.localizedMessage?.let { it1 -> requireContext().showToast(it1) }
        }
    }

    private fun loginWithFireBase(email: String, password: String) {
        loaderAlertDialog?.show()
        fireBaseInstance.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                loaderAlertDialog?.dismiss()
                requireContext().showToast(getString(R.string.logged_in_successfully))
                // save user session
                it.result?.user?.let { user ->
                    appViewModel.saveUser(user.toDomain())
                }
                // Navigate to products fragment
                val action = LoginFragmentDirections.actionLoginFragmentToProductsFragment()
                findNavController().navigate(action)
            } else {
                loaderAlertDialog?.dismiss()
                requireContext().showToast(getString(R.string.login_failed))
            }
        }
    }

    private fun initViews() {
        with(binding) {
            emailTiET = userEmailTiet
            passwordTiET = userPassword
            loginButton = signUpBtn
            signInWithGoogleButton = signInWithGoogle
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loaderAlertDialog = null
    }
}