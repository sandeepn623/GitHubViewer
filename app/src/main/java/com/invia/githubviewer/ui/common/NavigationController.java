package com.invia.githubviewer.ui.common;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.invia.githubviewer.R;
import com.invia.githubviewer.ui.repoviewer.GithubRepoViewerFragment;

/**
 * Created by Sandeepn on 09-12-2017.
 */

public class NavigationController {
    private int containerId;
    private final FragmentManager fragmentManager;

    public NavigationController(AppCompatActivity compatActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = compatActivity.getSupportFragmentManager();
    }

    public void navigateToGithubRepoViewerFragment() {
        GithubRepoViewerFragment fragment = new GithubRepoViewerFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, "GithubRepoViewerFragment")
                .commitAllowingStateLoss();
    }
}
