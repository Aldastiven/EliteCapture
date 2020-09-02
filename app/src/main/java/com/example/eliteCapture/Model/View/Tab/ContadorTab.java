package com.example.eliteCapture.Model.View.Tab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContadorTab {
    private int id;
    private int usuario;
    String fecha;
    private List<procesoTab> procesos = new ArrayList<>();

    public ContadorTab(int usuario) {
        this.fecha = getFecha();
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


    public String getFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha[] = sdf.format(new Date()).split(" ");
        return fecha[0];
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


