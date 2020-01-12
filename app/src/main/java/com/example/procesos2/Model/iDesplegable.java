package com.example.procesos2.Model;

import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.interfaz.Desplegable;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class iDesplegable extends sqlConect implements Desplegable {

    public List<DesplegableTab> DT = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;

    public String nombre = "";


    public iDesplegable(String path) throws Exception{
        this.cn = getConexion();
        getPath(path);
    }

    public void getPath(String path){
        ja = new JsonAdmin();
        this.path = path;
    }

    @Override
    public List<DesplegableTab> forDesplegable(Long cod, String option) throws Exception {
        return null;
    }

    @Override
    public boolean local(Long cod) throws Exception {
        return false;
    }

    @Override
    public String insert(DesplegableTab o) throws Exception {
        return null;
    }

    @Override
    public String update(DesplegableTab o, Long id) throws Exception {
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }

    @Override
    public String limpiar(DesplegableTab o) throws Exception {
        return null;
    }

    @Override
    public DesplegableTab oneId(Long id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement("SELECT [Filtro]\n" +
                                                        ",[Codigo]\n" +
                                                        ",[Opcion]\n" +
                                                        "FROM [dbo].[Desplegables]");
        rs = ps.executeQuery();

        while (rs.next()){
            DT.add(gift(rs));
        }

        closeConexion(cn,rs);
        String contenido = DT.toString();

        return ja.WriteJson(path,nombre,contenido);
    }


    public String group(){
        List<String> nombresdesp = new ArrayList<>();
        try {
            ResultSet rs;
            PreparedStatement ps = cn.prepareStatement("SELECT [Filtro]\n" +
                                                            "FROM [dbo].[Desplegables]\n" +
                                                            "Group by [Filtro]");
            rs = ps.executeQuery();

            while (rs.next()) {
                nombresdesp.add(rs.getString("Filtro"));
            }

            //return nombresdesp.toString();
            return  nombresdesp.toString();

        }catch (Exception EX){
            return nombresdesp.toString()+"\n"+EX;
        }

    }

    public boolean traerDesplegable(String nombreD){

        //DT.clear();
        try {
            ResultSet rs;
            PreparedStatement ps = cn.prepareStatement("SELECT \n" +
                                                            "      [Filtro]\n" +
                                                            "      ,[Codigo]\n" +
                                                            "      ,[Opcion]\n" +
                                                            "  FROM [dbo].[Desplegables]\n" +
                                                            "  WHERE [Filtro]='"+nombreD+"'");
            rs = ps.executeQuery();
            while (rs.next()) {
                DT.add(gift(rs));
            }

            rs.isBeforeFirst();
            String contenido = DT.toString();


            return ja.WriteJson(path,nombre,contenido);
        }catch (Exception EX){
            return false;
        }
    }

    @Override
    public List<DesplegableTab> all() throws Exception {
        Gson gson = new Gson();
        DT = gson.fromJson(ja.ObtenerLista(path,nombre), new TypeToken<List<DesplegableTab>>(){
        }.getType());
        return DT;
    }

    public DesplegableTab gift(ResultSet rs) throws Exception{
        DesplegableTab d = new DesplegableTab();
        d.setFiltro(rs.getString("Filtro"));
        d.setCod(rs.getString("Codigo"));
        d.setOptions(rs.getString("Opcion"));
        return d;
    }

}
