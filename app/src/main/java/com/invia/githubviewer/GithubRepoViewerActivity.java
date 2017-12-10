package com.invia.githubviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.invia.githubviewer.ui.common.NavigationController;


public class GithubRepoViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_repo_viewer);
        if(savedInstanceState == null) {
            new NavigationController(this).navigateToGithubRepoViewerFragment();
        }
    }
}
