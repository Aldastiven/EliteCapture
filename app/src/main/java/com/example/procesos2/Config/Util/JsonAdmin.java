package com.example.procesos2.Config.Util;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonAdmin {
    private int idtabla;

    //ESCRIBE DATOS EN EL JSON
    public Boolean WriteJson(String path, String nombre, String contenido) {
        boolean ok = false;
        try {
            FileOutputStream fos = null;
            File f = new File(path + nombre + ".json");
            fos = new FileOutputStream(f);
            fos.write(contenido.getBytes());
            fos.close();
            ok = true;
        } catch (Exception e) {
            ok = false;
        }
        return ok;
    }

    //OBTINE EN UN TOAST LO QUE CONTIENE MI ARCHIVO JSON
    public String ObtenerLista(String path, String nombre) throws Exception {
        String jsonString = "";
        path = path + nombre + ".json";
        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        jsonString = sb.toString();

        return jsonString;

    }

}
