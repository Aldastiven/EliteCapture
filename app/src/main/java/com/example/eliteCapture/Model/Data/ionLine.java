package com.example.eliteCapture.Model.Data;

import android.util.Log;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Tab.onLineTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ionLine {

    String path, nombre = "Online";
    List<onLineTab> Lonline = new ArrayList<>();

    public ionLine(String path) {
        this.path = path;
        if (!exist()){local("offLine");}
    }

    public boolean local(String d){
        Lonline.clear();
        Lonline.add(new onLineTab(d));
        return new JsonAdmin().WriteJson(path, nombre, new Gson().toJson(Lonline));
    }

    public String all(){
        try {
            Lonline = new Gson().fromJson(new JsonAdmin().ObtenerLista(path, nombre),
                    new TypeToken<List<onLineTab>>() {
                    }.getType()
            );
            String Sonline = "";
            for (onLineTab on : Lonline) {
                Sonline = on.getOnline();
            }
            if (Lonline != null) {
                return Sonline;
            } else {
                return "no existe";
            }
        }catch (Exception e){
            Log.i("Error",e.toString());
            return "no exixte";
        }
    }

    public boolean exist(){
        try {
            return new JsonAdmin().ExitsJson(path, nombre);
        }catch (Exception e){
            return false;
        }
    }
}
