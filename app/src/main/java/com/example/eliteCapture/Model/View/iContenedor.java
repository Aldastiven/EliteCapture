package com.example.eliteCapture.Model.View;

import android.util.ArrayMap;
import android.util.Log;

import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Interfaz.Contenedor;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.Tab.DetalleTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class iContenedor implements Contenedor {
    public static List<ContenedorTab> ct = new ArrayList<>();
    String path = "";

    public iContenedor(String path) {
        this.path = path;
    }

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

    @Override
    public String json(ContenedorTab o) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public float calcular(ContenedorTab c) {
        float ponderado = 0;
        float calificacion = 0;

        for (RespuestasTab respuesta : c.getQuestions()) {
            if (respuesta.getRespuesta() != null && !respuesta.getRespuesta().equals("-1")) {
                ponderado += respuesta.getPonderado();
                calificacion += Float.parseFloat(respuesta.getRespuesta());
            }
        }

        if (c.getFooter().size() > 0) {
            for (RespuestasTab respuesta : c.getFooter()) {
                if (!respuesta.getRespuesta().equals("-1")) {
                    ponderado += respuesta.getPonderado();
                    calificacion += Float.parseFloat(respuesta.getRespuesta());
                }
            }
        }

        return calificacion / ponderado;
    }

    public ContenedorTab generarContenedor(int usuario, List<DetalleTab> formulario) throws Exception {
        List<RespuestasTab> header = new ArrayList<>();
        List<RespuestasTab> questions = new ArrayList<>();
        List<RespuestasTab> footers = new ArrayList<>();

        Log.i("Error_onCreate", "Entro a la generar");
        for (DetalleTab detalle : formulario) {
            switch (detalle.getTipo_M()) {
                case "H":
                    header.add(convertirDetallaRespuesta((long) header.size(), detalle));
                    break;
                case "Q":
                    questions.add(convertirDetallaRespuesta((long) questions.size(), detalle));
                    break;
                case "F":
                    footers.add(convertirDetallaRespuesta((long) footers.size(), detalle));
                    break;
                default:
                    Log.i("ERROR:", "El tipo de detalle no esta limitado (H,Q,F)");
                    break;
            }
        }

        return new ContenedorTab(
                formulario.get(0).getId_proceso(),
                header,
                questions,
                footers,
                usuario
        );
    }


    public boolean temporal(ContenedorTab formTemporal) throws Exception {
        String nombre = "temp" + formTemporal.getIdProceso();
        return false;
    }

    public RespuestasTab convertirDetallaRespuesta(Long id, DetalleTab detalle) throws Exception {

        Log.i("Error_onCreate", "a convertir " + detalle.getLista_desp());
        return new RespuestasTab(
                id,
                detalle.getId_proceso(),
                detalle.getId_detalle(),
                detalle.getTipo(),
                detalle.getNombre_detalle(),
                detalle.getPorcentaje(),
                null,
                (detalle.getLista_desp() != null && !detalle.getLista_desp().isEmpty()) ? opciones(detalle.getLista_desp()) : null
        );
    }

    public List<DesplegableTab> opciones(String desplegable) throws Exception {

        iDesplegable desp = new iDesplegable(null, path);

        Log.i("Error_onCreate", "generando opciones " + desplegable);
        desp.nombre = desplegable;
        desp.all();

        return desp.all();
    }

    public ArrayMap<Integer, List<Long>> validarVacios(ContenedorTab c) {
        ArrayMap<Integer, List<Long>> retorno = new ArrayMap<>();
        retorno.put(1, vacios(c.getHeader()));
        retorno.put(2, vacios(c.getQuestions()));
        retorno.put(3, vacios(c.getFooter()));

        return retorno;
    }

    public List<Long> vacios(List<RespuestasTab> lista) {
        List<Long> v = new ArrayList<>();

        for (RespuestasTab respuesta : lista) {
            if (respuesta.getRespuesta() == null) {
                v.add(respuesta.getId());
            }
        }
        return v;
    }
}
