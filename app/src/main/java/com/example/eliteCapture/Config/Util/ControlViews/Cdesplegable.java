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
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class Cdesplegable {
    private Context context;
    private Long id;
    private String contenido;
    private List<DesplegableTab> opciones;

    View ControlView;

    ControlGnr Cgnr = null;

    //contructor
    public Cdesplegable(Context context, Long id, String contenido, List<DesplegableTab> opciones) {
        this.context = context;
        this.id = id;
        this.contenido = contenido;
        this.opciones = opciones;
    }

    //metodo que crea el control desplegable y retorna view
    public View desplegable() {

        LinearLayout.LayoutParams llparams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparams.weight = 1;
        llparams.setMargins(5, 10, 5, 20);

        //esta es la pregunta
        final TextView tvp = new TextView(context);
        tvp.setId(id.intValue());
        tvp.setText(contenido);
        tvp.setTextSize(20);
        tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparams);

        //este es el desplegable
        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(context, R.layout.spinner_item_personal, soloOpciones(opciones));
        final Spinner spinner = new Spinner(context);
        spinner.setId(id.intValue());
        spinner.setAdapter(spinnerArray);
        spinner.setLayoutParams(llparams);

        Cgnr = new ControlGnr(context, id, tvp, spinner, null, "hx2");
        ControlView = Cgnr.Contenedor();

        Funspinner(spinner);

        return ControlView;
    }

    public ArrayList soloOpciones(List<DesplegableTab> opcion) {
        ArrayList<String> opc = new ArrayList<>();

        for (DesplegableTab des : opcion) {
            opc.add(des.getOpcion());
        }
        return opc;
    }

    //funcion del spinner
    public void Funspinner(Spinner spn) {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "" + Cgnr.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}
