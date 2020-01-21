package com.example.procesos2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.Conexion.CheckedConexion;
import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.iIndex;
import com.example.procesos2.Model.iUsuarioProceso;
import com.example.procesos2.Model.tab.IndexTab;
import com.example.procesos2.Model.tab.UsuarioProcesoTab;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Index extends AppCompatActivity {
    LinearLayout linearCheck,titulodata;
    TextView txtPanelid,txtPaneluser,txtELige,txtEstadoActivity;
    RelativeLayout contenUser;

    int sizeprocesos = 0;
    int mostrarError = 0;

    public String path = null;

    SharedPreferences sp = null;
    List<IndexTab> iP = new ArrayList<>();
    List<UsuarioProcesoTab> liUP = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_index);
        linearCheck = findViewById(R.id.LinearCheck);
        titulodata = findViewById(R.id.titulodata);
        contenUser = findViewById(R.id.contenUser);
        txtPanelid = findViewById(R.id.txtPanelid);
        txtPaneluser = findViewById(R.id.txtPaneluser);
        txtELige = findViewById(R.id.txtELige);
        txtEstadoActivity = findViewById(R.id.txtEstadoActivity);

        path = getExternalFilesDir(null) + File.separator;
        CheckedConexion Cc = new CheckedConexion();
        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        try {
            Conexion con = new Conexion(path,this);

            txtEstadoActivity.setVisibility(View.INVISIBLE);

            iIndex iI = new iIndex(con.getConexion(), path);
            iI.nombre="Procesos";

            iUsuarioProceso iUP = new iUsuarioProceso(path);
            iUP.nombre="UsuariosProceso";

            if(Cc.checkedConexionValidate(this)){
                //index
                iI.local();
                iP = iI.all();
                //usuario_proceso
                liUP = iUP.all();
            }else{
                iP = iI.all();
                liUP = iUP.all();
            }
            traerDataUser();
            validateProce();


        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Error \n"+ex,Toast.LENGTH_SHORT).show();
        }
    }



    @Override protected void onResume() {
        super.onResume();

        Boolean Temporal = sp.getBoolean("Temporal", false);
        Log.i("Temporal llegada ", String.valueOf(Temporal));
        if(Temporal){
            txtEstadoActivity.setText("Tienes formularios pendientes");
            txtEstadoActivity.setTextColor(Color.parseColor("#2ecc71"));
        }else{
            txtEstadoActivity.setText("No tienes formularios pendientes");
            txtEstadoActivity.setTextColor(Color.parseColor("#5f6a6a"));
        }
    }

    @Override protected void onPause() {
        super.onPause();
    }

    @Override protected void onRestart(){
        super.onRestart();
        //Toast.makeText(this,"Restart",Toast.LENGTH_SHORT).show();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"onDestroy index",Toast.LENGTH_SHORT).show();

        sp.edit().clear().apply();
    }

    public void traerDataUser(){
        try {
            int id = sp.getInt("codigo",0);
            String usu = sp.getString("nombre", "");

            txtPaneluser.setText("Empleado: "+usu);
            txtPanelid.setText(""+id);

        }catch (Exception ex){
            Toast.makeText(this,"Se genero un error al traer los datos del usuario \n \n"+ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void validateProce(){
        try{
            int idUser = Integer.parseInt(txtPanelid.getText().toString());

            //realiza logica para mostrar el mensaje de not found forms
            int restar = liUP.size()-1;
            mostrarError = sizeprocesos-restar;


            for(int i=0; i<liUP.size(); i++){
                if(idUser == liUP.get(i).getIdUsuario()){
                    CargaMenu(liUP.get(i).getIdProceso());
                    mostrarError = liUP.size()+1;
                }else if(idUser != liUP.get(i).getIdUsuario()){
                    sizeprocesos ++;
                }
            }

            if(mostrarError < 0){
                Messagenull(1);
            }

        }catch (Exception ex){
            Toast.makeText(this,"Ocurrio un error al generar el menu \n \n"+ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void Messagenull(int size){
        try{
            titulodata.removeView(txtELige);
            contenUser.removeView(txtEstadoActivity);

            for(int i = 0; i < size; i++) {

                LinearLayout.LayoutParams ltxtparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ltxtparams.setMargins(0,0,0,20);

                TextView tv = new TextView(getApplicationContext());
                tv.setText("Lo sentimos , \n pero no tienes formularios vinculados!!");
                tv.setTextColor(Color.parseColor("#C0392B"));
                tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextSize(25);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setLayoutParams(ltxtparams);

                ImageView btn = new ImageView(getApplicationContext());
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setAdjustViewBounds(true);
                btn.setImageDrawable(getDrawable(R.drawable.nofoundx400));

                linearCheck.addView(tv);
                linearCheck.addView(btn);
            }

        }catch (Exception ex){
            Toast.makeText(this,"Error  Message null "+ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void CargaMenu(int idProceso){
        for(int i=0;  i<iP.size(); i++) {
            if (iP.get(i).getCodProceso() == idProceso) {
                //creando boton dinamico
                ArrayList<check> lista = new ArrayList<check>();

                final String nombreTrim = iP.get(i).getNomProceso().trim();

                lista.add(new check(iP.get(i).getCodProceso().intValue(), nombreTrim));


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(30, 10, 30, 0);

                for (check c : lista) {
                    Button btn = new Button(getApplicationContext());
                    btn.setText(c.nombre);
                    btn.setId(idProceso);
                    btn.setTextColor(Color.parseColor("#FDFEFE"));
                    btn.setTextSize(16);
                    btn.setBackgroundColor(Color.parseColor("#3498db"));
                    btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    btn.setHeight(100);


                    //agregando check dinamicos
                    linearCheck.addView(btn, layoutParams);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putInt("cod_proceso", view.getId());
                            edit.putString("nom_proceso", nombreTrim);
                            edit.putString("blanquear", "");
                            edit.apply();

                            Intent intent = new Intent(getApplicationContext(), genated.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    public void Salir(View v){
        Intent i = new Intent(Index.this,Login.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void onBackPressed() {
    }

    protected class Conexion extends sqlConect{
        Connection cn = getConexion();
        String path = null;
        Context context;

        public Conexion(String path, Context context) {
            this.path = path;
            this.context = context;
            getPath(path);
        }

        public void getPath(String path) {
            this.path = path;
            if (cn != null) {
            } else {
            }
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
}
