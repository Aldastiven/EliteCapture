package com.example.procesos2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.procesos2.Conexion.CheckedConexion;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.iIndex;
import com.example.procesos2.Model.tab.IndexTab;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Index extends AppCompatActivity {
    LinearLayout linearCheck;

    public String path = null;

    SharedPreferences sp = null;
    List<IndexTab> iP = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_index);
        linearCheck = findViewById(R.id.LinearCheck);


        path = getExternalFilesDir(null) + File.separator;
        CheckedConexion Cc = new CheckedConexion();
        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        try {
            iIndex iI = new iIndex(path);
            iI.nombre="procesos";

            if(Cc.checkedConexionValidate(this)){
                iI.local();
                iP = iI.all();
                CargaMenu();
            }else{
                Toast.makeText(getApplicationContext(), "Por el momenta la app esta trabando con recursos internos", Toast.LENGTH_SHORT).show();
                iP = iI.all();
                CargaMenu();
            }


        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Error \n"+ex,Toast.LENGTH_SHORT).show();
        }
    }

    class check{
        public  int codigo;
        public  String nombre;

        public check(int codigo, String nombre) {
            this.codigo = codigo;
            this.nombre = nombre;
        }
    }

    public void CargaMenu(){
        for(int i=0;  i<iP.size(); i++){
            //creando boton dinamico
            ArrayList<check> lista =  new ArrayList<check>();
            String nombreTrim = iP.get(i).getNomProceso().trim();

            lista.add(new check(iP.get(i).getCodProceso().intValue(),nombreTrim));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(30, 10, 30, 0);

            for (check c : lista){
                Button btn = new Button(getApplicationContext());
                btn.setText(c.nombre);
                btn.setId(c.codigo);
                btn.setTextColor(Color.parseColor("#FDFEFE"));
                btn.setTextSize(20);
                btn.setBackgroundColor(Color.parseColor("#2980B9"));
                btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //agregando check dinamicos
                linearCheck.addView(btn,layoutParams);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("cod_proceso", view.getId());
                        edit.apply();

                        Intent intent = new Intent(getApplicationContext(),genated.class);
                        startActivity(intent);

                        //Toast.makeText(getApplicationContext(),"cod \n"+view.getId(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}
