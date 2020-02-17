package com.example.eliteCapture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.sql.Connection;

public class Index extends AppCompatActivity {
  LinearLayout linearCheck, titulodata;
  TextView txtPanelid, txtPaneluser, txtELige, txtEstadoActivity;
  RelativeLayout contenUser;

  int sizeprocesos = 0;
  int mostrarError = 0;

  public String path = null;

  SharedPreferences sp = null;
  UsuarioTab usu;


  Connection cn = null;
  Admin admin = null;

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
    sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

    try {


      admin = new Admin(null, path);
      txtEstadoActivity.setVisibility(View.INVISIBLE);


      traerDataUser();
      CargaMenu();

    } catch (Exception ex) {
      Toast.makeText(getApplicationContext(), "Error \n" + ex, Toast.LENGTH_SHORT).show();
    }
  }


  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  public void traerDataUser() {
    try {

      usu = new Gson().fromJson(sp.getString("usuario", ""), new TypeToken<UsuarioTab>() {
      }.getType());

      txtPaneluser.setText("Empleado: " + usu.getNombre_usuario());
      txtPanelid.setText("Te vamos a quitar :D");

    } catch (Exception ex) {
      Toast.makeText(this, "Se genero un error al traer los datos del usuario \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
    }
  }


  public void Messagenull(int size) {
    try {
      titulodata.removeView(txtELige);
      contenUser.removeView(txtEstadoActivity);

      for (int i = 0; i < size; i++) {

        LinearLayout.LayoutParams ltxtparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ltxtparams.setMargins(0, 0, 0, 20);

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

    } catch (Exception ex) {
      Toast.makeText(this, "Error  Message null " + ex.toString(), Toast.LENGTH_SHORT).show();
    }
  }

  public void CargaMenu() {


    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    layoutParams.setMargins(30, 10, 30, 0);

    try {
      for (final ProcesoTab c : admin.getProceso().procesosUsuario(usu.getProcesos())) {
        Log.i("Procesos:", c.getNombre_proceso());
        Button btn = new Button(getApplicationContext());
        btn.setText(c.getNombre_proceso());
        btn.setId(c.getCodigo_proceso());
        btn.setTextColor(Color.parseColor("#ffffff"));
        btn.setTextSize(16);
        btn.setBackgroundResource(R.drawable.buttongreen);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn.setHeight(100);

        //agregando check dinamicos
        linearCheck.addView(btn, layoutParams);

        btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            SharedPreferences.Editor edit = sp.edit();
            try {
              edit.putString("proceso", admin.getProceso().json(c));
              edit.apply();
            } catch (Exception e) {
              Log.i("Error onClick, Index", e.toString());
            }

            Intent intent = new Intent(getApplicationContext(), genated.class);
            startActivity(intent);
          }
        });
      }
    } catch (Exception e) {

      Log.i("Error PinProceso, Index", e.toString());
      e.printStackTrace();
    }

  }

  public void Salir(View v) {
    Intent i = new Intent(Index.this, Login.class);
    startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
  }

  public void onActualizar(View v) {
    Intent i = new Intent(Index.this, splash_activity.class);
    i.putExtra("class", "Index");
    startActivity(i);
  }

  public void onBackPressed() {
  }

}
