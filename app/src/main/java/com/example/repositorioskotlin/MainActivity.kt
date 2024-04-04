package com.example.repositorioskotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repositorioskotlin.Adapter.ReposAdapter
import com.example.repositorioskotlin.Model.Repo
import com.example.repositorioskotlin.Network.ApiAdapter.instance
import com.example.repositorioskotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ReposAdapter
    private val listRepos = mutableListOf<Repo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.svRepos.setOnQueryTextListener(this)
        binding.fab.setOnClickListener(this)

        initRecyclerView()

        // getRepos()
    }

    private fun initRecyclerView() {

        adapter = ReposAdapter(listRepos)
        // binding.rvDogs.setHasFixedSize(true)
        binding.rvRepos.layoutManager = LinearLayoutManager(this)
        binding.rvRepos.adapter = adapter
        adapter.onItemClick = {
            showMessage("Single Click: \n" + it.htmlUrl)
            val uri = Uri.parse(it.htmlUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(packageManager) != null)
                startActivity(intent)
            else
                showMessage("No hay un navegador")
        }

        adapter.onLongItemClick = {
            showMessage("Long Click: ${it.name}")
        }
    }

    private fun searchByName(query: String) {
        lateinit var call: Call<ArrayList<Repo>>

        CoroutineScope(Dispatchers.IO).launch {
            call = instance!!.listRepos(query)
            call.enqueue(object : Callback<ArrayList<Repo>> {
                override fun onResponse(
                    call: Call<ArrayList<Repo>>,
                    response: Response<ArrayList<Repo>>
                ) {
                    runOnUiThread {
                        hideKeyboard()
                        if (response.isSuccessful) {
                            // Handle the retrieved post data
                            val repos: ArrayList<Repo> =
                                (response.body() ?: emptyArray<Repo>()) as ArrayList<Repo>
                            listRepos.clear()
                            listRepos.addAll(repos)
                            adapter.notifyDataSetChanged()
                        } else {
                            // Handle error
                            showError(response.code().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<Repo>>, t: Throwable) {
                    // Handle failure
                    runOnUiThread {
                        showMessage(t.toString())
                    }
                }
            })
        }
    }

    private fun searchByNameResponse(query: String) {
        // lateinit var call: Call<ArrayList<Repo>>

        CoroutineScope(Dispatchers.IO).launch {

            val response = instance!!.listReposResponse(query)
            runOnUiThread {
                hideKeyboard()
                if (response.isSuccessful) {
                    // Handle the retrieved post data
                    val repos: ArrayList<Repo> =
                        (response.body() ?: emptyArray<Repo>()) as ArrayList<Repo>
                    listRepos.clear()
                    listRepos.addAll(repos)
                    adapter.notifyDataSetChanged()
                } else {
                    // Handle error
                    showError(response.code().toString())
                }
            }
        }

        /*
        override fun onFailure(call: Call<ArrayList<Repo>>, t: Throwable) {
            // Handle failure
            runOnUiThread {
                showMessage(t.toString())
            }
        }
         */
    }


    fun getRepos() {

        var call: Call<ArrayList<Repo>> = instance!!.getRepos()

        call.enqueue(object : Callback<ArrayList<Repo>> {
            override fun onResponse(
                call: Call<ArrayList<Repo>>,
                response: Response<ArrayList<Repo>>
            ) {
                // hideKeyboard()
                if (response.isSuccessful) {
                    // Handle the retrieved post data
                    val repos: ArrayList<Repo> =
                        (response.body() ?: emptyArray<Repo>()) as ArrayList<Repo>
                    listRepos.clear()
                    listRepos.addAll(repos)
                    adapter.notifyDataSetChanged()
                } else {
                    // Handle error
                    showError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<Repo>>, t: Throwable) {
                // Handle failure
                showMessage(t.toString())
                Log.e("Error", t.toString())
            }
        })
    }

    fun showMessage(message: String) {
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }

    fun showError(message: String) {
        Toast.makeText(this, "Ha ocurrido un error:\n $message", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String): Boolean {

        if (!query.isNullOrEmpty())
            // searchByName(query.lowercase())
            searchByNameResponse(query.lowercase())

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    override fun onClick(v: View?) {
        if (v === binding.fab) {
            val user = binding.svRepos.query.toString()
            if (user.isNullOrEmpty())
                showMessage("Introduzca un usuario")
            else
                searchByName(user)
            //searchByNameResponse(query.lowercase())
        }
    }

}