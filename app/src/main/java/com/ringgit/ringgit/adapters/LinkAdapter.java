package com.ringgit.ringgit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ringgit.ringgit.R;
import com.ringgit.ringgit.data.Link;
import com.ringgit.ringgit.views.LinkView;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Adapter for rendering a list of {@link Link}
 */

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {
    private List<Link> links;
    private Action1<String> itemClickAction;

    public void addLink(Link link) {
        if (links == null) links = new ArrayList<>();
        links.add(link);
        notifyItemInserted(getItemCount() - 1);
    }

    public void clearLinks() {
        if (links != null) {
            links.clear();
            notifyDataSetChanged();
        }
    }

    public void setItemClickAction(Action1<String> itemClickAction) {
        this.itemClickAction = itemClickAction;
    }

    public boolean isEmpty() {
        return links == null || getItemCount() == 0;
    }

    @Override
    public LinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinkView rootView = new LinkView(parent.getContext());
        // RecyclerView has a weird quirk where it will default views to wrap_content despite what's in the XML
        // This forces the view to match the parent's width
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(layoutParams);

        return new LinkViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(LinkViewHolder holder, int position) {
        holder.bind(links.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return links != null ? links.size() : 0;
    }

    class LinkViewHolder extends RecyclerView.ViewHolder {
        LinkViewHolder(LinkView itemView) {
            super(itemView);
        }

        void bind(Link.LinkContent content) {
            LinkView linkView = (LinkView) itemView;

            // Set our content
            linkView.setContent(content, itemClickAction);

            linkView.getThumbnailIV().setImageDrawable(null);
            // See if our thumbnail matches up with any of our defaults. Otherwise, load the unique url
            switch (content.getThumbnail()) {
                case Link.THUMBNAIL_DEFAULT:
                    linkView.getThumbnailIV().setImageResource(R.drawable.ic_link_black_48dp);
                    break;
                case Link.THUMBNAIL_SELF:
                    linkView.getThumbnailIV().setImageResource(R.drawable.ic_comment_black_48dp);
                    break;
                default:
                    loadThumbnailUrl(linkView, content);
                    break;
            }
        }

        private void loadThumbnailUrl(LinkView linkView, final Link.LinkContent content) {
            // Load our thumbnail
            Glide.with(linkView.getContext())
                    .load(content.getThumbnail())
                    .centerCrop()
                    .placeholder(android.R.drawable.gallery_thumb)
                    .crossFade()
                    .into(linkView.getThumbnailIV());

            // Set our thumbnail click action
            linkView.getThumbnailIV().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Pass back our thumbnail url to open
                    itemClickAction.call(content.getFirstImageSourceUrl());
                }
            });
        }
    }
}
