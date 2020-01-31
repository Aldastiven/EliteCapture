package com.example.procesos2.Model.tab;

public class  RespuestasTab {
    private Long idreg;
    private String Fecha;
    private Long idProceso;
    private Long idPregunta;
    private String Respuesta;
    private String Terminal;
    private Double Porcentaje;
    private int idUsuario;
    private int Consecutivo;

    //CONSTRUCTOR VACIO
    public RespuestasTab() {
    }

    //CONSTRUCTOR

    public RespuestasTab(Long idreg, String fecha, Long idProceso, Long idPregunta, String respuesta, String terminal, Double porcentaje, int idUsuario, int consecutivo) {
        this.idreg = idreg;
        Fecha = fecha;
        this.idProceso = idProceso;
        this.idPregunta = idPregunta;
        Respuesta = respuesta;
        Terminal = terminal;
        Porcentaje = porcentaje;
        this.idUsuario = idUsuario;
        Consecutivo = consecutivo;
    }


    //S&G
    public Long getIdreg() {
        return idreg;
    }

    public void setIdreg(Long idreg) {
        this.idreg = idreg;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public Long getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Long idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(String respuesta) {
        Respuesta = respuesta;
    }

    public String getTerminal() {
        return Terminal;
    }

    public void setTerminal(String terminal) {
        Terminal = terminal;
    }

    public Double getPorcentaje() {
        return Porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        Porcentaje = porcentaje;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getConsecutivo() {
        return Consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        Consecutivo = consecutivo;
    }

    @Override
    public String toString(){
        return " {" +
                "\"idreg\" :" + idreg + ",\n" +
                "\"Fecha\" : \""+ Fecha +"\",\n"+
                "\"idProceso\" :" + idProceso + ",\n" +
                "\"idPregunta\" :" + idPregunta + ",\n" +
                "\"Respuesta\" : \"" + Respuesta + "\",\n" +
                "\"Porcentaje\" :" + Porcentaje + " ,\n" +
                "\"Terminal\" : \"" + Terminal + "\" ,\n" +
                "\"idUsuario\" :" + idUsuario + ",\n" +
                "\"Consecutivo\":" + Consecutivo + "\n"+
                "} \n";
    }
}
