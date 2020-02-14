package com.example.eliteCapture.Model.Data.Interfaz;

import com.example.eliteCapture.Config.DAO;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;

import java.util.List;

public interface Desplegable extends DAO<Long, DesplegableTab, String> {
    List<DesplegableTab> forDesplegable(Long cod, String option) throws Exception;

    boolean local(Long cod) throws Exception;

    boolean traerDesplegable(String nombreD);

    List<String> group();

    String getNombre();

    void setNombre(String nombre);

}
