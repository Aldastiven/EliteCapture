package com.example.eliteCapture.Model.Data.Tab;

public class DesplegableTab {
    private String Filtro;
    private String Codigo;
    private String Opcion;
    private String Condicional;


    public DesplegableTab(String filtro, String codigo, String opcion, String Condicional) {
        this.Filtro = filtro;
        this.Codigo = codigo;
        this.Opcion = opcion;
        this.Condicional = Condicional;
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

    public String getCondicional() {
        return Condicional;
    }

    public void setCondicional(String condicional) {
        Condicional = condicional;
    }
}
