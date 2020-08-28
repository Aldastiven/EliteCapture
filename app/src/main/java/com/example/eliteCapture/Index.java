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

import com.example.eliteCapture.Model.View.Tab.ContadorTab;
import com.example.eliteCapture.Model.View.iContador;
import com.example.eliteCapture.Model.View.iContenedor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Index extends AppCompatActivity {
    LinearLayout linearCheck, titulodata;
    TextView txtPaneluser, txtELige, txtUpdateData;
    RelativeLayout contenUser;

    public String path = null;

    SharedPreferences sp = null;
    UsuarioTab usu;

    Admin admin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_index);
        linearCheck = findViewById(R.id.LinearCheck);
        titulodata = findViewById(R.id.titulodata);
        contenUser = findViewById(R.id.contenUser);
        txtPaneluser = findViewById(R.id.txtPaneluser);
        txtELige = findViewById(R.id.txtELige);

        path = getExternalFilesDir(null) + File.separator;
        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        try {

            admin = new Admin(null, path);

            traerDataUser();
            traerFechaUpDate();
            CargaMenu();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error \n" + ex, Toast.LENGTH_SHORT).show();
        }
    }

    private void traerFechaUpDate() {
        String fechaUp = sp.getString("fechaUpDate", "");
    }

    public void traerDataUser() {
        try {
            usu = new Gson().fromJson(sp.getString("usuario", ""), new TypeToken<UsuarioTab>() {
            }.getType());

            txtPaneluser.setText("Usuario : " + usu.getNombre_usuario());
        } catch (Exception ex) {
            Toast.makeText(this, "Se genero un error al traer los datos del usuario \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void CargaMenu() {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 10, 30, 0);

        try {
            iContador contar = new iContador(path);

            for (final ProcesoTab c : admin.getProceso().procesosUsuario(usu.getProcesos())) {

                LinearLayout.LayoutParams layoutParamsbtn = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                int cuenta = contar.getCantidad(usu.getId_usuario(), c.getCodigo_proceso());
                String cuenText = (cuenta > 0) ? " (" + cuenta + ")" : "";
                Log.i("Procesos:", c.getNombre_proceso());
                Button btn = new Button(getApplicationContext());
                btn.setText(c.getNombre_proceso() + cuenText);
                btn.setId(c.getCodigo_proceso());
                btn.setTextColor(Color.parseColor("#ffffff"));
                btn.setTextSize(16);
                //btn.setBackgroundColor(Color.parseColor("#27ae60"));
                btn.setBackgroundColor(Color.parseColor("#2e2d33"));
                btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                btn.setLayoutParams(layoutParamsbtn);

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

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("usuario", "");

        edit.apply();

        Intent i = new Intent(Index.this, Login.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void onActualizar(View v) {
        Intent i = new Intent(Index.this, splash_activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("class", "Index");

        startActivity(i);
        linearCheck.removeAllViews();
    }

    public void onBackPressed() {
    }

}
