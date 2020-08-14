package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class CBE {
    Context context;
    String ubicacion, path;
    RespuestasTab rt;
    boolean vacio, initial;
    String idDespUsu;

    EditText edt;

    containerAdmin ca;
    GIDGET pp;
    textAdmin ta;

    TextView respuestaPonderado;
    LinearLayout contenedorcampAut, LineRespuesta;
    AutoCompleteTextView campAut;
    Spinner campSpin;

    iDesplegable iDesp;

    public CBE(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.path = path;
        this.initial = initial;
        this.vacio = rt.getRespuesta() != null;

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        iDesp = new iDesplegable(null, path);

        LineRespuesta = (LinearLayout) pp.resultadoFiltro();

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? " Resultado : "+rt.getPonderado() : " Resultado : ");
    }

    public View crear(){
        contenedorcampAut = ca.container();
        contenedorcampAut.setOrientation(LinearLayout.VERTICAL);
        contenedorcampAut.setPadding(10, 0, 10, 5);
        contenedorcampAut.setGravity(Gravity.CENTER_HORIZONTAL);

        contenedorcampAut.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
        //contenedorcampAut.addView(pintarRespuesta(rt.getRespuesta()));
        contenedorcampAut.addView(camp());
        pp.validarColorContainer(contenedorcampAut, vacio, initial);//pinta el contenedor del item si esta vacio o no

        return contenedorcampAut;
    }

    public View pintarRespuesta(String causa){//PINTA LA RESPUESTA DE BUSQUEDA DEL JSON SI SE REQUIERE
        if (LineRespuesta.getChildCount() > 0) LineRespuesta.removeAllViews();
        if (causa != null) {
            if (!causa.isEmpty())
                LineRespuesta.addView(ta.textColor(causa, "rojo", 15, "l"));
            contenedorcampAut.setBackgroundResource(causa != null ? R.drawable.bordercontainer : R.drawable.bordercontainerred);
        }

        return LineRespuesta;
    }

    public View camp(){
        LinearLayout line = ca.container();
        line.setWeightSum(2);
        line.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams ll = ca.params();
        ll.setMargins(5,0,5,0);
        ll.weight = 1;

        campSpin = new Spinner(context);
        campSpin.setAdapter(getAdapter(getDesp()));
        campSpin.setSelection((vacio ? getDesp().indexOf(rt.getCausa()) : 0));
        campSpin.setBackgroundResource(R.drawable.myspinner);
        campSpin.setLayoutParams(ll);

        FunsDesp(campSpin);

        edt = (EditText) pp.campoEdtable("Edit","grisClear");
        edt.setText((vacio ? rt.getValor() : ""));
        edt.setLayoutParams(ll);
        funCamp();

        line.addView(campSpin);
        line.addView(edt);

        return line;
    }

    public List<String> getDesp(){
        try {
            List<String> Loptions = new ArrayList<>();
            iDesp.nombre = rt.getDesplegable();
            Loptions.add("Selecciona");
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
        int resource =  R.layout.items_des;
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(context,resource , listaCargada);
        return autoArray;
    }

    //FUNCIONES

    public void funCamp(){//FUNCION DEL CAMPO
        edt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(idDespUsu.isEmpty()){
                    if(!edt.getText().toString().isEmpty()) edt.setText("");
                }

                respuestaPonderado.setText(!edt.getText().toString().isEmpty() ? "Resultado : "+rt.getPonderado() : "Resultado :");
            }
        });
    }

    public void FunsDesp(final Spinner spn) {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DesplegableTab dt = filtroDesplegable(spn.getItemAtPosition(position).toString());
                if(dt != null || !filtroDesplegable(spn.getItemAtPosition(position).toString()).equals("Selecciona")){
                    idDespUsu = dt.getCodigo();
                }else {
                    idDespUsu = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

}
