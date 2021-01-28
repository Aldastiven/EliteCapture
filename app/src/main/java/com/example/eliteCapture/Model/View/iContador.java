package com.example.eliteCapture.Model.View;

import android.util.Log;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.View.Interfaz.Contador;
import com.example.eliteCapture.Model.View.Tab.ContadorTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class iContador implements Contador {
    List<ContadorTab> ct;
    String path;
    String nombre;

    public iContador(String path) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.nombre = "contado-" + sdf.format(new Date());
        this.path = path;
        ct = new ArrayList<>();

        if (!exist()){local();}
        if (all() == null){local();}
    }

    @Override
    public String insert(ContadorTab o){
        o.setId((ct != null) ? ct.size() : 0);
        ct.add(o);
        local();
        return "Ok";
    }

    @Override
    public String delete(Long id) {
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
            int cantidad = 0;
            ContadorTab c = contUsuario(usuario);
            for (ContadorTab.procesoTab p : c.getProcesos()) {
                if (p.getProceso() == proceso) {
                   cantidad = p.getCantidad();
                   break;
                }
            }
            return cantidad;
        } catch (Exception e) {
            return 0;
        }
    }

    public ContadorTab contUsuario(int usuario) {
        ContadorTab cc = null;
        all();
        for (ContadorTab c : ct) {
            if (c.getUsuario() == usuario) {
                cc = c;
                break;
            }
        }
        return cc;
    }

    @Override
    public boolean local(){
        return new JsonAdmin()
                .WriteJson(
                        path,
                        nombre,
                        new Gson().toJson(ct));
    }

    @Override
    public List<ContadorTab> all() {
        ct = new Gson()
                .fromJson(
                        new JsonAdmin().ObtenerLista(path, nombre),
                        new TypeToken<List<ContadorTab>>() {
                        }.getType());
        return ct;
    }

    @Override
    public String json(ContadorTab o) {
        return new Gson().toJson(o);
    }

    public boolean exist(){
        try {
            return new JsonAdmin().ExitsJson(path, nombre);
        }catch (Exception e){
            return false;
        }
    }
}
