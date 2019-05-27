package com.example.storagechooser;

import android.content.Context;
/**import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;**/
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;
    private int GridOrList=2;

    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<String> mImages, Context mContext, int type) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
        this.GridOrList = type;
    }

    public int getItemViewType() {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return GridOrList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_griditem, parent, false);
        switch (viewType) {
            case 0: return new ViewHolder0(view0);
            case 2: return new ViewHolder2(view2);

        }return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called. ");

        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0)holder;
                Glide.with(mContext)
                        .asBitmap()
                        .load(mImages.get(position))
                        .into(viewHolder0.image);

                viewHolder0.imageName.setText(mImageNames.get(position));

                viewHolder0.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
                        showTheDialog(position);
                    }
                });

                break;

            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2)holder;

                Glide.with(mContext)
                        .asBitmap()
                        .load(mImages.get(position))
                        .into(viewHolder2.image);

                viewHolder2.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
                        showTheDialog(position);
                    }
                });

                break;

        }


    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    private void showTheDialog(int position){
        Bundle bundle = new Bundle();
        String[] imageUrls = new String[mImages.size()];
        imageUrls = mImages.toArray(imageUrls);

        bundle.putStringArray("imageUrls",imageUrls);
        bundle.putInt("position", position);
        SliderDialog newFragment = new SliderDialog();
        newFragment.setArguments(bundle);
        newFragment.show(((FragmentActivity) mContext).getSupportFragmentManager(),"slider");
    }

    public class ViewHolder0 extends RecyclerView.ViewHolder{

        ImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder0(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout0);
        }
        public ViewHolder0(@NonNull View itemView,int scale){
            super(itemView);

        }
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder{

        ImageView image;
        TextView imageName;
        GridLayout parentLayout;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
