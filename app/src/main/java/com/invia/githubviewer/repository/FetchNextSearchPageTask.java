package com.invia.githubviewer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.invia.githubviewer.cache.RepoCacheDao;
import com.invia.githubviewer.http.GithubService;
import com.invia.githubviewer.http.GithubApiResponse;
import com.invia.githubviewer.http.GithubQueryResponse;
import com.invia.githubviewer.model.RepoItem;
import com.invia.githubviewer.model.RepoQueryResult;
import com.invia.githubviewer.model.Resource;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

/**
 * Created by Sandeepn on 10-12-2017.
 */

public class FetchNextSearchPageTask implements Runnable {
    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final MutableLiveData<List<RepoItem>> repoLiveData = new MutableLiveData<>();

    private final String query;
    private final GithubService githubService;
    private final RepoCacheDao repoDao = RepoCacheDao.getInstance();
    FetchNextSearchPageTask(String query, GithubService githubService) {
        this.query = query;
        this.githubService = githubService;
    }

    @Override
    public void run() {
        RepoQueryResult current = repoDao.getRepoQueryResult(query);
        if(current == null) {
            liveData.postValue(null);
            return;
        }
        final Integer nextPage = current.next;
        if (nextPage == null) {
            liveData.postValue(Resource.success(false));
            return;
        }
        try {
            Response<GithubQueryResponse> response = githubService
                    .searchRepos(query, nextPage).execute();
            GithubApiResponse<GithubQueryResponse> apiResponse = new GithubApiResponse<GithubQueryResponse>(response);
            if (apiResponse.isSuccessful()) {
                RepoQueryResult merged = new RepoQueryResult(query, apiResponse.body.getTotalCount(), apiResponse.getNextPage());
                repoDao.setRepoQueryResult(merged);
                repoDao.insertRepoItems(apiResponse.body.getRepoItems());
                repoLiveData.postValue(repoDao.loadRepoItems().getValue());
                liveData.postValue(Resource.success(apiResponse.getNextPage() != null));
            } else {
                liveData.postValue(Resource.error(apiResponse.errorMessage, true));
            }
        } catch (IOException e) {
            liveData.postValue(Resource.error(e.getMessage(), true));
        }
    }

    LiveData<Resource<Boolean>> getLiveData() {
        return liveData;
    }
}
