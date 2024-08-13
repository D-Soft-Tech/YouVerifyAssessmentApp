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
import com.example.youverifyassessment.databinding.FragmentSignUpBinding
import com.example.youverifyassessment.domain.DeviceUtilsContract
import com.example.youverifyassessment.presentation.viewModels.AppViewModel
import com.example.youverifyassessment.utils.ModelMapper.toDomain
import com.example.youverifyassessment.utils.UtilsAndExtensions.getLoadingAlertDialog
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
class SignUpFragment : Fragment() {
    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions
    @Inject
    lateinit var deviceUtilsUseCase: DeviceUtilsContract
    @Inject
    lateinit var fireBaseInstance: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var signUpWithFireBaseAuthButton: Button
    private lateinit var signUpWithGoogleButton: Button

    private val appViewModel: AppViewModel by activityViewModels()
    private var loaderAlertDialog: Dialog? = null

    private val googleSignInRequestLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(it)
                        if (deviceUtilsUseCase.isConnectionAvailable()) {
                            loaderAlertDialog?.show()
                            if (task.isSuccessful) {
                                loaderAlertDialog?.dismiss()
                                val loggedInUser = task.result
                                signInWithGoogleThenFireBase(loggedInUser)
                            } else {
                                requireContext().showToast(getString(R.string.request_not_yet_completed))
                            }
                        } else {
                            requireContext().showToast(getString(R.string.no_connection))
                        }
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        loaderAlertDialog = requireContext().getLoadingAlertDialog()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

        // Sign up with google
        signUpWithGoogleButton.setOnClickListener {
            googleSignInRequestLauncher.launch(googleSignInClient.signInIntent)
        }

        // Sign up with firebase
        signUpWithFireBaseAuthButton.setOnClickListener {
            signUpWithFireBase(email.text.toString(), password.text.toString())
        }
    }

    private fun signInWithGoogleThenFireBase(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        fireBaseInstance.signInWithCredential(credential).addOnSuccessListener {
            requireContext().showToast(getString(R.string.logged_in_successfully))
            // save user session
            it?.user?.let { user ->
                appViewModel.saveUser(user.toDomain())
            }
            // Navigate to product fragment
            val action = SignUpFragmentDirections.actionSignUpFragmentToProductsFragment()
            findNavController().navigate(action)
        }.addOnFailureListener {
            it.localizedMessage?.let { it1 -> requireContext().showToast(it1) }
        }
    }

    private fun signUpWithFireBase(email: String, password: String) {
        fireBaseInstance.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                requireContext().showToast(getString(R.string.logged_in_successfully))
                // save user session
                it.result?.user?.let { user ->
                    appViewModel.saveUser(user.toDomain())
                }
                // Navigate to main app
                val action = SignUpFragmentDirections.actionSignUpFragmentToProductsFragment()
                findNavController().navigate(action)
            } else {
                requireContext().showToast(getString(R.string.login_failed))
            }
        }.addOnFailureListener {
            requireContext().showToast(getString(R.string.failed_try_again))
        }
    }

    private fun initViews() {
        with(binding) {
            this@SignUpFragment.email = emailTiet
            this@SignUpFragment.password = passwordTiet
            signUpWithFireBaseAuthButton = signUpBtn
            signUpWithGoogleButton = signInWithGoogle
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        loaderAlertDialog = null
    }
}