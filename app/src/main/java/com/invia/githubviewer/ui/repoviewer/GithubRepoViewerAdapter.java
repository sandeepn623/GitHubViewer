package com.invia.githubviewer.ui.repoviewer;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.invia.githubviewer.R;
import com.invia.githubviewer.databinding.GitRepoViewerItemBinding;
import com.invia.githubviewer.model.RepoItem;
import com.invia.githubviewer.ui.GithubRepoViewerClickCallback;
import com.invia.githubviewer.ui.common.DataBoundListAdapter;
import com.invia.githubviewer.utils.Objects;

/**
 * Created by Sandeepn on 09-12-2017.
 */

public class GithubRepoViewerAdapter extends DataBoundListAdapter<RepoItem, GitRepoViewerItemBinding> {

    private DataBindingComponent mDataBindingComponent;

    @Nullable
    private GithubRepoViewerClickCallback mGithubRepoViewerClickCallback;

    public GithubRepoViewerAdapter(@Nullable DataBindingComponent dataBindingComponent) {
        this.mDataBindingComponent = dataBindingComponent;
    }

    @Override
    protected GitRepoViewerItemBinding createBinding(ViewGroup parent) {
        GitRepoViewerItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.git_repo_viewer_item,
                        parent, false, mDataBindingComponent);
        return binding;
    }

    @Override
    protected void bind(GitRepoViewerItemBinding binding, RepoItem item) {
        binding.setRepo(item);
    }

    @Override
    protected boolean areItemsTheSame(RepoItem oldItem, RepoItem newItem) {
        return Objects.equals(oldItem.getRepoOwner(), newItem.getRepoOwner()) &&
                Objects.equals(oldItem.getName(), newItem.getName());
    }

    @Override
    protected boolean areContentsTheSame(RepoItem oldItem, RepoItem newItem) {
        return Objects.equals(oldItem.getDescription(), newItem.getDescription());
    }
}
