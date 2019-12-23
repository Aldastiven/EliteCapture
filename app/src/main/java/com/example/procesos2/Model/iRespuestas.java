package com.example.procesos2.Model;

import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Model.interfaz.respuestas;
import com.example.procesos2.Model.tab.RespuestasTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class iRespuestas extends sqlConect implements respuestas {
    public String nombre;

    public String ins = "INSERT INTO Datos_Procesos_Detalle (Fecha, Id_Procesos, Id_Procesos_Detalle, Valor_Resp_D, Porc_Resp_D, Id_Terminal)\n" +
                        "VALUES (?,?,?,?,?,?)";

    List<RespuestasTab> RT = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;

    public iRespuestas(String path){
        this.cn = getConexion();
        getPath(path);
    }

    public  void getPath(String path){
        ja = new JsonAdmin();
        this.path = path;
    }

    @Override
    public List<RespuestasTab> forProceso(long id, String respuesta) throws Exception {
        return null;
    }

    @Override
    public boolean local(Long id) throws Exception {
        return false;
    }

    @Override
    public String insert(RespuestasTab o) throws Exception {
        try{
            o.setIdreg((long) RT.size() + 1);
            RT.add(o);
            local();

            return "registro de respuestas con exito" + RT.size();

        }catch (Exception ex){
            return "Error al registrar las respuestas"+ex;
        }
    }

    @Override
    public String update(RespuestasTab o, Long id) throws Exception {
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        try {
            all();
            int id2 = (int) id.longValue() - 1;
            RT.clear();
            local();

            return "se elimino";
        }catch (Exception ex){
         return ex.toString();
        }
    }

    @Override
    public String limpiar(RespuestasTab o) throws Exception {
        try {
            all();
            RT.clear();
            local();
            return "";
        }catch (Exception ex){
            return "Exception al limpiar el archivo \n"+ex;
        }
    }

    @Override
    public RespuestasTab oneId(Long id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        String contenido = RT.toString();
        return ja.WriteJson(path, nombre, contenido);
    }

    @Override
    public List<RespuestasTab> all() throws Exception {
        Gson gson = new Gson();
        RT = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<RespuestasTab>>() {
        }.getType());

        return RT;
    }

    public String Record(RespuestasTab o){
        try{
            PreparedStatement ps = cn.prepareStatement(ins);
            ps.setString(1, o.getFecha());
            ps.setLong(2, o.getIdProceso());
            ps.setLong(3, o.getIdPregunta());
            ps.setString(4, o.getRespuesta());
            ps.setDouble(5, o.getPorcentaje());
            ps.setString(6, o.getTerminal());

            ps.executeUpdate();
            return "";

        }catch (Exception ex){
            return "Error al insertar el servidor \n"+ex;
        }
    }
}
