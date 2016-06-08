package com.droneemployee.client;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by simon on 07.06.16.
 */

public class SwitchButton implements View.OnClickListener {
    public interface OnSwitchListener{
        void switchOn();
        void switchOff();
    }
    public SwitchButton(ImageView imageView, Drawable firstImage, Drawable secondImage,
                        OnSwitchListener onSwitchListener){
        mFirstImage = firstImage;
        mSecondImage = secondImage;
        mListener = onSwitchListener;
        mImageView = imageView;
    }

    public void on(){
        if(!mFlag){
            mImageView.setImageDrawable(mFirstImage);
            mListener.switchOn();
            mFlag = true;
        }
    }

    public void off(){
        if(mFlag){
            mImageView.setImageDrawable(mSecondImage);
            mListener.switchOff();
            mFlag = false;
        }
    }

    @Override
    public void onClick(View view) {
        ImageView button = mImageView;
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
    private ImageView mImageView;

}
