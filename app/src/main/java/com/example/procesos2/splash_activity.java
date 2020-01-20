package com.example.procesos2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Model.iDetalles;
import com.example.procesos2.Model.iIndex;
import com.example.procesos2.Model.iUsuarios;
import com.example.procesos2.Model.tab.IndexTab;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class splash_activity extends AppCompatActivity {

    private long delayed;
    long ini, fin;

    TextView txtStatus;

    String path = null;
    Calendar calendarDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activity);

        txtStatus = (TextView) findViewById(R.id.idStatus);

        delayed = 5000;
        path = getExternalFilesDir(null) + File.separator;

        getSupportActionBar().hide();


        ini = Calendar.getInstance().getTimeInMillis();
        try {
            Conexion con = new Conexion(path, this, txtStatus);

            fin = Calendar.getInstance().getTimeInMillis();
            if((fin - ini)>5000){
                delayed = 0;
            }else{
                delayed = delayed - (fin - ini);
            }


            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent i = new Intent(splash_activity.this, Login.class);
                    startActivity(i);
                    finish();
                };
            },delayed);

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();;
        }


    }

    protected class Conexion extends sqlConect {

        Connection cn = null;
        String path = null;
        JsonAdmin ja = null;

        Context context;
        TextView Status;

        public Conexion(String path, Context context, TextView status) {
            this.cn = getConexion();
            this.path = path;
            this.context = context;
            Status = status;
            getPath(path);
        }

        public void getPath(String path) {
            ja = new JsonAdmin();
            this.path = path;
            if (cn != null) {
                txtStatus.setText("Conectado...");
                CargeInicial();
            } else {
                txtStatus.setText("No hay Conexion a la BD");
            }
        }

        private void CargeInicial() {
            ObtenerUsuarios();
            ObtenerProcesos();
            ObtenerDetalles();
            ObtenerDesplegables();
        }

        public void ObtenerUsuarios() {
            try {
                txtStatus.setText("Cargando Usuarios...");
                iUsuarios iL = new iUsuarios(path);
                iL.nombre = "Usuarios";
                iL.local();
                //msgStatus("Cargando...","Datos de usuarios Cargado",10);
            } catch (Exception ex) {
                Log.i("Error", ex.toString());
            }
        }

        public void ObtenerProcesos() {
            try {
                txtStatus.setText("Cargando Procesos...");
                iIndex iI = new iIndex(path);
                iI.nombre = "Procesos";
                iI.local();
                //msgStatus("Cargando...","Datos de procesos Cargado",10);
            } catch (Exception ex) {
                Log.i("Error", ex.toString());
            }
        }

        public void ObtenerDetalles() {
            try {
                txtStatus.setText("Cargando Detalles...");
                iDetalles iI = new iDetalles(path);
                iI.nombre = "Detalles";
                iI.local();
                //msgStatus("Cargando...","Datos de Detalles Cargado",10);
            } catch (Exception ex) {
                Log.i("Error", ex.toString());
            }
        }

        public void ObtenerDesplegables() {
            try {
                iDesplegable iD = new iDesplegable(path);
                //iD.nombre = iD.group();
                iD.traerDesplegable(iD.group());
                //msgStatus("Cargando...","Datos de desplegables Cargado",10);
            } catch (Exception ex) {
                Log.i("Error", ex.toString());
            }
        }

        public void msgStatus(final String msgCarga, final String msgCompletado, final int tiempoSalteado) {
            try {
                final int totalProgressTime = 100;
                final Thread t = new Thread() {
                    @Override
                    public void run() {
                        int jumpTime = 0;

                        while (jumpTime < totalProgressTime) {
                            try {
                                jumpTime += tiempoSalteado;
                                txtStatus.setText(msgCarga);
                                sleep(200);
                            } catch (InterruptedException e) {
                                Toast.makeText(splash_activity.this, "Progress Bar \n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int dur = 2500;
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        txtStatus.setText(msgCompletado);
                                    }
                                }, dur);

                            }
                        });

                    }
                };
                t.start();
            }catch (Exception ex){
                Toast.makeText(context, "error "+ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }


    }



}
