package com.ringgit.ringgit.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.ringgit.ringgit.BuildConfig;
import com.ringgit.ringgit.R;

import java.util.Date;

import rx.functions.Action1;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Helper for manipulating text
 */

public class TextUtil {
    public static CharSequence formatSubmissionData(long time, String authorName, String subreddit,
                                                    Context context, Action1<String> urlAction) {
        String timestamp = context.getString(R.string.submitted_by, DateUtil.getRelativeTimeSinceLong(time));
        Spannable coloredName = setLink(authorName, appendPermalinkToBaseUrl("/user/" + authorName), urlAction);
        String submittedTo = context.getString(R.string.submitted_to);
        Spannable coloredSubreddit = setLink(subreddit, appendPermalinkToBaseUrl("/" + subreddit), urlAction);

        return TextUtils.concat(timestamp, coloredName, submittedTo, coloredSubreddit);
    }

    public static String appendPermalinkToBaseUrl(String permalink) {
        return BuildConfig.BASE_URL + permalink;
    }

    private static Spannable setLink(String text, final String link, final Action1<String> urlAction) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                urlAction.call(link);
            }
        }, 0, spannable.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        return spannable;
    }
}
