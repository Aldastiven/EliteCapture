package com.example.eliteCapture.Model.View.Tab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContenedorTab {

    private int consecutivo;
    private String fecha;
    private int idProceso;
    private List<RespuestasTab> header;
    private List<RespuestasTab> questions;
    private List<RespuestasTab> footer;
    private String terminal;
    private int idUsuario;
    private int estado;

    public ContenedorTab(int idProceso, List<RespuestasTab> header, List<RespuestasTab> questions, List<RespuestasTab> footer, int idUsuario, String terminal) {
        this.idProceso = idProceso;
        this.fecha = "";
        this.header = header;
        this.questions = questions;
        this.footer = footer;
        this.idUsuario = idUsuario;
        this.terminal = terminal;
        this.estado = 0;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String fechaString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
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
