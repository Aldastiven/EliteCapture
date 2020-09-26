package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;

import java.util.ArrayList;
import java.util.List;

import static com.example.eliteCapture.R.drawable.bordercontainer;

public class RB {

    Context context;
    RespuestasTab rt;
    String ubicacion, path;
    boolean vacio, initial;

    TextView respuestaPonderado;
    LinearLayout contenedorCamp;
    containerAdmin ca;
    GIDGET pp;
    iDesplegable iDesp;

    public RB(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        iDesp = new iDesplegable(null, path);

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : "+(rt.getValor().equals("-1") ? "N/A" : rt.getValor()) : "Resultado :");
        Log.i("UBICACION","Ubicacion : "+ubicacion);
    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        contenedorCamp = ca.container();
        contenedorCamp.setOrientation(LinearLayout.VERTICAL);
        contenedorCamp.setPadding(10, 0, 10, 0);
        contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

        contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
        contenedorCamp.addView(campo());
        pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

        return contenedorCamp;
    }

    public View campo() {//CAMPO DE USUARIO
        LinearLayout line = ca.container();
        line.setPadding(2,10,2,10);
        line.setBackgroundColor(Color.parseColor("#eeeeee"));

        RadioGroup rg = new RadioGroup(context);
        rg.setOrientation(LinearLayout.HORIZONTAL);
        rg.setGravity(Gravity.CENTER_HORIZONTAL);

        getOption(rg);
        line.addView(rg);

        return line;
    }

    public void getOption(RadioGroup rg){
        try {
            iDesp.nombre = rt.getDesplegable();
            for (DesplegableTab desp : iDesp.all()) {
                rg.addView(Rb(desp));
            }
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public RadioButton Rb(DesplegableTab dt){
        RadioButton rb = new RadioButton(context);
        rb.setId(Integer.parseInt(dt.getCodigo()));
        rb.setText(dt.getOpcion());
        rb.setId(Integer.parseInt(dt.getCodigo()));
        rb.setTextSize(12);
        rb.setChecked((dt.getOpcion().equals(rt.getRespuesta())));

        switch(rb.getId()){
            case 0://NO CUMPLE
                rb.setButtonTintList(ColorSelected(231, 76, 60));
                break;
            case 1://NO APLICA
                rb.setButtonTintList(ColorSelected(33, 47, 61));
                break;
            case 2://SI CUMPLE
                rb.setButtonTintList(ColorSelected(34, 153, 84));
                break;
        }
        FunRb(rb);
        return rb;
    }

    public ColorStateList ColorSelected(int red, int green, int blue) {
        final ColorStateList colorStateList = new ColorStateList(
            new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked}
            },
            new int[]{
                    Color.DKGRAY,
                    Color.rgb(red, green, blue),
            }
        );
        return colorStateList;
    }

    public void FunRb(final RadioButton rb){
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rta = rb.getText().toString(), vlr = "";
                switch(rb.getId()){
                    case 0://NO CUMPLE
                        vlr = "0";
                        break;
                    case 1://NO APLICA
                        vlr = "N/A";
                        break;
                    case 2://SI CUMPLE
                        vlr = ""+rt.getPonderado();
                        break;
                }
                contenedorCamp.setBackgroundResource(bordercontainer);
                respuestaPonderado.setText(!rta.isEmpty() ? "Resultado : "+ vlr : "Resultado :");
                registro(!rta.isEmpty() ? rta : null, !rta.isEmpty() ? (vlr.equals("N/A") ? "-1" : vlr) : null);
            }
        });
    }

    public void registro(String rta, String valor) {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }
}
