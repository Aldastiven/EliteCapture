package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.content.Intent;
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
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

public class Cscanner {

    private Context context;
    private String path;
    private Long id;
    private String contenido;
    private String ubicacion;
    private RespuestasTab r;
    private String camera;

    int ID;

    View controlView;
    ControlGnr Cgnr = null;

    public Cscanner(Context context, String path, Long id, String contenido, String ubicacion, RespuestasTab r, String camera) {
        this.context = context;
        this.path = path;
        this.id = id;
        this.contenido = contenido;
        this.ubicacion = ubicacion;
        this.r = r;
        this.camera = camera;
    }

    //metodo que crea dinamicamente el contol scanner
    public View scanner() {

        final TextView tv = new TextView(context);
        tv.setId(id.intValue());
        tv.setText("Escanea el codigo de barras activando la camara:");
        tv.setTextColor(Color.parseColor("#979A9A"));
        tv.setPadding(5, 15, 5, 5);
        tv.setBackgroundColor(Color.parseColor("#ffffff"));
        tv.setTypeface(null, Typeface.BOLD);

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
        controlView = Cgnr.Contenedor();

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
                    context.startActivity(i);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(context, "Ocurrio un error \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}

