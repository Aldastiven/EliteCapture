package com.example.eliteCapture.Config;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class sqlConect {
    //================ CONEXION 123 ====================

    String direccion = "10.50.1.132";
    String url = "jdbc:jtds:sqlserver://"+direccion+";instance=Production;databaseName=Formularios";
    String user = "servicios_app";
    String pass = "S3rv!c!0s4pp?";



    public Connection getConexion() {
        try {
            Log.i("CONEXION", "intentando conectar : "+getTime());
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
            Connection c = DriverManager.getConnection(url, user, pass);
            Log.i("CONEXION", "nos conectamos : "+getTime());
            return c;
        } catch (Exception e) {
            Log.i("CONEXION_ERROR", "error de conexion \n" + e);
            return null;
        }
    }


    public String getTime(){
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                LocalDateTime now = LocalDateTime.now();
                return dtf.format(now);
            }else{
                return "Error time else";
            }

        }catch (Exception e){
            Log.i("Error_Time", e.toString());
            return "Error Time : "+e.toString();
        }
    }

    public int excecutePing(Context c){
        try {
            Process p;
            p = Runtime.getRuntime().exec("/system/bin/ping -c 4  10.50.1.123");

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            StringBuffer output = new StringBuffer();
            String temp;

            int count = 0;
            String str = "";
            while ( (temp = reader.readLine()) != null) {
                output.append(temp);
                count++;
            }

            reader.close();

            if(count > 0) {
                str = output.toString();
            }

            Log.i("ping", str);

            int respuesta = 2;
            if(str.length() > 0) {
                String data = str.split("statistics")[1];
                String data2[] = data.split(",");
                int res = Integer.parseInt(data2[2].split(" ")[1].substring(0, 1));
                respuesta = res < 25 ? 0 : 1;
            }

            p.destroy();
            return respuesta;
        }catch (Exception e){
            Log.i("CONEXION_ERROR", "error de conexion \n" + e.toString());
            return 0000;
        }
    }
}
