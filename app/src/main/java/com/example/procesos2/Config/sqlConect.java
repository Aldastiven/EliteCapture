package com.example.procesos2.Config;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public abstract class sqlConect {

    Context context;
    /*
    String url = "jdbc:jtds:sqlserver://10.50.1.123;instance=Mercedes;databaseName=Formularios";
    String user = "Inventarios";
    String pass = "Inventarios2016*";
    */
    String url = "jdbc:jtds:sqlserver://10.50.1.120;databaseName=Formularios";
    String pass = "4dm1nPr0c";
    String user = "sa";



    public Connection getConexion() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
            //Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Log.i("my app","nos conectamos");
            return DriverManager.getConnection(url, user, pass);


        } catch (Exception e) {
            Log.i("my app","error de conexion \n"+e);
            return null;
        }

    }

    public void closeConexion(Connection con) throws Exception {
        if (con != null) {
            con.close();
        }
    }

    public void closeConexion(Connection con, ResultSet rs) throws Exception {
        if (con != null) {
            con.close();
        }
        if (rs != null) {
            rs.close();
        }


    }

}
