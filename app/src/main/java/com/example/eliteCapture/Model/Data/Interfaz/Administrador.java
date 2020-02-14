package com.example.eliteCapture.Model.Data.Interfaz;

public interface Administrador {
    Desplegable getDesplegable() throws Exception;

    Detalle getDetalles() throws Exception;

    Proceso getProceso() throws Exception;

    Usuario getUsuario() throws Exception;
}
