package com.example.procesos2.Chead;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
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

import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.R;

import java.sql.Connection;
import java.util.ArrayList;

public class CfilAuto {
    public Conexion con = null;
    iDesplegable iDES;
    Context context;
    View ControlView;
    TextView complete;

    public void Carga(String path){
        try{
            con = new Conexion(path,context);
            iDES = new iDesplegable(con.getConexion(),path);
        }catch (Exception ex){
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View autocompletado(final Context context, Long id, String contenido, String desplegable){
        int i;
        for(i = 0; i<=1; i++){
            ArrayList<consCfilAuto> lista = new ArrayList<>();
            lista.add(new consCfilAuto(context,id.intValue(),contenido));

            //ORGANIZA LOS CONTROLES INTEGRADOS
            LinearLayout.LayoutParams llparamsTotal = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            llparamsTotal.setMargins(0,0,0,10);

            LinearLayout LLtotal = new LinearLayout(context);
            LLtotal.setLayoutParams(llparamsTotal);
            LLtotal.setWeightSum(2);
            LLtotal.setOrientation(LinearLayout.VERTICAL);
            LLtotal.setPadding(8,15,8,12);
            LLtotal.setGravity(Gravity.CENTER_HORIZONTAL);
            LLtotal.setBackgroundResource(R.drawable.bordercontainer);

            for(consCfilAuto au : lista){

                LinearLayout.LayoutParams llparams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparams.weight = 1;
                llparams.setMargins(5, 10, 5, 20);

                TextView tvItem = new TextView(context);
                tvItem.setText(""+id.intValue());
                tvItem.setTextColor(Color.parseColor("#58d68d"));
                tvItem.setVisibility(View.INVISIBLE);
                tvItem.setTextSize(5);
                tvItem.setTypeface(null,Typeface.BOLD);


                final TextView tvp = new TextView(context);
                tvp.setId(id.intValue());
                tvp.setText(au.contenido);
                tvp.setTextSize(20);
                tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(llparams);

                iDES.nombre = desplegable;

                try{
                    iDES.all();
                    ArrayList<String> OptionArray = new ArrayList<>();

                    for(DesplegableTab ds : iDES.all()){
                        if(ds.getFiltro().equals(desplegable)){
                            OptionArray.add(ds.getOptions());
                        }else {}
                    }

                    AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(context);
                    ArrayAdapter<String> autoArray = new ArrayAdapter<>(context,R.layout.auto_complete_personal,OptionArray);
                    autoCompleteTextView.setAdapter(autoArray);
                    autoCompleteTextView.setLayoutParams(llparams);
                    autoCompleteTextView.setBackgroundColor(Color.parseColor("#E5E7E9"));
                    autoCompleteTextView.setSingleLine(true);

                    LLtotal.addView(tvItem);
                    LLtotal.addView(tvp);
                    LLtotal.addView(autoCompleteTextView);
                    ControlView = LLtotal;


                }catch (Exception ex) {
                    Toast.makeText(context, "Exc al traer los datos del desplegable\n"+ex.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("Exc","Exc al traer los datos del desplegable\n"+ex.toString());
                }
            }
        }

        return ControlView;
    }



    class consCfilAuto{
        private Context context;
        private int id;
        private String contenido;

        public consCfilAuto(Context context, int id, String contenido) {
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
