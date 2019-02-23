@file:Suppress("MemberVisibilityCanBePrivate")

package hulkdx.com.data.mapper

import hulkdx.com.data.model.GitHubRepositoryEntity
import hulkdx.com.domain.models.GitHubRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
@Singleton
class GithubRepoMapper @Inject constructor() {

    fun convert(listEntity: Collection<GitHubRepositoryEntity>): List<GitHubRepository> {
        val list = mutableListOf<GitHubRepository>()
        for (entity in listEntity) {
            list.add(convert(entity))
        }
        return list
    }

    fun convert(entity: GitHubRepositoryEntity): GitHubRepository {
        return GitHubRepository(entity.name ?: "", entity.url ?: "")
    }

}
