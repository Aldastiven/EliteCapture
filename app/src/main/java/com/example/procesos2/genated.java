package com.example.procesos2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.Conexion.CheckedConexion;
import com.example.procesos2.Model.iDetalles;
import com.example.procesos2.Model.interfaz.Detalles;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.IndexTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class genated extends AppCompatActivity {

    LinearLayout linearPrinc;

    public String path = null;
    private long idElement;

    List<DetallesTab> iP = new ArrayList<>();
    SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genated);
        getSupportActionBar().hide();

        linearPrinc = findViewById(R.id.LinearCheck);

        path = getExternalFilesDir(null)+ File.separator;
        CheckedConexion Cc = new CheckedConexion();
        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        try {
            iDetalles iD = new iDetalles(path);

            if(Cc.checkedConexionValidate(this)){
                iD.nombre = "Detalles";
                iD.local();
                iP = iD.all();
            }else {
                iD.nombre = "Detalles";
                iP = iD.all();
            }
            ValidarProceso();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Error onCreate \n" +ex,Toast.LENGTH_SHORT).show();
        }

    }

    public void ValidarProceso(){

        try{
            iDetalles iD = new iDetalles(path);
            int cod = sp.getInt("cod_proceso", 0);

            iD.nombre = "Detalles";
            List<DetallesTab> dt = iD.all();

            for (DetallesTab d : dt) {

                    for (int i = 0; i < iP.size(); i++) {
                        if (d.getIdProceso() == cod) {
                            //ValidarTipoRespuesta();
                        }
                    }
            }

            ValidarTipoRespuesta();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Exception ValidarProceso \n"+ex,Toast.LENGTH_SHORT).show();
        }
    }

    public void ValidarTipoRespuesta(){
        try {
            for (DetallesTab d : iP) {

                String tipo1 = "RS"; //BOTON DE CANTIDAD
                String tipo2 = "SWH"; //BOTON DE SI O NO

                if (d.getTipoDetalle().equals(tipo1)) {

                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    CrearSumRes(id, pregunta);

                }else if(d.getTipoDetalle().equals(tipo2)){
                    Long id = d.getIdProceso();
                    String pregunta = d.getQuesDetalle();
                    CrearSwicht(id, pregunta);

                } else{
                    Toast.makeText(getApplicationContext(),"No paso de forma correcta al if",Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Error al validar los tipos de repuestas \n"+ex,Toast.LENGTH_LONG).show();
        }
    }

    public void CrearSumRes(Long id,String pregunta){
        try{
            for(int i=0; i<1; i++) {
                ArrayList<buttonsSumRes> lista = new ArrayList<>();
                lista.add(new buttonsSumRes(id, pregunta, iP.get(i).getTipoDetalle()));

                    for (buttonsSumRes btnsr : lista) {
                        TextView tvp = new TextView(getApplicationContext());
                        tvp.setId(id.intValue());
                        tvp.setText(btnsr.pregunta);
                        tvp.setTextColor(Color.parseColor("#979A9A"));
                        tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                        tvp.setPadding(10,10,10,10);
                        tvp.setTypeface(null, Typeface.BOLD);



                        LinearLayout.LayoutParams llparamsPrincipal = new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        LinearLayout LLprincipal = new LinearLayout(getApplicationContext());
                        LLprincipal.setLayoutParams(llparamsPrincipal);
                        LLprincipal.setWeightSum(3);
                        LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
                        LLprincipal.setPadding(5,5,5,5);
                        llparamsPrincipal.setMargins(0,0,0,25);


                        LinearLayout.LayoutParams llparamsTXT =new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        llparamsTXT.weight = 1;
                        llparamsTXT.setMargins(5,5,5,5);

                        final TextView tvr = new TextView(getApplicationContext());
                        tvr.setText("0");
                        tvr.setTextSize(25);
                        tvr.setTextColor(Color.parseColor("#ffffff"));
                        tvr.setBackgroundColor(Color.parseColor("#CCD1D1"));
                        tvr.setTypeface(null, Typeface.BOLD);
                        tvr.setGravity(Gravity.CENTER);

                        tvr.setLayoutParams(llparamsTXT);



                        LinearLayout.LayoutParams llparamsBTN2 =new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        llparamsBTN2.weight = 1;
                        llparamsBTN2.setMargins(5,5,5,5);

                        final Button btn2 = new Button(getApplicationContext());
                        btn2.setBackgroundColor(Color.parseColor("#F1948A"));
                        btn2.setText("-");
                        btn2.setTextSize(25);
                        btn2.setTypeface(null, Typeface.BOLD);
                        btn2.setLayoutParams(llparamsBTN2);
                        btn2.setId(id.intValue());



                        LinearLayout.LayoutParams llparamsBTN3 =new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        llparamsBTN3.weight = 1;
                        llparamsBTN3.setMargins(5,5,5,5);

                        Button btn3 = new Button(getApplicationContext());
                        btn3.setBackgroundColor(Color.parseColor("#7DCEA0"));
                        btn3.setText("+");
                        tvr.setTypeface(null, Typeface.BOLD);
                        btn3.setTextSize(25);
                        btn3.setLayoutParams(llparamsBTN2);
                        btn3.setId(id.intValue());


                        LLprincipal.addView(tvr);
                        LLprincipal.addView(btn2);
                        LLprincipal.addView(btn3);

                        //agregando check dinamicos
                        linearPrinc.addView(tvp);
                        linearPrinc.addView(LLprincipal);

                     btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int n = Integer.parseInt(tvr.getText().toString());

                                if(n>0){
                                    tvr.setText(valueOf(n - 1));
                                }

                            }
                        });

                        btn3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int n = Integer.parseInt(tvr.getText().toString());

                                if(n<9){
                                    tvr.setText(valueOf(n + 1));
                                }

                            }
                        });
                }
            }
        }catch (Exception ex){
            Toast.makeText(this,"Se genero un exception \n " +
                                            "al crear el tipo de respuesta \n" +
                                            "en el metodo CrearSumRes",Toast.LENGTH_LONG).show();
        }
    }

    public void CrearSwicht(Long id,String pregunta){
        try {
            for(int i=0; i<1; i++){
                ArrayList<SwichtQ> lista = new ArrayList<>();
                lista.add(new SwichtQ(id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

                for(SwichtQ sw : lista){

                    TextView tvp = new TextView(getApplicationContext());
                    tvp.setId(id.intValue());
                    tvp.setText(sw.pregunta);
                    tvp.setTextColor(Color.parseColor("#979A9A"));
                    tvp.setPadding(5,5,5,5);
                    tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvp.setTypeface(null, Typeface.BOLD);


                    LinearLayout.LayoutParams llparamsPrincipal = new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    LinearLayout LLprincipal = new LinearLayout(getApplicationContext());
                    LLprincipal.setLayoutParams(llparamsPrincipal);
                    LLprincipal.setWeightSum(3);
                    LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
                    LLprincipal.setPadding(5,5,5,5);


                    LinearLayout.LayoutParams llparamsTXT1 =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    llparamsTXT1.weight = 1;
                    llparamsTXT1.setMargins(5,5,5,5);

                    TextView tvsi = new TextView(getApplicationContext());
                    tvsi.setText("SI");
                    tvsi.setTextSize(30);
                    tvsi.setTextColor(Color.parseColor("#ABB2B9"));
                    tvsi.setTypeface(null, Typeface.BOLD);
                    tvsi.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    tvsi.setLayoutParams(llparamsTXT1);



                    LinearLayout.LayoutParams llparamsSWC =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    llparamsSWC.weight = 1;
                    llparamsSWC.setMargins(5,0,5,0);

                    Switch  swit = new Switch(getApplicationContext());
                    swit.setHeight(100);

                    swit.setLayoutParams(llparamsSWC);

                    LinearLayout.LayoutParams llparamsTXT2 =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    llparamsTXT2.weight = 1;
                    llparamsTXT2.setMargins(5,5,5,5);

                    TextView tvno = new TextView(getApplicationContext());
                    tvno.setText("NO");
                    tvno.setTextSize(30);
                    tvno.setTextColor(Color.parseColor("#ABB2B9"));
                    tvno.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tvno.setTypeface(null, Typeface.BOLD);

                    tvno.setLayoutParams(llparamsTXT2);


                    LLprincipal.addView(tvno);
                    LLprincipal.addView(swit);
                    LLprincipal.addView(tvsi);

                    //agregando check dinamicos
                    linearPrinc.addView(tvp);
                    linearPrinc.addView(LLprincipal);

                }
            }
        }catch (Exception ex){
            Toast.makeText(this,"Se genero un exception \n " +
                                            "al crear el tipo de respuesta \n" +
                                            "en el metodo CrearSwicht" +ex,Toast.LENGTH_LONG).show();
        }
     }


    class buttonsSumRes{
        private Long codDetalle;
        private String pregunta;
        private String tipoRespuesta;

        public buttonsSumRes(Long codDetalle, String pregunta, String tipoRespuesta) {
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
        }
    }

    class SwichtQ{
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;

        public SwichtQ(int codDetalle, String pregunta, String tipoRespuesta) {
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
        }
    }

}
