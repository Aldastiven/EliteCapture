package com.example.eliteCapture.Config.Util.Controls;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
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
import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;

import com.example.eliteCapture.Model.Data.iJsonPlan;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.R;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class JSO {
    RespuestasTab rt;
    containerAdmin ca;
    textAdmin ta;
    GIDGET pp;
    Context context;
    LinearLayout contenedorCamp, LineRespuesta;
    TextView respuestaPonderado;
    boolean vacio, initial, pintada = false;
    Dialog dialog;
    ProgressDialog progress;
    CargarXmlTask c;

    String ubicacion, path;

    iJsonPlan ipl;

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
        ipl = new iJsonPlan(path);

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
                    BtnStarCamera();
                    break;
                case 2 :
                    c = new CargarXmlTask();
                    c.execute();
                    new Handler().postDelayed(() -> {
                        dialog();
                    }, 1000);
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

    public void dialog(){
        try {

        View v;
        try {
            dialog = new Dialog(context, R.style.TransparentDialog);
            LinearLayout linear = ca.container();
            String data = new JsonAdmin().ObtenerLista(path, "siembraJson");
            JSONArray jarr = new JSONArray(data);
            for (int i = 0; i < jarr.length(); i++) {
                parseJson(jarr.getJSONObject(i), linear);
            }
            v = linear;
        } catch (Exception e) {
            Log.i("JSOError", "" + e.toString());
            v = ta.textColor("ErrorJSON : " + e.toString(), "rojo", 15, "c");
        }
        dialog.setContentView(ca.scrollv(v));
        dialog.show();

        c.cancel(true);
        progress.dismiss();
        }catch (Exception e){
            Log.i("JSOError", "" + e.toString());
        }
    }

    private class CargarXmlTask extends AsyncTask<String,Integer,Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setMessage("Cargando datos, espere un momento por favor...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.setCancelable(false);
            progress.show();
        }
    }


    public void parseJson(JSONObject data, LinearLayout container){
        try {
            container.setPadding(10,10,0,10);
            if (data != null) {
                Iterator<String> it = data.keys();

                while (it.hasNext()) {
                    String key = it.next();

                    if (data.get(key) instanceof JSONArray) {
                        //se obtiene un array de objetos
                        TextView v = (TextView) ta.textColor("", "darkGray", 15, "l");
                        container.addView(v);

                        LinearLayout line2 = ca.borderGradient("#D7DBDD");
                        line2.setVisibility(View.GONE);
                        v.setText("{}"+key+(line2.getChildCount() > 0 ? "▼" : "▲"));

                        v.setOnClickListener(x -> {
                            try{
                                if(line2.getVisibility() == View.GONE) {
                                    LinearLayout.LayoutParams p = ca.params();
                                    p.setMargins(25, 0, 0, 0);
                                    line2.setLayoutParams(p);

                                    JSONArray arry = data.getJSONArray(key);
                                    for (int i = 0; i < arry.length(); i++) {
                                        parseJson(arry.getJSONObject(i), line2);
                                    }
                                    line2.setVisibility(View.VISIBLE);
                                }else{
                                    line2.removeAllViews();
                                    line2.setVisibility(View.GONE);
                                }
                            }catch (JsonSyntaxException e){
                            }catch (Exception e){
                             Log.i("datosJson", e.toString());
                            }
                            v.setText("{}"+key+(line2.getChildCount() > 0 ? "▼" : "▲"));
                        });
                        container.addView(line2);
                    }else {
                        String dataJson = key + " : " + data.getString(key);
                        View v = ta.textColor(dataJson, "darkGray", 15, "l");
                        container.addView(v);

                        v.setOnClickListener(x -> {
                            try {
                                Boolean b = false;
                                JSONArray jarr = new JSONArray("["+data+"]");
                                for(int i = 0; i < jarr.length(); i++){
                                    Iterator<String> ij = jarr.getJSONObject(i).keys();
                                    while (ij.hasNext()) {
                                        String keyx = ij.next();
                                        if (jarr.getJSONObject(i).get(keyx) instanceof JSONArray) {
                                            b = true;
                                        }
                                    }
                                }

                                if(!b){
                                    dialogPanel(jarr, data.length());
                                }
                            }catch (Exception e){
                             Log.i("dataJson", ""+e.toString());
                            }
                        });
                    }
                }
            }
        } catch (Exception  e) {
            Log.i("datosJson", e.toString());
        }
    }

    public void dialogPanel(JSONArray jarr, int size){
        try {
            dialog.dismiss();

            LinearLayout linePanel1 = ca.container();
            linePanel1.setPadding(5,0,0,0);

            LinearLayout linePanel2 = ca.container();
            linePanel2.setPadding(0,0,0,0);

            for(int j = 0; j < jarr.length(); j++){
                Iterator<String> ij = jarr.getJSONObject(j).keys();
                int i = 0;
                while (ij.hasNext()) {
                    String keyx = ij.next();
                    Object data = jarr.getJSONObject(j).get(keyx);
                    if(i < (size/2)){
                        linePanel1.addView(ta.text(keyx +" : ", 14, data.toString(), 14));
                    }else {
                        linePanel2.addView(ta.text(keyx +" : ", 14, data.toString(), 14));
                    }
                    i++;
                }
            }

            LinearLayout line = new LinearLayout(context);
            line.setLayoutParams(ca.params3());
            line.setOrientation(LinearLayout.HORIZONTAL);

            line.addView(linePanel1);
            line.addView(linePanel2);

            dialog.setContentView(ca.scrollv(line));
            dialog.show();
        }catch (Exception e){
            Log.i("dataJson", "Error : "+e.toString());
        }
    }
}
