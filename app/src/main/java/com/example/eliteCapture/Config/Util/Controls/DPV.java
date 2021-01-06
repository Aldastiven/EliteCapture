package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.despVariedadesTab;
import com.example.eliteCapture.Model.Data.idespVariedades;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class DPV {
    Context context;
    String ubicacion, path, producto;
    RespuestasTab rt;
    boolean vacio, initial;

    containerAdmin ca;
    GIDGET pp;
    textAdmin ta;

    idespVariedades idv;
    iContenedor icont;

    TextView respuestaPonderado;
    LinearLayout contenedorCamp, contenedorCampos, lineVariedad;
    AutoCompleteTextView camp1;

    int idDPV;

    public DPV(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.path = path;
        this.initial = initial;
        this.vacio = rt.getRespuesta() != null;

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        idv = new idespVariedades(path, null);
        icont = new iContenedor(path);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? " Resultado : "+rt.getPonderado() : " Resultado : ");

    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        try {
            contenedorCamp = ca.container();
            contenedorCamp.setOrientation(LinearLayout.VERTICAL);
            contenedorCamp.setPadding(10, 0, 10, 0);
            contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

            contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
            contenedorCamp.addView(campo2());
            pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

            return contenedorCamp;
        }catch (Exception e){
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }


    public View campo2(){
        try {
            lineVariedad = new LinearLayout(context);
            lineVariedad.setOrientation(LinearLayout.VERTICAL);
            lineVariedad.setPadding(0,0,0,10);

            AutoCompleteTextView camp2 = (AutoCompleteTextView) pp.campoEdtable("Auto", "grisClear");
            camp2.setText((vacio ? rt.getRespuesta() : ""));
            FunAut(camp2, 2);

            lineVariedad.addView(camp2);
            return lineVariedad;
        }catch (Exception e){
            Toast.makeText(context, "ERROR : "+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public ArrayAdapter<String> getAdapter(List<String> listaCargada){
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(context, R.layout.items_aut, listaCargada);
        return autoArray;
    }

    public List<String> getVariedad(){// ITEMS VARIEDAD
        try {
            List<String> Lvariedad = new ArrayList<>();
            List<ContenedorTab> Ltemporal = new ArrayList<>();
            Ltemporal.add(icont.optenerTemporal());


            for(ContenedorTab cont : Ltemporal){
                for(RespuestasTab resH : cont.getHeader()){
                    if(resH.getCodigo() == rt.getReglas()){
                        idDPV = Integer.parseInt(resH.getCausa());
                        break;
                    }
                }

                for(RespuestasTab resH : cont.getQuestions()){
                    if(resH.getCodigo() == rt.getReglas()){
                        idDPV = Integer.parseInt(resH.getCausa());
                        break;
                    }
                }
            }

            for (despVariedadesTab dt : idv.all(rt.getDesplegable())) {
                if(dt.getIdProducto() == idDPV){
                    for(despVariedadesTab.variedades variedades : dt.getVariedades()){
                        Lvariedad.add(variedades.getVariedad());
                    }
                    break;
                }
            }

            return Lvariedad;
        }catch (Exception e){
            Log.i("ERORPRODUCTO",e.toString());
            return null;
        }
    }

    //BUSQUEDA DEL PRODUCTO
    public String Buscar(String data) {
        try {
            String producto = "";
            for (despVariedadesTab desp : idv.all(rt.getDesplegable())) {
                if (desp.getProducto().equals(data)) {
                    producto = desp.getProducto();
                    break;
                }
            }
            return producto;
        }catch (Exception ex){
            Toast.makeText(context, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    //BUSQUEDA DEL VARIEDAD
    public despVariedadesTab.variedades BuscarVariedad(String data) {
        try {
            despVariedadesTab.variedades variedad = null;
            for (despVariedadesTab dt : idv.all(rt.getDesplegable())) {
                Log.i("BUSCADOR","idPadre : "+idDPV);
                if(dt.getIdProducto() == idDPV){
                    for(despVariedadesTab.variedades variedades : dt.getVariedades()){
                        if (variedades.getVariedad().equals(data)) {
                            variedad = variedades;
                            break;
                        }
                    }
                    break;
                }
            }
            return variedad;
        }catch (Exception ex){
            return null;
        }
    }

    //FUNCION DEL SPINNER DEL PRODUCTO
    public void FunAut(final AutoCompleteTextView etdauto, final int tipo) {
        etdauto.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    etdauto.setAdapter(getAdapter(getVariedad()));

                    despVariedadesTab.variedades variedad = BuscarVariedad(etdauto.getText().toString());
                    if(variedad != null) {
                        String var = variedad.getVariedad();
                        String resPond = var.isEmpty() ? "Resultado: " : "Resultado: " + rt.getPonderado();
                        respuestaPonderado.setText(resPond);
                        registro(var.isEmpty() ? null : var, var.isEmpty() ? null : String.valueOf(variedad.getIdVariedad()),var.isEmpty() ? null : producto);
                        contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
                    }else {
                        respuestaPonderado.setText("Resultado: ");
                        registro(null, null, null);
                    }
                } catch (Exception ex) {
                    Toast.makeText(context, "" + ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    public void registro(String rta, String valor, String causa)  {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), causa, rt.getReglas());
    }
}
