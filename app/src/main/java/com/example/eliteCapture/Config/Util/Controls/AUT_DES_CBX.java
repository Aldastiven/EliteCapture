package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class AUT_DES_CBX {
    Context context;
    String ubicacion, path;
    RespuestasTab rt;
    boolean vacio, initial;
    SharedPreferences sp;

    containerAdmin ca;
    GIDGET pp;
    textAdmin ta;

    TextView respuestaPonderado;
    LinearLayout contenedorcampAut, LineRespuesta;
    AutoCompleteTextView campAut;
    Spinner campSpin;

    iDesplegable iDesp;

    public AUT_DES_CBX(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.path = path;
        this.initial = initial;
        this.vacio = rt.getRespuesta() != null;

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        sp = context.getSharedPreferences("share", context.MODE_PRIVATE);

        iDesp = new iDesplegable(null, path);

        LineRespuesta = (LinearLayout) pp.resultadoFiltro();

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? " Resultado : "+rt.getPonderado() : " Resultado : ");
    }

    public View crear(){
        try {
            contenedorcampAut = ca.container();
            contenedorcampAut.setOrientation(LinearLayout.VERTICAL);
            contenedorcampAut.setPadding(10, 0, 10, 5);
            contenedorcampAut.setGravity(Gravity.CENTER_HORIZONTAL);

            contenedorcampAut.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
            contenedorcampAut.addView(pintarRespuesta(rt.getCausa() != null ? rt.getCausa() : ""));
            contenedorcampAut.addView(camp());
            pp.validarColorContainer(contenedorcampAut, vacio, initial);//pinta el contenedor del item si esta vacio o no

            return contenedorcampAut;
        }catch (Exception e){
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }

    public View camp(){
        try {
            LinearLayout.LayoutParams params = ca.params();
            params.setMargins(5, 2, 5, 5);

            String dataCamp;

            if (vacio) {
                dataCamp = rt.getRespuesta();
            } else {
                dataCamp = getFinca().isEmpty() ? "" : getFinca();
            }

            View v = null;
            switch (rt.getTipo()) {
                case "AUT":
                case "AUN":
                    campAut = (AutoCompleteTextView) pp.campoEdtable("Auto", "grisClear");
                    campAut.setText(dataCamp);
                    campAut.setAdapter(getAdapter(getDesp()));
                    campAut.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    if (rt.getTipo().equals("AUN"))
                        campAut.setRawInputType(Configuration.KEYBOARD_QWERTY);
                    FunAut(campAut);
                    v = campAut;
                    break;
                case "DES":
                case "CBX":

                    campSpin = new Spinner(context);
                    campSpin.setAdapter(getAdapter(getDesp()));
                    campSpin.setSelection(getDesp().indexOf(dataCamp));
                    campSpin.setBackgroundResource(R.drawable.myspinner);

                    if (rt.getId() == 0 && getFinca() != null) {
                        String opcion = filtroDesplegable(getFinca()).getOpcion();
                        campSpin.setSelection(getDesp().indexOf(opcion != null ? opcion : dataCamp));
                    } else {
                        campSpin.setSelection(getDesp().indexOf(dataCamp));
                    }

                    FunsDesp(campSpin);
                    v = campSpin;
                    break;
            }
            v.setLayoutParams(params);
            return v;
        }catch (Exception e){
            Log.i("ERRORCBX", e.toString());
            return null;
        }
    }

    public List<String> getDesp(){
        try {
            List<String> Loptions = new ArrayList<>();
            iDesp.nombre = rt.getDesplegable();
            if(rt.getTipo().equals("DES") || rt.getTipo().equals("CBX")) Loptions.add("Selecciona");
            for (DesplegableTab desp : iDesp.all()) {
                Loptions.add(desp.getOpcion());
            }
            return Loptions;
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public ArrayAdapter<String> getAdapter(List<String> listaCargada){
        int resource = rt.getTipo().equals("DES") || rt.getTipo().equals("CBX") ? R.layout.items_des : R.layout.items_aut;
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(context,resource , listaCargada);
        return autoArray;
    }

    public void FunAut(final AutoCompleteTextView etdauto) {
        etdauto.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {

                DesplegableTab dt = filtroDesplegable(campAut.getText().toString());
                registro(dt != null ? dt.getOpcion() + "" : null, dt != null ? dt.getCodigo() : null, dt != null ? String.valueOf(rt.getPonderado()) : null);
                respuestaPonderado.setText(dt != null ? "Resultado : " + rt.getPonderado() : "Resultado :");
                contenedorcampAut.setBackgroundResource(R.drawable.bordercontainer);
                pintarRespuesta(dt != null ? dt.getCodigo() : null);
            }
        });
    }

    public void FunsDesp(final Spinner spn) {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                DesplegableTab dt = filtroDesplegable(spn.getItemAtPosition(position).toString());
                registro(dt != null ? dt.getOpcion() + "" : null, dt != null ? dt.getCodigo() : null, dt != null ? String.valueOf(rt.getPonderado()) : null);
                respuestaPonderado.setText(dt != null ? "Resultado : " + rt.getPonderado() : "Resultado :");
                pintarRespuesta(dt != null ? dt.getCodigo() : null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void registro(String rta, String valor, String causa) {//REGISTRO
     new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, causa, rt.getReglas());
    }

    public DesplegableTab  filtroDesplegable(String rta){
        DesplegableTab dt = null;
        if(rt.getDesplegable() != null){
            DesplegableTab des = pp.busqueda(rta);
            if(des != null) {
                dt = des;
            }
        }
        return dt;
    }

    public View pintarRespuesta(String causa){//PINTA LA RESPUESTA DE BUSQUEDA DEL JSON SI SE REQUIERE
        if (LineRespuesta.getChildCount() > 0 || causa == null) LineRespuesta.removeAllViews();
        if (causa != null && rt.getDesplegable() != null) {
            if (!causa.isEmpty())
                LineRespuesta.addView(ta.textColor(causa, "verde", 15, "l"));
                contenedorcampAut.setBackgroundResource(causa != null ? R.drawable.bordercontainer : R.drawable.bordercontainerred);
        }

        return LineRespuesta;
    }

    public String getFinca(){
        String finca = null;
        if(sp != null) {
            finca = sp.getString("fincaAsignada", "");
            Log.i("fincaAsignada", finca);
        }
        return finca;
    }
}
