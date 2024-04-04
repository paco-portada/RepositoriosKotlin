package com.example.repositorioskotlin.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repositorioskotlin.Model.Repo
import com.example.repositorioskotlin.databinding.RepoItemBinding

class ReposAdapter(private val reposList : List<Repo>) : RecyclerView.Adapter<ReposAdapter.RepoViewHolder>() {

    lateinit var onItemClick: ((Repo) -> Unit)
    lateinit var onLongItemClick: ((Repo) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val itemBinding = RepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = reposList.size

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val item = reposList[position]
        holder.render(item)
    }

    inner class RepoViewHolder(binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val name = binding.tvName
        private val description = binding.tvDescription
        private val date = binding.tvCreatedAt

        init {
            itemView.setOnClickListener {
                onItemClick.invoke(reposList[layoutPosition])
            }

            itemView.setOnLongClickListener {
                onLongItemClick.invoke(reposList[layoutPosition])
                false
            }
        }

        fun render(repo: Repo) {
            name.text = repo.name
            description.text = repo.description
            date.text = repo.createdAt
        }
    }
}