package com.example.procesos2.Model;

import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Model.interfaz.temporal;
import com.example.procesos2.Model.tab.TemporalTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class iTemporal implements temporal {

    JsonAdmin ja;

    public String nombre;
    public String path;

    List<TemporalTab> tt = new ArrayList<>();

    public iTemporal(String path) throws Exception{
        getPath(path);
    }

    public void getPath(String path) {
        ja = new JsonAdmin();
        this.path = path;
    }


    @Override
    public boolean local(Long id_usuario) throws Exception {
        return false;
    }

    @Override
    public String insert(TemporalTab o) throws Exception {
        String msj = "";
        try{

            tt.add(o);
            local();
            msj = "se registro correctamente";

        }catch (Exception ex){
            msj = "Se genero una exception en las funcion insert de entidad temporal \n"+ex.toString();
        }
        return msj;
    }

    @Override
    public String delete(Long id) throws Exception {
        return null;
    }

    @Override
    public boolean local() throws Exception {
        String contenido = tt.toString();
        return ja.WriteJson(path,nombre,contenido);
    }

    @Override
    public List<TemporalTab> all() throws Exception {
        Gson gson = new Gson();
        tt = gson.fromJson(ja.ObtenerLista(path,nombre), new TypeToken<List<TemporalTab>>(){
        }.getType());

        return tt;
    }

    public String clear() throws Exception{
        tt.clear();
        local();
        return "ok limpio";
    }

    public String update(int item, TemporalTab o) throws Exception{
        String msj;
        try{
            o.setRespuesta(o.getRespuesta());
            tt.set(item-1,o);
            local();
            msj = "se actualizo correctamente";
        }catch (Exception ex){
            msj = "ocurrio un error al actualizar en update() \n"+ex.toString();
        }
        return msj;
    }
}
