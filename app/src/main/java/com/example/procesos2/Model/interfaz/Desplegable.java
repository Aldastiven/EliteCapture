package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.DesplegableTab;

import java.util.List;

public interface Desplegable extends DAO<Long, DesplegableTab, String> {
    List<DesplegableTab> forDesplegable(Long cod ,String option)throws Exception;
    boolean local(Long cod)throws  Exception;
}
