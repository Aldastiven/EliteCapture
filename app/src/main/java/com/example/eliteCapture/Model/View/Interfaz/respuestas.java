package com.example.eliteCapture.Model.View.Interfaz;

import com.example.eliteCapture.Config.DAO;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;

import java.util.List;

public interface respuestas extends DAO <Long, RespuestasTab, String>{
    List<RespuestasTab> forProceso(long id, String respuesta) throws Exception;
    boolean local(Long id) throws Exception;
}
