package com.example.eliteCapture.Config;

import static android.os.Environment.DIRECTORY_DCIM;

import android.app.Activity;
import android.os.Environment;
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
import java.nio.file.Files;
import java.util.Objects;

public class ftpConect extends Thread {
    static Activity c;
    static String path, pathPhoto, servFtp = "10.50.1.123", ftpUser = "usuario_FTP", userPass = "Elite.2020*";
    String pathDirPhoto = "";
    int countImage = 0;
    File pathSend;


    public ftpConect(Activity c, String path, String... pathPhoto) {
        this.c = c;
        this.path = path;
        this.pathPhoto = pathPhoto[0];
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        FTPClient conexion = getConexion();
        pathSend = validateDirectoryPhotos();
        syncronizedPhoto(conexion);
        //syncronizedPhoto();
    }

    public static FTPClient getConexion(){
        try {
            FTPClient client = new FTPClient();
            client.setControlEncoding("utf-8");

            client.connect(servFtp, 21);

            try {
                Log.i("FTPSUPREMO", client.login(ftpUser,userPass) ? "se conecto" : "error en el login");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("FTPSUPREMO", "ERROR SUPREMO : "+e.toString());
            }

            client.setFileType(FTP.ASCII_FILE_TYPE);
            client.enterLocalPassiveMode();
            client.setUseEPSVwithIPv4(true);

            return client;
        }catch (Exception e){
            e.printStackTrace();
            Log.i("FTPSUPREMO","Error : "+e.toString());
            return null;
        }
    }

    public void syncronizedPhoto(){
        try{

            String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Size: "+ files.length);

            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
            }
        }catch (Exception e){
            Log.i("FTPSUPREMO", "ERROR : "+e.toString());
        }
    }


    public void syncronizedPhoto(FTPClient ftp) {
        try {
            File dcim = new File(getPathPhoto());

            File[] lstFiles = dcim.listFiles();

            Log.i("FTPSUPREMO", "ruta de la imagenes : " + dcim.getAbsolutePath());


            for (File foto : Objects.requireNonNull(lstFiles)) {
                Log.i("FTPSUPREMO", foto.getName());
                String fileName = foto.getName().toUpperCase();
                boolean extension = fileName.endsWith(".JPG") || fileName.endsWith(".JPEG");

                if (extension) {
                    trasnferFtpFile(foto, ftp);
                }
            }
        }catch (Exception e){
            Log.i("FTPSUPREMO", "Exception : "+e.toString());
        }
    }

    public File validateDirectoryPhotos(){
        File dir = new File(getPathPhoto()+"/sended/");

        if(!dir.exists()){
            dir.mkdirs();
            pathDirPhoto = dir.getAbsolutePath();
        }

        return dir;
    }


    public void trasnferFtpFile(File src, FTPClient ftp) {
        try {

            Log.i("FTPSUPREMO", "es archivo : " + src.getName());

            FileInputStream srcStream = new FileInputStream(src);
            String upliadPath = src.getPath();
            System.out.println("Uploading file : " + upliadPath);

            ftp.changeWorkingDirectory("/photos");

            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            if (ftp.storeFile(src.getName(), srcStream)) {
                countImage++;

                System.out.println("enviando fotografia");
                FileUtils.copyFileToDirectory(src, pathSend);
                src.delete();

                System.out.println("termino de enviar fotografia");
            }
            srcStream.close();
            src.delete();
        } catch (Exception e) {
            System.out.println("Error : " + e.toString());
        }
    }

    public void txtMessage(){
        c.runOnUiThread(()-> {
            if(countImage > 0) {
                Toast.makeText(c, "Fotos enviadas ✓, se sicronizaron " + countImage + " fotos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getPathPhoto() {
        //return Environment.getExternalStorageDirectory()+ "/" + DIRECTORY_DCIM;// + "/Camera/Elite/";

        return Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getAbsolutePath();
    }
}
