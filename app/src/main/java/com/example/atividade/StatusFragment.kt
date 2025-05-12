package com.example.atividade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.atividade.databinding.FragmentStatusBinding

class StatusFragment: Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun updateStatus(isLightsOn: Boolean) {
        binding.tvStatus.text = "Estado das luzes: ${if (isLightsOn) "Ligado" else "Desligado"}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}