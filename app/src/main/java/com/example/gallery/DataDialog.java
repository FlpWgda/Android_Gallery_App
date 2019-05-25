package com.example.storagechooser;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import it.sephiroth.android.library.exif2.ExifInterface;
import it.sephiroth.android.library.exif2.ExifTag;

public class DataDialog extends DialogFragment {

    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    String path;
    String exifString="";
    List<ExifTag> exifList;
    String[] exifAll;
    ArrayList<String> exifXX;
    ArrayList<String> exifDetails;
    ArrayList<String> details;
    ArrayList<ArrayList<String>> listOfDetailArrays = new ArrayList<ArrayList<String>>(2);
    File file;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.data_dialog, container, false);

        mPager = (ViewPager) rootView.findViewById(R.id.pagerX2);
        pagerAdapter = new DataDialog.ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(pagerAdapter);


        path = getArguments().getString("path");

        ExifInterface exif = new ExifInterface();
        try {
            exif.readExif( path, ExifInterface.Options.OPTION_ALL );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            exif.writeExif(exifString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**exifList = new ArrayList<ExifTag>();
        exifList = exif.getAllTags();
        exifList.add(exif.getTag(0));
            exifAll = new String[exifList.size()];
            for (int i = 0; i < exifAll.length; i++) {
                exifAll[i] = exifList.get(i).toString() + ":" + exifList.get(i).getValueAsString();
            }**/
        exifXX = new ArrayList<String>();
        details = new ArrayList<String>(3);

        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(new File(path));
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        file = new File(path);

        details = new ArrayList<String>(3);
       details.add("Nazwa obrazu: "+ file.getName());
        details.add("Lokalizacja obrazu: " + file.getAbsolutePath());
        details.add("Format obrazu: " + details.get(0).substring(details.get(0).lastIndexOf(".")));

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if(directory.getName().equals("Exif SubIFD")||directory.getName().equals("Exif Thumbnail")||directory.getName().equals("Exif IFD0")) {
                    exifXX.add(String.format("%s = %s",
                            tag.getTagName(), tag.getDescription()));
                }
                if(directory.getName().equals("File")){
                    details.add(String.format("%s = %s",
                            tag.getTagName(), tag.getDescription()));
                }

            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
        //exifXX.add(exifString);


        listOfDetailArrays.add(details);
        listOfDetailArrays.add(exifXX);

        return rootView;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("exifArray",listOfDetailArrays.get(position));
            DetailsFragment newFragment = new DetailsFragment();
            newFragment.setArguments(bundle);
            return newFragment;
        }
        public CharSequence getPageTitle(int position){
            switch (position) {
                case 0:
                    return "Details";
                case 1:
                    return "Exif";
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
