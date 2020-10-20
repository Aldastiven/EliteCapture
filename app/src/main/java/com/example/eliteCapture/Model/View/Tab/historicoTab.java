package com.example.eliteCapture.Model.View.Tab;

import java.util.List;

public class historicoTab {

    private int consecutivo;
    private String fecha;
    private int idProceso;
    private List<RespuestasTab> header;
    private List<RespuestasTab> questions;
    private List<RespuestasTab> footer;
    private String terminal;
    private int idUsuario;
    private int estado;

    public historicoTab(int consecutivo, String fecha, int idProceso, List<RespuestasTab> header, List<RespuestasTab> questions, List<RespuestasTab> footer, String terminal, int idUsuario) {
        this.consecutivo = consecutivo;
        this.fecha = fecha;
        this.idProceso = idProceso;
        this.header = header;
        this.questions = questions;
        this.footer = footer;
        this.terminal = terminal;
        this.idUsuario = idUsuario;
        this.estado = 1;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public List<RespuestasTab> getHeader() {
        return header;
    }

    public void setHeader(List<RespuestasTab> header) {
        this.header = header;
    }

    public List<RespuestasTab> getQuestions() {
        return questions;
    }

    public void setQuestions(List<RespuestasTab> questions) {
        this.questions = questions;
    }

    public List<RespuestasTab> getFooter() {
        return footer;
    }

    public void setFooter(List<RespuestasTab> footer) {
        this.footer = footer;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
