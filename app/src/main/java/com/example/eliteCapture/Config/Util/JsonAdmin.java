package com.example.eliteCapture.Config.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonAdmin {

    public Boolean WriteJson(String path, String nombre, String contenido) {
        //CREACIÓN DE FICHEROS Y ESCRITURA DE DATOS AL JSON CON SU RESPECTIVO NOMBRE
        boolean ok;
        try {
            FileOutputStream fos = new FileOutputStream(new File(path + nombre + ".json"));
            fos.write(contenido.getBytes());
            fos.close();
            ok = true;
        } catch (Exception e) {
            Log.i("ErrorSplash_Usuario", e.toString());
            ok = false;
        }
        return ok;
    }

    public String ObtenerLista(String path, String nombre) {
        //LECTURA DE DATOS DESDE UN FICHERO JSON
        try {
            Log.i("CONSULTAJSON", path + nombre + ".json");
            FileInputStream fis = new FileInputStream(new File(path + nombre + ".json"));
            return new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8)).readLine() + "\n";
        } catch (IOException ex) {
            return null;
        }
    }

    public Boolean ExitsJson(String path, String nombre) {
        //COMPRUEBA SI EL FICHERO JSON EXISTE

        Log.i("rutaLlegada", path + nombre + ".json");

        return new File(path + nombre + ".json").exists();
    }
}
