package com.example.procesos2.Cquest;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Model.iTemporal;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.Model.tab.TemporalTab;
import com.example.procesos2.R;

import java.sql.Connection;
import java.time.temporal.Temporal;
import java.util.ArrayList;

public class CradioButton {
    View ControlView;

    public Conexion con = null;
    iDesplegable iDES;
    Context context;
    ArrayList<String> option = new ArrayList<>();
    ArrayList<TemporalTab> At = new ArrayList<>();

    iTemporal iT = null;

    int idd = 0 , idres = 0;

    //obtiene conexion y path
    public void Carga(String path){
        try {
            con = new Conexion(path, context);
            iDES = new iDesplegable(con.getConexion(), path);
            iT = new iTemporal(path);

        }catch (Exception ex){
        }
    }

    //crea el control del radio button y retorna el view
    public View Tradiobtn (final Context context, final Long id, final String contenido, String desplegable, final Float porcentaje, final String path, final String nompro, final int idpro){

        iT.path = path;
        iT.nombre = nompro;
        final TemporalTab tt = new TemporalTab();

        //ORGANIZA LOS CONTROLES INTEGRADOS
        LinearLayout.LayoutParams llparamsTotal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        llparamsTotal.setMargins(0,0,0,10);

        LinearLayout LLtotal = new LinearLayout(context);
        LLtotal.setLayoutParams(llparamsTotal);
        LLtotal.setWeightSum(2);
        LLtotal.setOrientation(LinearLayout.VERTICAL);
        LLtotal.setPadding(10,30,10,12);
        LLtotal.setGravity(Gravity.CENTER_HORIZONTAL);
        LLtotal.setBackgroundResource(R.drawable.bordercontainer);

        LinearLayout ll = new LinearLayout(context);
        RadioGroup rg = new RadioGroup(context);
        rg.setOrientation(LinearLayout.HORIZONTAL);
        rg.setGravity(Gravity.CENTER_HORIZONTAL);

        int i;
        traerdata(context,desplegable);

        LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 2.3;

        LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 0.7;

        TextView tvItem = new TextView(context);
        tvItem.setText("Item : "+id.intValue());
        tvItem.setTextColor(Color.parseColor("#58d68d"));
        tvItem.setTypeface(null,Typeface.BOLD);

        final TextView tvp = new TextView(context);
        tvp.setId(id.intValue());
        tvp.setText(contenido+"\nponderado: "+porcentaje.toString());
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setPadding(5, 5, 5, 5);
        tvp.setBackgroundColor(Color.parseColor("#ffffff"));
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparamsText);

        final TextView tvpor = new TextView(context);
        tvpor.setId(idres++);
        tvpor.setText("resultado: ");
        tvpor.setTextColor(Color.parseColor("#979A9A"));
        tvpor.setBackgroundColor(Color.parseColor("#ffffff"));
        tvpor.setPadding(10, 10, 10, 10);
        tvpor.setTypeface(null, Typeface.BOLD);
        tvpor.setLayoutParams(llparamsTextpo);


        for (i=0; i<=option.size()-1; i++){
            ArrayList<consradio> lista = new ArrayList<>();
            lista.add(new consradio(context, id, contenido));

            LinearLayout.LayoutParams llrb = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            llrb.setMargins(0,5,10,0);

            final RadioButton rb = new RadioButton(context);
            rb.setId(idd++);
            rb.setText(option.get(i));
            rb.setLayoutParams(llrb);
            rb.setTextSize(12);
            rg.addView(rb);

            if(rb.getText().toString().equals("NO APLICA")){
                rb.setButtonTintList(ColorSelected( 231, 76, 60 ));
            }else if(rb.getText().toString().equals("NO CUMPLE")){
                rb.setButtonTintList(ColorSelected( 153, 163, 164 ));
            }else if(rb.getText().toString().equals("SI CUMPLE")){
                rb.setButtonTintList(ColorSelected(34, 153, 84 ));
            }

            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        tt.setRespuesta(rb.getText().toString());
                        //Toast.makeText(context, ""+iT.update(rb.getId(),tt), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, ""+iT.all(), Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){
                        Toast.makeText(context, "Exc al insertar en CradioButton.class \n \n "+ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            try {
                tt.setItem(rb.getId());
                tt.setProceso(idpro);
                tt.setUsuario(id.intValue());
                tt.setRespuesta("");
                tt.setPorcentaje((double) 1);
                iT.insert(tt);
                iT.local();
            }catch (Exception ex){
                Toast.makeText(context, "Exc al insertar en en el json \n \n "+ex.toString(), Toast.LENGTH_LONG).show();
            }
        }

        LLtotal.addView(tvItem);
        LLtotal.addView(LLPREGUNTA(context,tvp,tvpor));
        LLtotal.addView(rg);

        ll.addView(LLtotal);
        ControlView = ll;
        return ControlView;
    }

    //retorna view de pregunta y porcentaje
    public View LLPREGUNTA(Context c,View v1, View v2){

        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout llpregunta = new LinearLayout(c);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);

        llpregunta.addView(v1); //retorna pregunta
        llpregunta.addView(v2); //retorna pocentaje

        return llpregunta;
    }

    //añade el color al radio segun el texto del control
    public ColorStateList ColorSelected(int red, int green, int blue){
        final ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{

                        Color.DKGRAY
                        , Color.rgb (red,green,blue),
                }
        );

        return colorStateList;
    }

    //retorna los resultados de los desplegables
    public ArrayList<String> traerdata(Context context, String desplegable){
        iDES.nombre = desplegable;
        try{
            for(DesplegableTab tb : iDES.all()) {
               option.add(tb.getOptions());
            }

        }catch (Exception ex){
            Toast.makeText(context, "Exception al traerdata\n\n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
        return option;
    }

    //constructor
    class consradio{
        Context context;
        Long id;
        String contenido;

        public consradio(Context context, Long id, String contenido) {
            this.context = context;
            this.id = id;
            this.contenido = contenido;
        }
    }

    //sql & path
    protected class Conexion extends sqlConect {
        Connection cn = getConexion();
        String path = null;
        Context context;

        public Conexion(String path, Context context) {
            this.path = path;
            this.context = context;
            getPath(path);
        }

        public boolean getPath(String path) {
            this.path = path;
            if (cn != null) {
                return true;
            } else {
                return false;
            }
        }
    }
}
