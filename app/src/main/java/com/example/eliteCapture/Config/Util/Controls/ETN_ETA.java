package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
            camp.setRawInputType(rt.getTipo().equals("ETN") ? Configuration.KEYBOARD_QWERTY : Configuration.KEYBOARD_NOKEYS);
            funLimit();
            //funCamp();
            validateHasFocus();
            return camp;
        }catch (Exception e){
            Log.i("llegadaRespuesta", e.toString());
            return null;
        }
    }

    public void validateHasFocus(){
        camp.setOnFocusChangeListener((v, hasFocus) -> {

            try {
                if (!hasFocus) {
                    int i = camp.getText().toString().length();
                    String res = camp.getText().toString();

                    //valida si exite un punto como ultimo caracter y lo elimina
                    camp.setText(res.charAt(i-1) == (char) 46 ? res.substring(0, i - 1) : res);
                    respuestaCampo = camp.getText().toString();
                    registro(respuestaCampo, respuestaCampo != null ? rt.getPonderado() + "" : "");
                    respuestaPonderado.setText(respuestaCampo != null ? "Resultado : " + rt.getPonderado() : "Resultado :");
                    contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
                }
            }catch (Exception e){
                Log.i("etnres", "respuesta : " + e.toString());
            }
        });

    }

    public void funCamp(){//FUNCION DEL CAMPO
        camp.setOnFocusChangeListener((v, hasFocus) -> {
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
        });
    }

    public void funLimit() {
        camp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (rt.getTipo().equals("ETN")) {
                        String r = reemplazarDecimal();

                        if (rt.getDesde_hasta() != null) {
                            String data[] = rt.getDesde_hasta().trim().split("-");

                            int from = Integer.parseInt(data[0].trim()),
                                    to = Integer.parseInt(data[1].trim());

                            double rta = 0;
                            if (!camp.getText().toString().isEmpty()) {
                                rta = Double.parseDouble(r);
                            }

                            noti.removeAllViews();
                            if(rta > to || rta < from) {
                                noti.addView(new textAdmin(context).textColor("No se encuentra dentro del rango requerido (" + from + " a " + to + ")", "rojo", 15, "l"));
                                new Handler().postDelayed(() -> {
                                    camp.setText("");
                                }, 3000);
                                registro(null, null);
                            }else {
                                respuestaCampo = r;
                                registro(respuestaCampo, respuestaCampo != null ? rt.getPonderado() + "" : "");
                            }
                        } else {
                            respuestaCampo = camp.getText().toString();
                            registro(respuestaCampo, respuestaCampo != null ? rt.getPonderado() + "" : "");
                        }
                    }else if (rt.getTipo().equals("ETA")){
                        respuestaCampo = camp.getText().toString();
                        registro(respuestaCampo, respuestaCampo != null ? rt.getPonderado() + "" : "");
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "limit : " + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("funLimit","error : "+e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public String reemplazarDecimal(){

        String d = camp.getText().toString();

        //elimina punto si el campo es un entero
        if (d.contains(".") && rt.getDecimales() == 0 || d.length() == 1) {
            d = d.replaceAll("\\.", "");
        }else {
            //valida si digita mas de un punto
            if (getNumerCountCharacter(d, (char) 46) == 2) {
                d = d.substring(0, d.length() - 1);
            }

            //valida si digita mas de un coma
            if (getNumerCountCharacter(d, (char) 44) > 0) {
                d = d.substring(0, d.length() - 1);
            }

            //valida si digita mas de un espacio
            if (getNumerCountCharacter(d, (char) 32) > 0) {
                d = d.substring(0, d.length() - 1);
            }

            //valida si digita mas de un quion
            if (getNumerCountCharacter(d, (char) 45) > 0) {
                d = d.substring(0, d.length() - 1);
            }

            //valida cantidad de digitos despues del punto
            if (d.contains(".")) {
                try {
                    String[] r = d.split("\\.");
                    d = r[0] + "." + r[1].substring(0, r[1].length() == rt.getDecimales() ? r[1].length() : r[1].length() - (r[1].length() - rt.getDecimales()));
                }catch (Exception e){
                    Log.i("decimal", "error en el campo : "+e.toString());
                }
            }
        }

        //imprime en el campo si el dato registrado es diferente al inicia
        if (d.length() != camp.getText().length()) {
            camp.setText(d);
        }

        camp.setSelection(camp.getText().length());
        respuestaCampo = d;
        return d;
    }

    public int getNumerCountCharacter(String txt, char _toCompare){
        int c = 0;
        char []caracteres = txt.toCharArray();
        for(int i = 0; i <= caracteres.length - 1; i++){
            if(_toCompare == caracteres[i]){
                c++;
            }
        }
        return c;
    }

    public void registro(String rta, String valor) {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }
}