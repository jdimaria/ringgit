package com.ringgit.ringgit.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ringgit.ringgit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joe DiMaria on 3/10/2017.
 * Fragment for displaying any web page
 */

public class WebFragment extends Fragment {
    private static final String KEY_ALREADY_LOADED = "already_loaded";

    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.webview)
    WebView webView;
    private String url;
    private boolean hasAlreadyLoaded = false;

    public static WebFragment newInstance(String url) {
        WebFragment webFragment = new WebFragment();
        webFragment.url = url;

        return webFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain instance of Fragment when activity orientation changes
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.bind(this, rootView);
        initWebView();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Note: Restoring WebView state to prevent reloading is not a reliable method.
        // You could simply hold on to the same WebView and re-attach it on rotation, but
        // that would be a memory leak since it still holds a reference to the destroyed Activity.
        // The other option is handling config changes manually, but that's a bit over-optimizing
        // for a project of this scope, in my opinion.
        webView.restoreState(savedInstanceState);
        if (savedInstanceState != null)
            hasAlreadyLoaded = savedInstanceState.getBoolean(KEY_ALREADY_LOADED, false);

        if (!hasAlreadyLoaded) {
            webView.loadUrl(url);
            hasAlreadyLoaded = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
        outState.putBoolean(KEY_ALREADY_LOADED, hasAlreadyLoaded);
    }

    private void initWebView() {
        // Set our custom client
        webView.setWebViewClient(new ProgressClient());
        // Allow user to adjust zoom in WebView
        webView.getSettings().setBuiltInZoomControls(true);
        // TODO Should let user enable/disable JavaScript on their own
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private class ProgressClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            progressBar.setVisibility(View.GONE);
        }
    }
}
