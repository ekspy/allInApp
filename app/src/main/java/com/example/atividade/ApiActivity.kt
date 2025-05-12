package com.example.atividade

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.atividade.databinding.ActivityApiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchPosts()

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 10000)
    }

    private fun fetchPosts() {
        val call = RetrofitInstance.api.getPosts()
        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts = response.body()
                    val title = posts?.firstOrNull()?.Title ?: "Sem título"
                    binding.tvApiData.text = "Título do primeiro post: $title"
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.e("API_RESPONSE", "Erro: ${t.message}")
                binding.tvApiData.text = "Erro ao carregar dados"
            }
        })
    }
}