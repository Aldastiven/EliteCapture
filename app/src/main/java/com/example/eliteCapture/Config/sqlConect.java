package com.example.eliteCapture.Config;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public abstract class sqlConect {

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
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
            Log.i("CONEXION", "nos conectamos");
            //return DriverManager.getConnection(ConnectionURL);
            return DriverManager.getConnection(url,user,pass);
        } catch (Exception e) {
            Log.i("CONEXION_ERROR", "error de conexion \n" + e);
            return null;
        }

    }

}
