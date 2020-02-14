package com.example.eliteCapture.Conexion;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckedConexion {

    Context context;



    ConnectivityManager cm;
    NetworkInfo ni;


    //REVISA LA CONEXION DE RED
    public boolean checkedConexionValidate(Activity context){

        try{
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            ni = cm.getActiveNetworkInfo();
            boolean tipoConexionWifi = false;
            boolean tipoConexionDatos = false;

            if(ni != null){
                ConnectivityManager connManager1 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo WiFi = connManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                ConnectivityManager connManager2 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo MOBILE = connManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if(WiFi.isConnected()) {
                    tipoConexionWifi = true;
                    return tipoConexionWifi;
                }
                if(MOBILE.isConnected()) {
                    tipoConexionDatos = true;
                    return tipoConexionDatos;
                }
            }else {
                //Toast.makeText(context,"¡¡ NO ESTAS CONECTADO AL SERVIDOR ¡¡ \n por el momento se guardaron los datos en la terminal",Toast.LENGTH_LONG).show();
                tipoConexionWifi = false;
                tipoConexionDatos = false;

                return tipoConexionWifi;
            }

        }catch (Exception e){
            Toast.makeText(context,"¡¡ OCURRIO UN ERROR AL REVISAR LA CONEXION DE RED !! \n \n"+e,Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
