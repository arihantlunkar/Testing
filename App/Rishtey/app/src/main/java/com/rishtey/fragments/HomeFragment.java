package com.rishtey.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rishtey.R;
import com.rishtey.adapters.HomeAdapter;
import com.rishtey.data.HomeData;
import com.rishtey.servercommunicator.DisplayTaskServerCommunicator;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class HomeFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
	
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter = null;
    private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList<HomeData> mData = new ArrayList<HomeData>();
	private DisplayTaskServerCommunicator.RequiredData mRequiredData = new DisplayTaskServerCommunicator.RequiredData();
	private boolean mIsLoading = false;
	
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
		
		mRecyclerView = root.findViewById(R.id.homeRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
		
		mRequiredData.mCurrentPosition = 0;

		try {
			new DisplayTaskServerCommunicator(this.getContext(), mRequiredData, this, this).trigger();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

				if (!mIsLoading) {
					if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mData.size() - 1) {
						//bottom of list!
						loadMore();
						mIsLoading = true;
					}
				}
			}
			
		});
        return root;
    }
	
	@Override
	public void onResponse(JSONObject response) {
		try {
			if(null != mAdapter)
			{	
				mData.remove(mData.size() - 1);
				mAdapter.notifyItemRemoved(mData.size());
			}
			
			JSONArray results = response.getJSONArray("data");
			for (int i = 0; i < results.length(); i++) {
				HomeData homeData = new HomeData();
				homeData.mProfilePicsLink.add("http://192.168.0.10:8081/Testing/Backend/rishtey/" + results.getJSONObject(i).getString("image1Link"));	
				mData.add(homeData);
				mRequiredData.mCurrentPosition += 1;
			}
		
			if(null == mAdapter) 
			{
				mAdapter = new HomeAdapter(mData);
				mRecyclerView.setAdapter(mAdapter);
			}
			else
			{
				mAdapter.notifyDataSetChanged();
				mIsLoading = false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onErrorResponse(VolleyError error) {
		Toast.makeText(this.getContext(), "No internet connection!", Toast.LENGTH_LONG).show();
	}
	
	private void loadMore() {
        mData.add(null);
        mAdapter.notifyItemInserted(mData.size() - 1);
		try {
			new DisplayTaskServerCommunicator(this.getContext(), mRequiredData, this, this).trigger();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
}