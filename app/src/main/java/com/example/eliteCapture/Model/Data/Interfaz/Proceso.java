package com.example.eliteCapture.Model.Data.Interfaz;

import com.example.eliteCapture.Config.DAO;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;

import java.util.List;

public interface Proceso extends DAO<Long, ProcesoTab, String> {
    List<ProcesoTab> forProceso(long id_proceso, String nom_proceso) throws Exception;

    boolean local(Long id_proceso) throws Exception;

    List<ProcesoTab> procesosUsuario(int[] asignados) throws Exception;
}
