package com.example.procesos2.ElementH;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Ctextview {

    View ControlView; //esta variable retorna en el metodo que crea el control

    //metodo que crea dinamicamente el control del textview
    public View textview (Context context, Long id, String contenido){

        int i;
        for(i=0; i<1; i++){
            ArrayList<constexview> lista = new ArrayList<>();
            lista.add(new constexview(context,id,contenido));

            for(constexview tt : lista){

                LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv = new TextView(tt.context);
                tv.setId(tt.id.intValue());
                tv.setText(tt.contenido);
                tv.setTextSize(20);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setLayoutParams(llparams);

                ControlView = tv;
            }
        }
        return ControlView;
    }

    //constructor
    class constexview{
        Context context;
        Long id;
        String contenido;

        public constexview(Context context, Long id, String contenido) {
            this.context = context;
            this.id = id;
            this.contenido = contenido;
        }
    }

}
