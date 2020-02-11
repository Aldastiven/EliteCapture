package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.ContenedorTab;

public interface Contenedor extends DAO<Long, ContenedorTab, String> {
    String update(Long id, ContenedorTab ct) throws Exception;
}
