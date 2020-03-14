package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;
import java.util.ArrayList;

public class CdespelgableQ {
    Context context;
    RespuestasTab rt;
    String path;
    boolean incial;
    boolean vacio;
    int id;
    String ubicacion;
    View ControlView;

    ControlGnr Cgnr;

    public CdespelgableQ(Context context, RespuestasTab rt, String path, boolean incial,  String ubicacion) {
        this.context = context;
        this.rt = rt;
        this.path = path;
        this.incial = incial;
        this.vacio = rt.getRespuesta() != null;
        this.id = rt.getId().intValue();
        this.ubicacion = ubicacion;
    }

    public View Cdesp() {

        LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 2.3;

        final TextView tvp = new TextView(context);
        tvp.setId(rt.getId().intValue());
        tvp.setText(id + ". " + rt.getPregunta() + "\nponderado: " + rt.getPonderado());
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setPadding(5, 5, 5, 5);
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparamsText);

        LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 0.7;

        final TextView tvpor = new TextView(context);
        tvpor.setId(rt.getId().intValue());
        tvpor.setText((rt.getValor() == null || rt.getValor().equals("null") ? "Resultado : \n" : valor()));
        tvpor.setTextColor(Color.parseColor("#979A9A"));
        tvpor.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        tvpor.setPadding(10, 10, 10, 10);
        tvpor.setTypeface(null, Typeface.BOLD);
        tvpor.setLayoutParams(llparamsTextpo);

        Cgnr = new ControlGnr(context, rt.getId(), pp(tvp, tvpor),Cdespleg(llparamsText,tvpor), null, "vx2");
        ControlView = Cgnr.Contenedor(vacio, incial, rt.getTipo());

        return ControlView;
    }

    private View Cdespleg(LinearLayout.LayoutParams llparamsText, TextView tvpor) {
        ArrayList soloOpciones = soloOpciones(rt.getDesplegable());

        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(context, R.layout.spinner_item_personal, soloOpciones);
        final Spinner spinner = new Spinner(context);
        spinner.setId(rt.getId().intValue());
        spinner.setAdapter(spinnerArray);
        spinner.setSelection((vacio ? soloOpciones.indexOf(rt.getRespuesta()) : 0));
        spinner.setLayoutParams(llparamsText);
        Funspinner(spinner, tvpor);
        return spinner;
    }

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
                        registro(rta,vlr);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private ArrayList soloOpciones(String opcion) {
        try {
            ArrayList<String> opc = new ArrayList<>();

            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = opcion;
            opc.add("Selecciona");
            for (DesplegableTab des : iDesp.all()) {
                opc.add(des.getOpcion());
            }
            return opc;
        } catch (Exception ex) {
            Toast.makeText(context, "" + ex, Toast.LENGTH_SHORT).show();
            return null;
        }
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

    //retorna el layout del encabezado pregunta porcentaje
    public LinearLayout pp(View v1, View v2) {
        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout llpregunta = new LinearLayout(context);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);
        llpregunta.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        llpregunta.addView(v1); //retorna pregunta
        llpregunta.addView(v2); //retorna pocentaje

        return llpregunta;
    }

    public void registro(String rta, String valor) throws Exception {
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), null, rt.getReglas());
    }
}
