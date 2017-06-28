package com.example.mkomarovskiy.tinkoffnews.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.mkomarovskiy.tinkoffnews.R;
import com.example.mkomarovskiy.tinkoffnews.TinkoffNewsApplication;
import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;
import com.example.mkomarovskiy.tinkoffnews.model.RepositoryRequestResult;
import com.example.mkomarovskiy.tinkoffnews.ui.BaseActivity;
import com.example.mkomarovskiy.tinkoffnews.ui.details.NewsDetailsActivity;
import com.example.mkomarovskiy.tinkoffnews.ui.details.NewsDetailsFragment;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class NewsActivity extends BaseActivity implements NewsTitleListAdapter.InteractionListener {

    private static final String KEY_CURRENT_ITEM_ID = "itemid";
    private static final int ITEM_ID_FIRST = -1234;
    private static final String TAG_DETAILS = "details";

    private boolean isInTwoPaneMode;
    private long mCurrentItemId = ITEM_ID_FIRST;

    private SwipeRefreshLayout mPullToRefreshLayout;
    private RecyclerView mNewsTitleList;

    private CompositeDisposable mNewsRequestDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        isInTwoPaneMode = findViewById(R.id.news_details_container) != null;

        mPullToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        mPullToRefreshLayout.setOnRefreshListener(() -> {
            mCurrentItemId = ITEM_ID_FIRST;
            loadNewsAndShowItem(true);
        });

        mNewsTitleList = (RecyclerView) findViewById(R.id.news_list);
        mNewsTitleList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mNewsTitleList.setAdapter(new NewsTitleListAdapter(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentItemId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_CURRENT_ITEM_ID, ITEM_ID_FIRST) :
                ITEM_ID_FIRST;

        loadNewsAndShowItem(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(KEY_CURRENT_ITEM_ID, mCurrentItemId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        // A dirty hack to prevent automatic details fragment recreation

        Fragment details = getFragmentManager().findFragmentByTag(TAG_DETAILS);

        if (details != null)
            getFragmentManager()
                    .beginTransaction()
                    .remove(details)
                    .commit();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewsRequestDisposable.clear();
    }

    @Override
    public void onItemSelected(INewsTitle item) {
        mCurrentItemId = item.getId();
        selectItem(item.getId(), false);

        if (!isInTwoPaneMode) {
            NewsDetailsActivity.show(this, item.getId());
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private NewsTitleListAdapter getAdapter() {
        return (NewsTitleListAdapter) mNewsTitleList.getAdapter();
    }

    private void selectItem(long itemId, boolean scrollToSelected) {
        int position;

        if (itemId == ITEM_ID_FIRST)
            position = 0;
        else
            position = getAdapter().findItemPositionById(itemId);

        if (position == -1)
            position = 0;

        if (position < getAdapter().getItemCount()) {
            long id = getAdapter().getItemId(position);

            if (isInTwoPaneMode) {
                getAdapter().setSelectedItemId(id);
                loadNewsDetails(id);
            }

            if (scrollToSelected)
                mNewsTitleList.scrollToPosition(position);
        }
    }

    private void loadNewsAndShowItem(boolean forceRefresh) {
        mNewsRequestDisposable.clear();
        mNewsRequestDisposable.add(
                ((TinkoffNewsApplication) getApplication())
                        .getNewsRepository()
                        .getNewsTitleList(forceRefresh)
                        .map(result -> {
                            if (result.isSuccess())
                                Collections.sort(result.getPayload(), (o1, o2) ->
                                        o1.getPublicationDate().after(o2.getPublicationDate()) ?
                                                -1 :
                                                o1.getPublicationDate().before(o2.getPublicationDate()) ?
                                                        1
                                                        : o1.getId() > o2.getId() ? 1 : -1);
                            return result;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleRequestResult)
        );
    }

    private void loadNewsDetails(long id) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.news_details_container, NewsDetailsFragment.newInstance(id), TAG_DETAILS)
                .commit();
    }

    private void handleRequestResult(RepositoryRequestResult<List<INewsTitle>> result) {
        if (result.isInProgress()) {
            // do not show progress when loading cached data
            if (!result.isCached())
                showProgress(true);
        } else if (result.isSuccess()) {
            showProgress(false);

            ((NewsTitleListAdapter) mNewsTitleList.getAdapter()).setData(result.getPayload());
            selectItem(mCurrentItemId, true);
            setTitle(getString(R.string.app_name) + (result.isCached() ? getString(R.string.cached) : ""));
        } else if (result.isError()) {
            showProgress(false);
            showError(result.getErrorMessage());
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showProgress(boolean show) {
        mPullToRefreshLayout.setRefreshing(show);
    }
}
