package com.example.storagechooser;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
/**import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;**/

import com.codekidlabs.storagechooser.StorageChooser;
import com.codekidlabs.storagechooser.utils.DiskUtil;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class Main2Activity extends FragmentActivity  {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private File[] listOfImages;
    private String[] listOfImagePaths;

    private int gridOrList = 2;

    SharedPreferences preferences;
    private File folderPath;

    private SwitchCompat gridList;
    private ImageButton threeDots;

    private ViewPager mPager;

    private PagerAdapter pagerAdapter;

    private int mScaleFactor = 1;
    private GridLayoutManager mGridLayoutManager1, mGridLayoutManager2, mGridLayoutManager3, mGridLayoutManager4, mGridLayoutManager5;
    private RecyclerViewAdapter mRecyclerViewAdapter,mRecyclerViewAdapter1,mRecyclerViewAdapter2,mRecyclerViewAdapter3,mRecyclerViewAdapter4,mRecyclerViewAdapter5;
    private RecyclerView.LayoutManager mCurrentLayoutManager;
    RecyclerView recyclerView;

    private ScaleGestureDetector mScaleGestureDetector;

    /**String[] strings = new String[12];
    private SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor = preferences.edit();
    private TextView mDirectoryTextView;**/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        gridList = (SwitchCompat) findViewById(R.id.gridList);
        gridList.setTextOn("L");
        gridList.setTextOff("G");
        gridList.setShowText(true);
        gridList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    initRecyclerView(0);
                }
                else{
                    initRecyclerView(2);
                }
            }
        });

        threeDots = (ImageButton) findViewById(R.id.threeDots);
        threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(threeDots.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.actions, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.one:
                                pickFolder();
                                return true;

                        }
                        return true;
                    }
                });
                    popup.show();
                }

        });

        mGridLayoutManager1 = new GridLayoutManager(this, 2);
        mGridLayoutManager2 = new GridLayoutManager(this, 3);
        mGridLayoutManager3 = new GridLayoutManager(this, 4);
        mGridLayoutManager4 = new GridLayoutManager(this, 5);
        mGridLayoutManager5 = new GridLayoutManager(this, 6);
        //set layout manager
        mCurrentLayoutManager = mGridLayoutManager3;

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (detector.getCurrentSpan() > 200 && detector.getTimeDelta() > 200) {
                    if (detector.getCurrentSpan() - detector.getPreviousSpan() < -1) {
                        if (mCurrentLayoutManager == mGridLayoutManager1) {

                            mCurrentLayoutManager = mGridLayoutManager2;
                            recyclerView.setLayoutManager(mGridLayoutManager2);
                            return true;
                        } else if (mCurrentLayoutManager == mGridLayoutManager2) {
                            mCurrentLayoutManager = mGridLayoutManager3;
                            recyclerView.setLayoutManager(mGridLayoutManager3);
                            return true;
                        } else if (mCurrentLayoutManager == mGridLayoutManager3) {
                            mCurrentLayoutManager = mGridLayoutManager4;
                            recyclerView.setLayoutManager(mGridLayoutManager4);
                            return true;
                        } else if (mCurrentLayoutManager == mGridLayoutManager4) {
                            mCurrentLayoutManager = mGridLayoutManager5;
                            recyclerView.setLayoutManager(mGridLayoutManager5);
                            return true;
                        }
                    } else if(detector.getCurrentSpan() - detector.getPreviousSpan() > 1) {
                        if (mCurrentLayoutManager == mGridLayoutManager5) {
                            mCurrentLayoutManager = mGridLayoutManager4;
                            recyclerView.setLayoutManager(mGridLayoutManager4);
                            return true;
                        } else if (mCurrentLayoutManager == mGridLayoutManager4) {
                            mCurrentLayoutManager = mGridLayoutManager3;
                            recyclerView.setLayoutManager(mGridLayoutManager3);
                            return true;
                        } else if (mCurrentLayoutManager == mGridLayoutManager3) {
                            mCurrentLayoutManager = mGridLayoutManager2;
                            recyclerView.setLayoutManager(mGridLayoutManager2);
                            return true;
                        } else if (mCurrentLayoutManager == mGridLayoutManager2) {
                            mCurrentLayoutManager = mGridLayoutManager1;
                            recyclerView.setLayoutManager(mGridLayoutManager1);
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        preferences= getSharedPreferences("Folder", 0);
        String path = preferences.getString(DiskUtil.SC_PREFERENCE_KEY,"");
        folderPath = new File(path);

        if(path.equals("")){
            pickFolder();
        }
        else{
            listFiles();
            initRecyclerView(gridOrList);
        }
    }
    /**@Override
    public void onBackPressed() {

        if (findViewById(R.id.pager).getVisibility() == View.VISIBLE) {
            Log.d("onBack", "onBackPressed: mPager");
            mPager.setVisibility(View.GONE);

        } else {
            super.onBackPressed();
            Log.d("onBack2", "onBackPressed2: 222");
        }
    }**/


    private void listFiles(){
        Log.d("Path","exists");
        listOfImages = folderPath.listFiles();
        mImageUrls.clear();
        mNames.clear();


        for(int i=0; i<listOfImages.length;i++){
            String absolutePath =   listOfImages[i].getAbsolutePath();
            String name =           listOfImages[i].getName();
            String extension = "";
            if(name.lastIndexOf(".")>=0) {
                extension = name.substring(name.lastIndexOf("."));
            }
            Log.d("MyApp",absolutePath);
            if(extension.equals(".JPG") || extension.equals(".jpg") /**|| extension.equals(".png")**/){
                mImageUrls.add(absolutePath);
                mNames.add(name);
            }
        }
    }


    private void pickFolder(){
        Log.d("Path","doesnt exist");
        StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(Main2Activity.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                // --- ADD ---
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .actionSave(true)
                .withPreference(preferences)
                .build();

// Show dialog whenever you want by
        chooser.show();

// get path that the user has chosen
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                Log.e("SELECTED_PATH", path);
                Toast toast = (Toast) Toast.makeText(getApplicationContext(), preferences.getString(DiskUtil.SC_PREFERENCE_KEY,""), Toast.LENGTH_SHORT);
                toast.show();

                preferences= getSharedPreferences("Folder", 0);
                String path2 = preferences.getString(DiskUtil.SC_PREFERENCE_KEY,"");
                folderPath = new File(path2);

                listFiles();
                initRecyclerView(gridOrList);

            }
        });
    }

    private void initRecyclerView(int gridList){
            recyclerView = findViewById(R.id.recycler_view);
            mRecyclerViewAdapter = new RecyclerViewAdapter(mNames, mImageUrls, this,2);
            recyclerView.setAdapter(mRecyclerViewAdapter);

            recyclerView.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mScaleGestureDetector.onTouchEvent(event);

                    return false;
                }
            });
            if(gridList ==0){
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            if(gridList ==2){
                recyclerView.setLayoutManager(new GridLayoutManager(this,3));
            }

    }
    private void showTheDialog(){
        Bundle bundle = new Bundle();
        String[] imageUrls = new String[mImageUrls.size()];
        imageUrls = mImageUrls.toArray(imageUrls);

        bundle.putStringArray("imageUrls",imageUrls);
        SliderDialog newFragment = new SliderDialog();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm,ArrayList<String> ImageUrls, ArrayList<String> Names) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return mImageUrls.size();
        }
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(1,
                    Math.min(mScaleFactor, 4));
            //picture.setScaleX(mScaleFactor);
            Log.i("imageviewandontouch", "scale OK");
            //picture.setScaleY(mScaleFactor);
            return true;
        }
    }




}
