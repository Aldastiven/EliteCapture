package com.example.eliteCapture.Model.View;


import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Config.sqlConect;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Interfaz.Contenedor;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.Tab.DetalleTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class iContenedor implements Contenedor {
    private String nombre = "pendientes_envio", path;
    Calendar calendar;
    iContador contador = null;


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
            calendar = Calendar.getInstance();
            contador = new iContador(path);
        } catch (Exception e) {
            Log.i("Error_onCreate", e.toString());
        }
    }

    @Override
    public String insert(ContenedorTab o) throws Exception {
        all();
        o.setConsecutivo( contador.getCantidad(o.getIdUsuario(), o.getIdProceso()) );
        o.setFecha(o.fechaString());
        ct.add(o);
        local();

        return "Ok";
    }

    @Override
    public String delete(Long id) throws Exception {
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
        Log.i("Enviar_local", "Ingreso");
        return new JsonAdmin()
                .WriteJson(
                        path,
                        nombre,
                        new Gson().toJson(ct));
    }

    @Override
    public List<ContenedorTab> all() {
        try {
            Log.i("ALLConsulta", "llego a consultar");
            return ct = new Gson()
                    .fromJson(
                            new JsonAdmin().ObtenerLista(path, nombre),
                            new TypeToken<List<ContenedorTab>>() {
                            }.getType());
        }catch (Exception e){
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

    public ContenedorTab generarContenedor(int usuario, String terminal, List<DetalleTab> formulario) throws Exception {
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
                usuario,
                terminal
        );
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

    public ContenedorTab optenerTemporal() {
        try {
            return new Gson().fromJson(
                    new JsonAdmin().ObtenerLista(path, "temp"),
                    new TypeToken<ContenedorTab>() {
                    }.getType());
        } catch (Exception e) {
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
                    Log.i("reg_", "llego aqui encab");
                    conTemp.setHeader(editar(conTemp.getHeader(), idPregunta, respuesta, valor, causa, regla));
                    break;
                case "Q":
                    Log.i("reg_", "llego aqui");
                    conTemp.setQuestions(editar(conTemp.getQuestions(), idPregunta, respuesta, valor, causa, regla));
                    break;
                case "F":
                    conTemp.setFooter(editar(conTemp.getFooter(), idPregunta, respuesta, valor, causa, regla));
                    break;
            }
            Log.i("vcampo", "" + crearTemporal(conTemp));
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

    public RespuestasTab convertirDetallaRespuesta(Long id, DetalleTab detalle) throws Exception {

        Log.i("Error_onCreate", "a convertir " + detalle.getLista_desp());
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
                detalle.getTip()
        );
    }

    public List<DesplegableTab> opciones(String desplegable) throws Exception {

        iDesplegable desp = new iDesplegable(null, path);

        Log.i("Error_onCreate", "generando opciones " + desplegable);
        desp.nombre = desplegable;

        return desp.all();
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
            if (respuesta.getRespuesta() == null) {
                v.add(respuesta.getId());
            }
        }
        return v;
    }

    public String json(DesplegableTab o) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public boolean enviar() throws Exception {

        Log.i("Enviar_env", "Ingreso.");
        Connection cn = new Conexion().getConexion();

        if (cn != null) {
            String ins = "" +
                    "     INSERT INTO [dbo].[datos_procesos_detalle]\n" +
                    "           ([fecha]\n" +
                    "           ,[id_procesos]\n" +
                    "           ,[id_codigo]\n" +
                    "           ,[id_procesos_detalle]\n" +
                    "           ,[rUsu_resp_d]\n" +
                    "           ,[valor_resp_d]\n" +
                    "           ,[porc_resp_d]\n" +
                    "           ,[id_terminal]\n" +
                    "           ,[id_usuario]\n" +
                    "           ,[consec_json])\n" +
                    "     VALUES\n" +
                    "           (?,?,?,?,?,?,?,?,?,?);";


            int load = 0;

            PreparedStatement ps = cn.prepareStatement(ins);
            for (ContenedorTab c : ct) {
                if (c.getEstado() == 0) {
                    bloque(ps, c, c.getHeader());
                    bloque(ps, c, c.getQuestions());
                    bloque(ps, c, c.getFooter());
                    ps.executeBatch();
                    c.setEstado(1);
                    update((long) c.getConsecutivo(), c);
                    load++;
                }
            }
            Log.i("Enviar_env", "Registros actualizados: " + load);
            return true;
        } else {
            return false;
        }

    }

    public void bloque(PreparedStatement ps, ContenedorTab c, List<RespuestasTab> rtas) throws SQLException {
        for (RespuestasTab r : rtas) {
            ps.setString(1, c.getFecha());
            ps.setInt(2, c.getIdProceso());
            ps.setInt(3, r.getCodigo());
            ps.setLong(4, r.getIdPregunta());
            ps.setString(5, r.getRespuesta());
            ps.setString(6, (r.getCausa()!=null ? r.getCausa() : r.getValor()));
            ps.setFloat(7, r.getPonderado());
            ps.setString(8, c.getTerminal());
            ps.setInt(9, c.getIdUsuario());
            ps.setInt(10, c.getConsecutivo());
            ps.addBatch();
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
                Log.i("ESTADO", c.getEstado() + "");
                if (c.getEstado() < 1) {
                    size++;
                }
            }
            return size;
        }catch (Exception e){
            return 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void limpiarXfecha(){
        try {
            ct = all();
            int conteo = 0;
            newct.clear();
            for (ContenedorTab tab : ct) {

                Instant after= Instant.now().minus(Duration.ofDays(15));

                String fechaJson = tab.getFecha().split(" ")[0];
                String fechaRetrocedida = after.toString().split("T")[0];

                int añoJson = Integer.parseInt(splitdate(fechaJson)[0]);
                int mesJson = Integer.parseInt(splitdate(fechaJson)[1]);
                int diaJson = Integer.parseInt(splitdate(fechaJson)[2]);

                int año15 = Integer.parseInt(splitdate(fechaRetrocedida)[0]);
                int mes15 = Integer.parseInt(splitdate(fechaRetrocedida)[1]);
                int dia15 = Integer.parseInt(splitdate(fechaRetrocedida)[2]);

                Log.i("GETFECHA", "=============VAIDACION "+(conteo++)+" ===============");
                Log.i("GETFECHA", "fecha obtenida : " + fechaJson + " fecha parceada : " + fechaRetrocedida);
                boolean validado = false;
                if(añoJson <= año15){
                    if(mesJson < mes15){
                        validado = getEstado(tab.getEstado());
                    }else if(mesJson == mes15){
                        if(diaJson <= dia15){
                            validado = getEstado(tab.getEstado());
                        }
                    }
                    Log.i("GETFECHA", "validando mes : json --> " + mesJson +" VS hace 15 dias --> "+mes15);
                    Log.i("GETFECHA", "validando dia : json --> " + diaJson +" VS hace 15 dias --> "+dia15);
                    Log.i("GETFECHA", "validando año : json --> " + añoJson +" VS hace 15 dias --> "+año15);
                    Log.i("GETFECHA", "Estado json : " +tab.getEstado());
                }
                Log.i("GETFECHA", "validacion : " + validado);
                if(!validado) newct.add(tab);
                Log.i("GETFECHA", "=====================================");
            }
            ct.clear();
            ct = newct;
            Log.i("GETFECHA", "crea json "+local());
        }catch (Exception ex){
            Log.i("GETFECHA",ex.toString());
        }
    }

    public String[] splitdate(String d){
        String[] i = d.split("-");
        return i;
    }

    public boolean getEstado(int estado){
        return estado == 1;
    }
}
