package com.invia.githubviewer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Sandeepn on 10-12-2017.
 */

public class RepoQueryResult {
    public final String query;

    public final int totalCount;
    @Nullable
    public final Integer next;

    public RepoQueryResult(String query, int totalCount, @Nullable Integer next) {
        this.query = query;
        this.totalCount = totalCount;
        this.next = next;
    }
}
