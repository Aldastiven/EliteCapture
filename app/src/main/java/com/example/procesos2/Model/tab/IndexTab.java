package com.example.procesos2.Model.tab;

import android.util.Log;

public class IndexTab {
    private Long codProceso;
    private  String nomProceso;
    private String personalizado3;

    //CONTRUCTOR VACIO
    public IndexTab() {
    }

    public IndexTab(Long codProceso, String nomProceso, String personalizado3) {
        this.codProceso = codProceso;
        this.nomProceso = nomProceso;
        this.personalizado3 = personalizado3;
    }

    //JSON
    public String toString(){
        String json = "{"+
                "\"codProceso\":"+codProceso+",\n"+
                "\"nomProceso\": \"" + nomProceso + "\",\n" +
                "\"personalizado3\": \"" + personalizado3 + "\"\n" +
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

    public String getPersonalizado3() {
        return personalizado3;
    }

    public void setPersonalizado3(String personalizado3) {
        this.personalizado3 = personalizado3;
    }
}


