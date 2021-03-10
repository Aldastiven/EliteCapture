package com.example.eliteCapture.Model.View;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class iHistorico {

    String path, nombre = "Historico";
    List<ContenedorTab> listHistorico = new ArrayList<>();
    List<ContenedorTab> listHistoricoNuevo = new ArrayList<>();
    iContador contador;

    public iHistorico(String path) {
        try {
            this.path = path;
            contador = new iContador(path);
            if (!new JsonAdmin().ExitsJson(path, nombre) || all() == null) {
                local();
            }
        }catch (Exception e){
            Log.i("ERROR_HISTORICO",e.toString());
        }
    }

    public String insert(ContenedorTab o) {
        o.setEstado(1);
        listHistorico = all();
        listHistorico.add(o);
        local();
        return "Ok";
    }

    public List<ContenedorTab> all() {
        try {
            return new Gson().fromJson(new JsonAdmin().ObtenerLista(path, nombre),
                                        new TypeToken<List<ContenedorTab>>() {}.getType());
        }catch (Exception e){
            Log.i("ERRORLIST",e.toString());
            return null;
        }
    }

    public boolean local() {
        return new JsonAdmin().WriteJson(path, nombre, new Gson().toJson(listHistorico));
    }

    public void limpiarXfecha(Context c){
        listHistorico.clear();
        local();
        Toast.makeText(c, "Limpieza realizada con exito", Toast.LENGTH_SHORT).show();
    }

    //LIMPIA REGISTRO SEGUN LA FECHA
    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public void limpiarXfecha(Context c){
        try {
            listHistorico = all();

            //aqui podemos modificar a cuantos dias dias dejar registros en el json
            Instant after= Instant.now().minus(Duration.ofDays(3));

            int d = 0;
            for (ContenedorTab tab : listHistorico) {

                String fechaJson = tab.getFecha().split(" ")[0];
                String fechaRetrocedida = after.toString().split("T")[0];

                int añoJson = Integer.parseInt(splitdate(fechaJson)[0]);
                int mesJson = Integer.parseInt(splitdate(fechaJson)[1]);
                int diaJson = Integer.parseInt(splitdate(fechaJson)[2]);

                int año15 = Integer.parseInt(splitdate(fechaRetrocedida)[0]);
                int mes15 = Integer.parseInt(splitdate(fechaRetrocedida)[1]);
                int dia15 = Integer.parseInt(splitdate(fechaRetrocedida)[2]);

                boolean validado = false;
                if(añoJson <= año15){
                    if(mesJson < mes15){
                        validado = getEstado(tab.getEstado());
                    }else if(mesJson == mes15){
                        if(diaJson <= dia15){
                            validado = getEstado(tab.getEstado());
                        }
                    }
                }
                if(!validado) {
                    listHistoricoNuevo.add(tab);
                }
            }
            listHistorico = listHistoricoNuevo != null ? listHistoricoNuevo : all();
            Toast.makeText(c, d >= 1 ? "Se limpiaron los registros" : "No se encontraron registros por limpiar", Toast.LENGTH_LONG).show();
            local();
        }catch (Exception ex){
            Log.i("GETFECHA",ex.toString());
        }
    }*/

    public String[] splitdate(String d){
        String[] i = d.split("-");
        return i;
    }

    public boolean getEstado(int estado){
        return estado == 1;
    }
}
