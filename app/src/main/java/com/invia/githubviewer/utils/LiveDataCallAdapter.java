package com.invia.githubviewer.utils;


import android.arch.lifecycle.LiveData;

import com.invia.githubviewer.http.GithubApiResponse;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sandeepn on 10-12-2017.
 */

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<GithubApiResponse<R>>> {
    private final Type responseType;
    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<GithubApiResponse<R>> adapt(final Call<R> call) {
        return new LiveData<GithubApiResponse<R>>() {
            AtomicBoolean started = new AtomicBoolean(false);
            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            postValue(new GithubApiResponse<>(response));
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable throwable) {
                            postValue(new GithubApiResponse<R>(throwable));
                        }
                    });
                }
            }
        };
    }
}
