package com.example.procesos2.Model;


import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.interfaz.index;
import com.example.procesos2.Model.tab.IndexTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class iIndex extends sqlConect implements index {

    public List<IndexTab> i1 = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;

    public String nombre;

    final String one = "SELECT [codigo_proceso] ,[Nombre_proceso]\n" +
                        "  FROM [dbo].[Procesos]\n" +
                        "  WHERE  [id_proceso] = ?;";

    final String all = "SELECT [codigo_proceso] , [Nombre_Proceso]\n" +
                        "  FROM [dbo].[Procesos] order by [id_proceso];";


    public iIndex(String path) throws Exception {
        this.cn = getConexion();
        getPath(path);
    }

    public void getPath(String path) {
        ja = new JsonAdmin();
        this.path = path;
    }


    @Override
    public List<IndexTab> forProceso(long id_proceso, String nom_proceso) throws Exception {
        return null;
    }

    @Override
    public String insert(IndexTab o) throws Exception {
        return null;
    }

    @Override
    public String update(IndexTab o, Long id) throws Exception {
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        ResultSet rs;
        PreparedStatement ps =cn.prepareStatement(all);
        rs = ps.executeQuery();

        while (rs.next()){
            i1.add(gift(rs));
        }

        closeConexion(cn,rs);
        String contenido = i1.toString();

        return ja.WriteJson(path,nombre,contenido);
    }

    @Override
    public boolean local(Long id_proceso) throws Exception {
        ResultSet rs;
        PreparedStatement ps =cn.prepareStatement(all);
        ps.setLong(1, id_proceso);
        rs = ps.executeQuery();

        while (rs.next()){
            i1.add(gift(rs));
        }

        closeConexion(cn,rs);

        String contenido = i1.toString();

        return ja.WriteJson(path, nombre, contenido);
    }

    @Override
    public List<IndexTab> all() throws Exception {

        Gson gson = new Gson();
        i1 = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<IndexTab>>() {
        }.getType());

        return i1;
    }

    @Override
    public String limpiar(IndexTab o) throws Exception {
        return null;
    }

    private IndexTab gift(ResultSet sr) throws Exception{
        IndexTab i = new IndexTab();
        i.setCodProceso(sr.getLong("codigo_proceso"));
        i.setNomProceso(sr.getString("Nombre_Proceso").trim());
        return i;
    }

    @Override
    public IndexTab oneId(Long id) throws Exception {
        return null;
    }





}
