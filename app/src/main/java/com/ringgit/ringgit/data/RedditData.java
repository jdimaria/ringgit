package com.ringgit.ringgit.data;

import java.util.List;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Container of {@link RedditDataObject}s received from various API requests
 */

public class RedditData<D extends RedditDataObject> {
    // Shouldn't be directly modifiable by the client (handled by Gson), only exposing getters
    private List<D> children;
    private String before;
    private String after;

    public List<D> getChildren() {
        return children;
    }

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }
}
