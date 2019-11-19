package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.IndexTab;

import java.util.List;

public interface Detalles extends DAO <Long, DetallesTab, String> {
    List<IndexTab> forDetalle(long id_proceso, String nom_proceso) throws Exception;
    boolean local(Long id_proceso) throws Exception;
}
