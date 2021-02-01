package com.example.eliteCapture.Config;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;


public class sqlConect {

    Context context;

    //================ CONEXION 123 ====================


    String url = "jdbc:jtds:sqlserver://10.50.1.123;instance=Mercedes;databaseName=Formularios";
    String user = "Inventarios";
    String pass = "Inventarios2016*";


    //================ CONEXION 120 ====================

    //Variables de conexion.
    /*String server = "10.50.1.120";
    String db = "Formularios";
    String pass = "4dm1nPr0c";
    String user = "sa";
    // Tiempo de espera en  segundos
    int socketTimeout = 1;

    //Creacion de cadena de  de conexion.
    String ConnectionURL = "jdbc:jtds:sqlserver://" + server + "/" + db
            + ";user=" + user
            + ";password=" + pass + ";"
            + ";socketTimeout=" + socketTimeout + ";";
    */

    public Connection getConexion() {
        try {
            Log.i("CONEXION", "intentando conectar");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
            Connection c = DriverManager.getConnection(url,user,pass);;
            Log.i("CONEXION", "nos conectamos");
            return c;
        } catch (Exception e) {
            Log.i("CONEXION_ERROR", "error de conexion \n" + e);
            return null;
        }
    }

    public int excecutePing(){
        try {
            Process p;
            p = Runtime.getRuntime().exec("/system/bin/ping -c 1 10.50.1.123");
            int respuesta = p.waitFor();
            Log.i("CONEXION", "data : "+p.toString());
            p.destroy();
            return respuesta;
        }catch (Exception e){
            Log.i("CONEXION_ERROR", "error de conexion \n" + e.toString());
            return 0000;
        }
    }
}
