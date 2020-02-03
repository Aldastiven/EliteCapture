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
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.R;
import com.example.procesos2.genated;

import java.sql.Connection;
import java.util.ArrayList;

public class Cfiltro {
    public Conexion con = null;
    iDesplegable iDES;
    Context context;
    View ControlView; //esta varialble retorna en el metodo que crea el control

    public void Carga(String path){
        try {
            con = new Conexion(path, context);
            iDES = new iDesplegable(con.getConexion(), path);

        }catch (Exception ex){
        }
    }

    //metodo que crea dinamiamente el control del filtro
    public View filtro (final Context context, Long id, String contenido, final String desplegable){

        int i;
        for(i=0; i<1; i++){
            final ArrayList<consfiltro> lista = new ArrayList<>();
            lista.add(new consfiltro(context,id,contenido));

            for(consfiltro ff : lista){

                //ORGANIZA LOS CONTROLES INTEGRADOS
                LinearLayout.LayoutParams llparamsTotal = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparamsTotal.setMargins(2,5,2,5);

                final LinearLayout LLtotal = new LinearLayout(context);
                LLtotal.setLayoutParams(llparamsTotal);
                LLtotal.setWeightSum(2);
                LLtotal.setOrientation(LinearLayout.VERTICAL);
                LLtotal.setPadding(5, 15, 5, 5);
                LLtotal.setGravity(Gravity.CENTER_HORIZONTAL);
                LLtotal.setBackgroundResource(R.drawable.bordercontainer);

                LinearLayout.LayoutParams llparams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparams.weight = 1;
                llparams.setMargins(5, 0, 5, 10);

                LinearLayout.LayoutParams lltxtres = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lltxtres.setMargins(0,10,0,0);

                TextView tvItem = new TextView(context);
                tvItem.setText(""+id.intValue());
                tvItem.setTextColor(Color.parseColor("#58d68d"));
                tvItem.setVisibility(View.INVISIBLE);
                tvItem.setTextSize(5);
                tvItem.setTypeface(null,Typeface.BOLD);

                final TextView tv = new TextView(context);
                tv.setId(id.intValue());
                tv.setText("Resultados :");
                tv.setTextColor(Color.parseColor("#979A9A"));
                tv.setPadding(5, 5, 5, 5);
                tv.setBackgroundColor(Color.parseColor("#ffffff"));
                tv.setTypeface(null, Typeface.BOLD);
                tv.setLayoutParams(lltxtres);

                LinearLayout.LayoutParams llparamsPrincipal = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparamsPrincipal.setMargins(0,0,0,10);

                LinearLayout LLprincipal = new LinearLayout(context);
                LLprincipal.setLayoutParams(llparamsPrincipal);
                LLprincipal.setWeightSum(2);
                LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
                LLprincipal.setPadding(5, 5, 5, 5);
                LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

                LinearLayout.LayoutParams llparamsTXT1 = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                llparamsTXT1.weight = (float) 0.5;
                llparamsTXT1.setMargins(5, 10, 5, 5);

                final EditText edt = new EditText(context);
                edt.setHint("" + ff.contenido);
                edt.setHintTextColor(Color.parseColor("#626567"));
                edt.setBackgroundColor(Color.parseColor("#ffffff"));
                edt.setTextColor(Color.parseColor("#1C2833"));
                edt.setLayoutParams(llparamsTXT1);
                edt.setTypeface(null, Typeface.BOLD);
                edt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                edt.setRawInputType(Configuration.KEYBOARD_QWERTY);
                edt.setBackgroundColor(Color.parseColor("#eeeeee"));
                edt.setSingleLine();

                LinearLayout.LayoutParams llparamsBtn = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                llparamsBtn.weight = (float) 1.5;
                llparamsBtn.setMargins(5, 10, 5, 5);


                final Button btn = new Button(context);
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.lupa, 0);
                btn.setPadding(10, 10, 10, 10);
                btn.setLayoutParams(llparamsBtn);

                LLprincipal.addView(edt);
                LLprincipal.addView(btn);

                LLtotal.addView(tvItem);
                LLtotal.addView(tv);
                LLtotal.addView(LLprincipal);

                ControlView = LLtotal;

                    //ACTIVA LA FUNCION DE LA BUSQUEDA Y RETORNA RESULTADO PARA ALMACENARLA EN EL TXV
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String resultado = Buscar(edt.getText().toString(),desplegable);

                            if(!resultado.isEmpty()){
                                tv.setText(Buscar(edt.getText().toString(),desplegable));
                                tv.setTextColor(Color.parseColor("#58d68d"));
                                LLtotal.setBackgroundResource(R.drawable.bordercontainer);
                            }else {
                                tv.setText("No se encontraron resultados");
                                tv.setTextColor(Color.parseColor("#f1948a"));
                                LLtotal.setBackgroundResource(R.drawable.bordercontainerred);
                            }
                        }
                    });

            }
        }

    return ControlView;
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

    //constructor
    class consfiltro{
        Context context;
        Long id;
        String contenido;

        public consfiltro(Context context, Long id, String contenido) {
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
