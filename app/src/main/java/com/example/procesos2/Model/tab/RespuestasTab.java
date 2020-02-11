package com.example.procesos2.Model.tab;

public class  RespuestasTab {
    private Long id;
    private int idProceso;
    private int idPregunta;
    private float ponderado;
    private String respuesta;

    //CONSTRUCTOR VACIO
    public RespuestasTab() {
    }

    public RespuestasTab(Long id, int idProceso, int idPregunta, float ponderado, String respuesta) {
        this.id = id;
        this.idProceso = idProceso;
        this.idPregunta = idPregunta;
        this.ponderado = ponderado;
        this.respuesta = respuesta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public float getPonderado() {
        return ponderado;
    }

    public void setPonderado(float ponderado) {
        this.ponderado = ponderado;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
