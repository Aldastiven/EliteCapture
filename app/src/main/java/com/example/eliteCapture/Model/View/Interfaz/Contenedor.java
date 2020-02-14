package com.example.eliteCapture.Model.View.Interfaz;

import com.example.eliteCapture.Config.DAO;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;

public interface Contenedor extends DAO<Long, ContenedorTab, String> {
    String update(Long id, ContenedorTab ct) throws Exception;
}
