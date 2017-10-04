package com.example.w10.reddit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * Created by W10 on 9/26/2017.
 */

public class CommentsAdapter extends ArrayAdapter{

    private static final String LOG_TAG = "CommentsAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {

        TextView mTitle;
        TextView mAuthor;
        TextView mDateUpdated;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public CommentsAdapter(Context context, int resource, ArrayList<Comment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the persons information
        String currentComment = ((Comment) getItem(position)).getComment();
        String currentAuthor = ((Comment) getItem(position)).getAuthor();
        String currentDateUpdated = ((Comment) getItem(position)).getDateUpdated();

        try {
            //create the view
            final View result;

            //ViewHolder object
            final ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder = new ViewHolder();
                holder.mTitle = (TextView) convertView.findViewById(R.id.comment_title);
                holder.mAuthor = (TextView) convertView.findViewById(R.id.comment_author);
                holder.mDateUpdated = (TextView) convertView.findViewById(R.id.comment_date_updated);

                result = convertView;

                convertView.setTag(holder); // TODO: check once bit confused
            } else {
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }

            holder.mTitle.setText(currentComment);
            holder.mAuthor.setText(currentAuthor);
            holder.mDateUpdated.setText(currentDateUpdated);


            return convertView;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage());
            return convertView;
        }

    }
}

