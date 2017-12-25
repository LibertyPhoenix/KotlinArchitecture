package com.smartstudy.uskid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerViewAdapter基类
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    protected static final int TYPE_ITEM = 100;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterItem<T> adapterItem = createAdapterItem();
        View view = LayoutInflater.from(mContext).inflate(adapterItem.getLayoutResId(viewType), parent, false);
        return new ViewHolder(view, adapterItem);
    }

    public abstract AdapterItem<T> createAdapterItem();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        T t = getItem(position);
        ((ViewHolder) holder).item.bindData(mContext, position, t, getItemViewType(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(v, position);
            });
        }
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int createViewType(int position, T t) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemViewType(int position) {
        T t = getItem(position);
        return createViewType(position, t);
    }

    static class ViewHolder<T> extends RecyclerView.ViewHolder {
        protected AdapterItem<T> item;

        protected ViewHolder(View itemView, AdapterItem<T> item) {
            super(itemView);
            this.item = item;
            this.item.bindViews(itemView);
        }
    }
}