package com.example.eliteCapture.Config.Util.Container;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

    public View scrollv(View l){
        LinearLayout.LayoutParams lscv = new LinearLayout.LayoutParams(1000, LinearLayout.LayoutParams.WRAP_CONTENT);

        ScrollView scv = new ScrollView(c);
        scv.setLayoutParams(lscv);
        scv.addView(l);

        return scv;
    }

    public LinearLayout card(View v, String color){//PINTA EL CONTENEDOR DE TODOS LOS ITEMS
        LinearLayout.LayoutParams params = params();
        params.setMargins(10,10,10,10);

        LinearLayout line = new LinearLayout(c);
        line.setGravity(View.TEXT_ALIGNMENT_CENTER);
        line.setBackgroundColor(Color.parseColor(color));
        line.setPadding(1,25,0,25);
        line.setLayoutParams(params);
        line.addView(v);

        return line;
    }

    public LinearLayout borderGradient(String color) {
        LinearLayout.LayoutParams params = params();
        params.setMargins(10,0,0,0);

        LinearLayout line2 = container();
        line2.setLayoutParams(params);
        line2.setVisibility(View.GONE);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFFFFFFFF); // Changes this drawbale to use a single color instead of a gradient
        gd.setCornerRadius(5);
        gd.setStroke(2, Color.parseColor(color));

        line2.setBackground(gd);

        return line2;
    }
}
