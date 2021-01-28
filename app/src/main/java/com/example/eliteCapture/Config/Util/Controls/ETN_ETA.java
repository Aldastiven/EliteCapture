package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;
import com.example.eliteCapture.genated;

import org.apache.commons.lang3.StringUtils;

public class ETN_ETA extends ContextWrapper {

    Context context;
    RespuestasTab rt;
    String ubicacion, path, respuestaCampo = null;
    boolean vacio, initial;

    TextView respuestaPonderado;
    EditText camp;
    LinearLayout contenedorCamp, noti;
    containerAdmin ca;
    GIDGET pp;

    public ETN_ETA(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        super(context);//CONSTRUCTOR
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
            contenedorCamp.addView(campo());
            contenedorCamp.addView(noti);
            pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

            return contenedorCamp;
        }catch (Exception e){
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }

    public EditText campo(){//CAMPO DE USUARIO
        try {
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
            if (rt.getTipo().equals("ETN")) {
                camp.setRawInputType(Configuration.KEYBOARD_QWERTY);
                funLimit();
            }
            funCamp();
            return camp;
        }catch (Exception e){
            Log.i("llegadaRespuesta", e.toString());
            return null;
        }
    }

    public void funCamp(){//FUNCION DEL CAMPO
        camp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    if (!hasFocus) {
                        if (rt.getTipo().equals("ETA")) {
                            respuestaCampo = camp.getText().toString();
                        }

                        registro(respuestaCampo, respuestaCampo != null ? rt.getPonderado() + "" : "");
                        respuestaPonderado.setText(respuestaCampo != null ? "Resultado : " + rt.getPonderado() : "Resultado :");
                        contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Funcamp : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void funLimit() {
        camp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (rt.getTipo().equals("ETN")) {
                        String r = reemplazarDecimal();
                        if (rt.getDesdeHasta() != null) {

                            String data[] = rt.getDesdeHasta().split("-");
                            int from = Integer.parseInt(data[0].trim()),
                                    to = Integer.parseInt(data[1].trim());
                            double rta = 0;
                            if (!camp.getText().toString().isEmpty()) {
                                rta = Double.parseDouble(r);
                            }
                            noti.removeAllViews();
                            if (rta > to || rta < from) {
                                noti.addView(new textAdmin(context).textColor("No se encuentra dentro del rango requerido (" + from + " a " + to + ")", "rojo", 15, "l"));
                                respuestaCampo = null;
                            } else {
                                respuestaCampo = r;
                            }
                        } else {
                            respuestaCampo = camp.getText().toString();
                        }
                        respuestaCampo = respuestaCampo != null ? respuestaCampo.replaceAll("\\W+", "") : "";
                    }
                    registro(respuestaCampo, respuestaCampo != null ? rt.getPonderado() + "" : "");
                } catch (Exception e) {
                    //Toast.makeText(context, "limit : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String reemplazarDecimal(){
        String d = camp.getText().toString();

        if(d.contains(",")){
            d = d.replaceAll(",", "");
        }else if(d.contains(" ")){
            d = d.replaceAll(" ", "");
        }else if(d.contains("-")){
            d = d.replaceAll("-", "");
        }else if(d.contains(".") && rt.getDecimales() == 0){
            d = d.replaceAll("\\.", "");
        }

        if(getNumerCountCharacter(d) == 2 && rt.getDecimales() > 0){
            d = d.substring(0, d.length() - 1);
        }
        /*if(d.contains(".")) {
            String[] r = d.split("\\.");
            d = r[0] + "." + r[1].substring(0, r[1].length() == rt.getDecimales() ? r[1].length() : r[1].length() - (r[1].length() - rt.getDecimales()));
        }*/

        if(d.length() < camp.getText().length()) {
            camp.setText(d);
        }
        camp.setSelection(camp.getText().length());

        return d;
    }

    public int getNumerCountCharacter(String txt){
        int c = 0;
        char _toCompare = '.';
        char []caracteres = txt.toCharArray();
        for(int i = 0; i <= caracteres.length - 1; i++){
            if(_toCompare == caracteres[i]){
                c++;
            }
        }
        return c;
    }

    public void registro(String rta, String valor) {//REGISTRO
        Log.i("registro", "respuesta : " + rta);
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }
}