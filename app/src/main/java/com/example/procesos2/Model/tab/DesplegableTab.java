package com.example.procesos2.Model.tab;

public class DesplegableTab {
    private String cod;
    private String Options;

    //CONSTRUCTOR VACIO
    public DesplegableTab() {
    }

    //CONSTRUCTOR
    public DesplegableTab(String cod, String options) {
        this.cod = cod;
        Options = options;
    }

    //S&G

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getOptions() {
        return Options;
    }

    public void setOptions(String options) {
        Options = options;
    }

    @Override
    public String toString(){
        return "{\n" +
                "\"cod\":\""+ cod +"\",\n"+
                "\"Options\":\""+ Options +"\"\n"+
                "}";
    }
}
