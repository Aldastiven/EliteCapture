package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;

public class Cetnum {
    private Context context;
    private String path;
    private String ubicacion;
    private RespuestasTab rt;
    private Boolean vacio;
    private Boolean inicial;


    View ControlView;

    ControlGnr Cgnr;

    public Cetnum(Context context, String path, String ubicacion, RespuestasTab rt,Boolean inicial)  {
        this.context = context;
        this.path = path;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.vacio = rt.getRespuesta() != null;
        this.inicial = inicial;
    }


    //metodo que va crear el control de edittext numerico
    public View tnumerico() {

        LinearLayout.LayoutParams llparams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparams.weight = 1;
        llparams.setMargins(5, 10, 5, 10);

        final TextView tvp = new TextView(context);
        tvp.setId(rt.getId().intValue());
        tvp.setText(rt.getPregunta());
        tvp.setTextSize(20);
        tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparams);

        final EditText etxtN = new EditText(context);

        if (rt.getReglas() != 0) {
            etxtN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(rt.getReglas())});
        }

        etxtN.setId(rt.getId().intValue());
        etxtN.setText((vacio ? rt.getRespuesta() : ""));
        etxtN.setTextSize(20);
        etxtN.setHint("NULL");
        etxtN.setHintTextColor(Color.TRANSPARENT);
        etxtN.setRawInputType(Configuration.KEYBOARD_QWERTY);
        etxtN.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        etxtN.setTextColor(Color.parseColor("#515A5A"));
        etxtN.setBackgroundColor(Color.parseColor("#E5E7E9"));
        etxtN.setTypeface(null, Typeface.BOLD);
        etxtN.setLayoutParams(llparams);
        etxtN.setBackgroundColor(Color.parseColor("#eeeeee"));
        etxtN.setSingleLine();


        Cgnr = new ControlGnr(context, rt.getId(), tvp, etxtN, null, "hx2");

        ControlView = Cgnr.Contenedor(vacio, inicial);

        Funetn(etxtN);

        return ControlView;
    }

    //funcion del control
    public void Funetn(final EditText etn) {
        etn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);
                    String rta = etn.getText().toString();
                    if (!rta.isEmpty()) {
                        registro(rta, null);
                    } else {
                        registro(null, null);
                    }
                } catch (Exception ex) {
                }
            }
        });
    }

    //funcion de registro en el tempóral
    public void registro(String rta, String valor) throws Exception {
        iContenedor conTemp = new iContenedor(path);
        conTemp.editarTemporal(ubicacion, rt.getId().intValue(), rta, valor);
    }

}
