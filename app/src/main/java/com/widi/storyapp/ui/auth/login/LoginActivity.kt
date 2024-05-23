package com.widi.storyapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.widi.storyapp.R
import com.widi.storyapp.ui.main.ViewModelFactory
import com.widi.storyapp.data.Result
import com.widi.storyapp.databinding.ActivityLoginBinding
import com.widi.storyapp.ui.auth.signup.SignUpActivity
import com.widi.storyapp.ui.story.StoryActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val loginViewModel: LoginViewModel by viewModels<LoginViewModel> {
        factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            tvRegister.setOnClickListener{
                val registerActivity = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(registerActivity)
            }

            btnLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text?.toString()

                if (email.isNotEmpty() && !password.isNullOrEmpty() && password.length >= 8) {
                    loginViewModel.submitLogin(
                        email = email,
                        password = password
                    )
                } else {
                    Toast.makeText(this@LoginActivity, "Please fill the form correctly", Toast.LENGTH_SHORT).show()
                }
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
            builder.setView(R.layout.loading)
            val dialog: AlertDialog = builder.create()

            loginViewModel.responseResult.observe(this@LoginActivity) { response ->
                when (response) {
                    is Result.Loading -> dialog.show()
                    is Result.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Result.Success -> {
                        dialog.dismiss()
                        val intent = Intent(this@LoginActivity, StoryActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }

                    else -> dialog.dismiss()
                }
            }
        }
    }

}