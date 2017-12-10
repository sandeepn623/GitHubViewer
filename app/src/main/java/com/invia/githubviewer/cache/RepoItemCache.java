package com.invia.githubviewer.cache;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.invia.githubviewer.model.RepoItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by Sandeepn on 10-12-2017.
 */

public final class RepoItemCache {

    private static final RepoItemCache INSTANCE = new RepoItemCache();

    private Map<Integer, RepoItem> repoItemCache = new LinkedHashMap<Integer, RepoItem>();

    private MutableLiveData<List<RepoItem>> repositoryItems = new MutableLiveData<>();

    public static RepoItemCache getInstance() {
        return INSTANCE;
    }

    public void setRepoItem(RepoItem item) {
        repoItemCache.put(item.getId(), item);
    }

    public void insertRepoItems(List<RepoItem> repoItems) {
        for(RepoItem repoItem : repoItems){
            setRepoItem(repoItem);
        }
    }

    public LiveData<List<RepoItem>> loadRepoItems() {
        List<RepoItem> repoItems = new ArrayList<>();
        Iterator<Map.Entry<Integer, RepoItem>> iterator = repoItemCache.entrySet().iterator();
        while (iterator.hasNext()) {
            repoItems.add(iterator.next().getValue());
        }
        repositoryItems.postValue(repoItems);
        return repositoryItems;
    }
}
