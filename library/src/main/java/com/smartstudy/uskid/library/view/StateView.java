package com.smartstudy.uskid.library.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.uskid.library.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.smartstudy.uskid.library.view.StateView.State.CONTENT;
import static com.smartstudy.uskid.library.view.StateView.State.EMPTY;
import static com.smartstudy.uskid.library.view.StateView.State.ERROR;
import static com.smartstudy.uskid.library.view.StateView.State.LOADING;

/**
 * @author malinkang
 */
public class StateView extends RelativeLayout {


    public enum State {
        CONTENT, LOADING, EMPTY, ERROR
    }

    LayoutInflater inflater;
    LayoutParams layoutParams;
    List<View> mContentViews = new ArrayList<>();

    View mLoadingView;
    View mEmptyView;
    View mErrorView;

    TextView mEmptyHintTextView;
    TextView mErrorHintTextView;
    TextView mRetryButton;


    public StateView(Context context) {
        super(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child.getTag() == null || (!child.getTag().equals("empty_view") &&
                !child.getTag().equals("error_view") && !child.getTag().equals("loading_view"))) {
            mContentViews.add(child);
        }
    }

    public void showContent() {
        switchState(CONTENT, null, null, Collections.<Integer>emptyList());
    }

    public void showContent(List<Integer> skipIds) {
        switchState(CONTENT, null, null, skipIds);
    }

    public void showLoading() {
        switchState(LOADING, null, null, Collections.<Integer>emptyList());
    }

    public void showLoading(List<Integer> skipIds) {
        switchState(LOADING, null, null, skipIds);
    }

    public void showEmpty(String hint) {
        switchState(EMPTY, hint, null, Collections.<Integer>emptyList());
    }

    public void showEmpty(String hint, List<Integer> skipIds) {
        switchState(EMPTY, hint, null, skipIds);
    }

    public void showError(String hint, OnClickListener onClickListener) {
        switchState(ERROR, hint, onClickListener, Collections.<Integer>emptyList());
    }


    private void switchState(State state, String hint, OnClickListener onClickListener, List<Integer> skipIds) {

        switch (state) {
            case CONTENT:
                //Hide all state views to display content
                hideLoadingView();
                hideEmptyView();
                hideErrorView();

                setContentVisibility(true, skipIds);
                break;
            case LOADING:
                hideEmptyView();
                hideErrorView();
                setLoadingView();
                setContentVisibility(false, skipIds);
                break;
            case EMPTY:
                hideLoadingView();
                hideErrorView();
                setEmptyView();
                mEmptyHintTextView.setText(hint);
                setContentVisibility(false, skipIds);
                break;
            case ERROR:
                hideLoadingView();
                hideEmptyView();
                setErrorView();
                mErrorHintTextView.setText(hint);
                mRetryButton.setOnClickListener(onClickListener);
                setContentVisibility(false, skipIds);
                break;
        }
    }

    private void setLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = inflater.inflate(R.layout.loading_view, null);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);
            addView(mLoadingView, layoutParams);
        } else {
            mLoadingView.setVisibility(VISIBLE);
        }
    }

    private void setEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = inflater.inflate(R.layout.empty_view, null);
            mEmptyHintTextView = mEmptyView.findViewById(R.id.tv_empty_hint);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);
            addView(mEmptyView, layoutParams);
        } else {
            mEmptyView.setVisibility(VISIBLE);
        }
    }

    private void setErrorView() {
        if (mErrorView == null) {
            mErrorView = inflater.inflate(R.layout.error_view, null);
            mErrorHintTextView = mErrorView.findViewById(R.id.tv_error_hint);
            mRetryButton = (Button) mErrorView.findViewById(R.id.btn_retry);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);
            addView(mErrorView, layoutParams);
        } else {
            mErrorView.setVisibility(VISIBLE);
        }
    }

    private void setContentVisibility(boolean visible, List<Integer> skipIds) {
        for (View v : mContentViews) {
            if (!skipIds.contains(v.getId())) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void hideLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
    }

    /**
     * 隐藏View
     */
    private void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
        }
    }

    private void hideErrorView() {
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }
    }
}