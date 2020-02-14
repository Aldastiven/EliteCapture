package com.example.eliteCapture.Model.Data.Tab;

public class ProcesoTab {

    Long id_Proceso;
    int codigo_proceso;
    String nombre_proceso;
    String Personalizado1;
    String Personalizado2;
    String Personalizado3;
    String Personalizado4;
    String Personalizado5;
    String Personalizado1_Valor;

    public ProcesoTab(Long id_Proceso, int codigo_proceso, String nombre_proceso, String personalizado1, String personalizado2, String personalizado3, String personalizado4, String personalizado5, String personalizado1_Valor) {
        this.id_Proceso = id_Proceso;
        this.codigo_proceso = codigo_proceso;
        this.nombre_proceso = nombre_proceso;
        Personalizado1 = personalizado1;
        Personalizado2 = personalizado2;
        Personalizado3 = personalizado3;
        Personalizado4 = personalizado4;
        Personalizado5 = personalizado5;
        Personalizado1_Valor = personalizado1_Valor;
    }

    public Long getId_Proceso() {
        return id_Proceso;
    }

    public void setId_Proceso(Long id_Proceso) {
        this.id_Proceso = id_Proceso;
    }

    public int getCodigo_proceso() {
        return codigo_proceso;
    }

    public void setCodigo_proceso(int codigo_proceso) {
        this.codigo_proceso = codigo_proceso;
    }

    public String getNombre_proceso() {
        return nombre_proceso;
    }

    public void setNombre_proceso(String nombre_proceso) {
        this.nombre_proceso = nombre_proceso;
    }

    public String getPersonalizado1() {
        return Personalizado1;
    }

    public void setPersonalizado1(String personalizado1) {
        Personalizado1 = personalizado1;
    }

    public String getPersonalizado2() {
        return Personalizado2;
    }

    public void setPersonalizado2(String personalizado2) {
        Personalizado2 = personalizado2;
    }

    public String getPersonalizado3() {
        return Personalizado3;
    }

    public void setPersonalizado3(String personalizado3) {
        Personalizado3 = personalizado3;
    }

    public String getPersonalizado4() {
        return Personalizado4;
    }

    public void setPersonalizado4(String personalizado4) {
        Personalizado4 = personalizado4;
    }

    public String getPersonalizado5() {
        return Personalizado5;
    }

    public void setPersonalizado5(String personalizado5) {
        Personalizado5 = personalizado5;
    }

    public String getPersonalizado1_Valor() {
        return Personalizado1_Valor;
    }

    public void setPersonalizado1_Valor(String personalizado1_Valor) {
        Personalizado1_Valor = personalizado1_Valor;
    }

}


