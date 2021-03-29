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
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JSO {
    RespuestasTab rt;
    containerAdmin ca;
    textAdmin ta;
    GIDGET pp;
    Context context;
    LinearLayout contenedorCamp, LineRespuesta;
    TextView respuestaPonderado;
    EditText campConteo;
    boolean vacio, initial, pintada = false, dialogPanel;
    Dialog dialog;
    ProgressDialog progress;
    CargarXmlTask c;

    String ubicacion, path;

    iJsonPlan ipl;

    public JSO(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial, boolean dialogPanel) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;
        this.dialogPanel = dialogPanel;

        ca = new containerAdmin(context);
        ta = new textAdmin(context);
        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);
        ipl = new iJsonPlan(path);
        progress = new ProgressDialog(context);
        c = new CargarXmlTask("Consultando datos, espere un momento...");

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

            dialog = new Dialog(context, R.style.MyProgressDialogNormal);

            if(dialogPanel){
                if(!progress.isShowing()){
                    c.execute();
                }
                new Handler().postDelayed(() -> {
                    try {
                        String data = new JsonAdmin().ObtenerLista(path, "siembraJson");
                        if(data.contains(campConteo.getText().toString())) {
                            JSONArray jarr = new JSONArray(data);
                            for (int i = 0; i < jarr.length(); i++) {
                                if (filterParseJson(jarr.getJSONObject(i), true)) {
                                    break;
                                }
                            }
                            c.cancel(true);
                            progress.dismiss();
                        }else{
                            Toast.makeText(context, "no se encontro el dato relacionado", Toast.LENGTH_SHORT).show();
                            campConteo.setText("");
                            c.cancel(true);
                            progress.dismiss();
                        }

                    }catch (Exception e){
                        Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, 100);
            }

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

        campConteo = (EditText) pp.campoEdtable("Edit", "grisClear");
        campConteo.setLayoutParams(params( rt.getTipo().equals("RSE") ? (float) 0.7 : (float) 1));
        campConteo.setText(vacio ? rt.getValor() : "");
        campConteo.setTextSize(15);
        campConteo.setRawInputType(Configuration.KEYBOARD_QWERTY);

        Button btnSca = getButton("Scanner", 1);
        Button btnNav = getButton("Navegar", 2);

        line.addView(btnSca);
        line.addView(campConteo);
        line.addView(btnNav);

        funCamp();
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
                    c = new CargarXmlTask("Cargando datos, espere un momento por favor...");
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
            LinearLayout linear = ca.container();
            String data = new JsonAdmin().ObtenerLista(path, "planJSON");
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

    private class CargarXmlTask extends AsyncTask<String, Void, Void> {

        String dataText;

        public CargarXmlTask(String dataText) {
            this.dataText = dataText;
        }

        @Override
        protected Void doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {

            //"Cargando datos, espere un momento por favor..."
            progress.setMessage(dataText);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        protected void onProgressUpdate (String... strings) {
            Log.i("AsyncClass", strings[0]);
            progress.setMessage(strings[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
        }
    }

    public void parseJson(JSONObject data, LinearLayout container){
        try {
            container.setPadding(10,10,0,10);
            if (data != null) {
                Iterator<String> it = data.keys();

                int paso = 0;

                LinearLayout lineCamp = ca.card(ca.container(), "#EAEDED");
                lineCamp.setOrientation(LinearLayout.VERTICAL);
                lineCamp.removeAllViews();

                while (it.hasNext()) {
                    String key = it.next();
                    LinearLayout line2 = ca.borderGradient("#D7DBDD");

                    if (data.get(key) instanceof JSONArray) {
                        //se obtiene un array de objetos
                        TextView v = (TextView) ta.textColor("", "darkGray", 15, "l");
                        lineCamp.addView(v);

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
                    }else {

                        Boolean b2 = false;
                        JSONArray jarr2 = new JSONArray("["+data+"]");
                        for(int i = 0; i < jarr2.length(); i++){
                            Iterator<String> ij = jarr2.getJSONObject(i).keys();
                            while (ij.hasNext()) {
                                String keyx = ij.next();
                                if (jarr2.getJSONObject(i).get(keyx) instanceof JSONArray) {
                                    b2 = true;
                                    break;
                                }
                            }
                        }

                        View v = null;
                        String dataJson = key + " : " + data.getString(key);
                        if(!b2){
                            if(key.equals("Header")) {
                                v = ca.card(ta.textColor(dataJson.split(":")[1], "darkGray", 15, "c"), "#58D68D");
                                container.addView(v);
                            }
                        }else{
                            v = ta.textColor(dataJson, "darkGray", 15, "l");

                            lineCamp.addView(v);
                        }

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
                                            break;
                                        }
                                    }
                                }

                                if(!b){
                                    dialogPanel(jarr);
                                }else{
                                    if(line2.getVisibility() == View.GONE) {
                                        line2.setVisibility(View.VISIBLE);
                                    }else{
                                        line2.removeAllViews();
                                        line2.setVisibility(View.GONE);
                                    }
                                }
                            }catch (Exception e){
                             Log.i("dataJson", ""+e.toString());
                            }
                        });
                    }
                    if(lineCamp.getChildCount() == 3) {
                        container.addView(lineCamp);
                        container.addView(line2);
                    }
                }
                c.onProgressUpdate("cargo finca");
            }
        } catch (Exception  e) {
            Log.i("datosJson", e.toString());
        }
    }

    @SuppressLint("ResourceType")
    public void dialogPanel(JSONArray jarr){
        try {
            if(dialog.isShowing()){
                dialog.dismiss();
            }

            LinearLayout linePanel1 = ca.container();
            linePanel1.setPadding(5,0,0,0);

            LinearLayout linePanel2 = ca.container();
            linePanel2.setPadding(0,0,0,0);

            String dataInfo = "";

             for(int j = 0; j < jarr.length(); j++){
                Iterator<String> ij = jarr.getJSONObject(j).keys();
                int i = 0;
                while (ij.hasNext()) {
                    String keyx = ij.next();
                    Object data = jarr.getJSONObject(j).get(keyx);
                    if(!keyx.equals("Header")) {
                        if(i == 1){
                            dataInfo = data.toString();
                        }
                        if (!(i % 2 == 0)) {
                            linePanel1.addView(ta.text(keyx + " : ", 14, data.toString(), 14));
                        } else {
                            linePanel2.addView(ta.text(keyx + " : ", 14, data.toString(), 14));
                        }
                    }
                    i++;
                }
            }

            LinearLayout linePrincipal = new LinearLayout(context);
            linePrincipal.setLayoutParams(ca.params3());
            linePrincipal.setOrientation(LinearLayout.VERTICAL);

            LinearLayout line = new LinearLayout(context);
            line.setLayoutParams(ca.params3());
            line.setOrientation(LinearLayout.HORIZONTAL);


            line.addView(linePanel1);
            line.addView(linePanel2);


            LinearLayout lineFoot = ca.container();
            lineFoot.setLayoutParams(ca.params2());
            lineFoot.setOrientation(LinearLayout.HORIZONTAL);
            lineFoot.setWeightSum(2);

            LinearLayout.LayoutParams params = ca.params2();
            params.weight = 1;

            Button btnCancelar = (Button) pp.boton("Cancelar", "gris");
            btnCancelar.setId(1);
            btnCancelar.setLayoutParams(params);

            Button btnAceptar = (Button) pp.boton("Aceptar", "verde");
            btnAceptar.setId(0);
            btnAceptar.setLayoutParams(params);

            funModalInfo(btnCancelar, "");
            funModalInfo(btnAceptar, dataInfo);

            lineFoot.addView(btnCancelar);
            lineFoot.addView(btnAceptar);

            linePrincipal.addView(line);
            linePrincipal.addView(lineFoot);

            dialog.setContentView(ca.scrollv(linePrincipal));
            dialog.show();
        }catch (Exception e){
            Log.i("dataJson", "Error : "+e.toString());
        }
    }

    public void funModalInfo(Button btn, String data){
        btn.setOnClickListener(v -> {
            switch (btn.getId()){
                case 0 :
                    campConteo.setText(data);
                    dialog.dismiss();
                    break;
                case 1 :
                    campConteo.setText("");
                    dialog.dismiss();
                    break;
            }
        });
    }

    public boolean filterParseJson(JSONObject data, boolean buscar){
        Boolean b = false;
        try {
            String dataCama = "no encontro";
            if (data != null && buscar) {
                Iterator<String> it = data.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    if (data.get(key) instanceof JSONArray && !b) {
                        JSONArray arry = data.getJSONArray(key);
                        for (int i = 0; i < arry.length(); i++) {
                            b = filterParseJson(arry.getJSONObject(i), true);
                            if(b) {
                                Log.i("JSO", "Encontro efectivamente");
                                b = true;
                                break;
                            }
                        }
                    }else {

                        JSONArray jarr = new JSONArray("["+data+"]");
                        for(int i = 0; i < jarr.length(); i++){
                            Iterator<String> ij = jarr.getJSONObject(i).keys();
                            while (ij.hasNext()) {
                                String keyx = ij.next();
                                if(jarr.getJSONObject(i).get(keyx).toString().equals(campConteo.getText().toString())){
                                    //AQUI ENCUENTRA EL RESULTADO
                                    dataCama = jarr.getJSONObject(i).get(keyx).toString();

                                    dialogPanel(jarr);

                                    Log.i("JSONDATA","encontro la cama : "+dataCama);
                                    b = true;
                                    break;
                                }
                            }
                        }
                    }

                    if(b){
                        filterParseJson(null, false);
                        break;
                    }
                }
            }else{
                Log.i("JSONDATA","termino el proceso de busqueda : "+dataCama);
            }
        } catch (Exception  e) {
            Log.i("datosJson", e.toString());
        }
        return b;
    }

    public void funCamp(){//FUNCION DEL CAMPO
        campConteo.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                if (!hasFocus) {
                    registro(campConteo.getText().toString(), campConteo.getText().toString() != null ? rt.getPonderado() + "" : "");
                }
            } catch (Exception e) {
                Toast.makeText(context, "Funcamp : " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registro(String rta, String valor) {//REGISTRO
        Log.i("registro", "respuesta : " + rta);
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }
}
