package com.example.atividade

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.atividade.databinding.ActivityLogsBinding
import java.io.File
import kotlin.math.log

class LogsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLogsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logFile = File(filesDir, "activity_logs.txt")

        binding.btnSaveLog.setOnClickListener {
            val log = binding.etLog.text.toString()
            if (log.isNotEmpty()) {
                logFile.appendText("$log em ${System.currentTimeMillis()}\n")
                binding.etLog.text.clear()
            }
        }

        binding.btnViewLogs.setOnClickListener {
            if (logFile.exists()) {
                binding.tvLogs.text = "Logs:\n${logFile.readText()}"
            } else {
                binding.tvLogs.text = "Nenhum log registrado."
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, ApiActivity::class.java))
        }, 10000)
    }
}