package com.example.procesos2.Chead;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.procesos2.ControlGnr;
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.R;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CfilAuto {

    ControlGnr Cgnr;

    private Context context;
    private Long id;
    private String contenido;
    private String desplegable;

    View ControlView;
    iDesplegable iDES;

    public CfilAuto(Context context, Long id, String contenido, String desplegable) {
        this.context = context;
        this.id = id;
        this.contenido = contenido;
        this.desplegable = desplegable;
    }

    public void Carga(String path){
        try{
            iDES = new iDesplegable(null,path);
        }catch (Exception ex){
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View autocompletado() throws Exception{

                final TextView tvp = new TextView(context);
                tvp.setId(id.intValue());
                tvp.setText("Resultados :");
                tvp.setTextSize(15);
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(medidas(1));

                AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(context);
                ArrayAdapter<String> autoArray = new ArrayAdapter<>(context,R.layout.auto_complete_personal,traerdesp());
                autoCompleteTextView.setAdapter(autoArray);
                autoCompleteTextView.setHint(contenido);
                autoCompleteTextView.setTextSize(15);
                autoCompleteTextView.setLayoutParams(medidas(1));
                autoCompleteTextView.setBackgroundColor(Color.parseColor("#E5E7E9"));
                autoCompleteTextView.setSingleLine(true);
                autoCompleteTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                autoCompleteTextView.setTextColor(Color.parseColor("#515A5A"));
                autoCompleteTextView.setTypeface(null, Typeface.BOLD);

                Cgnr = new ControlGnr(context,id,tvp,autoCompleteTextView,null,"vx2");
                ControlView = Cgnr.Contenedor();

                FunAuto(autoCompleteTextView, tvp);

        return ControlView;
    }

    //funcionalidad del auto completado
    public void FunAuto(final AutoCompleteTextView etdauto, final TextView tdp){

        etdauto.addTextChangedListener(new TextWatcher()  {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s){
                try {
                    for (DesplegableTab ds : iDES.all()) {
                        if (etdauto.getText().toString().equals(ds.getOptions())) {
                            tdp.setText("Resultados :   "+ds.getCod());
                            tdp.setTextColor(Color.parseColor("#58d68d"));
                        }else {}
                    }
                }catch (Exception ex){
                }
            }
        });

    }

    //metodo que trae los datos del desplegable correspondiente
    public List traerdesp()throws Exception{

        iDES.nombre = desplegable;

        iDES.all();
        ArrayList<String> OptionArray = new ArrayList<>();

        for(DesplegableTab ds : iDES.all()){
            if(ds.getFiltro().equals(desplegable)){
                OptionArray.add(ds.getOptions());
            }else {}
        }

        return OptionArray;
    }

    public LinearLayout.LayoutParams medidas(double med){

        LinearLayout.LayoutParams llparams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llparams.weight = (float) med;
        llparams.setMargins(5, 10, 5, 20);

        return llparams;
    }
}
