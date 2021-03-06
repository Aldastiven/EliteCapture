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

    public String JsonDes(String path, String nombre , int pos)     {
        JsonParser parser = new JsonParser();
        String f = path+nombre+".json";
        try {
            Object obj = parser.parse(new FileReader(f));
            JsonArray jsonArray = (JsonArray) obj;

            JsonObject objeto = jsonArray.get(pos).getAsJsonObject();
            JsonArray datoJson = (JsonArray) objeto.get("respuestasTab");

            for (int i=0; i<datoJson.size(); i++){
                JsonObject objidRes = datoJson.get(i).getAsJsonObject();
                String datoidRes = objidRes.get("idPregunta").toString();

                JsonObject objRes = datoJson.get(i).getAsJsonObject();
                String datoRes = objRes.get("Respuesta").toString();
                return "ID : "+datoidRes+"\n RES "+datoRes;
            }

        }catch (Exception ex){
            return "Exception al deserializar el json "+ex.toString();
        }
        return "";
    }

    public String EliminarLista(String path, String nombre) throws Exception {
        String msj = "";
        path = path + nombre + ".json";
        File file = new File(path);
        if (file.delete()){
            msj="se limpio los registros";
            return msj;
        }else {
            msj="hubo un problema";
            return msj;
        }
    }

    public String recorerJSON(String[] args,String path){
        String msj="";
        try {
            JsonParser parser = new JsonParser();
            JsonArray gsonArr = parser.parse(new FileReader(path+"procesos_2.json")).getAsJsonArray();


            for(JsonElement obj: gsonArr) {

                JsonObject gsonObj = obj.getAsJsonObject();

                String name = gsonObj.get("Fecha").getAsString();
                msj = "nombre \n" + name + "\n" + gsonArr.size();
                return msj;
            }

        }catch (Exception ex){
            msj = "Exception \n"+ex;
        }
        return msj;
    }



}
