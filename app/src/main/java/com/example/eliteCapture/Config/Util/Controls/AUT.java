package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.Tab.despVariedadesTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class AUT {
    Context context;
    String ubicacion, path;
    RespuestasTab rt;
    boolean vacio, initial;

    containerAdmin ca;
    preguntaPonderado pp;
    textAdmin ta;

    TextView respuestaPonderado;
    LinearLayout contenedorCamp, LineRespuesta;
    AutoCompleteTextView camp;

    iDesplegable iDesp;

    public AUT(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.path = path;
        this.initial = initial;
        this.vacio = rt.getRespuesta() != null;

        ca = new containerAdmin(context);
        pp = new preguntaPonderado(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        iDesp = new iDesplegable(null, path);

        LineRespuesta = (LinearLayout) pp.resultadoFiltro();

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? " Resultado : "+rt.getPonderado() : " Resultado : ");
    }

    public View crear(){
        contenedorCamp = ca.container();
        contenedorCamp.setOrientation(LinearLayout.VERTICAL);
        contenedorCamp.setPadding(10, 0, 10, 0);
        contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

        contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
        contenedorCamp.addView(pintarRespuesta(rt.getRespuesta()));
        contenedorCamp.addView(campo());
        pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

        return contenedorCamp;
    }

    public View campo(){
        LinearLayout.LayoutParams params = ca.params();
        params.setMargins(5,2,5,10);

        camp = (AutoCompleteTextView) pp.campoEdtable("Auto");
        camp.setText((vacio ? rt.getCausa() : ""));
        camp.setAdapter(getAdapter(getDesp()));
        camp.setLayoutParams(params);
        FunAut(camp);

        return camp;
    }

    public List<String> getDesp(){
        try {
            List<String> Loptions = new ArrayList<>();
            iDesp.nombre = rt.getDesplegable();
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
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(context, R.layout.spinner_item_personal, listaCargada);
        return autoArray;
    }

    public void FunAut(final AutoCompleteTextView etdauto) {
        etdauto.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {

                DesplegableTab dt = filtroDesplegable(camp.getText().toString());
                registro(dt != null ? dt.getCodigo() : null, dt != null ? String.valueOf(rt.getPonderado()) : null, dt != null ? dt.getOpcion() + "" : null);
                respuestaPonderado.setText(dt != null ? "Resultado : " + rt.getPonderado() : "Resultado :");
                contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
                pintarRespuesta(dt != null ? dt.getCodigo() : null);
            }
        });
    }

    public void registro(String rta, String valor, String causa) {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), causa, rt.getReglas());
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
        if (causa != null) {
            if (!causa.isEmpty())
                LineRespuesta.addView(ta.textColor(causa, "verde", 15, "l"));
        }
        return LineRespuesta;
    }
}
