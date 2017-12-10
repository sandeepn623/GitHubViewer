package com.invia.githubviewer.ui.repoviewer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.invia.githubviewer.R;
import com.invia.githubviewer.binding.FragmentDataBindingComponent;
import com.invia.githubviewer.databinding.GitRepoViewerFragmentBinding;
import com.invia.githubviewer.model.RepoItem;
import com.invia.githubviewer.model.Resource;
import com.invia.githubviewer.utils.AutoClearedValue;
import com.invia.githubviewer.viewmodel.RepoItemViewModel;

import java.util.List;

/**
 * Created by Sandeepn on 09-12-2017.
 */

public class GithubRepoViewerFragment extends Fragment {

    private DataBindingComponent mDataBindingComponent = new FragmentDataBindingComponent(this);

    private RepoItemViewModel mRepoItemViewModel;

    private AutoClearedValue<GitRepoViewerFragmentBinding> mBinding;

    private AutoClearedValue<GithubRepoViewerAdapter> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GitRepoViewerFragmentBinding dataBinding = DataBindingUtil.
                inflate(inflater, R.layout.git_repo_viewer_fragment,
                        container, false);
        mBinding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRepoItemViewModel = ViewModelProviders.of(this).get(RepoItemViewModel.class);
        GithubRepoViewerAdapter githubRepoViewerAdapter = new GithubRepoViewerAdapter(mDataBindingComponent);
        mAdapter = new AutoClearedValue<>(this, githubRepoViewerAdapter);
        mBinding.get().repoList.setAdapter(githubRepoViewerAdapter);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mBinding.get().repoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == mAdapter.get().getItemCount() - 1) {
                    mRepoItemViewModel.loadNextPage();
                }
            }
        });
        mRepoItemViewModel.getResults().observe(this, new Observer<Resource<List<RepoItem>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<RepoItem>> listResource) {
                mBinding.get().setQueryResource(listResource);
                mBinding.get().setResultCount((listResource == null || listResource.data == null)
                        ? 0 : listResource.data.size());
                mAdapter.get().replace(listResource == null ? null : listResource.data);
                mBinding.get().executePendingBindings();
            }
        });

        mRepoItemViewModel.getLoadMoreStatus().observe(this, new Observer<RepoItemViewModel.LoadMoreState>() {
            @Override
            public void onChanged(@Nullable RepoItemViewModel.LoadMoreState loadMoreState) {
                if (mRepoItemViewModel.getLoadMoreStatus() == null) {
                    mBinding.get().setLoadingMore(false);
                } else {
                    mBinding.get().setLoadingMore(mRepoItemViewModel.getLoadMoreStatus().getValue().isRunning());
                    String error = mRepoItemViewModel.getLoadMoreStatus().getValue().getErrorMessageIfNotHandled();
                    if (error != null) {
                        handleError(error);
                    }
                }
                mBinding.get().executePendingBindings();
            }
        });
    }

    private void handleError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
