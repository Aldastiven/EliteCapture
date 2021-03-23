package com.example.eliteCapture.Config.Util.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class textAdmin {
    Context context;

    public textAdmin(Context context) {
        this.context = context;
    }

    public LinearLayout ubication(View v1 , View v2){
        LinearLayout.LayoutParams paramsl = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout line = new LinearLayout(context);
        line.setWeightSum(2);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutParams(paramsl);

        LinearLayout.LayoutParams paramsv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsv.weight = 1;

        v1.setLayoutParams(paramsv);
        v2.setLayoutParams(paramsv);

        line.addView(v1);
        line.addView(v2);

        return line;
    }

    public LinearLayout text(String titulo, int sixe1, String dato, int sixe2){

        LinearLayout.LayoutParams ltext = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        TextView tit = new TextView(context);
        tit.setText(titulo);
        tit.setTextColor(Color.parseColor("#17202A"));
        tit.setTypeface(null,Typeface.BOLD);
        tit.setTextSize(sixe1);
        tit.setLayoutParams(ltext);

        TextView tda = new TextView(context);
        tda.setText(dato);
        tda.setTextColor(Color.parseColor("#85929E" ));
        tda.setTextSize(sixe2);
        tda.setLayoutParams(ltext);

        return ubication(tit, tda);
    }

    public View textTitulo(String titulo, int size){
        LinearLayout.LayoutParams ltext = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView tit = new TextView(context);
        tit.setText(titulo);
        tit.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tit.setTextColor(Color.parseColor("#17202A"));
        tit.setTypeface(null,Typeface.BOLD);
        tit.setTextSize(size);
        tit.setLayoutParams(ltext);
        tit.setPadding(0,5,0,5);
        //tit.setBackgroundResource(R.drawable.border_gray);

        return tit;
    }

    public View textColor(String texto, String color, int size, String orientation){
        TextView txt = new TextView(context);
        txt.setText(texto);
        txt.setTextColor(colorText(color));
        txt.setTypeface(null,Typeface.BOLD);
        txt.setTextSize(size);
        txt.setGravity(AlingText(orientation));

        return txt;
    }

    public int colorText(String color){
        int C = 0;
        switch (color){
            case "verde":
                C = Color.parseColor("#2ecc71");
                break;
            case "gris":
                C = Color.parseColor("#85929E");
                break;
            case "negro":
                C = Color.parseColor("#17202A");
                break;
            case "rojo":
                C = Color.parseColor("#E74C3C");
                break;
            case "darkGray":
                C = Color.parseColor("#154360");
                break;
        }
        return C;
    }

    public int AlingText(String orientation){
        int G = 0;
        switch (orientation){
            case "c":
                G = Gravity.CENTER;
                break;
            case "l":
                G = Gravity.LEFT;
                break;
            case "r":
                G = Gravity.RIGHT;
                break;
        }
        return G;
    }

}
