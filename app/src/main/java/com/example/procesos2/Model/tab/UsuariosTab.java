package com.example.procesos2.Model.tab;

import com.google.gson.Gson;

public class UsuariosTab {

    private int idUsuario;
    private String nombreUsuario;
    private int passUsuario;

    //constructor vacio
    public UsuariosTab() {
    }

    //constructor
    public UsuariosTab(int idUsuario, String nombreUsuario, int passUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.passUsuario = passUsuario;
    }

    //S&G


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getPassUsuario() {
        return passUsuario;
    }

    public void setPassUsuario(int passUsuario) {
        this.passUsuario = passUsuario;
    }


    public String toString(){
        return "{\n" +
                "\"idUsuario\":"+idUsuario+",\n"+
                "\"nombreUsuario\": \""+nombreUsuario+"\",\n"+
                "\"passUsuario\":"+passUsuario+"\n"+
                "}\n";
    }


}
