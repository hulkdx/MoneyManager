package hulkdx.com.data.repository

import hulkdx.com.data.mapper.GithubRepoMapper
import javax.inject.Inject
import javax.inject.Singleton

import hulkdx.com.data.remote.RemoteService
import hulkdx.com.domain.models.GitHubRepository
import hulkdx.com.domain.repository.GithubRepoRepository
import io.reactivex.Flowable

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */

@Singleton
class GithubRepoRepositoryImpl @Inject constructor(
        private val mRemoteService: RemoteService,
        private val mGithubRepoMapper: GithubRepoMapper

) : GithubRepoRepository
{
    override fun getGithubRepos(): Flowable<List<GitHubRepository>> {
        return mRemoteService.repos().map { mGithubRepoMapper.convert(it) }
    }
}
