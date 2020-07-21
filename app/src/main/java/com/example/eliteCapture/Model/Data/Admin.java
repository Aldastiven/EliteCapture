package com.example.eliteCapture.Model.Data;

import com.example.eliteCapture.Model.Data.Interfaz.Administrador;
import com.example.eliteCapture.Model.Data.Interfaz.Desplegable;
import com.example.eliteCapture.Model.Data.Interfaz.Detalle;
import com.example.eliteCapture.Model.Data.Interfaz.Proceso;
import com.example.eliteCapture.Model.Data.Interfaz.Usuario;

import java.sql.Connection;

public class Admin implements Administrador {

    public Connection con = null;
    public String path = null;

    private Desplegable des = null;
    private Detalle det = null;
    private Proceso pro = null;
    private Usuario usu = null;
    private idespVariedades var = null;
    private ionLine online = null;


    public Admin(Connection con, String path) {
        this.con = con;
        this.path = path;
    }

    @Override
    public Desplegable getDesplegable() throws Exception {
        if (des == null) {
            des = new iDesplegable(con, path);
        }
        return des;
    }

    @Override
    public Detalle getDetalles() throws Exception {
        if (det == null) {
            det = new iDetalle(con, path);
        }
        return det;
    }

    @Override
    public Proceso getProceso() throws Exception {
        if (pro == null) {
            pro = new iProceso(con, path);
        }
        return pro;
    }

    @Override
    public Usuario getUsuario() throws Exception {
        if (usu == null) {
            usu = new iUsuario(con, path);
        }
        return usu;
    }

    public idespVariedades getProductos() throws Exception{
        if(var == null){
            var = new idespVariedades(path, con);
        }
        return var;
    }

    public ionLine getOnline() throws Exception{
        if(online == null){
            online = new ionLine(path);
        }
        return online;
    }
}
