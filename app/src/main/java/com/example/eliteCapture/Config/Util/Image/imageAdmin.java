package com.example.eliteCapture.Config.Util.Image;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class imageAdmin {
    Context context;

    public imageAdmin(Context context) {
        this.context = context;
    }

    public View image(int images){
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(images);

        return imageView;
    }
}
