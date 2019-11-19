package com.example.procesos2.Model.tab;

public class DetallesTab {

    private Long idProceso;
    private Long codDetalle;
    private String quesDetalle;
    private String tipoDetalle;

    //CONSTRUCTOR VACIO
    public DetallesTab() {
    }

    //CONSTRUCTOR CON PARAMETROS A USAR
    public DetallesTab(Long idProceso, Long codDetalle, String quesDetalle, String tipoDetalle) {
        this.idProceso = idProceso;
        this.codDetalle = codDetalle;
        this.quesDetalle = quesDetalle;
        this.tipoDetalle = tipoDetalle;
    }


    //SG
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

    //JSON
    public String toString(){
        return"\n{" +
                "\"idProceso\":"+idProceso+",\n"+
                "\"codDetalle\":"+codDetalle+",\n"+
                "\"quesDetalle\": \"" + quesDetalle + "\",\n" +
                "\"tipoDetalle\": \"" +tipoDetalle+ "\"\n"+
                "}";
    }
}
