package com.example.eliteCapture.Model.Data.Tab;

public class envioTab {

    private String fecha;
    private Long idProceso;
    private Long idDetalle;
    private String respuesta;
    private Float porcentaje;
    private String terminal;
    private int idUsuario;
    private int consecutivoJson;

    public envioTab() {
    }

    public envioTab(String fecha, Long idProceso, Long idDetalle, String respuesta, Float porcentaje, String terminal, int idUsuario, int consecutivoJson) {
        this.fecha = fecha;
        this.idProceso = idProceso;
        this.idDetalle = idDetalle;
        this.respuesta = respuesta;
        this.porcentaje = porcentaje;
        this.terminal = terminal;
        this.idUsuario = idUsuario;
        this.consecutivoJson = consecutivoJson;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getConsecutivoJson() {
        return consecutivoJson;
    }

    public void setConsecutivoJson(int consecutivoJson) {
        this.consecutivoJson = consecutivoJson;
    }
}
