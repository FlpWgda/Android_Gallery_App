package com.example.storagechooser;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import it.sephiroth.android.library.exif2.ExifInterface;

public class DetailsFragment extends DialogFragment {

    ArrayList<String> detailsArray=new ArrayList<String>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_details, container, false);
        detailsArray = getArguments().getStringArrayList("exifArray");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.iitem_list,R.id.textData,detailsArray);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);




        return rootView;
    }


}
