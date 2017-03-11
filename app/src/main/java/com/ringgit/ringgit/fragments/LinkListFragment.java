package com.ringgit.ringgit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ringgit.ringgit.R;
import com.ringgit.ringgit.activities.MainActivity;
import com.ringgit.ringgit.adapters.LinkAdapter;
import com.ringgit.ringgit.data.Link;
import com.ringgit.ringgit.data.Listing;
import com.ringgit.ringgit.data.RedditData;
import com.ringgit.ringgit.interfaces.RedditService;
import com.ringgit.ringgit.util.NetUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Fragment that displays a list of {@link com.ringgit.ringgit.data.Link}
 */

public class LinkListFragment extends Fragment {
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.previous_button)
    Button previousButton;
    @BindView(R.id.next_button)
    Button nextButton;
    private LinkAdapter adapter;
    private RedditService redditService;
    private RedditData<Link> linkData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain instance of Fragment when activity orientation changes
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_link_list, container, false);
        // Bind our views
        ButterKnife.bind(this, view);
        initAdapter();
        setButtons();

        return view;
    }

    private void initAdapter() {
        // Make sure our adapter is initialized
        if (adapter == null) adapter = new LinkAdapter();
        // TODO Can make "getUrlAction()" be part of an interface if we want to use this in other Activities
        if (getActivity() instanceof MainActivity) {
            adapter.setItemClickAction(((MainActivity) getActivity()).getUrlAction());
        }
        // Always need to re-attach our adapter since RecyclerView gets recreated on rotation
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        if (adapter.isEmpty()) {
            // Fetch our top links and add to our adapter
            getTopTenPosts();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // It really only makes sense to refresh the top ten instead of whatever the current page is
                // since we're trying to get the most recent set of data
                getTopTenPosts();
            }
        });
    }

    private void getTopTenPosts() {
        swipeRefreshLayout.setRefreshing(true);
        getRedditService().getTopTenPosts().enqueue(listingCallback);
    }

    private void setButtons() {
        if (linkData == null) return;

        previousButton.setEnabled(!TextUtils.isEmpty(linkData.getBefore()));
        nextButton.setEnabled(!TextUtils.isEmpty(linkData.getAfter()));
    }

    @OnClick(R.id.previous_button)
    protected void onPreviousClick() {
        if (linkData != null) {
            swipeRefreshLayout.setRefreshing(true);
            getRedditService().getTopTenBefore(linkData.getBefore()).enqueue(listingCallback);
        }
    }

    @OnClick(R.id.next_button)
    protected void onNextClick() {
        if (linkData != null) {
            swipeRefreshLayout.setRefreshing(true);
            getRedditService().getTopTenAfter(linkData.getAfter()).enqueue(listingCallback);
        }
    }

    private RedditService getRedditService() {
        if (redditService == null) redditService = NetUtil.getRedditService(getContext());

        return redditService;
    }

    private final Callback<Listing> listingCallback = new Callback<Listing>() {
        @Override
        public void onResponse(Call<Listing> call, Response<Listing> response) {
            if (response.isSuccessful()) {
                // Update our linkData
                linkData = response.body().getData();
                // Update our buttons
                setButtons();
                // Clear our adapter if applicable
                if (adapter != null) adapter.clearLinks();
                // Transform our Listing response into an Observable list of links
                Observable<Link> linkObservable = Observable.from(linkData.getChildren());
                // Subscribe to our linkAction to queue up and display each link individually
                linkObservable.subscribe(linkAction);
            } else {
                System.out.println(response.errorBody());
                // TODO Add custom error handling
            }
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(Call<Listing> call, Throwable t) {
            t.printStackTrace();
            // TODO Add custom error handling
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        }
    };

    private final Action1<Link> linkAction = new Action1<Link>() {
        @Override
        public void call(Link link) {
            if (adapter != null) adapter.addLink(link);
        }
    };
}
