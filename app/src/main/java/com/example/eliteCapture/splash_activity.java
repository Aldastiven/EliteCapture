package com.example.eliteCapture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eliteCapture.Config.sqlConect;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class splash_activity extends AppCompatActivity {

    private long delayed;
    long ini, fin;
    SharedPreferences sp = null;

    TextView txtStatus;

    String path = null;

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
            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);
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

    public void onActualizar() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss a");
        String fechaUpDate = sdf.format(new Date());
        String horaUpDate = sdf2.format(new Date());

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("fechaUpDate", "Fecha : " + fechaUpDate + "\n Hora : " + horaUpDate);
        edit.commit();
        edit.apply();
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
            this.path = path;
            admin = new Admin(cn, this.path);
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
                obtenerUsuarios();
                obtenerProcesos();
                obtenerDetalles();
                obtenerDesplegables();
                enviarPendientes();
                recargarSession();
                onActualizar();

            } catch (Exception ex) {
                Log.i("Error Splash Carge", ex.toString());
            }
        }

        private void obtenerUsuarios() {
            try {
                txtStatus.setText("Cargando Usuarios...");
                admin.getUsuario().local();
                msgStatus("Cargando...", "Datos de usuarios Cargado", 500);
            } catch (Exception ex) {
                Log.i("ErrorSplash_Usuario", ex.toString());
            }
        }

        private void obtenerProcesos() {
            try {
                txtStatus.setText("Cargando Procesos...");
                admin.getProceso().local();
                msgStatus("Cargando...", "Datos de procesos Cargado", 500);
            } catch (Exception ex) {
                Log.i("ErrorSplash", ex.toString());
            }
        }

        private void obtenerDetalles() {
            try {
                txtStatus.setText("Cargando Detalles...");
                admin.getDetalles().local();
                msgStatus("Cargando...", "Datos de Detalles Cargado", 1000);
            } catch (Exception ex) {
                Log.i("ErrorSplashDetalle", ex.toString());
            }
        }

        private void obtenerDesplegables() {
            try {

                for (String e : admin.getDesplegable().group()) {

                    Log.i("resultado final ", e);
                    admin.getDesplegable().setNombre(e);
                    admin.getDesplegable().traerDesplegable(e);
                    admin.getProductos().local();
                    msgStatus("Cargando...", "Datos de Detalles: " + e, 200);
                }

            } catch (Exception ex) {
                Log.i("ErrorSplash", ex.toString());
            }
        }


        private void enviarPendientes() {
            try {
                boolean enviado = new iContenedor(path).enviar();
                msgStatus("Cargando...", "Enviando pendientes.", 200);
                Log.i("Envio", (enviado) ? "Envio exitoso" : "Error de envio");

            } catch (Exception ex) {
                Log.i("ErrorSplash_Envio", ex.toString());
            }
        }

        public void recargarSession() {
            String Session = sp.getString("usuario", "");
            UsuarioTab sess = null;
            try {
                if (!Session.isEmpty()) {
                    sess = new Gson()
                            .fromJson(Session,
                                    new TypeToken<UsuarioTab>() {
                                    }.getType());

                   sess = admin.getUsuario().login(sess.getId_usuario(), sess.getPassword());
                    SharedPreferences.Editor edit = sp.edit();
                    Session = admin.getUsuario().json(sess);

                    Log.i("Envio_Session", Session);
                    edit.putString("usuario", Session);
                    msgStatus("Cargando...", "Recargando Sesión.", 200);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        private void msgStatus(final String msgCarga, final String msgCompletado, final int tiempoSalteado) {
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
                                int dur = 500;
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
