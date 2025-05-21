package com.example.atividade

import android.os.Bundle
import android.widget.Toast
import com.example.atividade.databinding.ActivityLogsBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LogsActivity: BaseActivity() {
    private lateinit var binding: ActivityLogsBinding
    private lateinit var logFile: File
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar, title = "Logs")
        setupLogFile()
        setupSaveLog()
        setupViewLogs()
        loadLogs() // Load logs automatically when opening the screen
    }

    private fun setupLogFile() {
        logFile = File(filesDir, "activity_logs.txt")
        if (!logFile.exists()) {
            logFile.createNewFile()
        }
    }

    private fun setupSaveLog() {
        binding.btnSaveLog.setOnClickListener {
            val log = binding.etLog.text.toString().trim()
            
            when {
                log.isEmpty() -> {
                    Toast.makeText(this, "Digite um log para salvar", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val timestamp = dateFormat.format(Date())
                    logFile.appendText("[$timestamp] $log\n")
                    binding.etLog.text.clear()
                    Toast.makeText(this, "Log salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    loadLogs() // Reload logs after saving
                }
            }
        }
    }

    private fun setupViewLogs() {
        binding.btnViewLogs.setOnClickListener {
            loadLogs()
        }
    }

    private fun loadLogs() {
        if (logFile.exists() && logFile.length() > 0) {
            val logs = logFile.readText()
            binding.tvLogs.text = logs
        } else {
            binding.tvLogs.text = "Nenhum log registrado."
        }
    }
}