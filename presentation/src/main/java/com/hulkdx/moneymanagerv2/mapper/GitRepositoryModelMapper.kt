package com.hulkdx.moneymanagerv2.mapper

import com.hulkdx.moneymanagerv2.models.GithubRepositoryModel
import hulkdx.com.domain.models.GitHubRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
@Suppress("MemberVisibilityCanBePrivate")
@Singleton
class GitRepositoryModelMapper @Inject constructor() {

    fun convert(repository: GitHubRepository): GithubRepositoryModel {
        return GithubRepositoryModel(repository.name, repository.url)
    }

    fun convert(gitHubRepositories: List<GitHubRepository>): List<GithubRepositoryModel> {
        val list = mutableListOf<GithubRepositoryModel>()
        for (repository in gitHubRepositories) {
            list.add(convert(repository))
        }
        return list
    }
}
