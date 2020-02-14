package com.example.eliteCapture.Model.Data.Interfaz;

import com.example.eliteCapture.Config.DAO;
import com.example.eliteCapture.Model.Data.Tab.DetalleTab;

import java.util.List;

public interface Detalle extends DAO<Long, DetalleTab, String> {
    List<DetalleTab> forDetalle(long id_proceso) throws Exception;

    boolean local(Long id_proceso) throws Exception;
}
