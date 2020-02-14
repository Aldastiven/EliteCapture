package com.example.eliteCapture.Model.Data;


import android.util.Log;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Interfaz.Proceso;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class iProceso implements Proceso {

    public List<ProcesoTab> i1 = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;

    public String nombre = "Procesos";

    final String one = "SELECT [codigo_proceso] ,[Nombre_proceso]\n" +
            "  FROM [dbo].[Procesos]\n" +
            "  WHERE  [id_proceso] = ?;";

    final String all = "SELECT [id_Proceso],[codigo_proceso],[nombre_proceso],[Personalizado1],[Personalizado2],[Personalizado3],[Personalizado4],[Personalizado5],[Personalizado1_Valor]\n" +
            "  FROM [dbo].[Procesos]\n" +
            "  ORDER BY [codigo_proceso] ASC";


    public iProceso(Connection cn, String path) throws Exception {
        this.cn = cn;
        getPath(path);
    }

    public void getPath(String path) {
        ja = new JsonAdmin();
        this.path = path;
    }


    @Override
    public List<ProcesoTab> forProceso(long id_proceso, String nom_proceso) throws Exception {
        return null;
    }

    @Override
    public String insert(ProcesoTab o) throws Exception {
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(all);
        rs = ps.executeQuery();

        while (rs.next()) {
            i1.add(gift(rs));
        }

        String contenido = new Gson().toJson(i1);

        return ja.WriteJson(path, nombre, contenido);
    }

    @Override
    public boolean local(Long id_proceso) throws Exception {
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(all);
        ps.setLong(1, id_proceso);
        rs = ps.executeQuery();

        while (rs.next()) {
            i1.add(gift(rs));
        }


        Gson gson = new Gson();
        String contenido = gson.toJson(i1);
        Log.i("Proceso: ", contenido);

        return ja.WriteJson(path, nombre, contenido);
    }

    @Override
    public List<ProcesoTab> procesosUsuario(int[] asignados) throws Exception {
        int i = 0;
        List<ProcesoTab> pro = new ArrayList<>();

        for (ProcesoTab proceso : all()) {
            if (proceso.getCodigo_proceso() == asignados[i]) {
                pro.add(proceso);
                i++;
                if (i >= asignados.length) {
                    break;
                }
            }
        }
        return pro;
    }

    @Override
    public List<ProcesoTab> all() throws Exception {

        i1 = new Gson().fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<ProcesoTab>>() {
        }.getType());

        return i1;
    }

    @Override
    public String json(ProcesoTab o) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    private ProcesoTab gift(ResultSet rs) throws Exception {
        return new ProcesoTab(
                rs.getLong("id_Proceso"),
                rs.getInt("codigo_proceso"),
                rs.getString("Nombre_Proceso").trim(),
                rs.getString("Personalizado1"),
                rs.getString("Personalizado2"),
                rs.getString("Personalizado3"),
                rs.getString("Personalizado4"),
                rs.getString("Personalizado5"),
                rs.getString("Personalizado1_Valor"));

    }


}
