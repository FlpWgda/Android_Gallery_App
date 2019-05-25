package com.example.storagechooser;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class SliderDialog extends DialogFragment{
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private LockableViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;


    private ImageButton threeDots;

    String[] imageUrls;
    int position;

    public SliderDialog(){
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogStyle);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        setStyle(DialogFragment.STYLE_NORMAL,0);**/
    }
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
       /** dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR);
        dialog.getWindow().setStatusBarColor(Color.BLACK);**/

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        imageUrls = getArguments().getStringArray("imageUrls");
        position = getArguments().getInt("position");
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.FullScreenDialogStyle);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.activity_screen_slide, container,false);

        threeDots = (ImageButton) view.findViewById(R.id.threeDots);
        threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(threeDots.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.actions2, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.one:
                                Log.d("one one one", "one one");
                                return true;
                            case R.id.two:
                                Log.d("two two two", "onMenuItemClick: two ");
                                return true;
                            case R.id.three:
                                Log.d("three", "three");

                                String path = imageUrls[mPager.getCurrentItem()];

                                Bundle bundle = new Bundle();
                                bundle.putString("path",imageUrls[position]);

                                DataDialog dataDialog = new DataDialog();
                                dataDialog.setArguments(bundle);
                                dataDialog.show(getFragmentManager(),"Dialog");

                                return true;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        mPager = (LockableViewPager) view.findViewById(R.id.pagerX2);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(position);



        return view;
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("path",imageUrls[position]);
            ScreenSlidePageFragment newFragment = new ScreenSlidePageFragment();
            newFragment.setArguments(bundle);
            return newFragment;
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }
    }
}