package siri.android.recyclerviewendlessscrolling;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_PROGRESSBAR = 1;

    private EndlessRecyclerViewScrollListener mOnLoadMoreListener;
    private List<PostModel> mPosts = new ArrayList<>();
    private Context mContext;

    public PostsAdapter(Context context) {
        mContext = context;
    }

    public void setOnLoadMoreListener(EndlessRecyclerViewScrollListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    //Adds a null user to the existing users lists.
    // This sets view type to progressbar.Indicates
    // end of list is reached and data is loading.
    public void addNullPost() {
        mPosts.add(null);
        notifyItemInserted(mPosts.size() - 1);
    }

    // appends new list of users to the existing list
    public void addPosts(List<PostModel> users) {
        mPosts.addAll(users);
        notifyDataSetChanged();
    }

    // Remove last user from the list, we invoke this method to
    // remove the previously added null user to the list. This indicates
    // it is ready to show the new loaded items in the list.
    public void removeLastPost() {
        mPosts.remove(mPosts.size() - 1);
        notifyItemRemoved(mPosts.size());
    }

    public void clearPosts() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPosts == null ? 0 : mPosts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mPosts.get(position) == null ? VIEW_TYPE_PROGRESSBAR : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(view);
        } else if (viewType == VIEW_TYPE_PROGRESSBAR) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.progressbar, parent, false);
            return new ProgressBarViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PostViewHolder) {
            final PostModel post = mPosts.get(position);
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            postViewHolder.idTv.setText(Integer.toString(post.getId()));
            postViewHolder.titleTv.setText(post.getTitle());
        } else if (holder instanceof ProgressBarViewHolder) {
            ProgressBarViewHolder loadingViewHolder = (ProgressBarViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView idTv;

        public PostViewHolder(View view) {
            super(view);
            idTv = (TextView) view.findViewById(R.id.post_Id);
            titleTv = (TextView) view.findViewById(R.id.post_title);

        }
    }

    private class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }

}

