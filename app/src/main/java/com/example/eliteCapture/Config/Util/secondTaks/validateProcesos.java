package com.example.eliteCapture.Config.Util.secondTaks;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.iDetalle;
import com.example.eliteCapture.Model.View.iContenedor;

import java.sql.Connection;

import static com.example.eliteCapture.R.drawable.ic_cloud;
import static com.example.eliteCapture.R.drawable.ic_cloud_noti;

public class validateProcesos {

    Activity act;
    TextView txtNotification, floatingServer;
    Connection conexion;
    String path;
    int intenotos = 3;

    public validateProcesos(Activity act, TextView txtNotification, TextView floatingServer, String path) {
        this.txtNotification = txtNotification;
        this.act = act;
        this.floatingServer = floatingServer;
        this.path = path;

        new Thread(()->{
            valideConexion(
                    new getConexion(act, txtNotification, intenotos)
            );
        }).start();
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }


    public void valideConexion(getConexion conexion){
        try {
            Thread.sleep(1000);

            if (!conexion.terminated){
                valideConexion(conexion);
            } else{
                iDetalle id = new iDetalle(conexion.getCn(), path);
                id.setTxtNotification(act, txtNotification);
                boolean b = id.validateDateFile();
                act.runOnUiThread(() -> {
                    if (b){
                        txtNotification.setTextColor(Color.parseColor("#E74C3C"));
                        txtNotification.setText("Tienes actulizaciones pendientes por descargar");
                        Toast.makeText(act, "hay actulizaciones pendientes por descargar", Toast.LENGTH_LONG).show();
                        Log.i("taskDownloader", "archivo detalle : " + b);
                    }else{
                        act.runOnUiThread(() -> {
                            txtNotification.setText("No tienes actualizaciones pendientes");
                        });
                    }
                    floatingServer.setCompoundDrawablesWithIntrinsicBounds(b ? ic_cloud_noti : ic_cloud, 0, 0, 0);
                    floatingServer.setCompoundDrawablesWithIntrinsicBounds(new iContenedor(path).pendientesCantidad() > 0 ? ic_cloud_noti : ic_cloud, 0, 0, 0);
                });
            }
        }catch (Exception e){
            Toast.makeText(act, e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("taskDownloader", "validateConexion : "+e.toString());
        }
    }


}
