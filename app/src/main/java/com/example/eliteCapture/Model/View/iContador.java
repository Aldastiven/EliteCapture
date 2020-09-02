package com.example.eliteCapture.Model.View;

import android.util.Log;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.View.Interfaz.Contador;
import com.example.eliteCapture.Model.View.Tab.ContadorTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class iContador implements Contador {
    public static List<ContadorTab> ct = new ArrayList<>();
    String path = "";
    String nombre = "";

    public iContador(String path) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaNombre = sdf.format(new Date());
        this.nombre = "contado-" + fechaNombre;

        this.path = path;
        try {
            ct = all();
            for(ContadorTab cc : ct){
                if(!cc.getFecha().equals(fechaNombre)){
                   ct.clear();
                   break;
                }
            }
        } catch (Exception e) {
            Log.i("Error_onCreate", e.toString());
        }
    }

    @Override
    public String insert(ContadorTab o) throws Exception {
        o.setId((ct != null) ? ct.size() : 0);
        ct.add(o);
        local();
        return "Ok";
    }

    @Override
    public String delete(Long id) throws Exception {
        ct.remove(id);
        local();
        return "Ok";
    }

    public String update(int usuario, int proceso) throws Exception {
        boolean edito = false;

        try {
            ContadorTab c = contUsuario(usuario);
            for (ContadorTab.procesoTab p : c.getProcesos()) {
                if (p.getProceso() == proceso) {
                    p.setCantidad(p.getCantidad() + 1);
                    edito = true;
                }
            }
            if (!edito) {
                c.addProcesos(proceso);
            }

            ct.set(c.getId(), c);
            local();

        } catch (Exception e) {

            ContadorTab cont = new ContadorTab(usuario);
            cont.addProcesos(proceso);

            Log.i("Enviar_error", "Update o_o " + json(cont));

            insert(cont);
        }
        return "Ok";
    }

    public int getCantidad(int usuario, int proceso) {
        try {
            ContadorTab c = contUsuario(usuario);
            for (ContadorTab.procesoTab p : c.getProcesos()) {
                if (p.getProceso() == proceso) {
                    return p.getCantidad();
                }
            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    public ContadorTab contUsuario(int usuario) {
        for (ContadorTab c : ct) {
            if (c.getUsuario() == usuario) {
                return c;
            }
        }
        return null;
    }

    @Override
    public boolean local() throws Exception {
        Log.i("Enviar_local", "Ingreso");
        return new JsonAdmin()
                .WriteJson(
                        path,
                        nombre,
                        new Gson().toJson(ct));
    }

    @Override
    public List<ContadorTab> all() throws Exception {
        return new Gson()
                .fromJson(
                        new JsonAdmin().ObtenerLista(path, nombre),
                        new TypeToken<List<ContadorTab>>() {
                        }.getType());
    }

    @Override
    public String json(ContadorTab o) throws Exception {
        return new Gson().toJson(o);
    }
}
