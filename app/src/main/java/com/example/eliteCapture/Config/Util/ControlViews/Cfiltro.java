package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Cfiltro {
    iDesplegable iDES;
    View ControlView;
    ControlGnr Cgnr = null;

    private Context context;
    private String path;
    private Long id;
    private String contenido;
    private List<DesplegableTab> desplegable;
    private String ubicacion;
    private RespuestasTab r;

    public Cfiltro(Context context, String path, Long id, String contenido, List<DesplegableTab> desplegable, String ubicacion, RespuestasTab r) {
        this.context = context;
        this.path = path;
        this.id = id;
        this.contenido = contenido;
        this.desplegable = desplegable;
        this.ubicacion = ubicacion;
        this.r = r;
    }

    //metodo que crea dinamiamente el control del filtro
    public View filtro() {

        final TextView tv = new TextView(context);
        tv.setId(id.intValue());
        tv.setText("Resultados :");
        tv.setTextColor(Color.parseColor("#979A9A"));
        tv.setPadding(5, 5, 5, 5);
        tv.setBackgroundColor(Color.parseColor("#ffffff"));
        tv.setTypeface(null, Typeface.BOLD);

        final EditText edt = new EditText(context);
        edt.setText((r.getRespuesta() != null ? r.getRespuesta() : ""));
        edt.setHint("" + contenido);
        edt.setHintTextColor(Color.parseColor("#626567"));
        edt.setBackgroundColor(Color.parseColor("#ffffff"));
        edt.setTextColor(Color.parseColor("#1C2833"));
        edt.setLayoutParams(medidas(0.5));
        edt.setTypeface(null, Typeface.BOLD);
        edt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        edt.setRawInputType(Configuration.KEYBOARD_QWERTY);
        edt.setBackgroundColor(Color.parseColor("#eeeeee"));
        edt.setSingleLine();

        final Button btn = new Button(context);
        btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.lupa, 0);
        btn.setPadding(10, 10, 10, 10);
        btn.setLayoutParams(medidas(1.5));

        Cgnr = new ControlGnr(context, id, tv, edt, btn, "hxbtn_izq");
        ControlView = Cgnr.Contenedor();

        Funfiltro(btn, edt, tv, desplegable);

        String resultado = Buscar(edt.getText().toString(), desplegable);

        if (!resultado.isEmpty()) {
            tv.setText("Resultado : " + resultado);
            tv.setTextColor(Color.parseColor("#58d68d"));
            Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);
        } else {
            edt.setText("");
        }

        return ControlView;
    }



    //funcion del boton
    public void Funfiltro(Button btn, final EditText edt, final TextView tv, final List<DesplegableTab> desplegable) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resultado = Buscar(edt.getText().toString(), desplegable);
                Log.i("FIL", "FIL " + resultado);

                if (!resultado.isEmpty()) {
                    tv.setText("Resultado : " + resultado);
                    tv.setTextColor(Color.parseColor("#58d68d"));
                    Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);
                } else {
                    tv.setText("No se encontraron resultados");
                    tv.setTextColor(Color.parseColor("#f1948a"));
                    Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainerred);
                }

                try {
                    registro(edt.getText().toString(), "");
                } catch (Exception e) {
                }
            }
        });
    }


    //medidas para el boton y el campo de busqueda
    public LinearLayout.LayoutParams medidas(double med) {

        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llparams.weight = (float) med;
        llparams.setMargins(5, 10, 5, 5);

        return llparams;
    }

    //funcion de la busqueda
    public String Buscar(String data, List<DesplegableTab> desplegable) {
        for (DesplegableTab desp : desplegable) {
            if (desp.getCodigo().equals(data)) {
                return desp.getOpcion();
            }
        }
        return "";
    }

    //funcion de registro en el tempóral
    public void registro(String rta, String valor) throws Exception {
        iContenedor conTemp = new iContenedor(path);
        conTemp.editarTemporal(ubicacion, r.getId().intValue(), rta, valor);
    }

}