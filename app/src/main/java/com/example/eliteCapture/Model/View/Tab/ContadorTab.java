package com.example.eliteCapture.Model.View.Tab;

import java.util.ArrayList;
import java.util.List;

public class ContadorTab {
    private int id;
    private int usuario;
    private List<procesoTab> procesos = new ArrayList<>();

    public ContadorTab(int usuario) {
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public List<procesoTab> getProcesos() {
        return procesos;
    }

    public void addProcesos(int proceso) {
        procesos.add(new procesoTab(proceso));
    }

    public void setProcesos(List<procesoTab> procesos) {
        this.procesos = procesos;
    }


    public class procesoTab {

        private int proceso;
        private int cantidad;

        public procesoTab(int proceso) {
            this.proceso = proceso;
            this.cantidad = 1;
        }

        public int getProceso() {
            return proceso;
        }

        public void setProceso(int proceso) {
            this.proceso = proceso;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
    }
}


