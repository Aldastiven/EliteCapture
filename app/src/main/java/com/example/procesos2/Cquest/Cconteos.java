package com.example.procesos2.Cquest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.R;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class Cconteos {
    View ControlView;

    int idres = 0;

    //metodo que crea el control del conteo
    public View Cconteo(Context context, Long id, String contenido, Float porcentaje){
        int i;
        for(i = 0; i<=1; i++){
            ArrayList<consconteos> lista = new ArrayList<>();
            lista.add(new consconteos(context,id,contenido));

            for(consconteos cc : lista){

                //ORGANIZA LOS CONTROLES INTEGRADOS
                LinearLayout.LayoutParams llparamsTotal = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                llparamsTotal.setMargins(0,0,0,10);

                LinearLayout LLtotal = new LinearLayout(context);
                LLtotal.setLayoutParams(llparamsTotal);
                LLtotal.setWeightSum(2);
                LLtotal.setOrientation(LinearLayout.VERTICAL);
                LLtotal.setPadding(10,30,10,10);
                LLtotal.setGravity(Gravity.CENTER_HORIZONTAL);
                LLtotal.setBackgroundResource(R.drawable.bordercontainer);


                LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparamsText.weight = (float) 2.3;

                LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparamsText.weight = (float) 0.7;

                final TextView tvp = new TextView(context);
                tvp.setId(id.intValue());
                tvp.setText(contenido+"\nponderado: "+porcentaje.toString());
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setPadding(5, 5, 5, 5);
                tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(llparamsText);

                final TextView tvpor = new TextView(context);
                tvpor.setId(idres++);
                tvpor.setText("resultado: ");
                tvpor.setTextColor(Color.parseColor("#979A9A"));
                tvpor.setBackgroundColor(Color.parseColor("#ffffff"));
                tvpor.setPadding(10, 10, 10, 10);
                tvpor.setTypeface(null, Typeface.BOLD);
                tvpor.setLayoutParams(llparamsTextpo);

                LLtotal.addView(LLPREGUNTA(context,tvp,tvpor));
                LLtotal.addView(Cbotones(context,id));

                ControlView = LLtotal;
            }
        }
        return ControlView;
    }

    //metodo que crea los botones de conteos
    public View Cbotones(final Context context, Long id){
        View viewTotal;

        LinearLayout llbtn = new LinearLayout(context);
        llbtn.setWeightSum(4);
        llbtn.setOrientation(LinearLayout.HORIZONTAL);
        llbtn.setVerticalGravity(Gravity.CENTER);
        llbtn.setPadding(10,10,10,10);

        LinearLayout.LayoutParams LLcmpweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LLcmpweight.weight = 3;

        final TextView tvr = new TextView(context);
        tvr.setText("-1");
        tvr.setId(id.intValue());
        tvr.setTextSize(40);
        tvr.setTextColor(Color.parseColor("#5d6d7e"));
        tvr.setTypeface(null, Typeface.BOLD);
        tvr.setGravity(Gravity.CENTER);
        tvr.setLayoutParams(LLcmpweight);

        LinearLayout.LayoutParams LLbtnweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LLbtnweight.weight =(float) 0.5;

        Button bntres = new Button(context);
        bntres.setId(id.intValue());
        bntres.setText("-");
        bntres.setTextSize(30);
        bntres.setTextColor(Color.parseColor("#ffffff"));
        bntres.setBackgroundResource(R.drawable.btnres);
        bntres.setLayoutParams(LLbtnweight);

        Button bntsum = new Button(context);
        bntsum.setId(id.intValue());
        bntsum.setText("+");
        bntsum.setTextSize(30);
        bntsum.setTextColor(Color.parseColor("#ffffff"));
        bntsum.setBackgroundResource(R.drawable.btnsum);
        bntsum.setLayoutParams(LLbtnweight);

        llbtn.addView(bntres);
        llbtn.addView(tvr);
        llbtn.addView(bntsum);

        viewTotal = llbtn;



        bntsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(tvr.getText().toString());
                if(n < 9){
                    tvr.setText(valueOf(n + 1));
                }
            }
        });

        bntres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(tvr.getText().toString());
                if(n > -1){
                    tvr.setText(valueOf(n - 1));
                }
            }
        });

        return viewTotal;
    }

    //retorna view de pregunta y porcentaje
    public View LLPREGUNTA(Context c,View v1, View v2){

        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout llpregunta = new LinearLayout(c);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);

        llpregunta.addView(v1); //retorna pregunta
        llpregunta.addView(v2); //retorna pocentaje

        return llpregunta;
    }

    //constructor
    class consconteos{
        Context context;
        Long id;
        String contenido;

        public consconteos(Context context, Long id, String contenido) {
            this.context = context;
            this.id = id;
            this.contenido = contenido;
        }
    }
}
