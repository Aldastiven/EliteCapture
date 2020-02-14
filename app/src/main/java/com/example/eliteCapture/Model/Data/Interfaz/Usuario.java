package com.example.eliteCapture.Model.Data.Interfaz;

import com.example.eliteCapture.Config.DAO;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;


public interface Usuario extends DAO<Long, UsuarioTab, String> {
    boolean local(Long id_usuario) throws Exception;

    UsuarioTab login(int user, int pass) throws Exception;
}
