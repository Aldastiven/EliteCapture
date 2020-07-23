package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.Tab.despVariedadesTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.Data.idespVariedades;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CVariedadJson {

    RespuestasTab rt;
    Context c;
    String path, ubicacion, nombre = "VariedadJson", producto;
    ControlGnr Cgnr;
    boolean incial, vacio;
    int id;

    idespVariedades idv;

    TextView tvpor;
    LinearLayout lineVariedad;

    public CVariedadJson(Context c, String path, boolean incial, RespuestasTab rt, String ubicacion) {
        this.c = c;
        this.path = path;
        this.incial = incial;
        this.id = rt.getId().intValue();
        this.vacio = rt.getRespuesta() != null;
        this.ubicacion = ubicacion;
        this.rt = rt;

        idv = new idespVariedades(path, null);
    }

    public View Cvariedad(){
        LinearLayout line = new LinearLayout(c);
        line.setOrientation(LinearLayout.VERTICAL);

        line.addView(crear().Contenedor(vacio, incial, rt.getTipo()));

        return line;
    }

    public ControlGnr crear(){
        LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 2.3;
        String pregunta = ubicacion.equals("Q") ? id + ". " + "Producto" : "Producto";
        String ponderado = "\nponderado: " + rt.getPonderado();

        final TextView tvp = new TextView(c);
        tvp.setId(rt.getId().intValue());
        tvp.setText(ubicacion.equals("Q") ? pregunta+ponderado : pregunta);
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setPadding(5, 5, 5, 5);
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparamsText);

        LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 0.7;

        tvpor = new TextView(c);
        tvpor.setId(rt.getId().intValue());
        tvpor.setText((rt.getValor() == null || rt.getValor().equals("null") ? "Resultado : \n" : valor()));
        tvpor.setTextColor(Color.parseColor("#979A9A"));
        tvpor.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        tvpor.setPadding(10, 10, 10, 10);
        tvpor.setTypeface(null, Typeface.BOLD);
        tvpor.setLayoutParams(llparamsTextpo);

        LinearLayout line = new LinearLayout(c);
        line.setOrientation(LinearLayout.VERTICAL);

        final TextView tvp2 = new TextView(c);
        tvp2.setId(rt.getId().intValue());
        tvp2.setText("\nVariedad ");
        tvp2.setTextColor(Color.parseColor("#979A9A"));
        tvp2.setPadding(5, 5, 5, 5);
        tvp2.setTypeface(null, Typeface.BOLD);
        tvp2.setLayoutParams(llparamsText);

        lineVariedad = new LinearLayout(c);
        lineVariedad.setOrientation(LinearLayout.VERTICAL);
        lineVariedad.addView(CdesplegVariedad());

        line.addView(CdesplegProducto(llparamsText));
        line.addView(tvp2);
        line.addView(lineVariedad);

        Cgnr = new ControlGnr(c, rt.getId(), pp(tvp, tvpor),line, null, "vx2");

        return Cgnr;
    }

    //retorna el layout del encabezado pregunta porcentaje
    public LinearLayout pp(View v1, View v2) {
        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout llpregunta = new LinearLayout(c);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);
        llpregunta.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        llpregunta.addView(v1); //retorna pregunta
        if(ubicacion.equals("Q")) llpregunta.addView(v2); //retorna pocentaje si es igual a una pregunta del detalle (Q)

        return llpregunta;
    }

    public String valor() {
        String valor;
        if (rt.getValor() == null) {
            valor = "Resultado : \n";
        } else {
            if (rt.getValor().equals("-1")) {
                valor = "Resultado :\nNA";
            } else {
                valor = "Resultado : \n" + rt.getValor();
            }
        }
        return valor;
    }

    private View CdesplegProducto(LinearLayout.LayoutParams llparamsText) {
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(c, R.layout.spinner_item_personal, getProducto());
        AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(c);

        autoCompleteTextView.setAdapter(autoArray);
        autoCompleteTextView.setHint("Producto");
        autoCompleteTextView.setText((vacio ? rt.getRespuesta() : ""));
        autoCompleteTextView.setTextSize(15);
        autoCompleteTextView.setLayoutParams(llparamsText);
        autoCompleteTextView.setBackgroundColor(Color.parseColor("#E5E7E9"));
        autoCompleteTextView.setSingleLine(true);
        autoCompleteTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        autoCompleteTextView.setTextColor(Color.parseColor("#515A5A"));
        autoCompleteTextView.setTypeface(null, Typeface.BOLD);

        FunspinnerVproducto(autoCompleteTextView);
        return autoCompleteTextView;
    }

    private View CdesplegVariedad() {

        LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 2.3;

        ArrayAdapter<String> autoArray = new ArrayAdapter<String>(c, R.layout.spinner_item_personal, getVariedad());
        AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(c);

        autoCompleteTextView.setAdapter(autoArray);
        autoCompleteTextView.setHint("variedad");
        autoCompleteTextView.setText((vacio ? rt.getRespuesta() : ""));
        autoCompleteTextView.setTextSize(15);
        autoCompleteTextView.setLayoutParams(llparamsText);
        autoCompleteTextView.setBackgroundColor(Color.parseColor("#E5E7E9"));
        autoCompleteTextView.setSingleLine(true);
        autoCompleteTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        autoCompleteTextView.setTextColor(Color.parseColor("#515A5A"));
        autoCompleteTextView.setTypeface(null, Typeface.BOLD);

        FunspinnerVariedad(autoCompleteTextView);
        return autoCompleteTextView;
    }

    //FUNCION DEL SPINNER DEL PRODUCTO
    public void FunspinnerVproducto(final AutoCompleteTextView etdauto) {

        etdauto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String resultado = Buscar(etdauto.getText().toString(), rt.getDesplegable());
                    Log.i("RESULTADO",resultado);
                    producto = resultado;
                    if (!resultado.isEmpty()) {
                        lineVariedad.removeAllViews();
                        lineVariedad.addView(CdesplegVariedad());
                    }
                } catch (Exception ex) {
                    Toast.makeText(c, "" + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //FUNCION DEL SPINNER DE LA VARIEDAD
    public void FunspinnerVariedad(final AutoCompleteTextView etdauto) {

        etdauto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    despVariedadesTab.variedades variedad = BuscarVariedad(etdauto.getText().toString());
                    String vlr;

                    if(variedad != null) {
                        if (!variedad.getVariedad().isEmpty()) {
                            vlr = String.valueOf(rt.getPonderado());
                            tvpor.setText("Resultado: \n" + vlr);

                            registro( variedad.getVariedad(), variedad.getIdVariedad()+ "");
                        } else {
                            tvpor.setText("Resultado: \n0");
                            registro(null, null);
                        }
                    }else{
                        tvpor.setText("Resultado: \n0");
                        registro(null, null);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    // ITEMS PRODUCTO
    public List<String> getProducto(){
        try {
            List<String> Lproducto = new ArrayList<>();
            for (despVariedadesTab dt : idv.all()) {
                Lproducto.add(dt.getProducto());
            }

            HashSet sh = new HashSet();
            sh.addAll(Lproducto);
            Lproducto.clear();
            Lproducto.addAll(sh);

            return Lproducto;
        }catch (Exception e){
            Log.i("ERORPRODUCTO",e.toString());
            return null;
        }
    }

    // ITEMS VARIEDAD
    public List<String> getVariedad(){
        try {
            List<String> Lvariedad = new ArrayList<>();

            for (despVariedadesTab dt : idv.all()) {
                if(dt.getProducto().equals(producto)){
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
    public String Buscar(String data, String desplegable) {
        try {
            String producto = "";
            for (despVariedadesTab desp : idv.all()) {
                if (desp.getProducto().equals(data)) {
                    producto = desp.getProducto();
                    break;
                }
            }
            return producto;
        }catch (Exception ex){
            Toast.makeText(c, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    //BUSQUEDA DEL VARIEDAD
    public despVariedadesTab.variedades BuscarVariedad(String data) {
        try {
            despVariedadesTab.variedades variedad = null;
            for (despVariedadesTab dt : idv.all()) {
                if(dt.getProducto().equals(producto)){
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


    //REGISTRO
    public void registro(String rta, String valor) throws Exception {
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), null, rt.getReglas());
    }
}
