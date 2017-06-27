package com.example.mkomarovskiy.tinkoffnews.ui.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.mkomarovskiy.tinkoffnews.R;
import com.example.mkomarovskiy.tinkoffnews.TinkoffNewsApplication;
import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;
import com.example.mkomarovskiy.tinkoffnews.model.RepositoryRequestResult;
import com.example.mkomarovskiy.tinkoffnews.repository.INewsRepository;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class NewsActivity extends AppCompatActivity implements NewsTitleListAdapter.InteractionListener {

    private boolean isInTwoPaneMode;

    private SwipeRefreshLayout mPullToRefreshLayout;
    private FrameLayout mDetailsContainer;
    private RecyclerView mNewsTitleList;
    private INewsRepository mNewsRepository;

    private CompositeDisposable mNewsRequestDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mNewsRepository = ((TinkoffNewsApplication) getApplication()).getNewsRepository();

        mDetailsContainer = (FrameLayout) findViewById(R.id.news_details_container);
        isInTwoPaneMode = mDetailsContainer != null;

        mPullToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        mPullToRefreshLayout.setOnRefreshListener(() -> loadNews(true));

        mNewsTitleList = (RecyclerView) findViewById(R.id.news_list);
        mNewsTitleList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mNewsTitleList.setAdapter(new NewsTitleListAdapter(this));

        loadNews(false);
    }

    private void loadNews(boolean forceRefresh) {
        mNewsRequestDisposable.add(
                mNewsRepository
                        .getNewsTitleList(forceRefresh)
                        .map(result -> {
                            if (result.isSuccess())
                                Collections.sort(result.getPayload(), (o1, o2) ->
                                        o1.getPublicationDate().after(o2.getPublicationDate()) ? -1 : 1);
                            return result;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleRequestResult)
        );
    }

    private void handleRequestResult(RepositoryRequestResult<List<INewsTitle>> result) {
        if (result.isInProgress()) {
            // do not show progress when loading cached data
            if (!result.isCached())
                showProgress(true);
        } else if (result.isSuccess()) {
            showProgress(false);
            ((NewsTitleListAdapter) mNewsTitleList.getAdapter()).setData(result.getPayload());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewsRequestDisposable.clear();
    }

    @Override
    public void onItemSelected(NewsTitleListAdapter adapter, int position, INewsTitle item) {
        adapter.setSelectedItemId(item.getId());
    }
}
