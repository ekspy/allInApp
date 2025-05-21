package com.example.atividade

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.atividade.databinding.ActivityMainBinding
import java.io.File

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var statusFragment: StatusFragment
    private val PREFS_NAME = "MyPrefs"
    private val KEY_NAME = "name"
    private val KEY_USERNAME = "username"
    private val KEY_PASSWORD = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar, false, "Controle Esperto")
        checkLoginCache()
        setupLoginButton()
        setupRegisterButton()
    }

    private fun checkLoginCache() {
        val cacheFile = File(cacheDir, "login_cache")
        if (cacheFile.exists()) {
            val credentials = cacheFile.readText().split(":")
            if (isValidCredentials(credentials[0], credentials[1])) {
                showDashboard()
            }
        }
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedUsername = prefs.getString(KEY_USERNAME, "admin")
        val savedPassword = prefs.getString(KEY_PASSWORD, "1234")
        return username == savedUsername && password == savedPassword
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etusername.text.toString()
            val password = binding.etPassword.text.toString()

            if (isValidCredentials(username, password)) {
                File(cacheDir, "login_cache").writeText("$username:$password")
                showDashboard()
            } else {
                Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            if (binding.registerCard.visibility == View.VISIBLE) {
                // Try to register
                val username = binding.etRegisterUsername.text.toString()
                val password = binding.etRegisterPassword.text.toString()
                val confirmPassword = binding.etConfirmPassword.text.toString()

                when {
                    username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                        Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }
                    password != confirmPassword -> {
                        Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Save credentials
                        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().apply {
                            putString(KEY_USERNAME, username)
                            putString(KEY_PASSWORD, password)
                            apply()
                        }
                        Toast.makeText(this, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        binding.registerCard.visibility = View.GONE
                        binding.loginCard.visibility = View.VISIBLE
                        binding.btnRegister.text = "Registrar"
                    }
                }
            } else {
                // Show register form
                binding.loginCard.visibility = View.GONE
                binding.registerCard.visibility = View.VISIBLE
                binding.btnRegister.text = "Confirmar Registro"
            }
        }
    }

    private fun showDashboard() {
        // Hide login views
        binding.loginCard.visibility = View.GONE
        binding.registerCard.visibility = View.GONE
        binding.btnRegister.visibility = View.GONE
        
        // Show dashboard
        binding.dashboardCard.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.VISIBLE

        setupStatusFragment()
        setupNamePreferences()
        setupLightSwitch()
        setupNavigationButtons()
    }

    private fun setupStatusFragment() {
        statusFragment = StatusFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, statusFragment)
            .commit()
    }

    private fun setupNamePreferences() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedName = prefs.getString(KEY_NAME, "")
        binding.tvName.text = "Nome salvo: $savedName"
        
        binding.btnSaveName.setOnClickListener {
            val name = binding.etName.text.toString()
            prefs.edit().putString(KEY_NAME, name).apply()
            binding.tvName.text = "Nome salvo: $name"
            Toast.makeText(this, "Nome salvo com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupLightSwitch() {
        binding.switchLights.setOnCheckedChangeListener { _, isChecked ->
            statusFragment.updateStatus(isChecked)
            logLightStatus(isChecked)
        }
    }

    private fun logLightStatus(isChecked: Boolean) {
        val logFile = File(filesDir, "activity_logs.txt")
        val status = if (isChecked) "ligadas" else "desligadas"
        logFile.appendText("Luzes $status em ${System.currentTimeMillis()}\n")
    }

    private fun setupNavigationButtons() {
        binding.btnContacts.setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }
        
        binding.btnLogs.setOnClickListener {
            startActivity(Intent(this, LogsActivity::class.java))
        }
        
        binding.btnApi.setOnClickListener {
            startActivity(Intent(this, ApiActivity::class.java))
        }
    }
}