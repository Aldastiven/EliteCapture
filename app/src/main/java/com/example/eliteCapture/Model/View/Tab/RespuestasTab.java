package com.example.eliteCapture.Model.View.Tab;

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;

import java.util.List;

public class  RespuestasTab {
    private Long id;
    private int idProceso;
    private Long idPregunta;
    String tipo;
    String Pregunta;
    private float ponderado;
    private String respuesta;
    List <DesplegableTab> desplegable;

    public RespuestasTab(Long id, int idProceso, Long idPregunta, String tipo, String pregunta, float ponderado, String respuesta, List <DesplegableTab> desplegable) {
        this.id = id;
        this.idProceso = idProceso;
        this.idPregunta = idPregunta;
        this.tipo = tipo;
        Pregunta = pregunta;
        this.ponderado = ponderado;
        this.respuesta = respuesta;
        this.desplegable = desplegable;
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

    public Long getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Long idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPregunta() {
        return Pregunta;
    }

    public void setPregunta(String pregunta) {
        Pregunta = pregunta;
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

    public List<DesplegableTab> getDesplegable() {
        return desplegable;
    }

    public void setDesplegable(List<DesplegableTab> desplegable) {
        this.desplegable = desplegable;
    }
}
