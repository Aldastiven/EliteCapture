package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Camera;
import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;

import org.apache.commons.lang3.StringUtils;

public class JSO {
    RespuestasTab rt;
    containerAdmin ca;
    textAdmin ta;
    GIDGET pp;
    Context context;
    LinearLayout contenedorCamp, LineRespuesta;
    TextView respuestaPonderado;
    boolean vacio, initial, pintada = false;

    String ubicacion, path;

    public JSO(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        ca = new containerAdmin(context);
        ta = new textAdmin(context);
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

            return contenedorCamp;
        }catch (Exception e){
            Log.i("ErrorCapm",e.toString());
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }

    public LinearLayout campo(){
        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setWeightSum(3);

        EditText campConteo = (EditText) pp.campoEdtable("Edit", "grisClear");
        campConteo.setLayoutParams(params( rt.getTipo().equals("RSE") ? (float) 0.7 : (float) 1));
        campConteo.setText(vacio ? rt.getValor() : "");
        campConteo.setTextSize(15);
        campConteo.setRawInputType(Configuration.KEYBOARD_QWERTY);

        Button btnSca = getButton("Scanner", 1);
        Button btnNav = getButton("Navegar", 2);

        line.addView(btnSca);
        line.addView(campConteo);
        line.addView(btnNav);
        return line;
    }

    public Button getButton(String d, int id){
        Button btn = (Button) pp.boton(d, "verde");
        btn.setId(id);
        btn.setLayoutParams(params((float) 1));
        btn.setTextSize(15);
        btn.setAllCaps(false);
        function(btn);
        return btn;
    }

    public void function(final Button btn){
        btn.setOnClickListener(v -> {
            switch (btn.getId()){
                case 1 :
                    Toast.makeText(context, "llego escanner", Toast.LENGTH_SHORT).show();
                    BtnStarCamera();
                    break;
                case 2 :
                    Toast.makeText(context, "llego navegacion", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public View pintarRespuesta(String causa, Boolean b) {//PINTA LA RESPUESTA DE BUSQUEDA DEL JSON SI SE REQUIERE

        if (LineRespuesta.getChildCount() > 0 || StringUtils.isEmpty(causa)) LineRespuesta.removeAllViews();

        if (!StringUtils.isEmpty(causa)) {
            LineRespuesta.addView(ta.textColor(causa, "verde", 15, "l"));

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

    public LinearLayout.LayoutParams params(float i){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(2,2,2,2);
        params.weight = i;

        return params;
    }

    public void BtnStarCamera() {//metodo que activa la camara
        try {
            Intent i = new Intent(context, Camera.class);
            i.putExtra("ubicacion", ubicacion);
            i.putExtra("path", path);
            i.putExtra("respuestaTab", rt);
            context.startActivity(i);
        } catch (Exception ex) {
            Toast.makeText(context, "Ocurrio un error \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
