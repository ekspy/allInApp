package com.example.atividade

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.atividade.databinding.ActivityApiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiActivity : BaseActivity() {
    private lateinit var binding: ActivityApiBinding
    private val postAdapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar, title = "API")
        setupRecyclerView()
        setupRefreshButton()
        fetchPosts()
    }

    private fun setupRecyclerView() {
        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(this@ApiActivity)
            adapter = postAdapter
        }
    }

    private fun setupRefreshButton() {
        binding.btnRefresh.setOnClickListener {
            fetchPosts()
        }
    }

    private fun fetchPosts() {
        showLoading(true)
        
        val call = RetrofitInstance.api.getPosts()
        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                showLoading(false)
                
                if (response.isSuccessful) {
                    response.body()?.let { posts ->
                        if (posts.isEmpty()) {
                            showError("Nenhum post encontrado")
                        } else {
                            postAdapter.updatePosts(posts)
                        }
                    } ?: showError("Dados não disponíveis")
                } else {
                    showError("Erro: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                showLoading(false)
                showError("Erro ao carregar dados: ${t.message}")
            }
        })
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRefresh.isEnabled = !show
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}