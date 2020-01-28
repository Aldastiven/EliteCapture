package com.example.procesos2.Cquest;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Model.tab.DesplegableTab;

import java.sql.Connection;
import java.util.ArrayList;

public class CradioButton {
    View ControlView;

    public Conexion con = null;
    iDesplegable iDES;
    Context context;
    ArrayList<String> option = new ArrayList<>();

    int idd = 0;

    public void Carga(String path){
        try {
            con = new Conexion(path, context);
            iDES = new iDesplegable(con.getConexion(), path);

        }catch (Exception ex){
        }
    }

    public View Tradiobtn (final Context context, Long id, final String contenido, String desplegable){

        LinearLayout ll = new LinearLayout(context);
        RadioGroup rg = new RadioGroup(context);
        rg.setOrientation(LinearLayout.HORIZONTAL);

        int i;
        traerdata(context,desplegable);


        for (i=0; i<=option.size()-1; i++){
            ArrayList<consradio> lista = new ArrayList<>();
            lista.add(new consradio(context, id, contenido));

            final RadioButton rb = new RadioButton(context);
            rb.setId(idd++);
            rb.setText(option.get(i));
            rg.addView(rb);

            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(rb.getId()==0){
                        Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
                        rb.setHighlightColor(Color.parseColor("#0c83bd"));
                    }else if(rb.getId()==(1)){
                        Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                    }else if(rb.getId()==(2)){
                        Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        ll.addView(rg);
        ControlView = ll;
        return ControlView;
    }

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
