package com.example.eliteCapture.Model.Data;

import android.widget.Toast;

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
    String path, nombre = "VariedadJson";
    Connection cn;

    List<despVariedadesTab> Lproductos = new ArrayList<>();

    public idespVariedades(String path, Connection cn) {
        this.path = path;
        this.cn = cn;
    }

    public boolean local() throws Exception{
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement("SELECT        idproducto, Producto\n" +
                                                        "FROM            Desp_variedades\n" +
                                                        "GROUP BY idproducto, Producto\n" +
                                                        "ORDER BY Producto ASC");
        rs = ps.executeQuery();

        while (rs.next()){
            Lproductos.add(new despVariedadesTab(rs.getInt("idproducto"),
                                    rs.getString("Producto"),
                                    getVariedades(rs.getInt("idproducto"))
            ));
        }
        return new JsonAdmin().WriteJson(path, nombre, new Gson().toJson(Lproductos));
    }

    public List<despVariedadesTab.variedades> getVariedades(int idProducto) throws Exception{

        List<despVariedadesTab.variedades> Lvariedades = new ArrayList<>();

        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement("SELECT IDVariedad, variedad\n" +
                                                        "FROM   Desp_variedades\n" +
                                                        "WHERE idproducto = '"+idProducto+"' \n"+
                                                        "ORDER BY variedad ASC");
        rs = ps.executeQuery();

        while (rs.next()){
            Lvariedades.add(new despVariedadesTab.variedades(rs.getInt("IDVariedad"),
                                                             rs.getString("variedad"))
            );
        }

        return Lvariedades;
    }

    public List<despVariedadesTab> all() throws Exception{
            Lproductos = new Gson().fromJson(new JsonAdmin().ObtenerLista(path, nombre),
                    new TypeToken<List<despVariedadesTab>>() {
                    }.getType());

            return Lproductos;
    }
}
