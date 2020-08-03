package com.example.eliteCapture.Config.Util.Container;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class containerAdmin {

    Context c;

    public containerAdmin(Context c) {
        this.c = c;
    }

    public LinearLayout container(){
        LinearLayout.LayoutParams lcontainer = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                                );
        lcontainer.setMargins(1, 5, 1, 5);

        LinearLayout container = new LinearLayout(c);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(lcontainer);

        return container;
    }

    public LinearLayout.LayoutParams params(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public LinearLayout.LayoutParams params2(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public LinearLayout.LayoutParams params3(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public View scrollv(LinearLayout l){
        LinearLayout.LayoutParams lscv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ScrollView scv = new ScrollView(c);
        scv.setLayoutParams(lscv);
        scv.addView(l);

        return scv;
    }
}
