package com.ringgit.ringgit.data;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Generic collection of {@link RedditDataObject} contained in a {@link RedditData} container
 */

public class RedditDataCollection<D extends RedditDataObject> {
    // TODO Value not necessary for the demo, but if we ever want to extend to cover other data types it's useful
    private String kind;
    private RedditData<D> data;

    public RedditData<D> getData() {
        return data;
    }
}
