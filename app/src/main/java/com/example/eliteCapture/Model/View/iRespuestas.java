package com.example.eliteCapture.Model.View;

import com.example.eliteCapture.Model.View.Interfaz.respuestas;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.google.gson.Gson;

import java.util.List;

public class iRespuestas implements respuestas {


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
        return null;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        return false;
    }

    @Override
    public List<RespuestasTab> all() throws Exception {
        return null;
    }

    @Override
    public String json(RespuestasTab o) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(o);
    }


}
