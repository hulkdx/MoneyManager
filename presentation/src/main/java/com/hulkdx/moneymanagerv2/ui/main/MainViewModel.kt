package com.hulkdx.moneymanagerv2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hulkdx.moneymanagerv2.di.ConfigPersistent
import javax.inject.Inject

import com.hulkdx.moneymanagerv2.mapper.GitRepositoryModelMapper
import com.hulkdx.moneymanagerv2.models.GithubRepositoryModel
import hulkdx.com.domain.interactor.GetGithubRepositoryList
import io.reactivex.functions.Consumer

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
@ConfigPersistent
class MainViewModel @Inject constructor(
        private val mGetGithubRepositoryList: GetGithubRepositoryList,
        private val mGitRepositoryModelMapper: GitRepositoryModelMapper

): ViewModel() {

    private var githubRepositoryModels = MutableLiveData<MainContract>()

    override fun onCleared() {
        super.onCleared()
        mGetGithubRepositoryList.dispose()
    }

    fun loadData() {
        mGetGithubRepositoryList.execute(
                Consumer { gitHubRepositories ->
                    val data = mGitRepositoryModelMapper.convert(gitHubRepositories)
                    githubRepositoryModels.value = MainContract.LoadDataSuccess(data)
                },
                Consumer { throwable ->
                    githubRepositoryModels.value = MainContract.LoadDataError(throwable)
                })
    }

    fun getGithubRepositoryModels(): LiveData<MainContract> = githubRepositoryModels
}

sealed class MainContract {
    class LoadDataSuccess constructor(val value: List<GithubRepositoryModel>): MainContract()
    class LoadDataError constructor(val value: Throwable): MainContract()
}
