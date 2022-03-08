package com.example.eliteCapture.Config.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompressFile {
    FileOutputStream outStream = null;

    public void imageCompress(File dir, Context context){

        try {

            File outfile = new File(dir, dir.getName());
            outfile.setExecutable(true, false);
            outfile.setWritable(true, false);

            outStream = new FileOutputStream(outfile);
            
        }catch (FileNotFoundException e){
            Log.i("ExceptionCompress", "FileNotFoundException : "+e.toString());
        } finally {

        }
    }
}
