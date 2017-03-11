package com.ringgit.ringgit.data;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Base model to deal with Reddit data more abstractly.
 * Can add common fields like "kind" and such here and then subclass it
 */

public abstract class RedditDataObject<C extends RedditDataObject.Content> {
    // Tells us whether it's a link, comment, etc.
    private String kind;
    // The actual data
    private C data;

    public String getKind() {
        return kind;
    }

    public C getData() {
        return data;
    }

    public abstract static class Content {}
}
