
package com.smartstudy.uskid.library.adapter;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;


/**
 * @author drakeet
 */
public class SwipeRefreshDelegate {

    SwipeRefreshLayout swipeRefreshLayout;

    private OnSwipeRefreshListener providedListener;


    public interface OnSwipeRefreshListener {

        void onSwipeRefresh();
    }


    public SwipeRefreshDelegate(OnSwipeRefreshListener listener) {
        this.providedListener = listener;
    }


    public void attach(Activity activity) {
        trySetupSwipeRefresh();
    }


    public void attach(View fragment) {
        trySetupSwipeRefresh();
    }


    private void trySetupSwipeRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> providedListener.onSwipeRefresh());
        }
    }


    public void setRefresh(boolean requestDataRefresh) {
        if (swipeRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            swipeRefreshLayout.postDelayed(() -> {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }
    }


    public void setEnabled(boolean enable) {
        if (swipeRefreshLayout == null) {
            throw new IllegalAccessError("The SwipeRefreshLayout has not been initialized.");
        }
        swipeRefreshLayout.setEnabled(enable);
    }


    public boolean isShowingRefresh() {
        return swipeRefreshLayout.isRefreshing();
    }


    public void setProgressViewOffset(boolean scale, int start, int end) {
        swipeRefreshLayout.setProgressViewOffset(scale, start, end);
    }
}
