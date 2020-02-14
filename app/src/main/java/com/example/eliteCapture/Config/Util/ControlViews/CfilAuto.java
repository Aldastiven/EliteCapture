package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class CfilAuto {

    ControlGnr Cgnr;

    private Context context;
    private Long id;
    private String contenido;
    private List<DesplegableTab> desplegable;

    View ControlView;

    public CfilAuto(Context context, Long id, String contenido, List<DesplegableTab> desplegable) {
        this.context = context;
        this.id = id;
        this.contenido = contenido;
        this.desplegable = desplegable;
    }

    public View autocompletado() throws Exception {

        final TextView tvp = new TextView(context);
        tvp.setId(id.intValue());
        tvp.setText("Resultados :");
        tvp.setTextSize(15);
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(medidas(1));

        AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(context);
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(context, R.layout.auto_complete_personal, soloOpciones(desplegable));
        autoCompleteTextView.setAdapter(autoArray);
        autoCompleteTextView.setHint(contenido);
        autoCompleteTextView.setTextSize(15);
        autoCompleteTextView.setLayoutParams(medidas(1));
        autoCompleteTextView.setBackgroundColor(Color.parseColor("#E5E7E9"));
        autoCompleteTextView.setSingleLine(true);
        autoCompleteTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        autoCompleteTextView.setTextColor(Color.parseColor("#515A5A"));
        autoCompleteTextView.setTypeface(null, Typeface.BOLD);

        Cgnr = new ControlGnr(context, id, tvp, autoCompleteTextView, null, "vx2");
        ControlView = Cgnr.Contenedor();

        FunAuto(autoCompleteTextView, tvp);

        return ControlView;
    }


    public ArrayList soloOpciones(List<DesplegableTab> opcion) {
        ArrayList<String> opc = new ArrayList<>();

        for (DesplegableTab des : opcion) {
            opc.add(des.getOpcion());
        }
        return opc;
    }

    //funcionalidad del auto completado
    public void FunAuto(final AutoCompleteTextView etdauto, final TextView tdp) {

        etdauto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    for (DesplegableTab ds : desplegable) {
                        if (etdauto.getText().toString().equals(ds.getOpcion())) {
                            tdp.setText("Resultados :   " + ds.getCodigo());
                            tdp.setTextColor(Color.parseColor("#58d68d"));
                        } else {
                        }
                    }
                } catch (Exception ex) {
                }
            }
        });

    }


    public LinearLayout.LayoutParams medidas(double med) {

        LinearLayout.LayoutParams llparams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llparams.weight = (float) med;
        llparams.setMargins(5, 10, 5, 20);

        return llparams;
    }
}
