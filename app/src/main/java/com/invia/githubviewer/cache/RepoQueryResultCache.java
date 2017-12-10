package com.invia.githubviewer.cache;

import android.arch.lifecycle.LiveData;

import com.invia.githubviewer.model.RepoQueryResult;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Sandeepn on 10-12-2017.
 */

public final class RepoQueryResultCache {
    private static final RepoQueryResultCache INSTANCE = new RepoQueryResultCache();

    private HashMap<String, RepoQueryResult> repoQueryResultCache = new HashMap<String, RepoQueryResult>();

    public static RepoQueryResultCache getInstance() {
        return INSTANCE;
    }

    public RepoQueryResult getRepoQueryResult(String query) {
        return repoQueryResultCache.get(query);
    }

    public void setRepoQueryResult(RepoQueryResult repoQueryResult) {
        repoQueryResultCache.put(repoQueryResult.query, repoQueryResult);
    }
}
