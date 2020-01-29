package com.example.procesos2.Chead;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.procesos2.Camera;
import com.example.procesos2.R;
import java.util.ArrayList;

public class Cscanner {

    Context context;
    View controlView;

    //metodo que crea dinamicamente el contol scanner
    public View scanner(final Context context, final Long id, String contenido){

        int i;
        for(i=0; i<=1; i++){
            ArrayList<consScanner>  lista = new ArrayList<>();
            lista.add(new consScanner(context, id, contenido));

            for (consScanner cs : lista){

                //ORGANIZA LOS CONTROLES INTEGRADOS
                LinearLayout.LayoutParams llparamsTotal = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                LinearLayout LLtotal = new LinearLayout(context);
                LLtotal.setLayoutParams(llparamsTotal);
                LLtotal.setWeightSum(2);
                LLtotal.setOrientation(LinearLayout.VERTICAL);
                LLtotal.setPadding(5, 5, 5, 5);
                LLtotal.setGravity(Gravity.CENTER_HORIZONTAL);

                //view que va a mostrar el resultado de la lectura
                final TextView tv = new TextView(context);
                tv.setId(id.intValue());
                tv.setText("Escanea el codigo de barras activando la camara:");
                tv.setTextColor(Color.parseColor("#979A9A"));
                tv.setPadding(5, 5, 5, 5);
                tv.setBackgroundColor(Color.parseColor("#ffffff"));
                tv.setTypeface(null, Typeface.BOLD);

                //organiza el campo de contenido y el boton que activa la camara
                LinearLayout.LayoutParams llparamsPrincipal = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

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
                edt.setHint("" + cs.contenido);
                edt.setHintTextColor(Color.parseColor("#626567"));
                edt.setBackgroundColor(Color.parseColor("#ffffff"));
                edt.setTextColor(Color.parseColor("#1C2833"));
                edt.setLayoutParams(llparamsTXT1);
                edt.setTypeface(null, Typeface.BOLD);
                edt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                edt.setRawInputType(Configuration.KEYBOARD_QWERTY);
                edt.setBackgroundResource(R.drawable.bordertext);

                LinearLayout.LayoutParams llparamsBtn = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                llparamsBtn.weight = (float) 1.5;
                llparamsBtn.setMargins(5, 10, 5, 5);


                final Button btn = new Button(context);
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bar, 0);
                btn.setPadding(10, 10, 10, 10);
                btn.setLayoutParams(llparamsBtn);

                LLprincipal.addView(edt);
                LLprincipal.addView(btn);

                LLtotal.addView(tv);
                LLtotal.addView(LLprincipal);

                controlView = LLtotal;

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StarCamera(context,id);
                    }
                });


            }
        }

        return controlView;
    }

    //metodo que activa la camara
    public void StarCamera(Context context, long id){
        try{
            Intent i = new Intent(context, Camera.class);
            i.putExtra("id",id);
            context.startActivity(i);
        }catch (Exception ex){
            Toast.makeText(context, "Ocurrio un error \n \n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    //constructor
    class consScanner{
        Context context;
        Long id;
        String contenido;

        public consScanner(Context context, Long id, String contenido) {
            this.context = context;
            this.id = id;
            this.contenido = contenido;
        }
    }

}
