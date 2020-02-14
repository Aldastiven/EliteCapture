package com.example.eliteCapture.Model.Data.Tab;

public class DesplegableTab {
    private String Filtro;
    private String Codigo;
    private String Opcion;


    public DesplegableTab(String filtro, String codigo, String opcion) {
        Filtro = filtro;
        Codigo = codigo;
        Opcion = opcion;
    }

    public String getFiltro() {
        return Filtro;
    }

    public void setFiltro(String filtro) {
        Filtro = filtro;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getOpcion() {
        return Opcion;
    }

    public void setOpcion(String opcion) {
        Opcion = opcion;
    }
}
