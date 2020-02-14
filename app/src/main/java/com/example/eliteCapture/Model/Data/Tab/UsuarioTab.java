package com.example.eliteCapture.Model.Data.Tab;

public class UsuarioTab {


    private int id_login;
    private int id_usuario;
    private String nombre_usuario;
    private int password;
    private String grupo1;
    private String grupo2;
    private String grupo3;
    private int[] procesos;

    public UsuarioTab(int id_login, int id_usuario, String nombre_usuario, int password, String grupo1, String grupo2, String grupo3, int[] procesos) {
        this.setId_login(id_login);
        this.setId_usuario(id_usuario);
        this.setNombre_usuario(nombre_usuario);
        this.setPassword(password);
        this.setGrupo1(grupo1);
        this.setGrupo2(grupo2);
        this.setGrupo3(grupo3);
        this.setProcesos(procesos);
    }

    public int getId_login() {
        return id_login;
    }

    public void setId_login(int id_login) {
        this.id_login = id_login;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public String getGrupo1() {
        return grupo1;
    }

    public void setGrupo1(String grupo1) {
        this.grupo1 = grupo1;
    }

    public String getGrupo2() {
        return grupo2;
    }

    public void setGrupo2(String grupo2) {
        this.grupo2 = grupo2;
    }

    public String getGrupo3() {
        return grupo3;
    }

    public void setGrupo3(String grupo3) {
        this.grupo3 = grupo3;
    }

    public int[] getProcesos() {
        return procesos;
    }

    public void setProcesos(int[] procesos) {
        this.procesos = procesos;
    }

}
