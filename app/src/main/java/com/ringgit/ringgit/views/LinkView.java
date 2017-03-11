package com.ringgit.ringgit.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ringgit.ringgit.R;
import com.ringgit.ringgit.data.Link;
import com.ringgit.ringgit.util.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Renders individual links that pass back the link when clicked
 */

public class LinkView extends RedditObjectView<Link.LinkContent> {
    @BindView(R.id.thumbnail)
    ImageView thumbnailIV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.author_and_timestamp)
    TextView authorAndTimestampTV;
    @BindView(R.id.num_comments)
    TextView numCommentsTV;

    public LinkView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.view_link, this);
        ButterKnife.bind(this);
    }

    @Override
    public void setContent(final Link.LinkContent content, final Action1<String> clickAction) {
        // Set title
        titleTV.setText(content.getTitle());

        // Set submission data
        authorAndTimestampTV.setText(TextUtil.formatSubmissionData(
                content.getCreatedInMilliseconds(),
                content.getAuthor(),
                content.getSubredditNamePrefixed(),
                getContext(),
                clickAction
        ));
        // Allow the links in our TextView to be clickable
        authorAndTimestampTV.setMovementMethod(LinkMovementMethod.getInstance());

        // Set num comments
        int numComments = content.getNumComments();
        numCommentsTV.setText(getResources().getQuantityString(R.plurals.comments, numComments, numComments));
        // Load our comments section on click
        numCommentsTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAction.call(TextUtil.appendPermalinkToBaseUrl(content.getPermalink()));
            }
        });

        // Set click listener
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send back our Url for the caller to handle
                clickAction.call(content.getUrl());
            }
        });
    }

    // Let the adapter handle loading the image. No need to have that logic here
    public ImageView getThumbnailIV() {
        return thumbnailIV;
    }
}
