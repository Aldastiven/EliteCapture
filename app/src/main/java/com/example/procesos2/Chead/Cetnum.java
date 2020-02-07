package com.example.procesos2.Chead;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.procesos2.ControlGnr;
import com.example.procesos2.R;
import com.example.procesos2.genated;

import java.util.ArrayList;

public class Cetnum {
    private Context context;
    private Long id;
    private String contenido;

    View ControlView;

    ControlGnr Cgnr;

    public Cetnum(Context context, Long id, String contenido) {
        this.context = context;
        this.id = id;
        this.contenido = contenido;
    }

    //metodo que va crear el control de edittext numerico
    public View tnumerico(){

        LinearLayout.LayoutParams llparams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparams.weight = 1;
        llparams.setMargins(5, 10, 5, 10);

        final TextView tvp = new TextView(context);
        tvp.setId(id.intValue());
        tvp.setText(contenido);
        tvp.setTextSize(20);
        tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparams);

        final EditText etxtN = new EditText(context);
        etxtN.setId(id.intValue());
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

        Cgnr = new ControlGnr(context,id,tvp,etxtN,null,"hx2");
        ControlView = Cgnr.Contenedor();

        Funetn(etxtN);

    return ControlView;
    }

    //funcion del control
    public void Funetn(final EditText etn){
        etn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(context, ""+Cgnr.getId(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, ""+etn.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
