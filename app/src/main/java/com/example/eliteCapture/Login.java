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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Modal.modalServer;
import com.example.eliteCapture.Config.Util.Modal.modalSetting;
import com.example.eliteCapture.Config.sqlConect;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.iSesion;
import com.example.eliteCapture.Model.Data.iUsuario;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.Data.ionLine;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.iContenedor;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import static com.example.eliteCapture.R.*;
import static com.example.eliteCapture.R.drawable.*;


public class Login extends AppCompatActivity {
    SharedPreferences sp;

    public String path = null;
    EditText txtUser, txtPass;
    TextView txtError, floatingServer;
    ImageView imgOnline;

    iContenedor icont;
    iSesion is;
    ionLine ionLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);
        getSupportActionBar().hide();

        Screen();

        path = getExternalFilesDir(null) + File.separator;
        try {
            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

            txtUser = findViewById(id.txtUser);
            txtPass = findViewById(id.txtPass);
            imgOnline = findViewById(id.imgOnline);
            floatingServer = findViewById(id.floatingServer);

            iUsuario iU = new iUsuario(null, path);
            iU.nombre = "Usuarios";

            recibirUsuario();

            icont = new iContenedor(path);
            is = new iSesion(path);
            ionLine = new ionLine(path);

            imgOnline.setBackgroundResource(new ionLine(path).all().equals("onLine") ? ic_wifi_on : ic_wifi_off);
            floatingServer.setCompoundDrawablesWithIntrinsicBounds(icont.pendientesCantidad() > 0 ? ic_cloud_noti : ic_cloud, 0, 0, 0);

            if(new Conexion().getConexion() == null) {
                imgOnline.setBackgroundResource(ic_wifi_off);
                ionLine.local("offLine");
                new ionLine(path).local("offLine");
            }
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
                String session = admin.getUsuario().json(m);
                Log.i("Session:", session);

                edit.putString("usuario", session);
                edit.putString("check", "ok");
                edit.commit();
                edit.apply();

                Intent intent = new Intent(this, Index.class);
                startActivity(intent);

                guardarUsuario(txt_user, txt_pass, m.getGrupo2());

                borrarTemp(m.getId_usuario());
                new iSesion(path).local(m.getId_usuario());

            } else if (m == null) {
                txtUser.setBackgroundResource(bordercontainerred);
                txtPass.setBackgroundResource(bordercontainerred);
                txtError.setText("Identificaficación o contraseña incorrectas !!!");
                new iSesion(path).local(0000);
            }

        } catch (NumberFormatException ex) {
            txtUser.setBackgroundResource(bordercontainerred);
            txtPass.setBackgroundResource(bordercontainerred);
            txtError.setText("No puedes dejar campos vacios !!!");
            new iSesion(path).local(0000);
        }
    }

    public void borrarTemp(int usuIngreso){
        try {
            if (is.all() != usuIngreso) {
                icont.crearTemporal(null);
            }
        }catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
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

    public void guardarUsuario(int id, int pass, String fincaAsignada) {
        try {

            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("codigoLogin", id);
            edit.putInt("passwordLogin", pass);
            edit.putString("fincaAsignada", fincaAsignada);
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


    public void onActualizar(View v) {
        new modalServer(this, path).modal().show();
    }

    public void Settings(View v){
        new modalSetting(this, path, imgOnline).modal().show();
    }

    public void onBackPressed() {
    }

    protected class Conexion extends sqlConect {
    }

}
