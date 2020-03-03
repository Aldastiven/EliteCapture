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
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class CfilAuto {

    ControlGnr Cgnr;

    private Context context;
    private String path;
    private Long id;
    private String contenido;
    private String ubicacion;
    private String desplegable;
    private RespuestasTab r;
    private Boolean vacio;
    private Boolean inicial;


    View ControlView;

    public CfilAuto(Context context, String path, Long id, String contenido, String ubicacion, String desplegable, RespuestasTab r, Boolean vacio, Boolean inicial) {
        this.context = context;
        this.path = path;
        this.id = id;
        this.contenido = contenido;
        this.ubicacion = ubicacion;
        this.desplegable = desplegable;
        this.r = r;
        this.vacio = vacio;
        this.inicial = inicial;
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
        autoCompleteTextView.setText((r.getRespuesta() != null ? r.getRespuesta() : ""));
        autoCompleteTextView.setTextSize(15);
        autoCompleteTextView.setLayoutParams(medidas(1));
        autoCompleteTextView.setBackgroundColor(Color.parseColor("#E5E7E9"));
        autoCompleteTextView.setSingleLine(true);
        autoCompleteTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        autoCompleteTextView.setTextColor(Color.parseColor("#515A5A"));
        autoCompleteTextView.setTypeface(null, Typeface.BOLD);

        Cgnr = new ControlGnr(context, id, tvp, autoCompleteTextView, null, "vx2");
        ControlView = Cgnr.Contenedor(vacio,inicial);

        FunAuto(autoCompleteTextView, tvp);

        String resultado = Buscar(autoCompleteTextView.getText().toString(), desplegable);

        if (!resultado.isEmpty()) {
            tvp.setText("Resultado : " + resultado);
            tvp.setTextColor(Color.parseColor("#58d68d"));
            Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);
        } else {}

        return ControlView;
    }

    public ArrayList soloOpciones(String opcion) {
        try {
            ArrayList<String> opc = new ArrayList<>();

            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = opcion;

            for (DesplegableTab des : iDesp.all()) {
                opc.add(des.getOpcion());
            }
            return opc;
        }catch (Exception ex){
            Toast.makeText(context, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //funcionalidad del auto completado
    public void FunAuto(final AutoCompleteTextView etdauto, final TextView tvp) {

        etdauto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etdauto.dismissDropDown();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {

                    Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);

                    String resultado = Buscar(etdauto.getText().toString(), desplegable);

                    if (!resultado.isEmpty()) {
                        tvp.setText("Resultado : " + resultado);
                        tvp.setTextColor(Color.parseColor("#58d68d"));
                        Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);

                        registro(etdauto.getText().toString(),resultado);
                    } else {
                        registro(null,null);
                    }
                } catch (Exception ex) {
                    Toast.makeText(context, "" + ex.toString(), Toast.LENGTH_SHORT).show();
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

    //funcion de registro en el tempóral
    public void registro(String rta, String valor) throws Exception {
        iContenedor conTemp = new iContenedor(path);
        conTemp.editarTemporal(ubicacion, r.getId().intValue(), rta, valor);
    }

    //funcion de la busqueda
    public String Buscar(String data, String desplegable) {
        try {

            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = desplegable;

            for (DesplegableTab desp : iDesp.all()) {
                if (desp.getOpcion().equals(data)) {
                    return desp.getCodigo();
                }
            }
            return "";
        }catch (Exception ex){
            Toast.makeText(context, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }
}
