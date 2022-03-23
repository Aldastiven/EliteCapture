package com.example.eliteCapture.Config.Util.secondTaks;

import static android.os.Environment.DIRECTORY_DCIM;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Objects;

public class photoAdmin extends Thread{

    @Override
    public void run(){
        super.run();
        deletePhotos();
    }

    public void deletePhotos() {
        try {
            File dcim = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getAbsolutePath());

            File[] lstFiles = dcim.listFiles();

            Log.i("photosAdmin", "ruta de la imagenes : " + dcim.getAbsolutePath());

            for (File foto : Objects.requireNonNull(lstFiles)) {
                Log.i("photosAdmin", foto.getName());
            }
        }catch (Exception e){
            Log.i("photosAdmin", "Exception : "+e.toString());
        }
    }
}
