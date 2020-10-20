package com.example.eliteCapture.Model.View;

import android.util.Log;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class iHistorico {

    String path, nombre = "Historico";
    List<ContenedorTab> listHistorico = new ArrayList<>();

    public iHistorico(String path) {
        try {
            this.path = path;
            if (!new JsonAdmin().ExitsJson(path, nombre) || all() == null) {
                local();
            }
        }catch (Exception e){
            Log.i("ERROR_HISTORICO",e.toString());
        }
    }

    public String insert(ContenedorTab o) {
        o.setEstado(o.getEstado());
        listHistorico = all();
        listHistorico.add(o);
        local();
        return "Ok";
    }

    public List<ContenedorTab> all() {
        try {
            return new Gson().fromJson(new JsonAdmin().ObtenerLista(path, nombre),
                                        new TypeToken<List<ContenedorTab>>() {}.getType());
        }catch (Exception e){
            Log.i("ERRORLIST",e.toString());
            return null;
        }
    }

    public boolean local() {
        return new JsonAdmin().WriteJson(path, nombre, new Gson().toJson(listHistorico));
    }
}
