package com.invia.githubviewer.utils;

/**
 * Created by Sandeepn on 09-12-2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * A value holder that automatically clears the reference if the Fragment's view is destroyed.
 * @param <T>
 */
public class AutoClearedValue<T> {
    private T value;
    public AutoClearedValue(final Fragment fragment, T value) {
        final FragmentManager fragmentManager = fragment.getFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        if (f == fragment) {
                            AutoClearedValue.this.value = null;
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                        }
                    }
                },false);
        this.value = value;
    }

    public T get() {
        return value;
    }
}
