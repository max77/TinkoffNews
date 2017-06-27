package com.example.mkomarovskiy.tinkoffnews.ui.details;

import android.content.ClipDescription;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebViewFragment;

import com.example.mkomarovskiy.tinkoffnews.R;
import com.example.mkomarovskiy.tinkoffnews.TinkoffNewsApplication;
import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;
import com.example.mkomarovskiy.tinkoffnews.model.RepositoryRequestResult;

import java.nio.charset.StandardCharsets;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 27/06/2017.
 */

public class NewsDetailsFragment extends WebViewFragment {

    private static final String KEY_NEWS_ID = "newsid";

    private CompositeDisposable mNewsDetailsDisposable = new CompositeDisposable();

    public static NewsDetailsFragment newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong(KEY_NEWS_ID, id);

        NewsDetailsFragment fragment = new NewsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long id = getArguments().getLong(KEY_NEWS_ID, -1);
        if (id != -1)
            loadDewsDetails(id);
    }

    private void loadDewsDetails(long id) {
        mNewsDetailsDisposable.clear();
        mNewsDetailsDisposable.add(
                ((TinkoffNewsApplication) getActivity().getApplication())
                        .getNewsRepository()
                        .getNewsDetailsById(id, false)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResult));
    }

    private void handleResult(RepositoryRequestResult<INewsDetails> result) {
        if (result.isInProgress())
            showDataInWebView(getString(R.string.loading));

        else if (result.isSuccess())
            showDataInWebView(result.getPayload().getContent());

        else if (result.isError())
            showDataInWebView(result.getErrorMessage());
    }

    private void showDataInWebView(String data) {
        getWebView().loadDataWithBaseURL("",
                data,
                ClipDescription.MIMETYPE_TEXT_HTML,
                StandardCharsets.UTF_8.name(),
                "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNewsDetailsDisposable.clear();
    }
}
