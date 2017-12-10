package com.invia.githubviewer.utils;

import android.arch.lifecycle.LiveData;

/**
 * Created by Sandeepn on 10-12-2017.
 */

public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }
    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}
