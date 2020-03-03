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
    String path;
    private Long id;
    private String contenido;
    private String ubicacion;
    private RespuestasTab r;
    private String camera;
    private Boolean vacio;
    private Boolean inicial;
    private SharedPreferences sp;

    TextView tv;

    int ID;
    String dataCapture ;

    View controlView;
    ControlGnr Cgnr = null;


    public Cscanner(Context context, String path, Long id, String contenido, String ubicacion, RespuestasTab r, String camera, Boolean vacio, Boolean inicial, SharedPreferences sp) {
        this.context = context;
        this.path = path;
        this.id = id;
        this.contenido = contenido;
        this.ubicacion = ubicacion;
        this.r = r;
        this.camera = camera;
        this.vacio = vacio;
        this.inicial = inicial;
        this.sp = sp;
    }

    public Cscanner(String path) {
        this.path = path;
    }

    //metodo que crea dinamicamente el contol scanner
    public View scanner() {

        tv = new TextView(context);
        tv.setId(id.intValue());
        tv.setPadding(5, 5, 5, 5);
        tv.setTypeface(null, Typeface.BOLD);

        if(r.getValor() != null){
            if(r.getValor().equals("NO DATA SCAN")){
                tv.setText("Escanea el codigo de barras activando la camara:");
                tv.setTextColor(Color.parseColor("#979A9A"));
            }else{
                tv.setText("Resultado : "+r.getValor());
                tv.setTextColor(Color.parseColor("#58d68d"));
            }
        }else{
            tv.setText("Escanea el codigo de barras activando la camara:");
            tv.setTextColor(Color.parseColor("#979A9A"));
        }

        final EditText edt = new EditText(context);
        edt.setHint("" + contenido);
        edt.setText((!camera.isEmpty() ? camera : r.getRespuesta()));
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

        ID = id.intValue();

        Cgnr = new ControlGnr(context, id, tv, edt, btn, "hxbtn_izq");
        controlView = Cgnr.Contenedor(vacio,inicial);

        StarCamera(btn);

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
                    i.putExtra("id", ID);
                    i.putExtra("ubi", ubicacion);
                    i.putExtra("path", path);
                    i.putExtra("desplegable", r.getDesplegable());
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
            return "" ;
        }
    }

}

