package com.example.procesos2.Model.tab;

public class TemporalTab {

    private int usuario;
    private int proceso;
    private int item;
    private String respuesta;
    private Double porcentaje;

    public TemporalTab() {
    }

    public TemporalTab(int usuario, int proceso, int item, String respuesta, Double porcentaje) {
        this.usuario = usuario;
        this.proceso = proceso;
        this.item = item;
        this.respuesta = respuesta;
        this.porcentaje = porcentaje;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public int getProceso() {
        return proceso;
    }

    public void setProceso(int proceso) {
        this.proceso = proceso;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String toString(){
        return "{\n" +
                "\"usuario\":"+usuario+",\n"+
                "\"proceso\":"+proceso+",\n"+
                "\"item\":"+item+",\n"+
                "\"respuesta\": \""+respuesta+"\",\n"+
                "\"porcentaje\":"+porcentaje+"\n"+
                "}";
    }

}
