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
    private List<DesplegableTab> desplegable;
    private RespuestasTab r;

    View ControlView;

    public CfilAuto(Context context, String path, Long id, String contenido, String ubicacion, List<DesplegableTab> desplegable, RespuestasTab r) {
        this.context = context;
        this.path = path;
        this.id = id;
        this.contenido = contenido;
        this.ubicacion = ubicacion;
        this.desplegable = desplegable;
        this.r = r;
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
        ControlView = Cgnr.Contenedor();

        FunAuto(autoCompleteTextView, tvp);

        String resultado = Buscar(autoCompleteTextView.getText().toString(), desplegable);

        if (!resultado.isEmpty()) {
            tvp.setText("Resultado : " + resultado);
            tvp.setTextColor(Color.parseColor("#58d68d"));
            Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);
        } else {}

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
    public void FunAuto(final AutoCompleteTextView etdauto, final TextView tvp) {

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
                    String resultado = Buscar(etdauto.getText().toString(), desplegable);

                    if (!resultado.isEmpty()) {
                        tvp.setText("Resultado : " + resultado);
                        tvp.setTextColor(Color.parseColor("#58d68d"));
                        Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);

                        registro(etdauto.getText().toString(),"");
                    } else {}
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
    public String Buscar(String data, List<DesplegableTab> desplegable) {
        for (DesplegableTab desp : desplegable) {
            if (desp.getOpcion().equals(data)) {
                return desp.getCodigo();
            }
        }
        return "";
    }
}