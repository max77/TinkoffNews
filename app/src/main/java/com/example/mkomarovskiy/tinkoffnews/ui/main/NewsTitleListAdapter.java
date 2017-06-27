package com.example.mkomarovskiy.tinkoffnews.ui.main;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mkomarovskiy.tinkoffnews.R;
import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;

import java.util.Calendar;
import java.util.List;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 27/06/2017.
 */

class NewsTitleListAdapter extends RecyclerView.Adapter<NewsTitleListAdapter.ViewHolder> {
    private static final CharSequence NEWS_DATE_FORMAT = "dd/MM/yyyy @ HH:mm:ss z";

    private List<INewsTitle> mNewsTitles;
    private long mSelectedId = -1;
    private InteractionListener mListener;

    public NewsTitleListAdapter(InteractionListener listener) {
        mListener = listener;
        setHasStableIds(true);
    }

    public void setData(List<INewsTitle> newsTitles) {
        mNewsTitles = newsTitles;
        notifyDataSetChanged();
    }

    // -1 to clear current selection
    public void setSelectedItemId(long id) {
        int oldPos = findItemPositionById(mSelectedId);

        mSelectedId = id;
        int newPos = findItemPositionById(mSelectedId);

        if (oldPos != -1)
            notifyItemChanged(oldPos);

        if (newPos != -1)
            notifyItemChanged(newPos);
    }

    @Override
    public long getItemId(int position) {
        return mNewsTitles.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_news_title, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        INewsTitle news = mNewsTitles.get(position);

        holder.mTitle.setText(Html.fromHtml(news.getTitle()));
        holder.mDate.setText(makeNiceDate(news.getPublicationDate()));
        highlightItem(holder.itemView, news.getId() == mSelectedId);

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null)
                mListener.onItemSelected(this, position, news);
        });
    }

    private void highlightItem(View itemView, boolean hilite) {
        itemView.setBackgroundColor(itemView.getResources().getColor(hilite ?
                R.color.news_title_background_selected : R.color.news_title_background_normal));
    }

    private CharSequence makeNiceDate(Calendar publicationDate) {
        return DateFormat.format(NEWS_DATE_FORMAT, publicationDate);
    }

    @Override
    public int getItemCount() {
        return mNewsTitles != null ? mNewsTitles.size() : 0;
    }

    private int findItemPositionById(long id) {
        for (int i = 0; i < mNewsTitles.size(); i++)
            if (id == mNewsTitles.get(i).getId())
                return i;

        return -1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mDate = (TextView) itemView.findViewById(R.id.date);
        }
    }

    public interface InteractionListener {
        void onItemSelected(NewsTitleListAdapter adapter, int position, INewsTitle item);
    }
}
