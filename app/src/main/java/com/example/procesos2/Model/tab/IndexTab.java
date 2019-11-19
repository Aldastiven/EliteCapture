package com.example.procesos2.Model.tab;

import android.util.Log;

public class IndexTab {
    private Long codProceso;
    private  String nomProceso;

    //CONTRUCTOR VACIO
    public IndexTab() {
    }

    //CONSTRUCTOR CON PARAMETROS A USAR
    public IndexTab(Long codProceso, String nomProceso) {
        this.codProceso = codProceso;
        this.nomProceso = nomProceso;
    }

    //JSON
    public String toString(){
        String json = "{"+
                "\"codProceso\":"+codProceso+",\n"+
                "\"nomProceso\": \"" + nomProceso + "\"\n" +
                "}";

        return json;

        //Log.i("my app",json);
    }


    //S & G
    public Long getCodProceso() {
        return codProceso;
    }

    public void setCodProceso(Long codProceso) {
        this.codProceso = codProceso;
    }

    public String getNomProceso() {
        return nomProceso;
    }

    public void setNomProceso(String nomProceso) {
        this.nomProceso = nomProceso;
    }

}


