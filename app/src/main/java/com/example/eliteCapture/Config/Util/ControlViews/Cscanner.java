package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Camera;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.io.File;
import java.util.ArrayList;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

public class Cscanner {

    private Context context;
    private String path;
    private RespuestasTab rt;
    private Boolean vacio;
    private Boolean inicial;
    private String ubicacion;
    public TextView tv;

    public View controlView;
    public ControlGnr Cgnr = null;


    public Cscanner(Context context, String path, String ubicacion, RespuestasTab rt,Boolean inicial)  {
        this.context = context;
        this.path = path;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.vacio = rt.getRespuesta() != null;
        this.inicial = inicial;
    }

    public Cscanner(String path) {
        this.path = path;
    }

    //metodo que crea dinamicamente el contol scanner
    public View scanner() {

        tv = new TextView(context);
        tv.setId(rt.getId().intValue());
        tv.setPadding(5, 5, 5, 5);
        tv.setTypeface(null, Typeface.BOLD);

        tv.setText((rt.getValor() == null ? rt.getPregunta() : rt.getPregunta()+ " : \n" + rt.getValor()));
        tv.setTextColor((rt.getValor() == null ? Color.parseColor("#979A9A") : Color.parseColor("#58d68d")));

        final EditText edt = new EditText(context);
        edt.setHint("" + rt.getPregunta());
        edt.setText((vacio ? rt.getRespuesta() : ""));
        edt.setHintTextColor(Color.parseColor("#626567"));
        edt.setBackgroundColor(Color.parseColor("#ffffff"));
        edt.setTextColor(Color.parseColor("#1C2833"));
        edt.setLayoutParams(medidas(0.5));
        edt.setTypeface(null, Typeface.BOLD);
        edt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        edt.setRawInputType(Configuration.KEYBOARD_QWERTY);
        edt.setBackgroundColor(Color.parseColor("#eeeeee"));
        edt.setSingleLine();

        final Button btn = new Button(context);
        btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bar, 0);
        btn.setPadding(10, 10, 10, 10);
        btn.setLayoutParams(medidas(1.5));

        Cgnr = new ControlGnr(context, rt.getId(), tv, edt, btn, "hxbtn_izq");
        controlView = Cgnr.Contenedor(vacio, inicial, rt.getTipo());

        StarCamera(btn);
        funScannerEdit(edt, tv);

        return controlView;
    }

    //medidas para el boton y el campo de busqueda
    public LinearLayout.LayoutParams medidas(double med) {

        LinearLayout.LayoutParams llparams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llparams.weight = (float) med;
        llparams.setMargins(5, 10, 5, 5);

        return llparams;
    }

    //metodo que activa la camara
    public void StarCamera(Button btn) {
        try {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Camera.class);
                    i.putExtra("id", rt.getId().intValue());
                    i.putExtra("ubi", ubicacion);
                    i.putExtra("path", path);
                    i.putExtra("desplegable", rt.getDesplegable());
                    i.putExtra("regla", rt.getReglas());
                    context.startActivity(i);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(context, "Ocurrio un error \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //funcion de la busqueda
    public String Buscar(String data, String desplegable) {
        try {
            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = desplegable;

            for (DesplegableTab desp : iDesp.all()) {
                if (desp.getCodigo().equals(data)) {
                    return desp.getOpcion();
                }
            }
            return "NO DATA SCAN";
        } catch (Exception ex) {
            return "NO DATA SCAN";
        }
    }

    //funcion de detecion de cambio en el cambo e inserta
    public void funScannerEdit(final EditText edt, final TextView tv) {

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!edt.getText().toString().isEmpty()) {

                    if (!rt.getDesplegable().isEmpty()) {
                        String res = Buscar(edt.getText().toString(), rt.getDesplegable());

                            tv.setText(!res.equals("NO DATA SCAN") ? "Resultado : " + res : "Escanea el codigo de barras activando la camara");
                            tv.setTextColor(!res.equals("NO DATA SCAN") ? Color.parseColor("#58d68d") : Color.parseColor("#979A9A"));

                            noDataScan(edt.getText().toString(), res);

                        } else {
                            registro(edt.getText().toString(), null);
                        }

                    } else {
                        registro(null, null);
                    }

                } catch (Exception ex) {
                    Log.i("ECX_REG",""+ex.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //funcion de registro en el tempóral
    public void registro(String rta, String valor) throws Exception {
        iContenedor conTemp = new iContenedor(path);
        conTemp.editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }

    //valida el resultado de la busqueda al momento de escribir en el campo
    public void noDataScan(String rta, String res) throws Exception {
        if (res.equals("NO DATA SCAN")) {
            registro(null, null);
        } else {
            registro(rta, res);
        }
    }
}

