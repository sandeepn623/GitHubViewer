package com.invia.githubviewer.cache;

import android.arch.lifecycle.LiveData;

import com.invia.githubviewer.model.RepoItem;
import com.invia.githubviewer.model.RepoQueryResult;

import java.util.List;

/**
 * Created by Sandeepn on 10-12-2017.
 */

public class RepoCacheDao {
    private static final RepoCacheDao INSTANCE = new RepoCacheDao();

    private RepoItemCache repoItemCache = RepoItemCache.getInstance();

    private RepoQueryResultCache repoQueryResultCache = RepoQueryResultCache.getInstance();

    public static RepoCacheDao getInstance() {
        return INSTANCE;
    }

    public void insertRepoItems(List<RepoItem> repoItems) {
        repoItemCache.insertRepoItems(repoItems);
    }

    public LiveData<List<RepoItem>> loadRepoItems() {
        return repoItemCache.loadRepoItems();
    }

    public void setRepoQueryResult(RepoQueryResult repoQueryResult) {
        repoQueryResultCache.setRepoQueryResult(repoQueryResult);
    }

    public RepoQueryResult getRepoQueryResult(String query) {
        return repoQueryResultCache.getRepoQueryResult(query);
    }
}
