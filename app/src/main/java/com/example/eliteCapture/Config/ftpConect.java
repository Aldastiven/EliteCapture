package com.example.eliteCapture.Config;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ftpConect extends Thread {
    static Activity c;
    static String path, pathPhoto, servFtp = "10.50.1.123", ftpUser = "usuario_FTP", userPass = "Elite.2020*";
    int countImage = 0;


    public ftpConect(Activity c, String path, String... pathPhoto) {
        this.c = c;
        this.path = path;
        this.pathPhoto = pathPhoto[0];
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        syncronizedPhoto(new File(path+"/photos").getAbsoluteFile(), getConexion());
        txtMessage();
    }

    public static FTPClient getConexion(){
        try {
            FTPClient client = new FTPClient();
            client.setControlEncoding("utf-8");

            client.connect(servFtp, 21);
            client.login(ftpUser,userPass);

            client.enterLocalPassiveMode();
            client.setUseEPSVwithIPv4( true );
            return client;
        }catch (Exception e){
            e.printStackTrace();
            Log.i("error","Error : "+e.toString());
            return null;
        }
    }


    public void syncronizedPhoto(File src, FTPClient ftp){
        try {
            if (src.isDirectory()) {
                ftp.makeDirectory(src.getName());
                ftp.changeWorkingDirectory(src.getName());
                Log.i("ftpSyncronized", "carpeta "+src.getName()+" es creada : "+ftp.printWorkingDirectory());
                for (File file : src.listFiles()) {
                    syncronizedPhoto(file, ftp);
                }
                ftp.changeToParentDirectory();
            } else {
                InputStream srcStream = null;
                try {
                    srcStream = src.toURI().toURL().openStream();
                    Log.i("ftpSyncronized", "archivo img : " + src.getName());
                    ftp.storeFile(src.getName(), srcStream);
                    countImage++;
                } finally {
                    if(srcStream != null) {
                        srcStream.close();
                    }
                }
            }
        }catch (IOException e){
            Log.i("error", "syncronizedPhoto : "+e.toString());
        }
    }

    public void txtMessage(){
        c.runOnUiThread(()-> {
            Toast.makeText(c, "Fotos enviadas ✓, se sicronizaron "+countImage+" fotos", Toast.LENGTH_SHORT).show();
        });
    }
}