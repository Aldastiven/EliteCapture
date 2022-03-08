package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.res.Configuration;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;

public class MUL {
    Context context;
    RespuestasTab rt;
    String ubicacion, path, respuestaCampo = null;
    boolean vacio, initial;

    TextView respuestaPonderado;
    EditText camp;
    LinearLayout contenedorCamp, noti;
    containerAdmin ca;
    GIDGET pp;

    public MUL(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : " + rt.getPonderado() : "Resultado :");

        noti = new LinearLayout(context);
        noti.setOrientation(LinearLayout.VERTICAL);
    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        try {
            contenedorCamp = ca.container();
            contenedorCamp.setOrientation(LinearLayout.VERTICAL);
            contenedorCamp.setPadding(10, 0, 10, 0);
            contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

            contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
            contenedorCamp.addView(generator());
            contenedorCamp.addView(noti);
            pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

            return contenedorCamp;
        }catch (Exception e){
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }


    public View generator(){

        Button generatorBtn = new Button(context);

        ScrollView scv = new ScrollView(context);

        LinearLayout containerPrinc = ca.container();
        containerPrinc.addView(generatorBtn);
        containerPrinc.addView(scv);


        LinearLayout container = ca.container();

        scv.addView(container);

        generatorBtn.setOnClickListener((v) -> {
            container.addView(getTextField());
        });

        return containerPrinc;
    }

    public View getTextField(){
        try{
            camp = (EditText) pp.campoEdtable("Edit", "grisClear");

            LinearLayout.LayoutParams llparams = ca.params();
            llparams.weight = 1;
            llparams.setMargins(5, 2, 5, 10);

            //para limitar cantidad de digitos segun la regla
            if (rt.getReglas() != 0) {
                camp.setFilters(
                        new InputFilter[]{
                                new InputFilter.LengthFilter(rt.getReglas())
                        });
            }

            camp.setText(vacio ? rt.getRespuesta() : "");
            camp.setRawInputType(rt.getTipo().equals("ETN") ? Configuration.KEYBOARD_QWERTY : Configuration.KEYBOARD_NOKEYS);
            return camp;
        }catch (Exception e){
            Log.i("ErrorMul", e.toString());
            return null;
        }
    }
}
