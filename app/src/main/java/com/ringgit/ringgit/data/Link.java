package com.ringgit.ringgit.data;

import com.ringgit.ringgit.util.DateUtil;

import java.util.List;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Links containing data on the inner post or link to an outside source
 */

public class Link extends RedditDataObject<Link.LinkContent> {
    public static final String THUMBNAIL_DEFAULT = "default";
    public static final String THUMBNAIL_SELF = "self";

    public static class LinkContent extends RedditDataObject.Content {
        // TODO Would be nice to add general flair support later. Score, etc.
        private String id;
        private String title;
        private String author; // TODO Might be nice to include a link to the author and show their flair
        private String thumbnail;
        private String permalink;
        private String url;
        private String subredditNamePrefixed;
        private Preview preview;
        private int numComments;
        private long created; // In local epoch-second format

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getPermalink() {
            return permalink;
        }

        public String getUrl() {
            return url;
        }

        public String getSubredditNamePrefixed() {
            return subredditNamePrefixed;
        }

        public String getFirstImageSourceUrl() {
            // Default to our thumbnail if no preview data is available
            if (preview.images == null || preview.images.isEmpty())
                return thumbnail;

            // Get the source of our first image, preferring GIF format if available
            Image image = preview.images.get(0);
            if (image.variants != null && image.variants.gif != null && image.variants.gif.source != null)
                return image.variants.gif.source.url;
            else
                return image.source.url;
        }

        public int getNumComments() {
            return numComments;
        }

        public long getCreatedInMilliseconds() {
            return created * DateUtil.S_TO_MS_MULTIPLE;
        }

        private static class Preview {
            private List<Image> images;
        }

        private static class Image {
            private Source source;
            private Variants variants;
        }

        private static class Variants {
            private Gif gif;
            // TODO Variants can also include MP4, among other things
        }

        private static class Gif {
            private Source source;
        }

        private static class Source {
            private String url;
        }
    }
}
