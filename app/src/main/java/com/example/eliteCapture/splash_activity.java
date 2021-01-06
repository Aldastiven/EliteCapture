package com.example.eliteCapture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eliteCapture.Config.Util.text.textAdmin;
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
  LinearLayout noti;
  String path = null, carga;

  int intance = 0000;

  textAdmin ta;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_splash_activity);

    txtStatus = (TextView) findViewById(R.id.idStatus);
    noti = findViewById(R.id.noti);

    delayed = 5000;
    path = getExternalFilesDir(null) + File.separator;

    getSupportActionBar().hide();


    ini = Calendar.getInstance().getTimeInMillis();
    try {
      sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);
      getActivity();

      ta = new textAdmin(this);

      String intent = screen() == 0 ? "intent" : "conexion";
      if(intent.equals("conexion"))  noti.addView(ta.textColor(carga.equals("BajarDatos") ? "Descargando datos, espera un momento ..." : "Enviando datos, espera un momento ...","negro",15, "c"));
      temporizador(intent, 3000, screen());

    } catch (Exception e) {
      Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
    }
  }

  public int screen(){
    Bundle b = getIntent().getExtras();
    int s = b != null ? b.getInt("redireccion",0) : 0;
    String enviDes = b != null ? b.getString("carga","") : "";
    return s;
  }

  public void temporizador(final String tipo, int duracion, final int intent){
    new Handler().postDelayed(new Runnable() {
      public void run() {
        if(tipo.equals("conexion")) {
          new CargaDeDatos(path, splash_activity.this, txtStatus);
        }else if(tipo.equals("intent")) {
          intent(intent);
        }
      }
    }, duracion);
  }

  public void intent(int s){
    Intent i = null;
    switch (s){
      case 0 :
        i = new Intent(this, Login.class);
        break;
      case 1 :
        i = new Intent(this, Login.class);
        break;
    }
    startActivity(i);
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


  public Class getActivity() {
    Class act = Login.class;
    try {
      Bundle bundle = getIntent().getExtras();

      if (bundle != null) {
        String code = bundle.getString("class");
        carga = bundle.getString("carga");
        if (code.equals("Index")) {
          act = Index.class;
        } else if(code.equals("Login")){
          act = Login.class;
        }
      }
    } catch (Exception ex) {
      return act;
    }
    return act;
  }


  protected class CargaDeDatos extends sqlConect {

    Connection cn;
    String path;
    Admin admin;

    Context context;
    TextView Status;


    public CargaDeDatos(String path, Context context, TextView status) {
      try {
        cn = getConexion();
        this.path = path;
        admin = new Admin(cn, this.path);
        this.context = context;
        Status = status;

        String onLine = admin.getOnline().all();
        if (onLine.isEmpty() || onLine.equals("no existe")) {
          admin.getOnline().local("offLine");
        }

        if (admin.getOnline().all().equals("onLine")) {
          // Validando conexion
          if (cn != null) {
            CargeInicial();
            if(noti.getChildCount() > 0) noti.removeAllViews();
            noti.addView(ta.textColor(carga.equals("BajarDatos") ? "Datos descargados con exito" : "Datos enviados con exito","verde",15, "c"));
          } else {
            if(noti.getChildCount() > 0) noti.removeAllViews();
            noti.addView(ta.textColor("No hay conexión","rojo",15, "c"));
            temporizador(screen(), 3000);
          }
        } else {
        }
      }catch (Exception e){
        Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
      }
    }


    private void CargeInicial() {
      try {

        if(carga.equals("BajarDatos")){
          obtenerUsuarios();
          obtenerProcesos();
          obtenerDetalles();
          obtenerDesplegables();
        }else{
          enviarPendientes();
          recargarSession();
        }
        onActualizar();
        temporizador(screen(), 3000);
      } catch (Exception ex) {
        Log.i("Error Splash Carge", ex.toString());
      }
    }

    private void obtenerUsuarios() {
      try {
        admin.getUsuario().local();
      } catch (Exception ex) {
        Log.i("ErrorSplash_Usuario", ex.toString());
      }
    }

    private void obtenerProcesos() {
      try {
        admin.getProceso().local();
      } catch (Exception ex) {
        Log.i("ErrorSplash", ex.toString());
      }
    }

    private void obtenerDetalles() {
      try {
        admin.getDetalles().local();
      } catch (Exception ex) {
        Log.i("ErrorSplashDetalle", ex.toString());
      }
    }

    private void obtenerDesplegables() {
      try {

        for (String e : admin.getDesplegable().group()) {
          admin.getDesplegable().setNombre(e);
          admin.getDesplegable().traerDesplegable(e);
        }
        //admin.getProductos().local();
        admin.getProductos().consultarSql();
      } catch (Exception ex) {
        Log.i("ErrorSplash", ex.toString());
      }
    }


    private void enviarPendientes() {
      try {
        boolean enviado = new iContenedor(path).enviar();
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

          edit.putString("usuario", Session);

        }

      } catch (Exception e) {
        e.printStackTrace();
      }


    }

    public void temporizador(final int intent, final int duracion){
      new Handler().postDelayed(new Runnable() {
        public void run() {
          intent(intent);
        }
      }, duracion);
    }

  }

}