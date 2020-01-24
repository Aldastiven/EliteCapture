package com.example.procesos2.Element;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ElTextView {
    private Context context;
    private String pregunta;

    View newView = new View(context);
    ArrayList<View> arrayListView = new ArrayList<>();

    //............constructor
    public ElTextView(Context context, String pregunta) {
        this.context = context;
        this.pregunta = pregunta;
    }



    //............metodo que crea el view control y retorna en mismo

    public View CrearTextViewFecha(Context context,Long id, String pregunta){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
        String fecha = sdf.format(cal.getTime());

        for(int i=0; i<1; i++){
            ArrayList<ElTextView> lista = new ArrayList<>();
            lista.add(new ElTextView(context,pregunta));

                for(ElTextView tv: lista){

                    LinearLayout.LayoutParams llparamsTXT1 =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                    llparamsTXT1.weight = 1;
                    llparamsTXT1.setMargins(5,10,5,5);

                    android.widget.TextView tvp = new android.widget.TextView(context);
                    tvp.setId(id.intValue());
                    tvp.setText(tv.pregunta);
                    tvp.setTextSize(20);
                    tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tvp.setTextColor(Color.parseColor("#979A9A"));
                    tvp.setTypeface(null, Typeface.BOLD);
                    tvp.setLayoutParams(llparamsTXT1);


                    android.widget.TextView tvr = new android.widget.TextView(context);
                    tvr.setId(id.intValue());
                    tvr.setText(fecha);
                    tvr.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tvr.setTextSize(20);
                    tvr.setTextColor(Color.parseColor("#979A9A"));
                    tvr.setTypeface(null, Typeface.BOLD);
                    tvr.setLayoutParams(llparamsTXT1);

                    /*linearHeader.addView(CrearLinearLayoutHeader(tvp,tvr));


                    String quest = String.valueOf(tvp.getId());
                    String respuesta = "NULL";

                    al.add(modulo +"--"+quest+"--"+respuesta+"--0");

                    String alData = modulo+"--"+quest+"--"+respuesta+"--0";
                    al.set((tvp.getId())-1,alData);
                     */

                    arrayListView.add(tvp);
                    arrayListView.add(tvr);
                    newView.addChildrenForAccessibility(arrayListView);
                }
        }

        return newView;
    }
}
