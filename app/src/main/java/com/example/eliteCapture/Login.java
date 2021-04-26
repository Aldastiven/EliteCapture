package com.example.eliteCapture;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Controls.GIDGET;
import com.example.eliteCapture.Config.Util.Modal.modalServer;
import com.example.eliteCapture.Config.Util.Modal.modalSetting;
import com.example.eliteCapture.Config.Util.permissions.permissionAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Config.ftpConect;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.Data.Tab.jsonPlanTab;
import com.example.eliteCapture.Model.Data.Tab.listFincasTab;
import com.example.eliteCapture.Model.Data.iJsonPlan;
import com.example.eliteCapture.Model.Data.iSesion;
import com.example.eliteCapture.Model.Data.iUsuario;
import com.example.eliteCapture.Model.Data.ionLine;
import com.example.eliteCapture.Model.View.iContenedor;

import java.io.File;
import java.util.List;

import static com.example.eliteCapture.R.drawable.bordercontainerred;
import static com.example.eliteCapture.R.drawable.ic_cloud;
import static com.example.eliteCapture.R.drawable.ic_cloud_noti;
import static com.example.eliteCapture.R.drawable.ic_wifi_off;
import static com.example.eliteCapture.R.drawable.ic_wifi_on;
import static com.example.eliteCapture.R.id;
import static com.example.eliteCapture.R.layout;


public class Login extends AppCompatActivity {
    SharedPreferences sp;

    public String path = null;
    int idFinca;
    EditText txtUser, txtPass;
    TextView txtError, floatingServer, txtFarmWork;
    ImageView imgOnline;
    LinearLayout notification, notificationFarm;

    iContenedor icont;
    iSesion is;
    ionLine ionLine;
    permissionAdmin pa;

    iJsonPlan ipl;

    GIDGET gg;
    containerAdmin ca;
    textAdmin ta;

    Dialog dialogListFincas;

    modalSetting modalSetting;

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
            txtError = findViewById(id.txtError);
            imgOnline = findViewById(id.imgOnline);
            floatingServer = findViewById(id.floatingServer);
            notification = findViewById(id.lineNotication);
            txtFarmWork = findViewById(id.txtFarmWork);

            dialogListFincas = new Dialog(this);
            iUsuario iU = new iUsuario(null, path);
            iU.nombre = "Usuarios";

            recibirUsuario();

            icont = new iContenedor(path);
            is = new iSesion(path);
            ionLine = new ionLine(path);
            pa = new permissionAdmin(this);
            pa.permissionGrantedCamera();

            String user = txtUser.getText().toString();

            ipl = new iJsonPlan(path, Integer.parseInt(user.isEmpty() ? "0000" : user), null);
            gg = new GIDGET(this, "", null , path);
            ca = new containerAdmin(this);
            ta = new textAdmin(this);



            modalSetting = new modalSetting(this, path, imgOnline, notification);
            modalSetting.modal();

            imgOnline.setBackgroundResource(ionLine.all().equals("onLine") ? ic_wifi_on : ic_wifi_off);
            floatingServer.setCompoundDrawablesWithIntrinsicBounds(icont.pendientesCantidad() > 0 ? ic_cloud_noti : ic_cloud, 0, 0, 0);
        } catch (Exception ex) {
            Toast.makeText(this, "Error onCrete : \n" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void ingresar(View v) throws Exception {
        try {
            Admin admin = new Admin(null, path);

            if(pa.getPermissionCamera() == -1){
                txtError.setText("No tienes permisos en la aplicacion !!!");
                pa.permissionGrantedCamera();
            }else {
                String dataUser = txtUser.getText().toString();
                String dataPass = txtPass.getText().toString();
                if(new iUsuario(null, path).all() != null) {

                    if (!dataUser.isEmpty() && !dataPass.isEmpty()) {
                        int txt_user = Integer.parseInt(dataUser);
                        int txt_pass = Integer.parseInt(dataPass);
                        UsuarioTab m = admin.getUsuario().login(txt_user, txt_pass, txtError);

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
                            txtError.setText("¡Identificación o contraseña incorrectas!");
                            new iSesion(path).local(0000);
                        }
                    } else {
                        txtUser.setBackgroundResource(bordercontainerred);
                        txtPass.setBackgroundResource(bordercontainerred);
                        txtError.setText("¡No puedes dejar campos vacios !");
                        new iSesion(path).local(0000);
                    }
                }else{
                    txtError.setText("¡No tienes datos de usuario, por favor realiza la descarga!");
                }
            }
        } catch (Exception ex) {
            txtError.setText("Error : "+ex.toString());
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
        new modalServer(this, path, txtUser.getText().toString()).modal().show();
    }

    public void Settings(View v){
        modalSetting.modal().show();
    }

    public void DowloadFarms(View v){
        try {
            LinearLayout linePrincipal = ca.container();
            LinearLayout lineText = ca.container();
            LinearLayout line = ca.container();
            line.setBackgroundColor(Color.parseColor("#EAEDED"));

            if(!txtUser.getText().toString().isEmpty()) {

                lineText.addView(ta.textColor(" Elige una finca para continuar el proceso de descarga", "darkGray", 15, "l"));

                if (ipl.allListFincas() != null) {
                    for (listFincasTab lf : ipl.allListFincas()) {
                        if (lf.getUsuario() == Integer.parseInt(txtUser.getText().toString())) {
                            for (listFincasTab.fincasTab finca : lf.getFincas()) {
                                getItem(finca, line);
                            }
                        }else{
                            lineText.removeAllViews();
                            lineText.addView(ta.textColor("  ¡Las fincas actuales no estan asociadas  al codigo de usuario, por favor actualiza las fincas e intentalo nuevamente!", "rojo", 15, "l"));
                            break;
                        }
                    }
                } else {
                    line.addView(ta.textColor(
                            "No se encontraron fincas Asociadas con el usuario",
                            "rojo",
                            15,
                            "c"
                    ));
                }
            }else{
                lineText.addView(ta.textColor("  ¡Ingresa tu codigo de usuario para continuar con la descarga!", "rojo", 15, "l"));
            }

            linePrincipal.addView(lineText);
            linePrincipal.addView(line);
            dialogListFincas.setContentView(ca.scrollv(linePrincipal));

            Window w = dialogListFincas.getWindow();
            w.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            w.setGravity(Gravity.CENTER);
            dialogListFincas.show();
        }catch (Exception e){
            Log.i("ErrorFarms", e.toString());
        }
    }

    public void getItem(listFincasTab.fincasTab nomBtn, LinearLayout line){
        Button btn = (Button) gg.boton(nomBtn.getNombreFinca(), "gris");
        btn.setTextColor(Color.parseColor("#000000"));
        new GIDGET().GradientDrawable(btn, "l");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 2, 5, 2);

        btn.setLayoutParams(params);

        btn.setOnClickListener(v -> {
            Intent i = new Intent(this, splash_activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("redireccion", 2);
            i.putExtra("carga", "BajarDatos");
            i.putExtra("idFinca", nomBtn.getIdFinca());

            idFinca = nomBtn.getIdFinca();

            startActivity(i);
        });

        line.addView(btn);
    }

    public void onBackPressed() {
    }
}
