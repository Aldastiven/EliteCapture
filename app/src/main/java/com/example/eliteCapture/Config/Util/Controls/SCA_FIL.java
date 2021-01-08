package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Camera;
import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.io.Serializable;

public class SCA_FIL implements Serializable {

    Context context;
    RespuestasTab rt;
    String ubicacion, path, causa = null, rta = null;
    boolean vacio, initial, pintada = false;

    EditText camp;
    TextView respuestaPonderado;
    LinearLayout contenedorCamp, LineRespuesta;
    containerAdmin ca;
    GIDGET pp;
    textAdmin ta;

    SharedPreferences sp;

    public SCA_FIL(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        sp = context.getSharedPreferences("shareResultados", context.MODE_PRIVATE);

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        LineRespuesta = (LinearLayout) pp.resultadoFiltro();

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : "+rt.getPonderado() : "Resultado :");
    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        try {
            contenedorCamp = ca.container();
            contenedorCamp.setOrientation(LinearLayout.VERTICAL);
            contenedorCamp.setPadding(10, 0, 10, 0);
            contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

            contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado4
            contenedorCamp.addView(pintarRespuesta(rt.getRespuesta(), false));
            contenedorCamp.addView(campo());
            pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

            sp.edit().clear().apply();

            return contenedorCamp;
        }catch (Exception e){
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }

    public View pintarRespuesta(String causa, Boolean b) {//PINTA LA RESPUESTA DE BUSQUEDA DEL JSON SI SE REQUIERE

        if (LineRespuesta.getChildCount() > 0 || causa == null) LineRespuesta.removeAllViews();

        if (causa != null) {
            if (!causa.equals("sin desplegable")) {
                LineRespuesta.addView(ta.textColor(causa, "verde", 15, "l"));
            }
        } else {
            switch (rt.getTipo()) {
                case "SCN":
                case "SCA":
                    if (b) {
                        LineRespuesta.addView(ta.textColor("No se encontro resultados", "gris", 15, "l"));
                        pintada = true;
                    }
                    break;
            }
        }
        return LineRespuesta;
    }

    public View campo(){
        String res = rt.getValor()+"causa";

        camp = (EditText) pp.campoEdtable("Edit", "grisClear");
        camp.setText(!res.equals("causanull") ? rt.getValor() : "");
        camp.setLayoutParams(params((float) 0.5));
        if(rt.getTipo().equals("SCN") || rt.getTipo().equals("FIN")) camp.setRawInputType(Configuration.KEYBOARD_QWERTY);

        Button btn = btnTipo();
        btn.setLayoutParams(params((float) 1.5));

        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.addView(camp);
        line.addView(btn);

        funCamp();
        return line;
    }

    public Button btnTipo(){
        Button btn = null;
        switch (rt.getTipo()){
            case "SCA" :
            case "SCN" :
                btn = (Button) pp.boton("Escanear", "verde");
                BtnStarCamera(btn);
                break;
            case "FIL" :
            case "FIN" :
                btn = (Button) pp.boton("Buscar", "verde");
                BtnBuscar(btn);
                break;
        }
        return btn;
    }

    public LinearLayout.LayoutParams params(float med) {
        LinearLayout.LayoutParams params = ca.params2();
        params.weight = med;
        return params;
    }

    public void BtnStarCamera(Button btn) {//metodo que activa la camara
        try {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Camera.class);
                    i.putExtra("ubicacion", ubicacion);
                    i.putExtra("path", path);
                    i.putExtra("respuestaTab", rt);
                    context.startActivity(i);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(context, "Ocurrio un error \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void BtnBuscar(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valida = filtroDesplegable(camp.getText().toString());
                if (valida != null) {
                    if (LineRespuesta.getChildCount() > 0 || causa == null)
                        LineRespuesta.removeAllViews();
                    LineRespuesta.addView(ta.textColor("No se encontraron resultados", "rojo", 15, "l"));
                }
            }
        });
    }

    public void funCamp(){//FUNCION DEL CAMPO
        camp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Boolean b = rt.getDesplegable() != null;
                    rta = b ? filtroDesplegable(camp.getText().toString()) : camp.getText().toString();
                    respuestaPonderado.setText(rta != null ? "Resultado : " + rt.getPonderado() : "Resultado :");
                    contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
                    registro(rta, causa, rta != null ? rt.getPonderado() + "" : null);
                    pintarRespuesta(causa, b);
                } catch (Exception e) {
                    Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public String filtroDesplegable(String rta) {
        String data = "";
        causa = null;
        if (rt.getDesplegable() != null) {
            DesplegableTab des = pp.busqueda(rta);
            if (des != null) {
                causa = des.getOpcion();
                data = des.getCodigo();
            }
        }
        return data;
    }


    public void registro(String rta, String valor, String causa) {//REGISTRO
        Log.i("registro", "respuesta sca : " + valor);
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), valor == null ? "sin desplegable" : valor, rta, causa, rt.getReglas());
    }
}