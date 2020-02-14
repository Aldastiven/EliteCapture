package com.example.eliteCapture.Model.Data.Tab;

public class DetalleTab {

    private Long id_detalle;
    private int id_proceso;
    private Long codigo_detalle;
    private String nombre_detalle;
    private String tipo;
    private String lista_desp;
    private String tipo_M;
    private Float porcentaje;


    //CONSTRUCTOR CON PARAMETROS A USAR

    public DetalleTab(Long id_detalle, int id_proceso, Long codigo_detalle, String nombre_detalle, String tipo, String lista_desp, String tipo_M, Float porcentaje) {
        this.id_detalle = id_detalle;
        this.id_proceso = id_proceso;
        this.codigo_detalle = codigo_detalle;
        this.nombre_detalle = nombre_detalle;
        this.tipo = tipo;
        this.lista_desp = lista_desp;
        this.tipo_M = tipo_M;
        this.porcentaje = porcentaje;
    }


    //SG


    public Long getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(Long id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_proceso() {
        return id_proceso;
    }

    public void setId_proceso(int id_proceso) {
        this.id_proceso = id_proceso;
    }

    public Long getCodigo_detalle() {
        return codigo_detalle;
    }

    public void setCodigo_detalle(Long codigo_detalle) {
        this.codigo_detalle = codigo_detalle;
    }

    public String getNombre_detalle() {
        return nombre_detalle;
    }

    public void setNombre_detalle(String nombre_detalle) {
        this.nombre_detalle = nombre_detalle;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLista_desp() {
        return lista_desp;
    }

    public void setLista_desp(String lista_desp) {
        this.lista_desp = lista_desp;
    }

    public String getTipo_M() {
        return tipo_M;
    }

    public void setTipo_M(String tipo_M) {
        this.tipo_M = tipo_M;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

}
