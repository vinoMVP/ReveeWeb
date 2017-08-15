package com.app.webagent.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageStatusLayout extends RelativeLayout {

    private static final String TAG_LOADING = "ProgressActivity.TAG_LOADING";
    private static final String TAG_EMPTY = "ProgressActivity.TAG_EMPTY";
    private static final String TAG_ERROR = "ProgressActivity.TAG_ERROR";
    private static final String TAG_NO_RECORD = "ProgressActivity.TAG_NO_RECORD";

    final String CONTENT = "type_content";
    final String LOADING = "type_loading";
    final String EMPTY = "type_empty";
    final String ERROR = "type_error";

    private final Context mContext;

    LayoutInflater inflater;
    View view;
    LayoutParams layoutParams;
    Drawable currentBackground;

    List<View> contentViews = new ArrayList<>();

    // 各种状态的view
    View loadingView;

    View emptyView;

    View errorView;

    private String state = CONTENT;

    public PageStatusLayout(Context context) {
        this(context, null);
    }

    public PageStatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public PageStatusLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        currentBackground = this.getBackground();
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR)
                && !child.getTag().equals(TAG_NO_RECORD))) {

            contentViews.add(child);
        }
    }

    /**
     * Hide all other states and show content
     */
    public void showContent() {
        switchState(CONTENT, Collections.<Integer>emptyList());
    }

    /**
     * Hide all other states and show content
     *
     * @param skipIds Ids of views not to show
     */
    public void showContent(List<Integer> skipIds) {
        switchState(CONTENT, skipIds);
    }

    /**
     * Hide content and show the progress bar
     */
    public void showLoading() {
        switchState(LOADING, Collections.<Integer>emptyList());
    }

    /**
     * Hide content and show the progress bar
     *
     * @param skipIds Ids of views to not hide
     */
    public void showLoading(List<Integer> skipIds) {
        switchState(LOADING, skipIds);
    }

    /**
     * Show empty baseView when there are not data to show
     */
    public void showEmpty() {
        switchState(EMPTY, Collections.<Integer>emptyList());
    }

    /**
     * Show empty baseView when there are not data to show
     *
     * @param skipIds Ids of views to not hide
     */
    public void showEmpty(List<Integer> skipIds) {
        switchState(EMPTY, skipIds);
    }

    /**
     * Show error baseView with a button when something goes wrong and prompting the user to try again
     */
    public void showError() {
        switchState(ERROR, Collections.<Integer>emptyList());
    }

    /**
     * Show error baseView with a button when something goes wrong and prompting the user to try again
     *
     * @param skipIds Ids of views to not hide
     */
    public void showError(List<Integer> skipIds) {
        switchState(ERROR, skipIds);
    }

    /**
     * Get which state is set
     *
     * @return State
     */
    public String getState() {
        return state;
    }

    /**
     * Check if content is shown
     *
     * @return boolean
     */
    public boolean isContent() {
        return state.equals(CONTENT);
    }

    /**
     * Check if loading state is shown
     *
     * @return boolean
     */
    public boolean isLoading() {
        return state.equals(LOADING);
    }

    /**
     * Check if empty state is shown
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return state.equals(EMPTY);
    }

    /**
     * Check if error state is shown
     *
     * @return boolean
     */
    public boolean isError() {
        return state.equals(ERROR);
    }

    private void switchState(String state, List<Integer> skipIds) {
        this.state = state;

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
                showLoadingView();
                setContentVisibility(true, skipIds);
                break;
            case EMPTY:
                hideLoadingView();
                hideErrorView();
                showEmptyView();

                setContentVisibility(false, skipIds);
                break;
            case ERROR:
                hideLoadingView();
                hideEmptyView();
                showErrorView();
                setContentVisibility(false, skipIds);
                break;
        }
    }

    /**
     * 设置loading的页面
     *
     * @param view
     */
    public void setLoadingView(View view) {
        if (view == null) {
            return;
        }
        loadingView = view;
        loadingView.setTag(TAG_LOADING);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(loadingView, layoutParams);
    }

    /**
     * 设置loading
     */
    private void showLoadingView() {
        if (loadingView != null) {
            loadingView.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置空页面的view
     *
     * @param view
     */
    public void setEmptyView(View view) {
        if (view == null) {
            return;
        }
        emptyView = view;
        emptyView.setTag(TAG_EMPTY);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(emptyView, layoutParams);
    }

    /**
     * 设置空的view
     */
    private void showEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(VISIBLE);
        }
    }

    public void setErrorView(View view) {
        if (view == null) {
            return;
        }
        errorView = view;
        errorView.setTag(TAG_ERROR);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(errorView, layoutParams);
    }

    /**
     * 设置错误的页面
     */
    private void showErrorView() {
        if (errorView != null) {
            errorView.setVisibility(VISIBLE);
        }
    }

    private void setContentVisibility(boolean visible, List<Integer> skipIds) {
        for (View v : contentViews) {
            if (!skipIds.contains(v.getId())) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * 隐藏loading的view
     */
    private void hideLoadingView() {
        if (loadingView != null) {
            loadingView.setVisibility(GONE);

            this.setBackgroundDrawable(currentBackground);
        }
    }

    /**
     * 隐藏空的view
     */
    private void hideEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(GONE);

            this.setBackgroundDrawable(currentBackground);
        }
    }

    /**
     * 隐藏错误的view
     */
    private void hideErrorView() {
        if (errorView != null) {
            errorView.setVisibility(GONE);

            this.setBackgroundDrawable(currentBackground);
        }
    }

}