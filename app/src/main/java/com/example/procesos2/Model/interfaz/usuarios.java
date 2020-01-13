package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.UsuariosTab;


public interface usuarios extends DAO<Long, UsuariosTab, String> {
    boolean local(Long id_usuario) throws Exception;
}
