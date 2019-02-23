package com.hulkdx.moneymanagerv2.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import javax.inject.Inject
import androidx.recyclerview.widget.RecyclerView
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.models.GithubRepositoryModel

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
class ExampleAdapter @Inject constructor(): RecyclerView.Adapter<ExampleAdapter.ViewHolder>() {

    private var mModels: List<GithubRepositoryModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_example, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mModels[position].apply {
            holder.mNameTitle.text = name
            holder.mURLTitle.text = url
        }
    }

    override fun getItemCount(): Int = mModels.size

    fun setData(githubRepositoryModels: List<GithubRepositoryModel>) {
        mModels = githubRepositoryModels
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mNameTitle: TextView = itemView.findViewById(R.id.git_name)
        var mURLTitle: TextView = itemView.findViewById(R.id.git_url)

    }
}
