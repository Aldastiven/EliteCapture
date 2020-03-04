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

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class Cdesplegable {
    private Context context;
    private String path;
    private String ubicacion;
    private RespuestasTab rt;
    private Boolean vacio;
    private Boolean inicial;
    public View ControlView;



    ArrayList<String> codigo = new ArrayList<>();

    ControlGnr Cgnr = null;

    //contructor
    public Cdesplegable(Context context, String path, String ubicacion, RespuestasTab rt,Boolean inicial)  {
        this.context = context;
        this.path = path;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.vacio = rt.getRespuesta() != null;
        this.inicial = inicial;
    }

    //metodo que crea el control desplegable y retorna view
    public View desplegable() {

        LinearLayout.LayoutParams llparams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparams.weight = 1;
        llparams.setMargins(5, 10, 5, 20);

        //esta es la pregunta
        final TextView tvp = new TextView(context);
        tvp.setId(rt.getId().intValue());
        tvp.setText(rt.getPregunta());
        tvp.setTextSize(20);
        tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparams);

        //este es el desplegable
        ArrayList soloOpciones = soloOpciones(rt.getDesplegable());

        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(context, R.layout.spinner_item_personal, soloOpciones);
        final Spinner spinner = new Spinner(context);
        spinner.setId(rt.getId().intValue());
        spinner.setAdapter(spinnerArray);
        spinner.setSelection((vacio ? soloOpciones.indexOf(rt.getRespuesta()) : 0));
        spinner.setLayoutParams(llparams);

        Cgnr = new ControlGnr(context, rt.getId(), tvp, spinner, null, "hx2");
        ControlView = Cgnr.Contenedor(vacio, inicial);

        try {
            String rta = spinner.getItemAtPosition(0).toString();
            if (rta.equals("Selecciona")) {
                registro(null, null);
            }
        } catch (Exception ex) {
        }

        Funspinner(spinner);

        return ControlView;
    }

    public ArrayList soloOpciones(String opcion) {
        try {
            ArrayList<String> opc = new ArrayList<>();

            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = opcion;
            opc.add("Selecciona");
            for (DesplegableTab des : iDesp.all()) {
                opc.add(des.getOpcion());
                codigo.add((des.getCodigo()));
            }
            opc.add("");
            return opc;
        } catch (Exception ex) {
            Toast.makeText(context, "" + ex, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //funcion del spinner
    public void Funspinner(final Spinner spn) {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String rta = spn.getItemAtPosition(position).toString();
                    if (spn.getSelectedItem() == "Selecciona") {
                        registro(null, null);
                    } else {
                        Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);
                        registro(rta, codigo.get(position-1));
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //funcion de registro en el tempóral
    public void registro(String rta, String valor) throws Exception {
        iContenedor conTemp = new iContenedor(path);
        conTemp.editarTemporal(ubicacion, rt.getId().intValue(), rta, valor);
    }

}
