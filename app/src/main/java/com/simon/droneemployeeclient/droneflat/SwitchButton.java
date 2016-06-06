package com.simon.droneemployeeclient.droneflat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.simon.droneemployeeclient.R;

/**
 * Created by simon on 07.06.16.
 */

public class SwitchButton implements View.OnClickListener {
    public interface OnSwitchListener{
        public void switchOn();
        public void switchOff();
    }

    public SwitchButton(Drawable firstImage, Drawable secondImage, OnSwitchListener onSwitchListener){
        mFirstImage = firstImage;
        mSecondImage = secondImage;
        mListener = onSwitchListener;
    }

    @Override
    public void onClick(View view) {
        FloatingActionButton button =(FloatingActionButton) view;
        if(mFlag){
            button.setImageDrawable(mSecondImage);
            mListener.switchOff();
        } else {
            button.setImageDrawable(mFirstImage);
            mListener.switchOn();
        }
        mFlag = !mFlag;
    }
    private final Drawable mFirstImage;
    private final Drawable mSecondImage;
    private final OnSwitchListener mListener;
    private boolean mFlag = false;

}
