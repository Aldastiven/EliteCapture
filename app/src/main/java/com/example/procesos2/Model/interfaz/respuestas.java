package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.RespuestasTab;

import java.util.List;

public interface respuestas extends DAO <Long, RespuestasTab, String>{
    List<RespuestasTab> forProceso(long id, String respuesta) throws Exception;
    boolean local(Long id) throws Exception;
}
