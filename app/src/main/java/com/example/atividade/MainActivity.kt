package com.example.atividade

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atividade.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var statusFragment: StatusFragment
    private val PREFS_NAME = "MyPrefs"
    private val KEY_NAME = "name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cacheFile = File(cacheDir, "login_cache")
        if (cacheFile.exists()) {
            val credentials = cacheFile.readText().split(":")
            if (credentials[0] == "admin" && credentials[1] == "1234") {
                showDashboard()
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etusername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username == "admin" && password == "1234") {

                cacheFile.writeText("$username:$password")
                showDashboard()
            } else {
                Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDashboard() {
        binding.etusername.visibility = android.view.View.GONE
        binding.etPassword.visibility = android.view.View.GONE
        binding.btnLogin.visibility = android.view.View.GONE
        binding.switchLights.visibility = android.view.View.VISIBLE
        binding.etName.visibility = android.view.View.VISIBLE
        binding.btnSaveName.visibility = android.view.View.VISIBLE
        binding.tvName.visibility = android.view.View.VISIBLE
        binding.btnContacts.visibility = android.view.View.VISIBLE
        binding.btnLogs.visibility = android.view.View.VISIBLE
        binding.btnApi.visibility = android.view.View.VISIBLE
        binding.fragmentContainer.visibility = android.view.View.VISIBLE

        statusFragment = StatusFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, statusFragment)
            .commit()

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedName = prefs.getString(KEY_NAME, "")
        binding.tvName.text = "Nome salvo: $savedName"
        binding.btnSaveName.setOnClickListener {
            val name = binding.etName.text.toString()
            prefs.edit().putString(KEY_NAME, name).apply()
            binding.tvName.text = "Nome salvo: $name"
        }

        binding.switchLights.setOnCheckedChangeListener { _, isChecked ->
            statusFragment.updateStatus(isChecked)

            val logFile = File(filesDir, "activity_logs.txt")
            logFile.appendText("Luzes ${if (isChecked) "ligadas" else "desligadas"} em ${System.currentTimeMillis()}\n")
        }

        binding.btnContacts.setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }
        binding.btnLogs.setOnClickListener {
            startActivity(Intent(this, LogsActivity::class.java))
        }
        binding.btnApi.setOnClickListener {
            startActivity(Intent(this, ApiActivity::class.java))
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, ContactsActivity::class.java))
        }, 5000)
    }
}