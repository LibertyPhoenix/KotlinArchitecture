package com.smartstudy.uskid.ui.base.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.smartstudy.uskid.R;
import com.smartstudy.uskid.library.api.ApiError;
import com.smartstudy.uskid.library.api.RetrofitException;
import com.smartstudy.uskid.library.view.StateView;
import com.smartstudy.uskid.model.BaseResponseModel;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


/**
 * 加载数据的Fragment
 *
 * @author 王宏杰
 * @date 2018/3/23
 */
public abstract class BaseLoadDataFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.state_view) public StateView mStateView;
    @BindView(R.id.swipeRefreshLayout) public SwipeRefreshLayout mSwipeRefreshLayout;


    private Handler mHandler;


    private Runnable mDeplayedLoadDataTask = () -> getData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }



    protected boolean isViewInitiated;
    protected boolean isDataLoaded;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSwipeRefreshLayout();
        mStateView.showLoading();
        isViewInitiated = true;
        prepareRequestData(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareRequestData(false);
    }


    public boolean prepareRequestData(boolean forceUpdate) {
        if (getUserVisibleHint() && isViewInitiated && (!isDataLoaded || forceUpdate)) {
            getData();
            return true;
        }
        return false;
    }

    public void getData() {
        Disposable disposable = getApi()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<BaseResponseModel<T>, ObservableSource<T>>)
                        tBaseResponseModel -> Observable.create(e -> {
                            e.onNext(tBaseResponseModel.getItems());
                            e.onComplete();
                            isDataLoaded = true;
                        }))
                .subscribe(t -> handleResult(t),
                        throwable -> handleError(throwable),
                        () -> handleComplete());

        addDisposable(disposable);

    }


    public void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }



    //
    public void handleError(Throwable throwable) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (throwable instanceof RetrofitException) {
            if (((RetrofitException) throwable).getKind() == RetrofitException.Kind.NETWORK && isShowErrorView()) {
                mStateView.showError("网络已走丢，努力寻找中...", view -> {
                    mStateView.showLoading();
                    mHandler.postDelayed(mDeplayedLoadDataTask, 500);

                });
            }
        } else if (throwable instanceof ApiError) {
            int errorCode = ((ApiError) throwable).getCode();
            if (errorCode == ApiError.NO_DATA && isShowEmptyView()) {
                mStateView.showEmpty("没有数据");
            }
        } else {
            mStateView.showError(throwable.getMessage(), view -> {
                mStateView.showLoading();
                mHandler.postDelayed(mDeplayedLoadDataTask, 500);
            });
        }


    }

    public boolean isShowRefreshView() {
        return true;
    }

    /**
     * 是否显示ErrorView 当分页的时候只有当pageCount=1的时候才显示
     *
     * @return
     */
    public boolean isShowErrorView() {
        return true;
    }

    /**
     * 是否显示EmptyView 当分页的时候只有当pageCount=1的时候才显示
     *
     * @return
     */
    public boolean isShowEmptyView() {
        return true;
    }

    public abstract boolean isEmpty(T t);//数据是否为空

    public void handleResult(T t) {
        //如果数据是空的并且isShowEmptyView为true,则显示
        if (isEmpty(t) && isShowEmptyView()) {
            if (mStateView != null) {
                mStateView.showEmpty("没有数据");
            }
            return;
        }
        if (mStateView != null) {
            mStateView.showContent();
        }
    }

    public void handleComplete() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public abstract Observable<BaseResponseModel<T>> getApi();

    protected boolean isClear = false;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        isClear = true;
        mSwipeRefreshLayout.setRefreshing(true);
        mHandler.postDelayed(mDeplayedLoadDataTask, 500);
    }
}
