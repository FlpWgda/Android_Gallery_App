package com.example.storagechooser;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class ScreenSlidePageFragment extends DialogFragment {

    ImageViewZoom picture;

    String path;

    OnPinchListener onPinchListener;
    private float mScaleFactor = 1.0f;

    // Used to detect pinch zoom gesture.
    private ScaleGestureDetector scaleGestureDetector = null;
    /**public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }**/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        path = getArguments().getString("path");

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);


        picture = (ImageViewZoom) rootView.findViewById(R.id.pictureXX);
        picture.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (picture.getScale() > 1f) {
                    picture.getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    picture.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        //onPinchListener = new OnPinchListener(getActivity(), picture);

        scaleGestureDetector = new ScaleGestureDetector(this.getActivity().getApplicationContext(), new ScaleListener());


        setImage();
        //rootView.getBackground().setAlpha(70);
        return rootView;
    }

    private void setImage(){
        picture.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            picture.setScaleX(mScaleFactor);
            Log.i("imageviewandontouch", "scale OK");
            picture.setScaleY(mScaleFactor);
            return true;
        }
    }

}
