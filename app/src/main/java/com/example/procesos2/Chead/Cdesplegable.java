package com.example.procesos2.Chead;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.R;

import java.sql.Connection;
import java.util.ArrayList;

public class Cdesplegable {

    public Conexion con = null;
    iDesplegable iDES;
    Context context;

    View ControlView;

    public void Carga(String path){
        try {
            con = new Conexion(path,context);
            iDES = new iDesplegable(con.getConexion(), path);

        }catch (Exception ex){
        }
    }

    //metodo que crea el control desplegable y retorna view
    public View desplegable (Context context,Long id,String contenido,String desplegable){
        int i=0;
        for(i=0; i<=1; i++){
            ArrayList<consdesp> lista = new ArrayList<>();
            lista.add(new consdesp(context, id, contenido));

            for(consdesp cd : lista){
                LinearLayout.LayoutParams llparams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparams.weight = 1;
                llparams.setMargins(5, 10, 5, 20);

                final TextView tvp = new TextView(context);
                tvp.setId(id.intValue());
                tvp.setText(cd.contenido);
                tvp.setTextSize(20);
                tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(llparams);

                iDES.nombre = desplegable;

                try {
                    iDES.all();
                    ArrayList<String> OptionArray = new ArrayList<>(200);

                    OptionArray.add("selecciona");

                    for (DesplegableTab ds : iDES.all()) {
                        if (ds.getFiltro().equals(desplegable)) {
                            OptionArray.add(ds.getOptions());
                        } else {
                        }
                    }

                    final Spinner spinner = new Spinner(context);
                    spinner.setId(id.intValue());

                    ArrayAdapter<String> spinnerArray = new ArrayAdapter<>(context, R.layout.spinner_item_personal, OptionArray);
                    spinner.setAdapter(spinnerArray);
                    spinner.setLayoutParams(llparams);

                    ControlView = CrearLinearLayoutHeader(tvp, spinner, context);

                }catch (Exception ex){ }

            }
        }

        return ControlView;
    }

    public LinearLayout CrearLinearLayoutHeader(View v1, View v2, Context context) {
        LinearLayout.LayoutParams llparamsPrincipal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout LLprincipal = new LinearLayout(context);
        LLprincipal.setLayoutParams(llparamsPrincipal);
        LLprincipal.setBackgroundColor(Color.parseColor("#ffffff"));
        LLprincipal.setWeightSum(2);
        LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
        LLprincipal.setPadding(5, 5, 5, 5);
        LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

        LLprincipal.addView(v1);
        LLprincipal.addView(v2);

        return LLprincipal;
    }

    //constructor
    class consdesp {
        Context context;
        Long id;
        String contenido;

        public consdesp(Context context, Long id, String contenido) {
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
