package com.invia.githubviewer.http;

import android.arch.lifecycle.LiveData;

import com.invia.githubviewer.model.RepoItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sandeepn on 09-12-2017.
 */

public interface GithubService {

    @GET("search/repositories")
    LiveData<GithubApiResponse<GithubQueryResponse>> queryRepos(@Query("q") String query);

    @GET("search/repositories")
    Call<GithubQueryResponse> searchRepos(@Query("q") String query, @Query("page") int page);
}
