package com.invia.githubviewer.repository;

import android.arch.lifecycle.LiveData;

import com.invia.githubviewer.model.RepoItem;
import com.invia.githubviewer.model.Resource;

import java.util.List;

/**
 * Created by Sandeepn on 09-12-2017.
 */

public interface GithubRepoRepository {
    LiveData<Resource<List<RepoItem>>> queryRepos(String query);

    LiveData<Resource<Boolean>> queryNextPage(String query);
}
