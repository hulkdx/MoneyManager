package com.hulkdx.moneymanagerv2.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hulkdx.moneymanagerv2.R
import com.hulkdx.moneymanagerv2.models.GithubRepositoryModel
import com.hulkdx.moneymanagerv2.ui.base.BaseFragment
import com.hulkdx.moneymanagerv2.utils.getViewModel
import timber.log.Timber


/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 *
 */
class MainFragment : BaseFragment() {

    private lateinit var mAdapter: ExampleAdapter
    private lateinit var mRecyclerView: RecyclerView

    private val mainActivity: MainActivity
        get() = this.activity as MainActivity? ?: throw RuntimeException("cannot cast activity")

    //---------------------------------------------------------------
    // Lifecycle
    //---------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mAdapter = ExampleAdapter()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mMainViewModel = getViewModel<MainViewModel>(mainActivity.mViewModelProviderFactory)
        mMainViewModel.getGithubRepositoryModels().observe(this, Observer {
            when (it) {
                is MainContract.LoadDataSuccess -> githubRepositoriesLoaded(it.value)
                is MainContract.LoadDataError -> githubRepositoriesError(it.value)
            }
        })
        mMainViewModel.loadData()
    }

    //---------------------------------------------------------------
    // ViewModel observers:
    //---------------------------------------------------------------

    private fun githubRepositoriesLoaded(githubRepositoryModels: List<GithubRepositoryModel>) {
        Timber.i("data loaded")
        mAdapter.setData(githubRepositoryModels)
        mAdapter.notifyDataSetChanged()
    }

    private fun githubRepositoriesError(throwable: Throwable) {
        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
    }
}
