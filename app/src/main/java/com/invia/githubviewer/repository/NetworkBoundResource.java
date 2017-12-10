package com.invia.githubviewer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.invia.githubviewer.BackgroundJobExecutor;
import com.invia.githubviewer.http.GithubApiResponse;
import com.invia.githubviewer.model.Resource;
import com.invia.githubviewer.utils.Objects;

/**
 * Created by Sandeepn on 10-12-2017.
 */

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final BackgroundJobExecutor bgExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    NetworkBoundResource(BackgroundJobExecutor bgExecutors) {
        this.bgExecutors = bgExecutors;
        result.setValue(Resource.loading(null));
        final LiveData<ResultType> cacheSource = loadFromCache();
        result.addSource(cacheSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType data) {
                result.removeSource(cacheSource);
                if (shouldFetch(data)) {
                    fetchFromNetwork(cacheSource);
                } else {
                    result.addSource(cacheSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType newData) {
                            setValue(Resource.success(newData));
                        }
                    });
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<ResultType> cacheSource) {
        LiveData<GithubApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(cacheSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType newData) {
                setValue(Resource.loading(newData));
            }
        });
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(cacheSource);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                bgExecutors.diskIO().execute(() -> {
                    saveCallResult(processResponse(response));
                    bgExecutors.mainThread().execute(
                        new Runnable() {
                             @Override
                             public void run() {
                                 result.addSource(loadFromCache(), new Observer<ResultType>() {
                                     @Override
                                     public void onChanged(@Nullable ResultType newData) {
                                         setValue(Resource.success(newData));
                                     }
                                 });
                             }
                         }
                    );
                });
            } else {
                onFetchFailed();
                result.addSource(cacheSource, new Observer<ResultType>() {
                    @Override
                    public void onChanged(@Nullable ResultType newData) {
                        setValue(Resource.error(response.errorMessage, newData));
                    }
                });
            }
        });
    }

    protected void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(GithubApiResponse<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromCache();

    @NonNull
    @MainThread
    protected abstract LiveData<GithubApiResponse<RequestType>> createCall();
}
