package com.example.eliteCapture.Config;

import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    }

    public static FTPClient getConexion(){
        try {
            FTPClient client = new FTPClient();
            client.setControlEncoding("utf-8");

            client.connect(servFtp, 21);
            client.login(ftpUser,userPass);

            client.setFileType(FTP.ASCII_FILE_TYPE);
            client.enterLocalPassiveMode();
            client.setUseEPSVwithIPv4( true );

            return client;
        }catch (Exception e){
            e.printStackTrace();
            Log.i("error","Error : "+e.toString());
            return null;
        }
    }


    public void syncronizedPhoto(File src, FTPClient ftp) {
        try {
            //c.runOnUiThread(() -> Toast.makeText(c, "Sincronizando fotos...", Toast.LENGTH_LONG).show());
            if (ftp != null) {
                if (src.isDirectory()) {
                    ftp.makeDirectory(src.getName());
                    ftp.changeWorkingDirectory(src.getName());
                    Log.i("ftpSyncronized", "carpeta " + src.getName() + " es creada : " + ftp.printWorkingDirectory());
                    for (File file : src.listFiles()) {
                        syncronizedPhoto(file, ftp);
                    }
                    ftp.changeToParentDirectory();
                } else {
                    InputStream srcStream;
                    try {
                        srcStream = new FileInputStream(src);
                        String upliadPath = src.getPath();
                        System.out.println("Uploading file : " + upliadPath);

                        ftp.setFileType(FTP.BINARY_FILE_TYPE);
                        ftp.enterLocalPassiveMode();

                        if(ftp.storeFile(src.getName(), srcStream)){
                            countImage++;
                            transferSent(src);
                        }
                        srcStream.close();
                    } catch (Exception e) {
                        System.out.println("Error : " + e.toString());
                    }
                }
            } else {
                c.runOnUiThread(() -> Toast.makeText(c, "sin conexion para sincronizar fotos", Toast.LENGTH_LONG).show());
            }
        } catch (IOException e) {
            Log.i("error", "syncronizedPhoto : " + e.toString());
        } finally {
            if (ftp != null) {
                try {
                    txtMessage();
                    ftp.logout();
                } catch (IOException ioe) {
                    Log.i("error", ioe.toString());
                }
            }
        }
    }

    public void transferSent(File src){
        try {
            Log.i("transferSent", "name : "+src.getName());
            File sentFile = new File(path + "/photos/sent");
            if (!sentFile.exists()) {
                Log.i("transferSent", "entro a crear la carpeta");
                sentFile.mkdirs();
                transferSent(src);
            } else {
                if (!src.isDirectory()) {
                    Log.i("transferSent", "entro a enviar el archivo");
                    FileUtils.copyFileToDirectory(src, sentFile);
                }
                src.delete();
            }
        }catch (Exception e){
            Log.i("ErrortransferSent", e.toString());
        }
    }

    public void txtMessage(){
        c.runOnUiThread(()-> {
            if(countImage > 0) {
                Toast.makeText(c, "Fotos enviadas ✓, se sicronizaron " + countImage + " fotos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
