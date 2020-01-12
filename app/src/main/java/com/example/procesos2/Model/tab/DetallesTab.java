package com.example.procesos2.Model.tab;

public class DetallesTab {

    private Long idConsecutivo;
    private Long idProceso;
    private Long codDetalle;
    private String quesDetalle;
    private String tipoDetalle;
    private String listaDesplegable;
    private String tipoModulo;
    private Float porcentaje;

    //CONSTRUCTOR VACIO
    public DetallesTab() {
    }

    //CONSTRUCTOR CON PARAMETROS A USAR

    public DetallesTab (Long idConsecutivo, Long idProceso, Long codDetalle, String quesDetalle, String tipoDetalle, String listaDesplegable, String tipoModulo, Float porcentaje) {
        this.idConsecutivo = idConsecutivo;
        this.idProceso = idProceso;
        this.codDetalle = codDetalle;
        this.quesDetalle = quesDetalle;
        this.tipoDetalle = tipoDetalle;
        this.listaDesplegable = listaDesplegable;
        this.tipoModulo = tipoModulo;
        this.porcentaje = porcentaje;
    }


    //SG

    public Long getIdConsecutivo() {
        return idConsecutivo;
    }

    public void setIdConsecutivo(Long idConsecutivo) {
        this.idConsecutivo = idConsecutivo;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public Long getCodDetalle() {
        return codDetalle;
    }

    public void setCodDetalle(Long codDetalle) {
        this.codDetalle = codDetalle;
    }

    public String getQuesDetalle() {
        return quesDetalle;
    }

    public void setQuesDetalle(String quesDetalle) {
        this.quesDetalle = quesDetalle;
    }

    public String getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle(String tipoDetalle) {
        this.tipoDetalle = tipoDetalle;
    }

    public String getListaDesplegable() {
        return listaDesplegable;
    }

    public void setListaDesplegable(String listaDesplegable) {
        this.listaDesplegable = listaDesplegable;
    }

    public String getTipoModulo() {
        return tipoModulo;
    }

    public void setTipoModulo(String tipoModulo) {
        this.tipoModulo = tipoModulo;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

    //JSON
    public String toString(){
        return"\n{" +
                "\"idConsecuivo\":"+idConsecutivo+",\n"+
                "\"idProceso\":"+idProceso+",\n"+
                "\"codDetalle\":"+codDetalle+",\n"+
                "\"quesDetalle\": \"" + quesDetalle + "\",\n" +
                "\"tipoDetalle\": \"" +tipoDetalle+ "\",\n"+
                "\"listaDesplegable\": \"" +listaDesplegable+ "\",\n"+
                "\"tipoModulo\": \"" +tipoModulo+ "\",\n"+
                "\"porcentaje\": \"" +porcentaje+ "\"\n"+
                "}";
    }
}
