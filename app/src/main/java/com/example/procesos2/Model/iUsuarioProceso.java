package com.example.procesos2.Model;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.UsuarioProcesoTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iUsuarioProceso extends sqlConect implements DAO {

    public List<UsuarioProcesoTab> upt = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;

    public String nombre;

    final String all ="SELECT [id_login_proc]\n" +
                        "      ,[id_usuario]\n" +
                        "      ,[id_procesos]\n" +
                        "  FROM [dbo].[login_proceso]";


    public iUsuarioProceso(String path){
        this.cn = getConexion();
        getPath(path);
    }

    public void getPath(String path){
        ja = new JsonAdmin();
        this.path = path;
    }

    @Override
    public String insert(Object o) throws Exception {
        return null;
    }

    @Override
    public String update(Object o, Object id) throws Exception {
        return null;
    }

    @Override
    public String delete(Object id) throws Exception {
        return null;
    }

    @Override
    public String limpiar(Object o) throws Exception {
        return null;
    }

    @Override
    public Object oneId(Object id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        ResultSet rs;
        PreparedStatement ps  = cn.prepareStatement(all);
        rs = ps.executeQuery();

        while (rs.next()){
            upt.add(gift(rs));
        }

        closeConexion(cn,rs);

        return ja.WriteJson(path,nombre,upt.toString());
    }

    @Override
    public List all() throws Exception {
        Gson gson = new Gson();
        upt = gson.fromJson(ja.ObtenerLista(path,nombre),new TypeToken<List<UsuarioProcesoTab>>(){
        }.getType());

        return upt;
    }

    public UsuarioProcesoTab gift(ResultSet rs)throws  Exception{
        UsuarioProcesoTab u = new UsuarioProcesoTab();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setIdProceso(rs.getInt("id_procesos"));
        return u;
    }
}
