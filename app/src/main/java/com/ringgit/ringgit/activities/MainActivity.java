package com.ringgit.ringgit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ringgit.ringgit.R;
import com.ringgit.ringgit.fragments.LinkListFragment;
import com.ringgit.ringgit.fragments.WebFragment;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Joe DiMaria on 3/10/2017.
 * MainActivity. Handles link loading logic
 */

public class MainActivity extends AppCompatActivity {
    private static final int FADE_IN = android.R.anim.fade_in;
    private static final int FADE_OUT = android.R.anim.fade_out;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListFragment();
    }

    private void initListFragment() {
        // Check if any fragments already exist before initializing
        if (hasFragments()) return;

        // Add our LinkListFragment
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(FADE_IN, FADE_OUT, FADE_IN, FADE_OUT)
                .add(R.id.container, new LinkListFragment(), LinkListFragment.class.getSimpleName())
                .commit();
    }

    private boolean hasFragments() {
        List<Fragment> currentFragments = getSupportFragmentManager().getFragments();
        return currentFragments != null && !currentFragments.isEmpty();
    }

    private final Action1<String> urlAction = new Action1<String>() {
        @Override
        public void call(String url) {
            // Pass our url to our newly created WebFragment and add it to our container
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(FADE_IN, FADE_OUT, FADE_IN, FADE_OUT)
                    .add(R.id.container, WebFragment.newInstance(url), WebFragment.class.getSimpleName())
                    .addToBackStack(WebFragment.class.getSimpleName())
                    .commit();
        }
    };

    public Action1<String> getUrlAction() {
        return urlAction;
    }
}
