package com.example.eliteCapture.Model.Data;


import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Tab.despVariedadesTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class idespVariedades {
    String path, nombre = "Dependiente_";
    Connection cn;

    List<despVariedadesTab> Lproductos = new ArrayList<>();

    public idespVariedades(String path, Connection cn) {
        this.path = path;
        this.cn = cn;
    }

    public void consultarSql() throws Exception{
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement("SELECT filtro FROM  Desp_Dependiente GROUP BY filtro");

        rs = ps.executeQuery();

        while (rs.next()){
            local(rs.getString("filtro"));
        }
    }

    public boolean local(String filtroName) throws Exception{
        Lproductos.clear();

        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement("SELECT filtro, Id_p, Des_p\n" +
                                                        "FROM  Desp_Dependiente\n" +
                                                        "WHERE filtro = '"+filtroName+"'"+
                                                        "GROUP BY filtro, Id_p, Des_p\n" +
                                                        "ORDER BY Des_p ASC");
        rs = ps.executeQuery();
        while (rs.next()){
            Lproductos.add(new despVariedadesTab(
                                    rs.getString("filtro"),
                                    rs.getInt("Id_p"),
                                    rs.getString("Des_p"),
                                    getHijo(rs.getInt("Id_p"), rs.getString("filtro"))
            ));
        }
        return new JsonAdmin().WriteJson(path, nombre+filtroName, new Gson().toJson(Lproductos));
    }

    public List<despVariedadesTab.variedades> getHijo(int idPadre, String filtro) throws Exception{
        List<despVariedadesTab.variedades> Lvariedades = new ArrayList<>();
        ResultSet rs;
        String q = "SELECT id_d, Des_d FROM Desp_Dependiente WHERE filtro = '"+filtro+"' AND Id_p = '"+idPadre+"' ";


        PreparedStatement ps = cn.prepareStatement(q);

        rs = ps.executeQuery();

        int contador = 0;

        while (rs.next()){
            contador ++;
            Lvariedades.add(new despVariedadesTab.variedades(rs.getInt("id_d"),
                                                             rs.getString("Des_d")));
        }
        return Lvariedades;
    }

    public List<despVariedadesTab> all(String filtroName) throws Exception{
            Lproductos = new Gson().fromJson(new JsonAdmin().ObtenerLista(path, nombre+filtroName),
                    new TypeToken<List<despVariedadesTab>>() {
                    }.getType());

            return Lproductos;
    }
}
