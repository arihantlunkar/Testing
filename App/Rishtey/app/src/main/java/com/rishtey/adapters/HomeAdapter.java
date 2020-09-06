package com.rishtey.adapters;

import android.net.Uri;
import java.util.ArrayList;

import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import com.rishtey.data.HomeData;
import com.rishtey.R;
import com.squareup.picasso.Picasso;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<HomeData> mDataset;
	private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
		public TextView mEducationTextView;
		public TextView mAgeTextView;
		public TextView mHeightTextView;
		public TextView mSampradayaTextView;
		public TextView mPlaceTextView;
		public ImageView mProfilePicImageView;
        public ItemViewHolder(View view) {
            super(view);
            mNameTextView = view.findViewById(R.id.nameTextView);
            mEducationTextView = view.findViewById(R.id.educationTextView);
            mAgeTextView = view.findViewById(R.id.ageTextView);
            mHeightTextView = view.findViewById(R.id.heightTextView);
            mSampradayaTextView = view.findViewById(R.id.sampradayaTextView);
            mPlaceTextView = view.findViewById(R.id.placeTextView);
			mProfilePicImageView = view.findViewById(R.id.profilePicImageView);
        }
    }
	
	public static class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        public ProgressBarViewHolder(View view) {
            super(view);
            mProgressBar = view.findViewById(R.id.progressBar);
        }
    }

    public HomeAdapter(ArrayList<HomeData> dataset) {
        mDataset = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		RecyclerView.ViewHolder viewHolder = null;
		switch(viewType){
			case VIEW_TYPE_ITEM:
				viewHolder = new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false));
			break;
			case VIEW_TYPE_LOADING:
				viewHolder = new ProgressBarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
			default:
				break;
		}
		return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ItemViewHolder && null != mDataset && 0 != mDataset.size()) {
			((ItemViewHolder)holder).mNameTextView.setText(mDataset.get(position).mName);
			((ItemViewHolder)holder).mEducationTextView.setText(String.join(" | ", mDataset.get(position).mEducation));
			((ItemViewHolder)holder).mAgeTextView.setText(mDataset.get(position).mAge);
			((ItemViewHolder)holder).mHeightTextView.setText(mDataset.get(position).mHeight);
			((ItemViewHolder)holder).mSampradayaTextView.setText(mDataset.get(position).mSampradaya);
			((ItemViewHolder)holder).mPlaceTextView.setText(mDataset.get(position).mPlace);
			Picasso.with(((ItemViewHolder)holder).mProfilePicImageView.getContext())
				   .load(mDataset.get(position).mProfilePicsLink.get(0))
				   .fit()
				   .centerCrop()
				   .into(((ItemViewHolder)holder).mProfilePicImageView);
		} 
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
	
	@Override
    public int getItemViewType(int position) {
        return mDataset.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
}
