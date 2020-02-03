package com.example.procesos2.Chead;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

public class CfilAuto {

    View controlview;

    public View autocompletado(Context c, Long id, String contenido, String desplegable){
        int i = 0;
        for(i = 0; i<=1; i++){
            ArrayList<consCfilAuto> lista = new ArrayList<>();
            lista.add(new consCfilAuto(c,id.intValue(),contenido));


        }

        return controlview;
    }

    class consCfilAuto{
        private Context context;
        private int id;
        private String pregunta;

        public consCfilAuto(Context context, int id, String pregunta) {
            this.context = context;
            this.id = id;
            this.pregunta = pregunta;
        }
    }

}
