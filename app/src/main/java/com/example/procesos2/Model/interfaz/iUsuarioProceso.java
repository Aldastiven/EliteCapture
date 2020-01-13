package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.UsuarioProcesoTab;

public interface iUsuarioProceso extends DAO <Long, UsuarioProcesoTab, String> {
    boolean local(Long id_usuario) throws Exception;
}
