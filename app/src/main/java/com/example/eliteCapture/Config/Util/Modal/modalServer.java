package com.example.eliteCapture.Config.Util.Modal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Controls.GIDGET;
import com.example.eliteCapture.Config.Util.Image.imageAdmin;
import com.example.eliteCapture.Config.Util.notification.notificationAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.ionLine;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;
import com.example.eliteCapture.splash_activity;

import org.w3c.dom.Text;

public class modalServer {
    Context context;
    String path, user;

    containerAdmin ca;
    textAdmin ta;
    imageAdmin ia;

    Dialog d, duser;

    iContenedor icont;
    ionLine io;
    GIDGET pp;

    TextView txtUser;

    public modalServer(Context context, String path, String user) {
        this.context = context;
        this.path = path;
        this.user = user;

        ca = new containerAdmin(context);
        ta = new textAdmin(context);
        ia = new imageAdmin(context);

        icont = new iContenedor(path);
        io = new ionLine(path);
        pp = new GIDGET(context, "", null, path);
    }

    public Dialog modal(){
        try {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setMargins(20, 20, 20, 20);
            LinearLayout line = new LinearLayout(context);
            line.setOrientation(LinearLayout.VERTICAL);
            line.setLayoutParams(ll);

            line.addView(Line());

            d = new Dialog(context, R.style.TransparentDialog);
            d.getWindow().setDimAmount(0);//sirve para quitar el fondo transparente
            d.setContentView(line);

            Window w = d.getWindow();
            w.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            w.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

            return d;
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public View Line(){

        LinearLayout linearLayout = ca.container();
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(descarga());
        if(icont.pendientesCantidad() > 0) linearLayout.addView(subida() );

        return linearLayout;
    }

    public LinearLayout descarga(){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 10, 0, 20);
        LinearLayout lineDescarga = line();
        lineDescarga.setBackgroundResource(R.drawable.camp_gray);
        lineDescarga.setPadding(0, 20, 0, 20);
        lineDescarga.setLayoutParams(params);

        TextView txtDescarga =(TextView) ta.textColor("Descargar/Actualizar datos.            ","gris",18,"l");
        txtDescarga.setLayoutParams(params(2));

        lineDescarga.addView(txtDescarga);
        lineDescarga.addView(img(1));

        funDescargar(lineDescarga);//ejecuta la funcion de descaga de datos

        return lineDescarga;
    }

    public LinearLayout subida(){

        LinearLayout lineSubida = line();
        lineSubida.setBackgroundResource(R.drawable.camp_gray);
        lineSubida.setPadding(0, 20, 0, 20);

        TextView txtDescarga =(TextView) ta.textColor("Tienes pendendientes ("+icont.pendientesCantidad()+") registros por enviar.","rojo",18,"l");
        txtDescarga.setLayoutParams(params(2));

        lineSubida.addView(txtDescarga);
        lineSubida.addView(img(2));

        funEnviar(lineSubida);//ejecuta la funcion de envio de datos

        return lineSubida;
    }

    public LinearLayout.LayoutParams params(int i){
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        l.weight =  i == 1 ?  0.1f :  0.9f;
        return l;
    }

    public LinearLayout line(){
        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setWeightSum(1f);

        return line;
    }

    public ImageView img(int i){
        ImageView imageDescarga = (ImageView) ia.image(i == 1 ? R.drawable.ic_cloud_download_white_18dp : R.drawable.ic_cloud_upload_white_18dp);
        imageDescarga.setLayoutParams(params(1));
        imageDescarga.setForegroundGravity(Gravity.RIGHT);

        return imageDescarga;
    }

    public void funDescargar(View v){
        v.setOnClickListener(v1 -> {
            if (io.all().equals("onLine")) {
                if (user.isEmpty()) {
                    modalUser();
                } else {
                    downloadData(Integer.parseInt(user));
                }
            }else{
                Toast.makeText(context, "Tienes que habilitar la opcion de conexión onLine para descargar datos", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void funEnviar(View v){
        v.setOnClickListener(v1 -> {
            Intent i = new Intent(context, splash_activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("redireccion", 1);
            i.putExtra("carga", "Pendientes");
            if(io.all().equals("onLine")) {
                context.startActivity(i);
            }else{
                Toast.makeText(context, "Tienes que habilitar la opcion de conexión onLine para enviar datos", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("ResourceType")
    public void modalUser(){
        duser = new Dialog(context, R.style.MyProgressDialogNormal);
        //duser.getWindow().setDimAmount(0);//sirve para quitar el fondo transparente


        LinearLayout line = ca.container();

        LinearLayout lineNoti = ca.container();
        line.addView(lineNoti);

        line.addView(ta.textColor("Digita tu usuario para continuar el proceso de descarga", "darkGray", 15, "l"));

        LinearLayout.LayoutParams param = ca.params();
        param.setMargins(10, 5, 10,5);
        txtUser = (EditText) pp.campoEdtable("Edit", "grisClear");
        txtUser.setLayoutParams(param);
        line.addView(txtUser);

        Button btnContinue = (Button) pp.boton("Continuar", "verde");
        line.addView(btnContinue);

        duser.setContentView(line);


        btnContinue.setOnClickListener(v -> {
            if(txtUser.getText().toString().isEmpty()){
                new notificationAdmin(context, "Por favor digita el codigo de usuario", "bad", 5000, lineNoti).crear();
            }else{
                downloadData(Integer.parseInt(txtUser.getText().toString()));
            }
        });

        Window w = duser.getWindow();
        w.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        w.setGravity(Gravity.CENTER);
        duser.show();
        d.dismiss();
    }

    public void downloadData(int idUsuario){
        Intent i = new Intent(context, splash_activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("redireccion", 1);
        i.putExtra("carga", "BajarDatos");
        i.putExtra("idUsuario", idUsuario);
        context.startActivity(i);
    }
}
