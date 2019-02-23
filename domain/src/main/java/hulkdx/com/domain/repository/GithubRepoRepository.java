package hulkdx.com.domain.repository;

import java.util.List;

import hulkdx.com.domain.models.GitHubRepository;
import io.reactivex.Flowable;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
public interface GithubRepoRepository {
    Flowable<List<GitHubRepository>> getGithubRepos();
}
