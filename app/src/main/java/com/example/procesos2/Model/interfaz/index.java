package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.IndexTab;
import com.example.procesos2.Model.tab.procesosTap;

import java.util.List;

public interface index extends DAO <Long, IndexTab, String> {
    List<IndexTab> forProceso(long id_proceso, String nom_proceso) throws Exception;
    boolean local(Long id_proceso) throws Exception;
}
