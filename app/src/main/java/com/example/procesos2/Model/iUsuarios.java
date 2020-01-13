package com.example.procesos2.Model;

import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.interfaz.usuarios;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.UsuariosTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iUsuarios extends sqlConect implements usuarios {

    public List<UsuariosTab> ut = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;

    public String nombre;

    final String all = "SELECT [id_login]\n" +
                        "      ,[id_usuario]\n" +
                        "      ,[nombre_usuario]\n" +
                        "      ,[password]\n" +
                        "  FROM [dbo].[login]";

    public iUsuarios(String path) throws Exception{
        this.cn = getConexion();
        getPath(path);
    }

    public void getPath(String path){
        ja = new JsonAdmin();
        this.path = path;
    }

    @Override
    public boolean local(Long id_usuario) throws Exception {
        return false;
    }

    @Override
    public String insert(UsuariosTab o) throws Exception {
        return null;
    }

    @Override
    public String update(UsuariosTab o, Long id) throws Exception {
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }

    @Override
    public String limpiar(UsuariosTab o) throws Exception {
        return null;
    }

    @Override
    public UsuariosTab oneId(Long id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        ResultSet rs;
        PreparedStatement ps = cn.prepareStatement(all);
        rs = ps.executeQuery();

        while (rs.next()){
            ut.add(gift(rs));
        }

        closeConexion(cn,rs);
        String contenido = ut.toString();

        return ja.WriteJson(path,nombre,contenido);
    }

    @Override
    public List<UsuariosTab> all() throws Exception {
        Gson gson = new Gson();
        ut = gson.fromJson(ja.ObtenerLista(path,nombre),new TypeToken<List<UsuariosTab>>(){
        }.getType());

        return ut;
    }

    private UsuariosTab gift(ResultSet rs)throws Exception{
        UsuariosTab u = new UsuariosTab();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombreUsuario(rs.getString("nombre_usuario").trim());
        u.setPassUsuario(rs.getInt("password"));
        return u;
    }

    public UsuariosTab login(int user, int pass) {
            for (UsuariosTab m : ut) {
                if (m.getIdUsuario() == user || m.getPassUsuario() == pass) {
                    msj(m.toString());
                    return m;
                }else {
                    return null;
                }
            }
        return null;
    }

    public String msj(String msj){
        return msj;
    }
}
