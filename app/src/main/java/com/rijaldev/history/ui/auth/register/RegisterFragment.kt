package com.rijaldev.history.ui.auth.register

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
import com.rijaldev.history.data.remote.response.RegisterResponse
import com.rijaldev.history.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private val authViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setButtonEnable()
        binding?.apply {
            btnRegister.setOnClickListener {
                isOnLoading(true)

                val name = edRegisterName.text.toString().trim()
                val email = edRegisterEmail.text.toString().trim()
                val password = edRegisterPassword.text.toString().trim()
                authViewModel.register(name, email, password).observe(viewLifecycleOwner, registerObserver)
            }
            edRegisterName.addTextChangedListener {
                setButtonEnable()
            }
            edRegisterEmail.addTextChangedListener {
                setButtonEnable()
            }
            edRegisterPassword.addTextChangedListener {
                setButtonEnable()
            }
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private val registerObserver = Observer<Result<RegisterResponse>> {
        when (it) {
            is Result.Success -> {
                isOnLoading(false)
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            is Result.Error -> {
                isOnLoading(false)
                Toast.makeText(requireActivity(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun setButtonEnable(isEnabled: Boolean = true) {
        binding?.apply {
            val name = edRegisterName.text
            val email = edRegisterEmail.text
            val password = edRegisterPassword.text

            btnRegister.isEnabled =
                !name.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty() && isEnabled
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