package com.droneemployee.client;

import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
    private static String TAG = "SwitchButton";

    private final Drawable firstImage;

    private final Drawable secondImage;
    private final OnSwitchListener listener;
    private boolean flag = false;
    private FloatingActionButton button;

    public SwitchButton(FloatingActionButton button, Drawable firstImage, Drawable secondImage,
                        OnSwitchListener onSwitchListener){
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.listener = onSwitchListener;
        this.button = button;
    }

    public void on(){
        if(!flag){
            button.setImageDrawable(firstImage);
            listener.switchOn();
            flag = true;
        }
    }

    public void off(){
        if(flag){
            button.setImageDrawable(secondImage);
            listener.switchOff();
            flag = false;
        }
    }

    @Override
    public void onClick(View view) {
        ImageView button = this.button;
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
        Log.i(TAG, "In updateCurrentTask: taskIndex: " + taskIndex);

        if(taskIndex == SharedTaskIndex.NOTSET) button.hide();
        else button.show();

        this.off();
    }
}
