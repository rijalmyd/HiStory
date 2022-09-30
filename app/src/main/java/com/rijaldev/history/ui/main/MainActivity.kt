package com.rijaldev.history.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rijaldev.history.R
import com.rijaldev.history.databinding.ActivityMainBinding
import com.rijaldev.history.ui.addpost.AddActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        binding.apply {
            bottomNav.setupWithNavController(navController)
            btnAddPost.setOnClickListener {
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                launcherAddPost.launch(intent)
            }
        }
    }

    private val launcherAddPost = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == UPLOAD_STATUS) {
            val isUploaded = it.data?.getBooleanExtra("isUploaded", false)
            if (isUploaded == true) binding.bottomNav.selectedItemId = R.id.homeFragment
        }
    }

    companion object {
        const val UPLOAD_STATUS = 0
    }
}