package com.smartstudy.uskid.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 带Header和Footer的Adapter
 * http://blog.sqisland.com/2014/12/recyclerview-grid-with-header.html
 */
public abstract class HeaderAndFooterRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<T> {

    private List<View> mHeaders = new ArrayList<>();
    private List<View> mFooters = new ArrayList<>();
    static final int TYPE_HEADER = 101;
    static final int TYPE_FOOTER = 102;

    public HeaderAndFooterRecyclerViewAdapter(Context context, List<T> list,
                                              RecyclerView.LayoutManager manager) {
        super(context, list);
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER || viewType == TYPE_HEADER) {
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new HeaderFooterViewHolder(frameLayout);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        View view;
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                view = mHeaders.get(position);
                prepareHeaderFooter((HeaderFooterViewHolder) holder, view);
                break;
            case TYPE_FOOTER:
                view = mFooters.get(position - mList.size() - mHeaders.size());
                prepareHeaderFooter((HeaderFooterViewHolder) holder, view);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public T getItem(int position) {

        return mList.get(position - mHeaders.size() >= 0 ? position - mHeaders.size() : 0);
    }

    private void prepareHeaderFooter(HeaderFooterViewHolder vh, View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        vh.base.removeAllViews();
        vh.base.addView(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        }
        if (isFooter(position)) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mHeaders.size() + mList.size() + mFooters.size();
    }

    public boolean isHeader(int position) {
        return (position < mHeaders.size());
    }

    private boolean isFooter(int position) {
        return (position >= mHeaders.size() + mList.size());
    }

    public boolean hasFooter() {
        return mFooters.size() > 0;
    }

    public void addHeader(View header) {
        if (!mHeaders.contains(header)) {
            mHeaders.add(header);
            notifyItemInserted(mHeaders.size() - 1);
        }
    }

    public void removeAllHeader() {
        notifyItemRangeChanged(0, mHeaders.size() - 1);

        mHeaders.clear();
    }

    public void removeHeader(View header) {
        if (mHeaders.contains(header)) {
            notifyItemRemoved(mHeaders.indexOf(header));
            mHeaders.remove(header);
        }
    }

    public void addFooter(View footer) {
        if (!mFooters.contains(footer)) {
            mFooters.add(footer);
            notifyItemInserted(mHeaders.size() + mList.size() + mFooters.size() - 1);
        }
    }

    public void removeFooter(View footer) {
        if (mFooters.contains(footer)) {
            notifyItemRemoved(mHeaders.size() + mList.size() + mFooters.indexOf(footer));
            mFooters.remove(footer);
        }
    }

    public static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        FrameLayout base;

        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
            base = (FrameLayout) itemView;
        }
    }

    public int getSpanSizeCount(int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            return 2;
        } else {
            return 1;
        }
    }
}
