package com.example.procesos2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.Conexion.CheckedConexion;
import com.example.procesos2.Model.iUsuarioProceso;
import com.example.procesos2.Model.iUsuarios;
import com.example.procesos2.Model.tab.UsuarioProcesoTab;
import com.example.procesos2.Model.tab.UsuariosTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    SharedPreferences sp;

    public String path = null;

    List<UsuariosTab> UP = new ArrayList<>();
    List<UsuarioProcesoTab> UPP = new ArrayList<>();

    EditText txtUser, txtPass;
    LinearLayout linearBox;
    TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Screen();

        path = getExternalFilesDir(null) + File.separator;
        CheckedConexion Cc = new CheckedConexion();

        try{

            sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

            txtUser = findViewById(R.id.txtUser);
            txtPass = findViewById(R.id.txtPass);
            linearBox = findViewById(R.id.linearBox);

            txtError = findViewById(R.id.txtError);

            iUsuarios iU = new iUsuarios(path);
            iU.nombre = "Usuarios";

            iUsuarioProceso iUP = new iUsuarioProceso(path);
            iUP.nombre = "UsuariosProceso";

            if(Cc.checkedConexionValidate(this)){

                iU.nombre = "Usuarios";
                //carga de datos usuarios al json
                iU.local();
                UP = iU.all();

                //carga de datos usuarios rel proceso al json
                iUP.local();
                UPP = iUP.all();
            }else {

                iUP.nombre = "UsuariosProceso";

                UP = iU.all();
                UPP = iUP.all();

            }

        }catch (Exception ex){
            Toast.makeText(this,"Se genero un Error al traer los datos de usuarios \n \n"+ex.toString(),Toast.LENGTH_LONG).show();
        }

    }

    public void ingresar(View v) throws Exception{
        try {
            int txt_user = Integer.parseInt(txtUser.getText().toString());
            int txt_pass = Integer.parseInt(txtPass.getText().toString());

            iUsuarios iU = new iUsuarios(path);
            iU.nombre = "Usuarios";
            iU.all();


            UsuariosTab m = iU.login(txt_user, txt_pass);

                if (m !=null){

                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("codigo", m.getIdUsuario());
                    edit.putString("nombre", m.getNombreUsuario());
                    edit.commit();
                    edit.apply();

                    Intent intent = new Intent(this, Index.class);
                    startActivity(intent);
                }else if(m == null){
                    txtUser.setBackgroundResource(R.drawable.bordercontainerred);
                    txtPass.setBackgroundResource(R.drawable.bordercontainerred);
                    txtError.setText("Identificaficación o contraseña incorrectas !!!");
                }

        }catch (NumberFormatException ex){
            txtUser.setBackgroundResource(R.drawable.bordercontainerred);
            txtPass.setBackgroundResource(R.drawable.bordercontainerred);
            txtError.setText("No puedes dejar campos vacios !!!");
        }
    }

    public void Screen(){
        if((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

            LinearLayout.LayoutParams ltxtparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ltxtparams.width = 500;

        }else if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

        }
    }

    public void SaveLogin(){
        try{

        }catch (Exception ex){

        }
    }

}
