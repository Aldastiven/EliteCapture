package com.example.eliteCapture.Model.View;


import android.util.Log;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Config.sqlConect;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.Tab.DetalleTab;
import com.example.eliteCapture.Model.View.Interfaz.Contenedor;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class iContenedor implements Contenedor {
    private String nombre = "pendientes_envio", path;
    Calendar calendar;
    iContador contador;
    iHistorico ih;

    public static List<ContenedorTab> ct = new ArrayList<>();
    public static List<ContenedorTab> newct = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public iContenedor(String path) {
        this.path = path;
        try {
            if (!exist()){local();}
            if (all() == null){local();}
            calendar = Calendar.getInstance();
            contador = new iContador(path);
            ih = new iHistorico(path);
        } catch (Exception e) {
            Log.i("Error_iContenedor", e.toString());
        }
    }

    @Override
    public String insert(ContenedorTab o) {
        all();
        o.setFecha(o.fechaString());
        ct.add(o);
        local();

        return "Ok";
    }

    @Override
    public String delete(Long id) {
        //ct.remove(id);
        //local();
        return "Ok";
    }

    @Override
    public String update(Long id, ContenedorTab o) throws Exception {
        ct.set(id.intValue(), o);
        local();
        return "Ok";
    }

    @Override
    public boolean local() {
        return new JsonAdmin()
                .WriteJson(
                        path,
                        nombre,
                        new Gson().toJson(ct));
    }

    @Override
    public List<ContenedorTab> all() {
        try {
            return ct = new Gson()
                    .fromJson(
                            new JsonAdmin().ObtenerLista(path, nombre),
                            new TypeToken<List<ContenedorTab>>() {
                            }.getType());
        }catch (Exception e){
            Log.i("ERRORLIST",e.toString());
            return null;
        }
    }

    @Override
    public String json(ContenedorTab o) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public float calcular(ContenedorTab c, boolean footer) {
        float ponderado = 0;
        float calificacion = 0;

        for (RespuestasTab respuesta : c.getQuestions()) {
            if (respuesta.getValor() != null && !respuesta.getValor().equals("-1")) {
                ponderado += respuesta.getPonderado();
                calificacion += Float.parseFloat(respuesta.getValor());
                Log.i("calificar", respuesta.getId() + ") resp:" + respuesta.getValor() + " pon: " + respuesta.getPonderado() + " cal: " + calificacion + " ponTotal: " + ponderado);
            }
        }

        if (footer) {
            for (RespuestasTab respuesta : c.getFooter()) {
                if (!respuesta.getValor().equals("-1")) {
                    ponderado += respuesta.getPonderado();
                    calificacion += Float.parseFloat(respuesta.getValor());
                }
            }
        }

        Log.i("calificar", "" + calificacion + " / " + ponderado + " = " + calificacion / ponderado * 100);

        DecimalFormatSymbols separador = new DecimalFormatSymbols();
        separador.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#.##", separador);

        return Float.parseFloat(format.format(calificacion / ponderado * 100));
    }

    public boolean crearTemporal(ContenedorTab formTemporal) throws Exception {
        try {
            return new JsonAdmin().WriteJson(
                    path,
                    "temp",
                    new Gson().toJson(formTemporal));
        } catch (Exception e) {
            Log.i("ProContenedor:", "Temporal Error" + e);
            return false;
        }
    }

    public ContenedorTab generarContenedor(int usuario, String terminal, List<DetalleTab> formulario)  {
        List<RespuestasTab> header = new ArrayList<>();
        List<RespuestasTab> questions = new ArrayList<>();
        List<RespuestasTab> footers = new ArrayList<>();

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
                contador.getCantidad(usuario, formulario.get(0).getId_proceso()),
                header,
                questions,
                footers,
                usuario,
                terminal
        );
    }

    public ContenedorTab optenerTemporal() {
        try {
            return new Gson().fromJson(
                    new JsonAdmin().ObtenerLista(path, "temp"),
                    new TypeToken<ContenedorTab>() {
                    }.getType());
        } catch (Exception e) {
            Log.i("Enviar_error_1", e.toString());
            return null;
        }
    }

    public void editarTemporal(String donde, int idPregunta, String respuesta, String valor, String causa, int regla){
        try {
            ContenedorTab conTemp = new Gson().fromJson(new JsonAdmin().ObtenerLista(path, "temp"),
                    new TypeToken<ContenedorTab>() {
                    }.getType());

            switch (donde) {
                case "H":
                    conTemp.setHeader(editar(conTemp.getHeader(), idPregunta, respuesta, valor, causa, regla));
                    break;
                case "Q":
                    conTemp.setQuestions(editar(conTemp.getQuestions(), idPregunta, respuesta, valor, causa, regla));
                    break;
                case "F":
                    conTemp.setFooter(editar(conTemp.getFooter(), idPregunta, respuesta, valor, causa, regla));
                    break;
            }
            crearTemporal(conTemp);
        }catch (Exception e){
            Log.i("ERROR","No se pudo registrar el temporal : "+e.toString());
        }
    }

    public List<RespuestasTab> editar(List<RespuestasTab> editar, int idPregunta, String respuesta, String valor, String causa, int regla) {
        editar.get(idPregunta).setRespuesta(respuesta);
        editar.get(idPregunta).setValor(valor);
        editar.get(idPregunta).setCausa(causa);
        editar.get(idPregunta).setReglas(regla);
        return editar;
    }

    public RespuestasTab convertirDetallaRespuesta(Long id, DetalleTab detalle) {
        return new RespuestasTab(
                id,
                detalle.getCodigo_detalle(),
                detalle.getId_proceso(),
                detalle.getId_detalle(),
                detalle.getTipo(),
                detalle.getNombre_detalle(),
                detalle.getPorcentaje(),
                null,
                null,
                null,
                detalle.getLista_desp(),
                detalle.getReglas(),
                detalle.getTip(),
                detalle.getDesdeHasta(),
                detalle.getDecimales(),
                detalle.getObligatorio()
        );
    }

    public Map<Integer, List<Long>> validarVacios(ContenedorTab c, boolean footer) {
        Map<Integer, List<Long>> retorno = new HashMap<Integer, List<Long>>();
        retorno.put(0, vacios(c.getHeader()));
        retorno.put(1, vacios(c.getQuestions()));
        if (footer) {
            retorno.put(2, vacios(c.getFooter()));
        }

        return retorno;
    }

    public List<Long> vacios(List<RespuestasTab> lista) {
        List<Long> v = new ArrayList<>();
        for (RespuestasTab respuesta : lista) {
            if (respuesta.getObligatorio() == 1 && respuesta.getRespuesta() == null) {
                v.add(respuesta.getId());
            }
        }
        Log.i("vacios", v.toString());
        return v;
    }

    public String json(DesplegableTab o){
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public boolean enviar()throws Exception{
        PreparedStatement ps = new Conexion().getConexion().prepareCall("EXEC recibir_json_ec ?");
        ps.setString(1, new JsonAdmin().ObtenerLista(path, nombre));
        ps.execute();

        ct.clear();
        local();
        return true;
    }



    public boolean enviarInmediato2(ContenedorTab c, int consecutivo){
        try {
            boolean env = false;
            PreparedStatement ps = new Conexion().getConexion().prepareCall("EXEC recibir_json_ec ?");

            Log.i("envioData", "convert : "+new Gson().toJson(c));

            c.setEstado(1);
            ps.setString(1, new Gson().toJson(c));
            ps.execute();

            return true;
        }catch (Exception e){
            Log.i("Error en el envio",""+e.toString());
            return false;
        }
    }

    public void bloque(PreparedStatement ps, ContenedorTab c, List<RespuestasTab> rtas){
        try {
            for (RespuestasTab r : rtas) {
                ps.setString(1, c.getFecha());
                ps.setInt(2, c.getIdProceso());
                ps.setInt(3, r.getCodigo());
                ps.setLong(4, r.getIdPregunta());
                ps.setString(5, r.getRespuesta());
                ps.setString(6, r.getValor());
                ps.setFloat(7, r.getPonderado());
                ps.setString(8, c.getTerminal());
                ps.setInt(9, c.getIdUsuario());
                ps.setInt(10, c.getConsecutivo());
                ps.setString(11, String.valueOf(r.getReglas()));
                ps.addBatch();

                Log.i("envioData", "fecha : "+c.getFecha());
            }
        }catch (Exception e){
            Log.i("Envio", e.toString());
        }
    }

    protected class Conexion extends sqlConect {
    }

    public boolean exist(){
        try {
            return new JsonAdmin().ExitsJson(path, nombre);
        }catch (Exception e){
            return false;
        }
    }

    public int pendientesCantidad(){
        try {
            int size = 0;
            for (ContenedorTab c : all()) {
                if (c.getEstado() < 1) {
                    size++;
                }
            }
            return size;
        }catch (Exception e){
            return 0;
        }
    }

}
