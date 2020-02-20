package com.example.eliteCapture.Model.Data;

import android.util.Log;

import com.example.eliteCapture.Config.DAO;
import com.example.eliteCapture.Model.Data.Tab.envioTab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class iEnvio implements DAO {

    String path = null;
    Connection cn = null;

    public iEnvio(String path, Connection cn) {
        this.path = path;
        this.cn = cn;
    }

    public String ins = "INSERT INTO Datos_Procesos_Detalle (fecha, id_procesos, id_procesos_detalle, valor_resp_d, porc_resp_d, id_terminal, id_usuario, consec_json)\n" +
            "VALUES (?,?,?,?,?,?,?)";

    @Override
    public String insert(Object o) throws Exception {
        return null;
    }

    @Override
    public String delete(Object id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        return false;
    }

    @Override
    public List all() throws Exception {
        return null;
    }

    @Override
    public String json(Object o) throws Exception {
        return null;
    }

    public String Record(envioTab o){
        try{
            /*
            PreparedStatement ps = cn.prepareStatement(ins);
            ps.setString(1, o.getFecha());
            ps.setLong(2, o.getIdProceso());
            ps.setLong(3, o.getIdDetalle());
            ps.setString(4, o.getRespuesta());
            ps.setDouble(5, o.getPorcentaje());
            ps.setString(6, o.getTerminal());
            ps.setInt(7, o.getIdUsuario());
            ps.setInt(8, o.getConsecutivoJson());
            ps.executeUpdate();
             */
            Log.i("envio","fecha : "+o.getFecha()+" id proceso : "+o.getIdProceso() +" id detalle : "+o.getIdDetalle()+" respuesta : "+o.getRespuesta()+" porcentaje : "+o.getPorcentaje()+" terminal : "+o.getTerminal()+" id usuario : "+o.getIdUsuario()+" consecutivoJson : "+o.getConsecutivoJson());
            return "Se enviaron con exito los datos";

        }catch (Exception ex){
            return "Error al insertar el servidor \n"+ex;
        }
    }
}
