package com.example.eliteCapture.Model.Data;

import android.util.Log;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Tab.DetalleTab;
import com.example.eliteCapture.Model.Data.Tab.jsonPlanTab;
import com.example.eliteCapture.Model.Data.Tab.listFincasTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class iJsonPlan {

    String path, nombre = "planJSON", nameFincas = "listFincas";
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
        String q = "SELECT * FROM loginFinca WHERE codigo = "+usuario;
        PreparedStatement ps = cn.prepareStatement(q);
        rs = ps.executeQuery();

        List<listFincasTab> listFincas = new ArrayList<>();
        List<listFincasTab.fincasTab> fincas = new ArrayList<>();

        while (rs.next()) {
            Log.i("ErrorDownload", rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3));
            fincas.add(new listFincasTab.fincasTab(
                    Integer.parseInt(rs.getString(2)),
                    rs.getString(3)
            ));
        }

        listFincas.add(new listFincasTab(
                usuario,
                fincas
        ));
        return ja.WriteJson(path, nameFincas, new Gson().toJson(listFincas));
    }

    public List<listFincasTab> allListFincas(){
        return new Gson().fromJson(
                ja.ObtenerLista(path, nameFincas),
                new TypeToken<List<listFincasTab>>() {
                }.getType());
    }

    public boolean localListFincas(int idFinca) throws Exception{
        ResultSet rs;
        String q = "SELECT  * FROM Plano_json_Bloque WHERE codigo = "+idFinca;
        PreparedStatement ps = cn.prepareStatement(q);
        rs = ps.executeQuery();

        String data = "";
        while (rs.next()) {
            data = rs.getString(2) + data;
            Log.i("nextFinca", rs.getString(1)+", "+rs.getString(2));
        }
        return ja.WriteJson(path, nombre, data);
    }

    public boolean exist(){
        return ja.ExitsJson(path, nombre);
    }
}
