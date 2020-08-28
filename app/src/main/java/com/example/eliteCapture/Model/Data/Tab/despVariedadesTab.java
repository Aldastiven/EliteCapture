package com.example.eliteCapture.Model.Data.Tab;

import java.util.List;

public class despVariedadesTab {

    String filtro;
    int idProducto;
    String producto;
    List<variedades> variedades;

    public despVariedadesTab(String filtro, int idProducto, String producto, List<despVariedadesTab.variedades> variedades) {
        this.filtro = filtro;
        this.idProducto = idProducto;
        this.producto = producto;
        this.variedades = variedades;
    }

    public String getFiltro() {
        return filtro;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getProducto() {
        return producto;
    }

    public List<despVariedadesTab.variedades> getVariedades() {
        return variedades;
    }

    public static class variedades{
        int idVariedad;
        String variedad;

        public variedades(int idVariedad, String variedad) {
            this.idVariedad = idVariedad;
            this.variedad = variedad;
        }

        public int getIdVariedad() {
            return idVariedad;
        }

        public void setIdVariedad(int idVariedad) {
            this.idVariedad = idVariedad;
        }

        public String getVariedad() {
            return variedad;
        }

        public void setVariedad(String variedad) {
            this.variedad = variedad;
        }
    }

}
