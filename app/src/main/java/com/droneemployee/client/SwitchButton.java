package com.droneemployee.client;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.droneemployee.client.common.Task;

/**
 * Created by simon on 07.06.16.
 */

public class SwitchButton implements
        View.OnClickListener,
        SharedTaskIndex.Observer
{
    public interface OnSwitchListener{

        void switchOn();
        void switchOff();
    }
    private static String LOGNAME = "SwitchButton";

    private final Drawable firstImage;

    private final Drawable secondImage;
    private final OnSwitchListener listener;
    private boolean flag = false;
    private ImageView imageView;
    public SwitchButton(ImageView imageView, Drawable firstImage, Drawable secondImage,
                        OnSwitchListener onSwitchListener){
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.listener = onSwitchListener;
        this.imageView = imageView;
    }

    public void on(){
        if(!flag){
            imageView.setImageDrawable(firstImage);
            listener.switchOn();
            flag = true;
        }
    }

    public void off(){
        if(flag){
            imageView.setImageDrawable(secondImage);
            listener.switchOff();
            flag = false;
        }
    }

    @Override
    public void onClick(View view) {
        ImageView button = imageView;
        if(flag){
            button.setImageDrawable(secondImage);
            listener.switchOff();
        } else {
            button.setImageDrawable(firstImage);
            listener.switchOn();
        }
        flag = !flag;
    }

    @Override
    public void setSharedCurrentTask(SharedTaskIndex sharedTaskIndex) {}

    @Override
    public void updateCurrentTask(int taskIndex) {
        Log.i(LOGNAME, "In updateCurrentTask: taskIndex: " + taskIndex);
        this.off();
    }
}
