package com.example.procesos2.Chead;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.ControlGnr;
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.R;
import com.example.procesos2.genated;

import java.sql.Connection;
import java.util.ArrayList;

public class Cfiltro {
    iDesplegable iDES;
    View ControlView;
    ControlGnr Cgnr = null;

    private Context context;
    private Long id;
    private String contenido;
    private String desplegable;

    public Cfiltro(Context context, Long id, String contenido, String desplegable) {
        this.context = context;
        this.id = id;
        this.contenido = contenido;
        this.desplegable = desplegable;
    }

    public void Carga(String path){
        try {
            iDES = new iDesplegable(null, path);
        }catch (Exception ex){
        }
    }

    //metodo que crea dinamiamente el control del filtro
    public View filtro (){

        final TextView tv = new TextView(context);
        tv.setId(id.intValue());
        tv.setText("Resultados :");
        tv.setTextColor(Color.parseColor("#979A9A"));
        tv.setPadding(5, 5, 5, 5);
        tv.setBackgroundColor(Color.parseColor("#ffffff"));
        tv.setTypeface(null, Typeface.BOLD);

        final EditText edt = new EditText(context);
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

        Cgnr = new ControlGnr(context,id,tv,edt,btn,"hxbtn_izq");
        ControlView = Cgnr.Contenedor();

        Funfiltro(btn,edt,tv,desplegable);

    return ControlView;
    }

    //funcion del boton
    public void Funfiltro(Button btn, final EditText edt, final TextView tv, final String desplegable){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String resultado = Buscar(edt.getText().toString(),desplegable);

                if(!resultado.isEmpty()){
                    tv.setText(Buscar(edt.getText().toString(),desplegable));
                    tv.setTextColor(Color.parseColor("#58d68d"));
                    Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);
                    Toast.makeText(context, ""+Cgnr.getId(), Toast.LENGTH_SHORT).show();
                }else {
                    tv.setText("No se encontraron resultados");
                    tv.setTextColor(Color.parseColor("#f1948a"));
                    Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainerred);
                    Toast.makeText(context, ""+Cgnr.getId(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //medidas para el boton y el campo de busqueda
    public LinearLayout.LayoutParams medidas(double med){

        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llparams.weight = (float) med;
        llparams.setMargins(5, 10, 5, 5);

        return llparams;
    }

    //funcion de la busqueda
    public String Buscar(String data,String desplegable){
        String resultado = "";
        iDES.nombre = desplegable;
        try {
            for (DesplegableTab dt : iDES.all()) {
                if(dt.getCod().equals(data)){
                    resultado = "Resultado :    " + dt.getOptions();
                }
            }
        }catch (Exception ex){
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }
        return resultado;
    }

}
