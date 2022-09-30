package com.rijaldev.history.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.rijaldev.history.R
import com.rijaldev.history.databinding.FragmentProfileBinding
import com.rijaldev.history.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpToolbar()
    }

    private fun setUpView() {
        when (Locale.getDefault().language) {
            "id" -> setImageEnd(R.drawable.ic_flag_id)
            "en" -> setImageEnd(R.drawable.ic_flag_us)
        }
        binding?.tvLanguage?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.profile_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    profileViewModel.setLoginStatus(false)
                    profileViewModel.deleteToken()
                    val intent = Intent(requireActivity(), AuthActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    true
                }
                else -> false
            }
    }

    private fun setImageEnd(drawable: Int) {
        binding?.tvLanguage?.setCompoundDrawablesWithIntrinsicBounds(
            null, null, ContextCompat.getDrawable(requireActivity(), drawable), null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}