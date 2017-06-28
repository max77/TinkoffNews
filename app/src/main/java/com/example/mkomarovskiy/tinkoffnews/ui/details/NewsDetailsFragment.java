package com.example.mkomarovskiy.tinkoffnews.ui.details;

import android.content.ClipDescription;
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

    private CompositeDisposable mNewsDetailsDisposable = new CompositeDisposable();
    private long mId = -1;

    @Override
    public void onDestroy() {
        mNewsDetailsDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mId != -1)
            loadDewsDetails(mId);
    }

    public void loadDewsDetails(long id) {
        if (!isAdded())
            mId = id;
        else {
            mNewsDetailsDisposable.clear();
            mNewsDetailsDisposable.add(
                    ((TinkoffNewsApplication) getActivity().getApplication())
                            .getNewsRepository()
                            .getNewsDetailsById(id, false)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::handleResult));
            mId = -1;
        }
    }

    private void handleResult(RepositoryRequestResult<INewsDetails> result) {
        if (result.isInProgress()) {
            if (!result.isCached())
                showDataInWebView(getString(R.string.loading));
        } else if (result.isSuccess())
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
}
