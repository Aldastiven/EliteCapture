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

public class iRespuestas  implements respuestas {
    public String nombre = "Respuestas";

    public String ins = "INSERT INTO Datos_Procesos_Detalle (Fecha, Id_Procesos, Id_Procesos_Detalle, Valor_Resp_D, Porc_Resp_D, Id_Terminal, Id_usuario)\n" +
                        "VALUES (?,?,?,?,?,?,?)";

    List<RespuestasTab> RT = new ArrayList<>();
    Connection cn = null;
    String path = null;
    JsonAdmin ja = null;

    public iRespuestas(Connection cn,String path){
        this.cn = cn;
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

            if(!local()) {
                o.setIdreg((long) RT.size());
                RT.add(o);
                local();
                return "registro de respuestas con exito" + RT.size();
            }else {
                o.setIdreg((long) all().size());
                RT.add(o);
                local();
                return "registro de respuestas con exito pasado" + all().size();
            }

        }catch (Exception ex){
            return "Error al registrar las respuestas"+ex;
        }
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
            ps.setInt(7, o.getIdUsuario());

            ps.executeUpdate();
            delete(o.getIdreg());
            local();
            return "Se envio";

        }catch (Exception ex){
            return "Error al insertar el servidor \n"+ex;
        }
    }
}
