package com.docshare.docshare.ui.login

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.docshare.docshare.R
import com.docshare.docshare.data.repository.AuthState
import com.docshare.docshare.data.repository.GoogleAuthRepository
import com.docshare.docshare.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val viewModel: LoginViewModel by viewModels {
        val repo = GoogleAuthRepository(requireContext(), FirebaseAuth.getInstance())
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(repo) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.handleSignInResult(result.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.googleButton.setOnClickListener {
            if (!isNetworkAvailable()) {
                Toast.makeText(requireContext(), getString(R.string.login_network_unavailable), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            launcher.launch(viewModel.getGoogleIntent())
        }

        binding.guestButton.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { state ->
                    when (state) {
                        is AuthState.Idle -> setLoading(false)
                        is AuthState.Loading -> setLoading(true)
                        is AuthState.Success -> {
                            setLoading(false)
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                        is AuthState.Error -> {
                            setLoading(false)
                            Toast.makeText(requireContext(), state.msg, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.googleButton.isEnabled = !loading
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = requireContext().getSystemService(ConnectivityManager::class.java)
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

