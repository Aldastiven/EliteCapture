
package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.QwertyKeyListener;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class CBE {
    Context context;
    RespuestasTab rt;
    String ubicacion, path, datoDesplegable;
    boolean vacio, initial;

    TextView respuestaPonderado;
    EditText edt;
    containerAdmin ca;
    LinearLayout contenedorCamp, noti;
    GIDGET pp;
    textAdmin ta;

    iDesplegable iDesp;

    public CBE(Context context, String ubicacion, RespuestasTab rt, String path , boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        iDesp = new iDesplegable(null, path);
        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText("Resultado :");

        noti = new LinearLayout(context);
        noti.setOrientation(LinearLayout.VERTICAL);
    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        contenedorCamp = ca.container();
        contenedorCamp.setOrientation(LinearLayout.VERTICAL);
        contenedorCamp.setPadding(10, 0, 10, 0);
        contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

        contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
        contenedorCamp.addView(noti);
        contenedorCamp.addView(camp());
        pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

        return contenedorCamp;
    }

    public View camp(){
        LinearLayout line = ca.container();
        line.setWeightSum(2);
        line.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams llcamp = ca.params();
        llcamp.setMargins(5,0,5,0);
        llcamp.weight = 1;

        LinearLayout.LayoutParams llspn = ca.params2();
        llspn.weight = 1;

        Spinner campSpin = new Spinner(context);
        campSpin.setAdapter(getAdapter(getDesp()));
        campSpin.setSelection((vacio ? getDesp().indexOf(getNameDesp(Integer.parseInt(rt.getValor()))) : 0));
        campSpin.setBackgroundResource(R.drawable.myspinner);
        campSpin.setLayoutParams(llspn);

        FunsDesp(campSpin);

        edt = (EditText) pp.campoEdtable("Edit","grisClear");
        edt.setText("");
        edt.setLayoutParams(llcamp);
        edt.setRawInputType(Configuration.KEYBOARD_QWERTY);
        respuestaPonderado.setText(edt.getText().toString().isEmpty() ? "Resultado : " : "Resultado : "+rt.getPonderado());
        FunCamp(edt);

        line.addView(campSpin);
        line.addView(edt);

        return line;
    }

    //COMPLEMENTOS DEL SPINNER
    public List<String> getDesp(){
        try {
            List<String> Loptions = new ArrayList<>();
            iDesp.nombre = rt.getDesplegable();
            //Loptions.add("Selecciona");
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
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(context, R.layout.items_des , listaCargada);
        return autoArray;
    }

    //FUNCIONES

    public void FunsDesp(final Spinner spn) {//FUNCION DEL CAMPO EDITABLE
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String rta = spn.getItemAtPosition(position).toString();
                    datoDesplegable = !rta.equals("Selecciona") ? filtroDesplegable(rta).getCodigo() : "";
                    if (rta.equals("Selecciona") && !edt.getText().toString().isEmpty()) {
                        edt.setText("");
                        registro(null, filtroDesplegable(rta).getCodigo());
                    } else {
                        registro(edt.getText().toString(), filtroDesplegable(rta).getCodigo());
                    }
                }catch (Exception e){}
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    public void FunCamp( final EditText edt ){//FUNCION DEL CAMPO EDÍTTEXT
        edt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    respuestaPonderado.setText(edt.getText().toString().isEmpty() ? "Resultado : " : "Resultado : "+rt.getPonderado());
                    if (datoDesplegable.isEmpty()) {
                        if (noti.getChildCount() < 1) {
                            noti.addView(ta.textColor("¡Debes seleccionar al menos una opción!", "rojo", 15, "l"));
                            temporizador(5000);
                            registro(null, null);
                        }
                        if(!edt.getText().toString().isEmpty()) {
                            edt.setText("");
                        }
                    } else {
                        String rtaEdt = edt.getText().toString();
                        respuestaPonderado.setText("Resultado : "+rt.getPonderado());
                        registro(!rtaEdt.isEmpty() ? rtaEdt : null, datoDesplegable);
                    }
                }catch (Exception e){}
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

    public String getNameDesp(int idDesp){
        String data = "";
        if(rt.getDesplegable() != null){
            DesplegableTab des = pp.busqueda(idDesp+"");
            if(des != null) {
                data = des.getOpcion();
            }
        }
        return data;
    }

    public void registro(String rta, String valor) {//REGISTRO AL JSON TEMPORAL
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }

    public void temporizador(int duracion){
        if(duracion > 0 ) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    noti.removeAllViews();
                }
            }, duracion);
        }
    }
}
