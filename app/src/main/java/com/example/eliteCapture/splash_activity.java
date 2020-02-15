package com.example.eliteCapture;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eliteCapture.Config.sqlConect;
import com.example.eliteCapture.Model.Data.Admin;

import java.io.File;
import java.sql.Connection;
import java.util.Calendar;

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
            CargaDeDatos cdd = new CargaDeDatos(path, this, txtStatus);

            fin = Calendar.getInstance().getTimeInMillis();
            if ((fin - ini) > 5000) {
                delayed = 0;
            } else {
                delayed = delayed - (fin - ini);
            }


            new Handler().postDelayed(new Runnable() {
                public void run() {
                    redirecion();
                }

                ;
            }, delayed);

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    public void redirecion() {
        try {
            Intent i;
            if (getActivity() != null) {
                i = new Intent(splash_activity.this, getActivity());
            } else {
                i = new Intent(splash_activity.this, Login.class);
            }
            startActivity(i);
            finish();
        } catch (Exception ex) {
            Toast.makeText(this, "Exception al redirecionar a la clase \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public Class getActivity() {
        Class act = Login.class;
        try {
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                String code = bundle.getString("class");
                if (code.equals("Index")) {
                    act = Index.class;
                }
            } else {
            }


        } catch (Exception ex) {
            return act;
        }
        return act;
    }


    protected class CargaDeDatos extends sqlConect {

        Connection cn = null;
        String path = null;
        Admin admin = null;

        Context context;
        TextView Status;


        public CargaDeDatos(String path, Context context, TextView status) {
            Connection cn = getConexion();
            admin = new Admin(getConexion(), path);
            this.context = context;
            Status = status;

            // Validando conexion
            if (cn != null) {
                txtStatus.setText("Conectado...");
                CargeInicial();
            } else {
                txtStatus.setText("No hay Conexion a la BD");
            }
        }


        private void CargeInicial() {
            try {
                ObtenerUsuarios();
                ObtenerProcesos();
                ObtenerDetalles();
                ObtenerDesplegables();

            } catch (Exception ex) {
                Log.i("Error Splash Carge", ex.toString());
            }
        }

        public void ObtenerUsuarios() {
            try {
                txtStatus.setText("Cargando Usuarios...");
                admin.getUsuario().local();
                msgStatus("Cargando...", "Datos de usuarios Cargado", 500);
            } catch (Exception ex) {
                Log.i("Error", ex.toString());
            }
        }

        public void ObtenerProcesos() {
            try {
                txtStatus.setText("Cargando Procesos...");
                admin.getProceso().local();
                msgStatus("Cargando...", "Datos de procesos Cargado", 500);
            } catch (Exception ex) {
                Log.i("Error", ex.toString());
            }
        }

        public void ObtenerDetalles() {
            try {
                txtStatus.setText("Cargando Detalles...");
                admin.getDetalles().local();
                msgStatus("Cargando...", "Datos de Detalles Cargado", 1000);
            } catch (Exception ex) {
                Log.i("Error", ex.toString());
            }
        }

        public void ObtenerDesplegables() {
            try {

                for (String e : admin.getDesplegable().group()) {

                    Log.i("resultado final ", e);
                    admin.getDesplegable().setNombre(e);
                    admin.getDesplegable().traerDesplegable(e);
                    msgStatus("Cargando...", "Datos de Detalles: " + e, 200);
                }

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
            } catch (Exception ex) {
                Toast.makeText(context, "error " + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }


    }

}
