package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.R;

public class ControlGnr {

    private Context context;
    private Long id;
    private View pregunta;
    private View respuesta;
    private View btn;
    private String tiporescont;
    private View Viewtt;

    LinearLayout LLtotal;

    public ControlGnr(Context context, Long id, View pregunta, View respuesta, View btn, String tiporescont) {
        this.context = context;
        this.id = id;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.btn = btn;
        this.tiporescont = tiporescont;
    }

    public ControlGnr() {
    }

    /*ORGANIZA LOS CONTROLES INTEGRADOS*/
    public View Contenedor(boolean vacio, boolean inicial) {

        LinearLayout.LayoutParams llparamsTotal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llparamsTotal.setMargins(0, 0, 0, 10);

        LLtotal = new LinearLayout(context);
        LLtotal.setId(id.intValue());
        LLtotal.setLayoutParams(llparamsTotal);
        LLtotal.setWeightSum(2);
        LLtotal.setOrientation(LinearLayout.VERTICAL);
        LLtotal.setPadding(8, 15, 8, 12);
        LLtotal.setGravity(Gravity.CENTER_HORIZONTAL);

        if(vacio) {
            LLtotal.setBackgroundResource(R.drawable.bordercontainer);
        }else if(!vacio && inicial){
            LLtotal.setBackgroundResource(R.drawable.bordercontainerred);
        }else{
            LLtotal.setBackgroundResource(R.drawable.bordercontainer);
        }

        LLtotal.addView(Item());

        switch (tiporescont) {
            case "hx2":
                LLtotal.addView(hx2(pregunta, respuesta));
                break;
            case "vx2":
                LLtotal.addView(vx2(pregunta, respuesta));
                break;
            case "hxbtn_izq":
                LLtotal.addView(pregunta);
                LLtotal.addView(hxbtn_izq(respuesta, btn));
                break;
            case "hxbtn_der":
                LLtotal.addView(pregunta);
                LLtotal.addView(hxbtn_der(respuesta, btn));
                break;
        }

        setViewtt(LLtotal);
        return LLtotal;
    }

    //CREA EL NUMERO DEL ITEM
    public View Item() {
        TextView tvItem = new TextView(context.getApplicationContext());
        tvItem.setText(String.valueOf(id.intValue()));
        tvItem.setVisibility(View.VISIBLE);
        tvItem.setTextSize(5);

        return tvItem;
    }


    //layout organizativo horizontal 2 elementos
    public LinearLayout hx2(View v1, View v2) {
        LinearLayout.LayoutParams llparamsPrincipal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsPrincipal.setMargins(2, 5, 2, 5);

        LinearLayout LLprincipal = new LinearLayout(context);
        LLprincipal.setLayoutParams(llparamsPrincipal);
        LLprincipal.setBackgroundColor(Color.parseColor("#ffffff"));
        LLprincipal.setWeightSum(2);
        LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
        LLprincipal.setPadding(5, 5, 5, 5);
        LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

        LLprincipal.addView(v1);
        LLprincipal.addView(v2);

        return LLprincipal;
    }

    //layout organizativo horizontal 2 elementos
    public LinearLayout vx2(View v1, View v2) {
        LinearLayout.LayoutParams llparamsPrincipal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsPrincipal.setMargins(2, 5, 2, 5);

        LinearLayout LLprincipal = new LinearLayout(context);
        LLprincipal.setLayoutParams(llparamsPrincipal);
        LLprincipal.setBackgroundColor(Color.parseColor("#ffffff"));
        LLprincipal.setWeightSum(2);
        LLprincipal.setOrientation(LinearLayout.VERTICAL);
        LLprincipal.setPadding(5, 5, 5, 5);
        LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

        LLprincipal.addView(v1);
        LLprincipal.addView(v2);

        return LLprincipal;
    }

    //layout organizativo horizontal 2 elementos (view + boton hacia la izquierda)
    public LinearLayout hxbtn_izq(View v1, View v2) {
        LinearLayout.LayoutParams llparamsPrincipal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout LLprincipal = new LinearLayout(context);
        LLprincipal.setLayoutParams(llparamsPrincipal);
        LLprincipal.setWeightSum(2);
        LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
        LLprincipal.setPadding(5, 5, 5, 5);
        LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

        LLprincipal.addView(v1);
        LLprincipal.addView(v2);

        return LLprincipal;
    }

    //layout organizativo horizontal 2 elementos (view + boton hacia la derecha)
    public LinearLayout hxbtn_der(View v1, View v2) {
        LinearLayout.LayoutParams llparamsPrincipal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout LLprincipal = new LinearLayout(context);
        LLprincipal.setLayoutParams(llparamsPrincipal);
        LLprincipal.setWeightSum(2);
        LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
        LLprincipal.setPadding(5, 5, 5, 5);
        LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

        LLprincipal.addView(v2);
        LLprincipal.addView(v1);

        return LLprincipal;
    }


    //trae el ID (Item de la pregunta)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //trae el view de la pregunta me sirve para administrar los campos nulos antes del registro de datos
    public View getViewtt() {
        return Viewtt;
    }

    public void setViewtt(View viewtt) {
        Viewtt = viewtt;
    }

}
