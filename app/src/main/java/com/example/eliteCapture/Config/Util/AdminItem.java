package com.example.eliteCapture.Config.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;

import java.util.ArrayList;
import java.util.List;

public class AdminItem {

    Context context;
    String path;

    ArrayList<String> items = new ArrayList<>();
    iDesplegable iDesp;

    public AdminItem(Context context, String path){
        try {
            this.context = context;
            this.path = path;
            iDesp = new iDesplegable(null, path);
        }catch (Exception e){
            Toast.makeText(context, "ERROR EN ADMIN ITEM : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<String> soloOpciones(String opcion) {
        try {
            iDesp.nombre = opcion;
            items.add("Selecciona");
            for (DesplegableTab des : iDesp.all() )
                if(des.getCondicional() != null && des.getCondicional().equals("3")) {
                    items.add(des.getOpcion());
                }
            return items;
        } catch (Exception ex) {
            Log.i("ITEMS",""+ex.toString());
            items.add("No encontro datos");
            return items;
        }
    }


}
