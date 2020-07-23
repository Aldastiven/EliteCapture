package com.example.eliteCapture.Config.Util.Modal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Image.imageAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.ionLine;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;
import com.example.eliteCapture.splash_activity;

public class modalServer {
    Context context;
    String path;

    containerAdmin ca;
    textAdmin ta;
    imageAdmin ia;

    Dialog d;

    iContenedor icont;
    ionLine io;

    public modalServer(Context context, String path) {
        this.context = context;
        this.path = path;

        ca = new containerAdmin(context);
        ta = new textAdmin(context);
        ia = new imageAdmin(context);

        icont = new iContenedor(path);
        io = new ionLine(path);
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
        if(pendientesCantidad()>0) linearLayout.addView(subida() );

        return linearLayout;
    }

    public LinearLayout descarga(){

        LinearLayout lineDescarga = line();

        TextView txtDescarga =(TextView) ta.textColor("Descargar datos.            ","gris",18,"l");
        txtDescarga.setLayoutParams(params(2));

        lineDescarga.addView(txtDescarga);
        lineDescarga.addView(img(1));

        funDescargar(lineDescarga);//ejecuta la funcion de descaga de datos

        return lineDescarga;
    }

    public LinearLayout subida(){

        LinearLayout lineSubida = line();

        TextView txtDescarga =(TextView) ta.textColor("Tienes pendendientes ("+pendientesCantidad()+") registros por enviar.","rojo",18,"l");
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

    public int pendientesCantidad(){
        try {
            int size = 0;
            for (ContenedorTab c : icont.all()) {
                Log.i("ESTADO", c.getEstado() + "");
                if (c.getEstado() < 1) {
                    size++;
                }
            }
            return size;
        }catch (Exception e){
            return 0;
        }
    }

    public void funDescargar(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, splash_activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("class", "Login");
                i.putExtra("carga", "BajarDatos");
                if(io.all().equals("onLine")) {
                    context.startActivity(i);
                }else{
                    Toast.makeText(context, "Tienes que habilitar la opcion de conexión onLine para descargar datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void funEnviar(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, splash_activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("class", "Login");
                i.putExtra("carga", "Pendientes");
                if(io.all().equals("onLine")) {
                    context.startActivity(i);
                }else{
                    Toast.makeText(context, "Tienes que habilitar la opcion de conexión onLine para enviar datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
