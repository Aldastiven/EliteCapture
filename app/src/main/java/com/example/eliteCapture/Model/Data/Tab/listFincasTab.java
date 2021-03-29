package com.example.eliteCapture.Model.Data.Tab;

import java.util.List;

public class listFincasTab {
    int usuario;
    List<fincasTab> fincas;

    public listFincasTab(int usuario, List<fincasTab> fincas) {
        this.usuario = usuario;
        this.fincas = fincas;
    }

    public int getUsuario() {
        return usuario;
    }

    public List<fincasTab> getFincas() {
        return fincas;
    }

    public static class fincasTab{
        int idFinca;
        String nombreFinca;

        public fincasTab(int idFinca, String nombreFinca) {
            this.idFinca = idFinca;
            this.nombreFinca = nombreFinca;
        }

        public int getIdFinca() {
            return idFinca;
        }

        public String getNombreFinca() {
            return nombreFinca;
        }
    }
}
