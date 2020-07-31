package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Camera;
import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

public class SCA {

    Context context;
    RespuestasTab rt;
    String ubicacion, path;
    boolean vacio, initial;

    EditText camp;
    TextView respuestaPonderado;
    LinearLayout contenedorCamp;
    containerAdmin ca;
    preguntaPonderado pp;

    public SCA(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        ca = new containerAdmin(context);
        pp = new preguntaPonderado(context, ubicacion, rt);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : "+rt.getPonderado() : "Resultado :");
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

    public View campo(){
        camp = (EditText) pp.campoEdtable("Edit");
        camp.setText((vacio ? rt.getRespuesta() : ""));
        camp.setLayoutParams(params((float) 0.5));

        final Button btn = new Button(context);
        btn.setBackgroundColor(Color.parseColor("#2ECC71"));
        btn.setText("scanner");
        btn.setTextColor(Color.WHITE);
        btn.setAllCaps(false);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setPadding(10, 10, 10, 10);
        btn.setLayoutParams(params((float) 1.5));

        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.addView(camp);
        line.addView(btn);

        StarCamera(btn);
        funCamp();

        return line;
    }

    public LinearLayout.LayoutParams params(float med) {

        LinearLayout.LayoutParams params = ca.params2();
        params.weight = med;
        params.setMargins(5, 0, 5, 0);

        return params;
    }

    public void StarCamera(Button btn) {//metodo que activa la camara
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

    public void funCamp(){//FUNCION DEL CAMPO
        camp.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String rta = camp.getText().toString();
                registro(!rta.isEmpty() ? rta : null, !rta.isEmpty() ? rt.getPonderado()+"" : null);
                respuestaPonderado.setText(!rta.isEmpty() ? "Resultado : "+rt.getPonderado() : "Resultado :");
                contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
            }
        });
    }

    public void registro(String rta, String valor) {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), null, rt.getReglas());
    }
}
