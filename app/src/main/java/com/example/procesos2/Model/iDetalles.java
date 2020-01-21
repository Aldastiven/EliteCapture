package com.example.procesos2.Model;

import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.interfaz.Detalles;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.IndexTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iDetalles  implements Detalles {

    public List<DetallesTab> D1 = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin  ja = null;

    public String nombre = "Detalles";

    public  String all = "SELECT [id_detalle]\n" +
                        "      ,[Id_proceso]\n" +
                        "      ,[Codigo_Detalle]\n" +
                        "      ,[Nombre_Detalle]\n" +
                        "      ,[Tipo]\n" +
                        "      ,[Lista_Desp]\n"+
                        "      ,[Tipo_M]\n"+
                        "      ,[Porcentaje]\n"+
                        " FROM [dbo].[Procesos_Detalle]" +
                        " ORDER BY [Id_Detalle] , [Codigo_Detalle]";

    public iDetalles(Connection cn,String path) throws Exception{
            this.cn = cn;
            getPath(path);
    }

    public void getPath(String path){
        ja = new JsonAdmin();
        this.path = path;
    }

    @Override
    public List<IndexTab> forDetalle(long id_proceso, String nom_proceso) throws Exception {
        return null;
    }

    @Override
    public boolean local(Long id_proceso) throws Exception {
        return false;
    }

    @Override
    public String insert(DetallesTab o) throws Exception {
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

        while (rs.next()){
            D1.add(gift(rs));
        }

        //closeConexion(cn,rs);
        String contenido = D1.toString();

        return ja.WriteJson(path,nombre,contenido);
    }

    @Override
    public List<DetallesTab> all() throws Exception {

        Gson gson = new Gson();
        D1 = gson.fromJson(ja.ObtenerLista(path,nombre),new TypeToken<List<DetallesTab>>(){
        }.getType());

        return D1;
    }

    private DetallesTab gift(ResultSet rs) throws Exception{
        DetallesTab d = new DetallesTab();
        d.setIdConsecutivo((long) rs.getInt("id_detalle"));
        d.setIdProceso(rs.getLong("Id_proceso"));
        d.setCodDetalle(rs.getLong("Codigo_Detalle"));
        d.setQuesDetalle(rs.getString("Nombre_Detalle").trim());
        d.setTipoDetalle(rs.getString("Tipo").trim());
        d.setListaDesplegable(rs.getString("Lista_Desp"));
        d.setTipoModulo(rs.getString("Tipo_M"));
        d.setPorcentaje(rs.getFloat("Porcentaje"));
        return d;
    }
}
