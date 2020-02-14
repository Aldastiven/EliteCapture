package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eliteCapture.R;

import static java.lang.String.valueOf;

public class Cconteos {
    View ControlView;
    int idres = 0;

    private Context context;
    private Long id;
    private String contenido;
    private Float porcentaje;

    ControlGnr Cgnr;

    public Cconteos(Context context, Long id, String contenido, Float porcentaje) {
        this.context = context;
        this.id = id;
        this.contenido = contenido;
        this.porcentaje = porcentaje;
    }

    //metodo que crea el control del conteo
    public View Cconteo(){

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

                Cgnr = new ControlGnr(context,id,pp(tvp,tvpor),Cbotones(),null,"vx2");
                ControlView = Cgnr.Contenedor();

        return ControlView;
    }

    //medidas para el boton y el campo de busqueda
    public LinearLayout.LayoutParams medidas(double med, String data){

        LinearLayout.LayoutParams llparams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llparams.weight = (float) med;
        llparams.setMargins(5, 10, 5, 5);

        return llparams;
    }

    //metodo que crea los botones de conteos
    public View Cbotones(){
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
                    if (n < 9) {
                        tvr.setText(valueOf(n + 1));
                    }
                }
            });

            bntres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int n = Integer.parseInt(tvr.getText().toString());
                    if (n > -1) {
                        tvr.setText(valueOf(n - 1));
                    }
                }
            });

        return viewTotal;
    }

    //retorna el layout del encabezado pregunta porcentaje
    public LinearLayout pp(View v1, View v2){
        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout llpregunta = new LinearLayout(context);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);

        llpregunta.addView(v1); //retorna pregunta
        llpregunta.addView(v2); //retorna pocentaje

        return llpregunta;
    }

}
