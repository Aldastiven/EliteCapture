package com.example.eliteCapture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Conexion.CheckedConexion;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.iUsuario;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    SharedPreferences sp;

    public String path = null;

    List<UsuarioTab> UP = new ArrayList<>();

    EditText txtUser, txtPass;
    LinearLayout linearBox;
    TextView txtError;
    CheckBox checkusu;
    String datacheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Screen();

        path = getExternalFilesDir(null) + File.separator;
        CheckedConexion Cc = new CheckedConexion();

        try {

            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

            txtUser = findViewById(R.id.txtUser);
            txtPass = findViewById(R.id.txtPass);
            linearBox = findViewById(R.id.linearBox);

            txtError = findViewById(R.id.txtError);
            checkusu = findViewById(R.id.guardarUsuario);

            iUsuario iU = new iUsuario(null, path);
            iU.nombre = "Usuarios";

            checkusu.setChecked(false);

            recibirUsuario();
            PintarCheck();

        } catch (Exception ex) {
            Toast.makeText(this, "Se genero un Error al traer los datos de Usuario \n \n" + ex.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void ingresar(View v) throws Exception {
        try {
            int txt_user = Integer.parseInt(txtUser.getText().toString());
            int txt_pass = Integer.parseInt(txtPass.getText().toString());
            Admin admin = new Admin(null, path);
            UsuarioTab m = admin.getUsuario().login(txt_user, txt_pass);

            if (m != null) {

                SharedPreferences.Editor edit = sp.edit();
                Log.i("Session:", admin.getUsuario().json(m));
                edit.putString("usuario", admin.getUsuario().json(m));
                edit.putInt("codigo", m.getPassword());
                edit.putString("check", "ok");
                edit.commit();
                edit.apply();

                Intent intent = new Intent(this, Index.class);
                startActivity(intent);

                guardarUsuario();

            } else if (m == null) {
                txtUser.setBackgroundResource(R.drawable.bordercontainerred);
                txtPass.setBackgroundResource(R.drawable.bordercontainerred);
                txtError.setText("Identificaficación o contraseña incorrectas !!!");
            }

        } catch (NumberFormatException ex) {
            txtUser.setBackgroundResource(R.drawable.bordercontainerred);
            txtPass.setBackgroundResource(R.drawable.bordercontainerred);
            txtError.setText("No puedes dejar campos vacios !!!");
        }
    }

    public void Screen() {
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

            LinearLayout.LayoutParams ltxtparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ltxtparams.width = 500;

        } else if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

        }
    }

    public void guardarUsuario() {
        try {

            int id = Integer.parseInt(txtUser.getText().toString());
            int pass = Integer.parseInt(txtPass.getText().toString());

            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("codigoLogin", id);
            edit.putInt("passwordLogin", pass);
            edit.commit();
            edit.apply();



        } catch (Exception e) {
            Toast.makeText(this, "Error \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void recibirUsuario() {
        try {
            if (sp != null) {
                int id = sp.getInt("codigoLogin", 0);
                int pass = sp.getInt("passwordLogin", 0);

                if (id == 0 || pass == 0) {
                    txtUser.setText("");
                    txtPass.setText("");
                } else {
                    txtUser.setText("" + id);
                    txtPass.setText("" + pass);
                }
            } else {
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void PintarCheck() {
        try {
            final SharedPreferences.Editor edit = sp.edit();

            if (sp != null) {

                datacheck = sp.getString("check", "");

                if (datacheck.equals("ok")) {
                    checkusu.setChecked(true);
                } else if (datacheck.equals("bad")) {
                    checkusu.setChecked(false);
                }
            }

            checkusu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    if (!isChecked) {
                        edit.putString("check", "bad");
                        edit.commit();
                        edit.apply();

                        SharedPreferences.Editor edit = sp.edit();
                        edit.clear().commit();
                        edit.apply();

                        txtError.setText("");
                    }else {
                        if(txtUser.getText().toString().isEmpty() || txtPass.getText().toString().isEmpty()){
                            txtError.setText("¡ No se puede guardar tu usuario. \n asegurate de no tener campos vacios !");
                            checkusu.setChecked(false);
                            checkusu.setSelected(false);
                            checkusu.setEnabled(true);
                        }else{
                            txtError.setText("");
                        }
                    }
                }
            });
        } catch (Exception ex) {
        }
    }

}
