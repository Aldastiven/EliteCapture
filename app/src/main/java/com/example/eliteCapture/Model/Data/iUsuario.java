package com.example.eliteCapture.Model.Data;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Interfaz.Usuario;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iUsuario implements Usuario {

    public List<UsuarioTab> ut = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;

    public String nombre = "Usuarios";

    final String all = "SELECT [id_login],l.[id_usuario],[nombre_usuario],[password],[Grupo1],[Grupo2],[Grupo3]," +
            "   STRING_AGG(lp.id_procesos, ',')  WITHIN GROUP (ORDER BY lp.id_procesos) as procesos\n" +
            "   FROM [Formularios].[dbo].[login] l \n" +
            "   left join [Formularios].[dbo].[login_proceso] lp on l.id_usuario = lp.id_usuario\n" +
            "   GROUP BY [id_login],l.[id_usuario],[nombre_usuario],[password],[Grupo1],[Grupo2],[Grupo3]";

    public iUsuario(Connection cn, String path) throws Exception {
        this.cn = cn;
        getPath(path);
    }

    public void getPath(String path) {
        ja = new JsonAdmin();
        this.path = path;
    }

    @Override
    public boolean local(Long id_usuario) throws Exception {
        return false;
    }

    @Override
    public String insert(UsuarioTab o) throws Exception {
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
            ut.add(gift(rs));
        }

        rs.close();

        String contenido = new Gson().toJson(ut);
        return ja.WriteJson(path, nombre, contenido);
    }

    @Override
    public List<UsuarioTab> all() throws Exception {
        Gson gson = new Gson();
        ut = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<UsuarioTab>>() {
        }.getType());

        return ut;
    }

    @Override
    public String json(UsuarioTab o) {
        return new Gson().toJson(o);
    }

    private UsuarioTab gift(ResultSet rs) throws Exception {
        return new UsuarioTab(
                rs.getInt("id_login"),
                rs.getInt("id_usuario"),
                rs.getString("nombre_usuario"),
                rs.getInt("password"),
                rs.getString("Grupo1"),
                rs.getString("Grupo2"),
                rs.getString("Grupo3"),
                (rs.getString("procesos") != null)? convertir(rs.getString("procesos").split(",")) : null);
    }

    public int[] convertir(String[] texto) {
        int[] n = new int[texto.length];
        for (int i = 0; i < texto.length; i++) {
            n[i] = Integer.parseInt(texto[i]);
        }
        return n;
    }

    @Override
    public UsuarioTab login(int user, int pass) throws Exception {
        all();
        for (UsuarioTab m : ut) {
            if (m.getId_usuario() == user && m.getPassword() == pass) {
                return m;
            }
        }
        return null;
    }

}
