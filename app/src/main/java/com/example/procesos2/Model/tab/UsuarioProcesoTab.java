package com.example.procesos2.Model.tab;

public class UsuarioProcesoTab {

    private int idUsuario;
    private int idProceso;

    //constructor vacio
    public UsuarioProcesoTab() {
    }

    //constructor
    public UsuarioProcesoTab(int idUsuario, int idProceso) {
        this.idUsuario = idUsuario;
        this.idProceso = idProceso;
    }

    //S & G

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public String toString(){
        return "{\n" +
                "\"idUsuario\": "+idUsuario+", \n"+
                "\"idProceso\": "+idProceso+" \n"+
                "\n}";
    }
}
