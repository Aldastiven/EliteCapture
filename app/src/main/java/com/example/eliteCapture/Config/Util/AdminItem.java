package com.example.eliteCapture.Config.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Interfaz.Contenedor;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;

import java.util.ArrayList;
import java.util.List;

public class AdminItem {

    Context context;
    String path;

    ArrayList<String> items = new ArrayList<>();
    iDesplegable iDesp;
    iContenedor iCont;

    public AdminItem(Context context, String path){
        try {
            this.context = context;
            this.path = path;
            iDesp = new iDesplegable(null, path);
            iCont = new iContenedor(path);
        }catch (Exception e){
            Toast.makeText(context, "ERROR EN ADMIN ITEM : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<String> soloOpciones(RespuestasTab rt) {
        try {
            iDesp.nombre = rt.getDesplegable();
            items.add("Selecciona");

            String referencia = "sin Refencia";
            int estado = 0;


            if(iCont.all() != null) {

                for (ContenedorTab ct : iCont.all())
                    for (RespuestasTab question : ct.getQuestions()) {
                        if (question.getCodigo() == rt.getReglas()) {
                                referencia = question.getRespuesta();
                                estado = 1;
                                break;
                        }
                    }
            }

            Log.i("estados",estado+"");

                switch (estado) {
                    case 0:
                        for (DesplegableTab des : iDesp.all())
                        items.add(des.getOpcion());
                        break;
                    case 1:
                        for (DesplegableTab des : iDesp.all())
                        if (des.getCondicional() != null && des.getCondicional().equals(referencia)) {
                            items.add(des.getOpcion());
                        }
                        break;
                }
            return items;
        } catch (Exception ex) {
            Log.i("ITEMS",""+ex.toString());
            items.add("No encontro datos");
            return items;
        }
    }


}
