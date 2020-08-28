package com.example.eliteCapture.Model.Data;

import android.util.Log;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Tab.onLineTab;
import com.example.eliteCapture.Model.Data.Tab.sesionTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class iSesion {

    String path, nombre = "Session";
    List<sesionTab> Lsesion = new ArrayList<>();

    public iSesion(String path) {
        this.path = path;
        if (!exist()){local(0000);}
    }

    public boolean local(int d){
        Lsesion.clear();
        Lsesion.add(new sesionTab(d));
        return new JsonAdmin().WriteJson(path, nombre, new Gson().toJson(Lsesion));
    }

    public int all(){
        try {
            Lsesion = new Gson().fromJson(new JsonAdmin().ObtenerLista(path, nombre),
                    new TypeToken<List<sesionTab>>() {
                    }.getType()
            );
            int Sonline = 0000;
            for (sesionTab on : Lsesion) {
                Sonline = on.getCodSesion();
            }
            if (Lsesion != null) {
                return Sonline;
            } else {
                return 0000;
            }
        }catch (Exception e){
            Log.i("Error",e.toString());
            return 0000;
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
