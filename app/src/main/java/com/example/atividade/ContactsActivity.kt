package com.example.atividade

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.atividade.databinding.ActivityContactsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val contactDao = db.contactDao()

        binding.btnSaveContact.setOnClickListener {
            val name = binding.etContactName.text.toString()
            if (name.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    contactDao.insert(Contact(name = name))
                }
                binding.etContactName.text.clear()
            }
        }

        contactDao.getAllContacts().observe(this) { contacts ->
            binding.tvContacts.text = "Contatos:\n${contacts.joinToString("\n") { it.name }}"
        }

        // Navegar para LogsActivity após 10 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LogsActivity::class.java))
        }, 10000)
    }
}