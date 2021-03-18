package com.example.eliteCapture.Model.Data;

import android.util.Log;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Tab.DetalleTab;
import com.example.eliteCapture.Model.Data.Tab.jsonPlanTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class iJsonPlan {

    String path, nombre = "planJSON";
    int usuario;

    Connection cn;
    JsonAdmin ja;

    public iJsonPlan(String path, int usuario, Connection cn) {
        Log.i("getIdUsuario", "llego el usuario : "+usuario);
        this.path = path;
        this.usuario = usuario;
        this.cn = cn;
        ja = new JsonAdmin();
    }

    public iJsonPlan(String path) {
        this.path = path;
        ja = new JsonAdmin();
    }

    public List<jsonPlanTab> all(){
        return new Gson().fromJson(
                ja.ObtenerLista(path, nombre),
                new TypeToken<List<jsonPlanTab>>() {
        }.getType());
    }

    public boolean local() throws Exception{
        ResultSet rs;
        String q = "SELECT Area FROM Plano_json_Bloque WHERE codigo = "+usuario;
        PreparedStatement ps = cn.prepareStatement(q);
        rs = ps.executeQuery();

        String data = "";
        while (rs.next()) {
            data = rs.getString(1) + data;
        }
        return ja.WriteJson(path, nombre, data);
    }

    public boolean exist(){
        return ja.ExitsJson(path, nombre);
    }
}
