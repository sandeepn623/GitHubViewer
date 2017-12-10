package com.invia.githubviewer.binding;

import android.databinding.DataBindingComponent;
import android.support.v4.app.Fragment;

/**
 * Created by Sandeepn on 09-12-2017.
 */

public class FragmentDataBindingComponent implements DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

    @Override
    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }
}
