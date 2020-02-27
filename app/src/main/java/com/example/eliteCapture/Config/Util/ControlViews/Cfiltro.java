package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
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
    private String desplegable;
    private String ubicacion;
    private RespuestasTab r;
    private Boolean vacio;
    private Boolean inicial;

    public Cfiltro(Context context, String path, Long id, String contenido, String desplegable, String ubicacion, RespuestasTab r, Boolean vacio, Boolean inicial) {
        this.context = context;
        this.path = path;
        this.id = id;
        this.contenido = contenido;
        this.desplegable = desplegable;
        this.ubicacion = ubicacion;
        this.r = r;
        this.vacio = vacio;
        this.inicial = inicial;
    }

    public Cfiltro(Context context, String path, Long id, String contenido, String desplegable, String ubicacion, RespuestasTab r, Boolean vacio) {
        this.context = context;
        this.path = path;
        this.id = id;
        this.contenido = contenido;
        this.desplegable = desplegable;
        this.ubicacion = ubicacion;
        this.r = r;
        this.vacio = vacio;
    }

    //metodo que crea dinamiamente el control del filtro
    public View filtro() {

        Log.i("pens", "filtro: " + r.getValor());

        final TextView tv = new TextView(context);
        tv.setId(id.intValue());
        tv.setText("Resultados :");
        tv.setTextColor(Color.parseColor("#979A9A"));
        tv.setPadding(5, 5, 5, 5);
        tv.setBackgroundColor(Color.parseColor("#ffffff"));
        tv.setTypeface(null, Typeface.BOLD);

        final EditText edt = new EditText(context);

        if (r.getReglas() != 0) {
            edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(r.getReglas())});
        }

        edt.setText((r.getValor() != null ? r.getValor() : ""));
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
        ControlView = Cgnr.Contenedor(vacio, inicial);

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
    public void Funfiltro(Button btn, final EditText edt, final TextView tv, final String desplegable) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String resultado = Buscar(edt.getText().toString(), desplegable);
                    Log.i("FIL", "FIL " + resultado);

                    if (!resultado.isEmpty()) {
                        tv.setText("Resultado : " + resultado);
                        tv.setTextColor(Color.parseColor("#58d68d"));
                        Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);

                        registro(resultado, edt.getText().toString());

                    } else {
                        tv.setText("No se encontraron resultados");
                        tv.setTextColor(Color.parseColor("#f1948a"));
                        Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainerred);

                        registro(null,null);
                    }
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
    public String Buscar(String data, String desplegable) {
        try {
            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = desplegable;

            for (DesplegableTab desp : iDesp.all()) {
                if (desp.getCodigo().equals(data)) {
                    return desp.getOpcion();
                }
            }
            return "";
        } catch (Exception ex) {
            return "";
        }
    }

    //funcion de registro en el tempóral
    public void registro(String rta, String valor) throws Exception {
        iContenedor conTemp = new iContenedor(path);
        conTemp.editarTemporal(ubicacion, r.getId().intValue(), rta, valor);
    }

}
