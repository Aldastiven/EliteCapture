package com.example.procesos2.Chead;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.procesos2.R;
import com.example.procesos2.genated;

import java.util.ArrayList;

public class Cetnum {
    View ControlView;

    //metodo que va crear el control de edittext numerico
    public View tnumerico (Context context,Long id,String contenido){
        int i;
        for (i=0; i<=1; i++){

            ArrayList<consCetnum> lista = new ArrayList<>();
            lista.add(new consCetnum(context, id, contenido));

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

            for(consCetnum en : lista){

                LinearLayout.LayoutParams llparams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparams.weight = 1;
                llparams.setMargins(5, 10, 5, 10);

                TextView tvItem = new TextView(context);
                tvItem.setText(""+id.intValue());
                tvItem.setTextColor(Color.parseColor("#58d68d"));
                tvItem.setVisibility(View.INVISIBLE);
                tvItem.setTextSize(5);
                tvItem.setTypeface(null,Typeface.BOLD);

                final TextView tvp = new TextView(context);
                tvp.setId(id.intValue());
                tvp.setText(en.contenido);
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

                //DesabilitarTeclado(etxtN);

                LLtotal.addView(tvItem);
                LLtotal.addView(CrearLinearLayoutHeader(tvp, etxtN, context));

                ControlView = LLtotal;

            }

        }

        return ControlView;
    }


    public LinearLayout CrearLinearLayoutHeader(View v1, View v2, Context context) {
        LinearLayout.LayoutParams llparamsPrincipal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsPrincipal.setMargins(2,5,2,5);

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
    class consCetnum{
        Context context;
        Long id;
        String contenido;

        public consCetnum(Context context, Long id, String contenido) {
            this.context = context;
            this.id = id;
            this.contenido = contenido;
        }
    }

}
