package com.example.procesos2.Model.tab;

public class procesosTap {

    private String fecha;
    private long idReg;
    private int codigo;
    private int bloque;
    private int cama;
    private String finca;
    private int pr1;
    private int pr2;
    private int pr3;
    private String pr4;
    private int pr5;
    private int pr6;
    private int pr7;
    private int pr8;
    private String pr9;
    private String pr10;
    private int pr11;
    private int pr12;
    private int pr13;
    private int pr14;
    private  String terminal;

    //constructor vacio
    public procesosTap() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getBloque() {
        return bloque;
    }

    public void setBloque(int bloque) {
        this.bloque = bloque;
    }

    public int getCama() {
        return cama;
    }

    public void setCama(int cama) {
        this.cama = cama;
    }

    public String getFinca() {
        return finca;
    }

    public void setFinca(String finca) {
        this.finca = finca;
    }

    public int getPr1() {
        return pr1;
    }

    public void setPr1(int pr1) {
        this.pr1 = pr1;
    }

    public int getPr2() {
        return pr2;
    }

    public void setPr2(int pr2) {
        this.pr2 = pr2;
    }

    public int getPr3() {
        return pr3;
    }

    public void setPr3(int pr3) {
        this.pr3 = pr3;
    }

    public String getPr4() {
        return pr4;
    }

    public void setPr4(String pr4) {
        this.pr4 = pr4;
    }

    public int getPr5() {
        return pr5;
    }

    public void setPr5(int pr5) {
        this.pr5 = pr5;
    }

    public int getPr6() {
        return pr6;
    }

    public void setPr6(int pr6) {
        this.pr6 = pr6;
    }

    public int getPr7() {
        return pr7;
    }

    public void setPr7(int pr7) {
        this.pr7 = pr7;
    }

    public int getPr8() {
        return pr8;
    }

    public void setPr8(int pr8) {
        this.pr8 = pr8;
    }

    public String getPr9() {
        return pr9;
    }

    public void setPr9(String pr9) {
        this.pr9 = pr9;
    }

    public String getPr10() {
        return pr10;
    }

    public void setPr10(String pr10) {
        this.pr10 = pr10;
    }

    public int getPr11() {
        return pr11;
    }

    public void setPr11(int pr11) {
        this.pr11 = pr11;
    }

    public int getPr12() {
        return pr12;
    }

    public void setPr12(int pr12) {
        this.pr12 = pr12;
    }

    public int getPr13() {
        return pr13;
    }

    public void setPr13(int pr13) {
        this.pr13 = pr13;
    }

    public int getPr14() {
        return pr14;
    }

    public void setPr14(int pr14) {
        this.pr14 = pr14;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    @Override
    /*public String toString(){
        return "{" +
                "\"fecha\": \"" + fecha +"\",\n"+
                "\"codigo\":" + codigo + ",\n" +
                "\"bloque\":" + bloque + ",\n" +
                "\"cama\":" + cama + ",\n" +
                "\"finca\": \"" + finca +"\",\n"+
                "\"preg 1\":" + pr1 + ",\n" +
                "\"preg 2\":" + pr2 + ",\n" +
                "\"preg 3\":" + pr3 + ",\n" +
                "\"preg 4\": \"" + pr4 +"\",\n"+
                "\"preg 5\":" + pr5 + ",\n" +
                "\"preg 6\":" + pr6 + ",\n" +
                "\"preg 7\":" + pr7 + ",\n" +
                "\"preg 8\":" + pr8 + ",\n" +
                "\"preg 9\": \"" + pr9 +"\",\n"+
                "\"preg 10\": \"" + pr10 +"\",\n"+
                "\"preg 11\":" + pr11 + ",\n" +
                "\"preg 12\":" + pr12 + ",\n" +
                "\"preg 13\":" + pr13 + ",\n" +
                "\"preg 14\":" + pr14 + "\n" +
                "}";
    }*/

    public String toString(){
        return "{" +
                "\"fecha\": \""+fecha+"\",\n"+
                "\"codigo\":" + codigo + ",\n" +
                "\"finca\": \""+finca+"\",\n"+
                "\"bloque\":" + bloque + ",\n" +
                "\"cama\":" + cama + ",\n" +
                "\"pr1\":" + pr1 + ",\n" +
                "\"pr2\":" + pr2 + ",\n" +
                "\"pr3\":" + pr3 + ",\n" +
                "\"pr4\": \""+pr4+"\",\n"+
                "\"pr5\":" + pr5 + ",\n" +
                "\"pr6\":" + pr6 + ",\n" +
                "\"pr7\":" + pr7 + ",\n" +
                "\"pr8\":" + pr8 + ",\n" +
                "\"pr9\": \""+pr9+"\",\n"+
                "\"pr10\": \""+pr10+"\",\n"+
                "\"pr11\":" + pr11 + ",\n" +
                "\"pr12\":" + pr12 + ",\n" +
                "\"pr13\":" + pr13 + ",\n" +
                "\"pr14\":" + pr14 + ",\n"+
                "\"terminal\": \"" + terminal+"\"\n"+
                "}\n";
    }
}
