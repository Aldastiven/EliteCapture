package com.example.procesos2.Model;

import com.example.procesos2.Model.interfaz.Contenedor;
import com.example.procesos2.Model.tab.ContenedorTab;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.RespuestasTab;

import java.util.ArrayList;
import java.util.List;



class iContenedor implements Contenedor {
    List<ContenedorTab> ct = new ArrayList<>();

    @Override
    public String insert(ContenedorTab o) throws Exception {
        ct.add(o);
        return "Ok";
    }

    @Override
    public String delete(Long id) throws Exception {
        ct.remove(id);
        return "Ok";
    }

    @Override
    public String update(Long id, ContenedorTab o) throws Exception {
        ct.set(id.intValue(), o);
        return "Ok";
    }

    @Override
    public boolean local() throws Exception {
        return false;
    }

    @Override
    public List<ContenedorTab> all() throws Exception {
        return null;
    }

    public
}
