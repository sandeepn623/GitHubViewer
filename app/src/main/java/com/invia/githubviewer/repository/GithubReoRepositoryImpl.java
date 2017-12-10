package com.invia.githubviewer.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.invia.githubviewer.BackgroundJobExecutor;
import com.invia.githubviewer.cache.RepoCacheDao;
import com.invia.githubviewer.http.GithubService;
import com.invia.githubviewer.http.GithubApiResponse;
import com.invia.githubviewer.http.GithubQueryResponse;
import com.invia.githubviewer.model.RepoItem;
import com.invia.githubviewer.model.RepoQueryResult;
import com.invia.githubviewer.model.Resource;
import com.invia.githubviewer.utils.LiveDataCallAdapterFactory;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sandeepn on 09-12-2017.
 */

public class GithubReoRepositoryImpl implements GithubRepoRepository {
    public static final String BASE_URL = "https://api.github.com/";
    private GithubService mGitHubService;
    private RepoCacheDao repoDao = RepoCacheDao.getInstance();
    private BackgroundJobExecutor bgExecutor = new BackgroundJobExecutor();

    public GithubReoRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                            .baseUrl(BASE_URL)
                            .build();
        mGitHubService = retrofit.create(GithubService.class);
    }

    @Override
    public LiveData<Resource<List<RepoItem>>> queryRepos(String query) {
        return new NetworkBoundResource<List<RepoItem>, GithubQueryResponse>(bgExecutor) {

            @Override
            protected void saveCallResult(@NonNull GithubQueryResponse item) {
                RepoQueryResult repoQueryResult = new RepoQueryResult(query, item.getTotalCount(), item.getNextPage());
                repoDao.insertRepoItems(item.getRepoItems());
                repoDao.setRepoQueryResult(repoQueryResult);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RepoItem> data) {
                return data == null || data.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<RepoItem>> loadFromCache() {
                return repoDao.loadRepoItems();
            }

            @NonNull
            @Override
            protected LiveData<GithubApiResponse<GithubQueryResponse>> createCall() {
                return mGitHubService.queryRepos(query);
            }

            @Override
            protected GithubQueryResponse processResponse(GithubApiResponse<GithubQueryResponse> response) {
                GithubQueryResponse body = response.body;
                if (body != null) {
                    body.setNextPage(response.getNextPage());
                }
                return body;
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> queryNextPage(String query) {
        FetchNextSearchPageTask fetchNextSearchPageTask = new FetchNextSearchPageTask(
                query, mGitHubService);
        bgExecutor.networkIO().execute(fetchNextSearchPageTask);
        return fetchNextSearchPageTask.getLiveData();
    }
}
