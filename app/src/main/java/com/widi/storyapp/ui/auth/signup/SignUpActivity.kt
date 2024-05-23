package com.widi.storyapp.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.widi.storyapp.R
import com.widi.storyapp.ui.main.ViewModelFactory
import com.widi.storyapp.data.Result
import com.widi.storyapp.databinding.ActivitySignUpBinding
import com.widi.storyapp.ui.auth.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val signupViewModel:SignUpViewModel by viewModels<SignUpViewModel>{
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val loginActivity = Intent(this, LoginActivity::class.java)
        binding.apply {
            tvLogin.setOnClickListener{
                startActivity(loginActivity)
            }

            btnRegister.setOnClickListener {
                val email = edRegisterEmail.text.toString()
                val name = edRegisterName.text.toString()
                val password = edRegisterPassword.text?.toString()

                if (email.isNotEmpty() && name.isNotEmpty() && !password.isNullOrEmpty() && password.length >= 8) {
                    signupViewModel.submitRegister(
                        name = name,
                        email = email,
                        password = password
                    )
                } else {
                    Toast.makeText(this@SignUpActivity, "Please fill the form correctly", Toast.LENGTH_SHORT).show()
                }
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@SignUpActivity)
            builder.setView(R.layout.loading)
            val dialog: AlertDialog = builder.create()

            signupViewModel.responseResult.observe(this@SignUpActivity) {
                    response ->
                when (response) {
                    is Result.Loading -> dialog.show()
                    is Result.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@SignUpActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Result.Success -> {
                        dialog.dismiss()
                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                        intent.putExtra("USERNAME", edRegisterName.text.toString())
                        startActivity(intent)
                        finish()
                    }

                    else -> dialog.dismiss()
                }
            }
        }
    }
}