package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.TemporalTab;


public interface temporal extends DAO<Long, TemporalTab, String>{
    boolean local(Long id_usuario) throws Exception;
}
