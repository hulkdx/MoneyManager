package hulkdx.com.domain.interactor

import hulkdx.com.domain.executor.PostExecutionThread
import hulkdx.com.domain.executor.ThreadExecutor
import javax.inject.Inject
import javax.inject.Singleton

import hulkdx.com.domain.models.GitHubRepository
import hulkdx.com.domain.repository.GithubRepoRepository
import io.reactivex.Flowable

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
@Singleton
class GetGithubRepositoryList @Inject constructor(
        mThreadExecutor: ThreadExecutor,
        mPostExecutionThread: PostExecutionThread,
        private val mRepository: GithubRepoRepository)
    : UseCase.FlowableUseCase<List<GitHubRepository>>(mThreadExecutor, mPostExecutionThread)
{
    override fun createFlowable(): Flowable<List<GitHubRepository>> {
        return mRepository.githubRepos
    }
}
