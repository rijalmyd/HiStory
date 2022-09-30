package com.rijaldev.history.ui.auth.login

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rijaldev.history.R
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.LoginResponse
import com.rijaldev.history.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setButtonEnable()
        setUpView()
    }

    private fun setUpView() {
        binding?.apply {
            btnLogin.setOnClickListener {
                isOnLoading(true)

                val email = edLoginEmail.text.toString().trim()
                val password = edLoginPassword.text.toString().trim()

                loginViewModel.login(email, password).observe(viewLifecycleOwner, loginObserver)
            }
            edLoginEmail.addTextChangedListener {
                setButtonEnable()
            }
            edLoginPassword.addTextChangedListener {
                setButtonEnable()
            }
            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private val loginObserver = Observer<Result<LoginResponse>> {
        when (it) {
            is Result.Success -> {
                loginViewModel.setLoginStatus(true)
                isOnLoading(false)
                val token = it.data?.loginResult?.token
                loginViewModel.saveToken("Bearer ".plus(token.toString()))
                moveToMain()
            }
            is Result.Error -> {
                isOnLoading(false)
                Toast.makeText(requireActivity(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToMain() {
        findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        activity?.finish()
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                activity?.onBackPressed()
                return false
            }
        })
    }

    private fun setButtonEnable(isEnable: Boolean = true) {
        binding?.apply {
            val email = edLoginEmail.text
            val password = edLoginPassword.text

            btnLogin.isEnabled = !email.isNullOrEmpty() && !password.isNullOrEmpty() && isEnable
        }
    }

    private fun isOnLoading(isLoading: Boolean) {
        binding?.spinKit?.isVisible = isLoading
        setButtonEnable(!isLoading)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}