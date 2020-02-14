package com.example.eliteCapture.Config.Util.ControlViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Cetalf {

    private Context context;
    private Long id;
    private String contenido;

    ControlGnr Cgnr;

    View ControlView;

    public Cetalf(Context context, Long id, String contenido) {
        this.context = context;
        this.id = id;
        this.contenido = contenido;
    }

    //metodo que crea el control edittext alfanumerico
    public View talfanumerico(){

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

        EditText etxtA = new EditText(context);
        etxtA.setId(id.intValue());
        etxtA.setTextSize(20);
        etxtA.setHint("NULL");
        etxtA.setHintTextColor(Color.TRANSPARENT);
        etxtA.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        etxtA.setTextColor(Color.parseColor("#515A5A"));
        etxtA.setBackgroundColor(Color.parseColor("#E5E7E9"));
        etxtA.setTypeface(null, Typeface.BOLD);
        etxtA.setLayoutParams(llparams);
        etxtA.setBackgroundColor(Color.parseColor("#eeeeee"));
        etxtA.setSingleLine();

        Cgnr = new ControlGnr(context,id,tvp,etxtA,null,"hx2");
        ControlView = Cgnr.Contenedor();

        Funeta(etxtA);

        return ControlView;
    }

    //funcion del control
    public void Funeta(final EditText eta){
        eta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(context, ""+Cgnr.getId(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, ""+eta.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
