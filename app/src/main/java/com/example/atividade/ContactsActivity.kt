package com.example.atividade

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.atividade.databinding.ActivityContactsBinding
import kotlinx.coroutines.launch

class ContactsActivity : BaseActivity() {
    private lateinit var binding: ActivityContactsBinding
    private lateinit var contactDao: ContactDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar, title = "Contatos")
        setupDatabase()
        setupContactsList()
        setupAddContact()
    }

    private fun setupDatabase() {
        val db = AppDatabase.getDatabase(this)
        contactDao = db.contactDao()
    }

    private fun setupContactsList() {
        contactDao.getAllContacts().observe(this) { contacts ->
            if (contacts.isEmpty()) {
                binding.tvContacts.text = "Nenhum contato cadastrado"
            } else {
                val contactsList = contacts.mapIndexed { index, contact ->
                    "${index + 1}. ${contact.name}"
                }.joinToString("\n")
                binding.tvContacts.text = contactsList
            }
        }
    }

    private fun setupAddContact() {
        binding.btnSaveContact.setOnClickListener {
            val name = binding.etContactName.text.toString().trim()
            
            when {
                name.isEmpty() -> {
                    Toast.makeText(this, "Digite um nome para o contato", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    lifecycleScope.launch {
                        contactDao.insert(Contact(name = name))
                        binding.etContactName.text.clear()
                        Toast.makeText(this@ContactsActivity, "Contato adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}