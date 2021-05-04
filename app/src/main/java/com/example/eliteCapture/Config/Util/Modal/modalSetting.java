package com.example.eliteCapture.Config.Util.Modal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Container.notificationAdmin;
import com.example.eliteCapture.Config.Util.secondTaks.getConexion;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Config.sqlConect;
import com.example.eliteCapture.Model.Data.ionLine;
import com.example.eliteCapture.R;
import com.example.eliteCapture.splash_activity;

import java.sql.Connection;

public class modalSetting {

    Activity act;
    Context context;
    String path;
    Dialog d;
    textAdmin ta;
    ImageView imgOnline;
    containerAdmin ca;

    Switch sw;
    TextView txtR;
    LinearLayout notifications;

    ionLine ionLine;

    public modalSetting(Activity act, Context context, String path, ImageView imgOnline, LinearLayout notifications) {
        this.act = act;
        this.context = context;
        this.path = path;
        this.imgOnline = imgOnline;


        ionLine = new ionLine(path);
        ta = new textAdmin(context);
        ca = new containerAdmin(context);
    }

    public Dialog modal(){
        try {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setMargins(20, 20, 20, 20);
            LinearLayout line = new LinearLayout(context);
            line.setOrientation(LinearLayout.VERTICAL);
            line.setLayoutParams(ll);

            line.addView(Line());

            ionLine = new ionLine(path);

            d = new Dialog(context, R.style.TransparentDialog);
            d.getWindow().setDimAmount(0);//sirve para quitar el fondo transparente
            d.setContentView(line);

            Window w = d.getWindow();
            w.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            w.setGravity(Gravity.RIGHT | Gravity.TOP);

            return d;
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public View Line(){
        LinearLayout linearLayout = ca.container();

        TextView txt = (TextView) ta.textColor("Selecciona si quieres trabajar con conexión o de forma local", "negro", 15, "c");

        LinearLayout linearLayout1 = ca.container();
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setWeightSum(2);

        LinearLayout.LayoutParams ll = ca.params();
        ll.weight = 1;
        ll.gravity = Gravity.CENTER;

        sw = new Switch(context);
        sw.setGravity(Gravity.CENTER);
        sw.setLayoutParams(ll);

        txtR = (TextView) ta.textColor(ionLine.all(), "negro", 15, "c");
        txtR.setLayoutParams(ll);

        notifications = new LinearLayout(context);
        notifications.setOrientation(LinearLayout.VERTICAL);

        estadoOnline();

        linearLayout1.addView(txtR);
        linearLayout1.addView(sw);

        linearLayout.addView(txt);
        linearLayout.addView(linearLayout1);
        linearLayout.addView(notifications);

        //validarConexion();
        sw.setChecked(ionLine.all().equals("onLine"));

        return linearLayout;
    }

    public void estadoOnline(){
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                if(sw.isChecked() && ionLine.all().equals("offLine")){
                    sw.setChecked(ionLine.all().equals("onLine"));
                    //new notificationAdmin(context, "Comprobando servidor disponible", "dark", 2000, notifications).crear();
                    /*new Handler().postDelayed((Runnable) () -> {
                        int conexion = new Conexion().excecutePing(context);
                    }, 2000);*/

                    TextView txt = (TextView) ta.textColor("Validando conexion", "verde", 15, "c");
                    notifications.addView(txt);

                    d.setCancelable(false);

                    new Thread(()->{
                        valideConexion(
                                new getConexion(act, txt, 3)
                        );
                    }).start();

                }else if(!sw.isChecked()){
                    ionLine.local("offLine");
                    sw.setButtonDrawable(R.color.rojo);
                    txtR.setText("offLine");
                    imgOnline.setBackgroundResource(R.drawable.ic_wifi_off);
                }
            }catch (Exception e){
                Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_checked},
                new int[] {android.R.attr.state_checked},
        };

        int[] thumbColors = new int[] {
                Color.rgb(236, 112, 99),
                Color.rgb(88, 214, 141)

        };

        int[] trackColors = new int[] {
                Color.rgb(236, 112, 99),
                Color.rgb(88, 214, 141)

        };
        sw.setButtonTintList(new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(sw.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(sw.getTrackDrawable()), new ColorStateList(states, trackColors));
    }

    public void validatetConexion(Connection conexion){
        new notificationAdmin(context, conexion != null ? "obtuvo Conexión" : "Conexión perdida, cod. : "+conexion, conexion != null ? "good" : "bad", 3000, notifications).crear();
        ionLine.local(conexion != null ? "onLine" : "offLine");
        sw.setButtonDrawable(conexion != null ? R.color.verde : R.color.rojo);
        sw.setChecked(conexion != null);
        txtR.setText(conexion != null ? "onLine" : "offLine");
        imgOnline.setBackgroundResource(conexion != null ? R.drawable.ic_wifi_on : R.drawable.ic_wifi_off);
    }

    public void validarConexion(){
        try{
            sw.setChecked(false);
            txtR.setText(ionLine.all());
            imgOnline.setBackgroundResource(ionLine.all().equals("offLine") ? R.drawable.ic_wifi_off : R.drawable.ic_wifi_on);
            ionLine.local(!sw.isChecked() ? "offLine" : "onLine");
        }catch (Exception e){
            Toast.makeText(act, e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("taskDownloader", "validateConexion : "+e.toString());
        }
    }


    public void valideConexion(getConexion conexion){
        try {
            Thread.sleep(1000);
            Log.i("taskDownloader", "termino : "+conexion.getTerminated());
            if(conexion.getCn() == null) {
                if(!conexion.getTerminated()){
                    valideConexion(conexion);
                }else {
                    Log.i("taskDownloader", "paso por aqui!!");
                    Thread.sleep(3000);
                    d.setCancelable(true);
                    d.dismiss();
                }
            }else if(conexion.getCn() != null) {
                validarConexion();
                Log.i("taskDownloader", "paso por aqui!!");
                Thread.sleep(3000);
                d.setCancelable(true);
                d.dismiss();
            }
        }catch (Exception e){
            Toast.makeText(act, e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("taskDownloader", "validateConexion : "+e.toString());
        }
    }

    protected class Conexion extends sqlConect {}
}