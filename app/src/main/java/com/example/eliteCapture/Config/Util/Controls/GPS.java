package com.example.eliteCapture.Config.Util.Controls;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.gpsAdmin;
import com.example.eliteCapture.Config.Util.notification.dialogMessageThread;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;

import static android.content.Context.LOCATION_SERVICE;

public class GPS {
    Context context;

    RespuestasTab rt;
    String ubicacion, path, respuestaCampo = null;
    boolean vacio, initial;

    TextView respuestaPonderado, campData;
    LinearLayout contenedorCamp, noti;

    containerAdmin ca;
    GIDGET pp;

    LocationManager locationManager;

    dialogMessageThread msg = null;

    public GPS(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : " + rt.getPonderado() : "Resultado :");

        noti = new LinearLayout(context);
        noti.setOrientation(LinearLayout.VERTICAL);
    }

    public View crear() {//GENERA EL CONTENEDOR DEL ITEM
        try {
            contenedorCamp = ca.container();
            contenedorCamp.setOrientation(LinearLayout.VERTICAL);
            contenedorCamp.setPadding(10, 0, 10, 0);
            contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

            contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
            contenedorCamp.addView(noti);
            contenedorCamp.addView(campo());
            pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

            return contenedorCamp;
        } catch (Exception e) {
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }

    public LinearLayout campo() {
        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setWeightSum(2);

        TextView btnGps = (Button) pp.boton("Obtener ubicación GPS", "verde");
        btnGps.setLayoutParams(params((float) 2));
        btnGps.setTextSize(15);
        line.addView(btnGps);


        btnGps.setOnClickListener(v -> {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                gpsAdmin gps = new gpsAdmin(context);

                if(noti.getChildCount() > 0){
                    noti.removeAllViews();
                }

                noti.addView(
                        new textAdmin(context).textColor(
                                "Lnt : "+gps.getLongitude()+", Ltd : "+gps.getLatitude(),
                                "gris",
                                15,
                                "l")
                );

                //se utilizan el cod. asccii yen (ALT + 190)
                registro(gps.getLongitude()+"¥"+gps.getLatitude(), null);
            } catch (Exception e) {
                Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
                registro(null, null);
            }
        });

        return line;
    }

    public LinearLayout.LayoutParams params(float i) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(2, 2, 2, 2);
        params.weight = i;

        return params;
    }

    public void registro(String rta, String valor) {//REGISTRO
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, valor, null, rt.getReglas());
    }
}
