package com.smartstudy.uskid.ui.base.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.smartstudy.uskid.R;
import com.smartstudy.uskid.adapter.HeaderAndFooterRecyclerViewAdapter;

import java.util.List;

/**
 * 分页加载的Fragment
 *
 * @author 王宏杰
 * @date 2018/3/23
 */

public abstract class BasePageListLoadDataFragment<T> extends BaseListLoadDataFragment<T> {


    private boolean isLoadMore = false;

    protected int mPageNumber = 0;

    protected boolean isLoadAllFinish = false;//是否全部加载完成

    public static final int PAGE_SIZE = 10;//

    private View mFooterView;

    private int totalDy = 0;

    private RecyclerView.OnScrollListener mOnScrollListener;


    @Override
    public boolean isShowRefreshView() {
        return mDataList.size() == 0;
    }

    @Override
    public boolean isShowEmptyView() {
        return mDataList.size() == 0;
    }

    @Override
    public boolean isShowErrorView() {
        return mDataList.size() == 0;//
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        mPageNumber = 0;
    }

    protected HeaderAndFooterRecyclerViewAdapter<T> adapter;

    @Override
    public void setupRecyclerView() {
        super.setupRecyclerView();
        adapter = (HeaderAndFooterRecyclerViewAdapter<T>) mAdapter;
        mFooterView = View.inflate(getActivity(), R.layout.item_loading_footer, null);
        createScrollListener();
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void createScrollListener() {
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            int firstVisibleItem, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
                firstVisibleItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && totalItemCount - visibleItemCount == firstVisibleItem) {
                    if (!isLoadAllFinish && !isLoadMore) {
                        isLoadMore = true;
                        if (!adapter.hasFooter()) {
                            adapter.addFooter(mFooterView);
                        }
                        new Handler().postDelayed(() -> getData(), 1000);
                    }
                }

            }
        };
    }

    @Override
    public void handleResult(List<T> list) {
        isLoadAllFinish = list.size() < PAGE_SIZE;
        super.handleResult(list);
        mPageNumber++;//页数增加

    }

    @Override
    public void handleError(Throwable throwable) {
        super.handleError(throwable);
        isLoadMore = false;
        if (adapter.hasFooter()) {
            adapter.removeFooter(mFooterView);
        }

    }

    @Override
    public void handleComplete() {
        super.handleComplete();
        isLoadMore = false;
        if (adapter.hasFooter()) {
            adapter.removeFooter(mFooterView);
        }
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        super.onDestroyView();
    }
}
