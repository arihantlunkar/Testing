package com.rishtey.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rishtey.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    private static ArrayList<Integer> removedItems;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.my_recycler_profile_view);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.versionArray[i],
                    MyData.id_[i],
                    MyData.drawableArray[i]
            ));
        }

        removedItems = new ArrayList<Integer>();

        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);

        return root;
    }

}


//***********************    Need to remove once dynamic content comes    *****************************//

class DataModel {

    String name;
    String version;
    int id_;
    int image;

    public DataModel(String name, String version, int id_, int image) {
        this.name = name;
        this.version = version;
        this.id_ = id_;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.ProfileAge);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.ProfileName);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.profileImage);
        }
    }

    public CustomAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getVersion());
        imageView.setImageResource(dataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

class MyData {

    static String[] nameArray = {"29Yrs,", "29Yrs,", "29Yrs,", "29Yrs,", "29Yrs,", "29Yrs,", "29Yrs,","29Yrs,", "29Yrs,", "29Yrs,", "29Yrs,"};
    static String[] versionArray = {"Shahrukh Khan", "Salman Khan", "Sushant Singh Rajput", "Akshay Kumar", "Amitabh Bachchan", "Aishwarya Rai Bachchan", "Priyanka Chopra", "Deepika Padukone", "Kareena Kapoor", "Alia Bhatt","Anushka Sharma"};

    static Integer[] drawableArray = {R.drawable.akshay_kumar, R.drawable.akshay_kumar, R.drawable.akshay_kumar,
            R.drawable.akshay_kumar, R.drawable.akshay_kumar, R.drawable.akshay_kumar, R.drawable.akshay_kumar,
            R.drawable.akshay_kumar, R.drawable.akshay_kumar, R.drawable.akshay_kumar,R.drawable.akshay_kumar};

    static Integer[] id_ = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
}

