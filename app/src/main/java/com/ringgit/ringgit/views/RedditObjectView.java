package com.ringgit.ringgit.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.ringgit.ringgit.data.RedditDataObject;

import rx.functions.Action1;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Generic container view for Reddit data
 */

public abstract class RedditObjectView<C extends RedditDataObject.Content> extends FrameLayout {
    abstract public void setContent(C content, Action1<String> clickAction);

    public RedditObjectView(@NonNull Context context) {
        super(context);
    }
}
