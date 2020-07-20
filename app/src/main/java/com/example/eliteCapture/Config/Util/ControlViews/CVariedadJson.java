package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.eliteCapture.Model.Data.Tab.despVariedadesTab;
import com.example.eliteCapture.Model.Data.idespVariedades;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
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

        final TextView tvp = new TextView(c);
        tvp.setId(rt.getId().intValue());
        tvp.setText(id + ". " + rt.getPregunta() + "\nponderado: " + rt.getPonderado());
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
        lineVariedad.addView(CdesplegVariedad(tvpor));

        line.addView(CdesplegProducto(llparamsText,tvpor));
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
        llpregunta.addView(v2); //retorna pocentaje

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

    private View CdesplegProducto(LinearLayout.LayoutParams llparamsText, TextView tvpor) {
        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(c, R.layout.spinner_item_personal, getProducto());
            final Spinner spinner = new Spinner(c);
            spinner.setId(rt.getId().intValue());
            spinner.setAdapter(spinnerArray);
            spinner.setSelection((vacio ? getProducto().indexOf(rt.getRespuesta()) : 0));
            spinner.setLayoutParams(llparamsText);
            FunspinnerVproducto(spinner);
        return spinner;
    }

    private View CdesplegVariedad(TextView tvpor) {

        LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 2.3;

        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(c, R.layout.spinner_item_personal, getVariedad());
        final Spinner spinner = new Spinner(c);
        spinner.setId(rt.getId().intValue());
        spinner.setAdapter(spinnerArray);
        spinner.setSelection((vacio ? getProducto().indexOf(rt.getRespuesta()) : 0));
        spinner.setLayoutParams(llparamsText);
        Funspinner(spinner, tvpor);
        return spinner;
    }

    //FUNCION DEL SPINNER DEL PRODUCTO
    public void FunspinnerVproducto(final  Spinner spn){
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                producto = spn.getItemAtPosition(position).toString();

                lineVariedad.removeAllViews();
                lineVariedad.addView(CdesplegVariedad(tvpor));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //FUNCION DEL SPINNER DE LA VARIEDAD
    private void Funspinner(final Spinner spn, final TextView tvpor) {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    String rta = spn.getItemAtPosition(position).toString();
                    String vlr;
                    if (spn.getSelectedItem() == "Selecciona") {
                        vlr = String.valueOf(0);
                        tvpor.setText("Resultado: \n" + vlr);
                        registro(rta,vlr);
                    } else {
                        vlr = String.valueOf(rt.getPonderado());
                        tvpor.setText("Resultado: \n" + vlr);

                        String idVar = "";

                        for (despVariedadesTab dt : idv.all()) {
                            if(dt.getProducto().equals(producto)){
                                for(despVariedadesTab.variedades variedades : dt.getVariedades()){
                                    if(variedades.getVariedad().equals(rta)){
                                        idVar = variedades.getIdVariedad()+"";
                                    }
                                }
                                break;
                            }
                        }

                        registro(idVar ,vlr);
                    }

                } catch (Exception ex) {
                    Toast.makeText(c, "Error en el cambio : "+ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // ITEMS PRODUCTO
    public List<String> getProducto(){
        try {
            List<String> Lproducto = new ArrayList<>();

            Lproducto.add("Selecciona");
            for (despVariedadesTab dt : idv.all()) {
                Lproducto.add(dt.getProducto());
            }

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

            Lvariedad.add("Selecciona");
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

    public void registro(String rta, String valor) throws Exception {
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), null, rt.getReglas());
    }
}
