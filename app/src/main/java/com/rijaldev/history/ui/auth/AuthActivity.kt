package com.rijaldev.history.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rijaldev.history.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_auth)
    }
}