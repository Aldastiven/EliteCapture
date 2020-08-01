package com.example.eliteCapture.Model.Data;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Interfaz.Desplegable;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class iDesplegable implements Desplegable {

  public List<DesplegableTab> DT = new ArrayList<>();
  Connection cn;
  String path = null;
  JsonAdmin ja = null;

  public String nombre = "";

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public iDesplegable(Connection cn, String path) {
    this.cn = cn;
    getPath(path);
  }

  public void getPath(String path) {
    ja = new JsonAdmin();
    this.path = path;
  }

  @Override
  public List<DesplegableTab> forDesplegable(Long cod, String option) throws Exception {
    return null;
  }

  @Override
  public boolean local(Long cod) throws Exception {
    return false;
  }

  @Override
  public String insert(DesplegableTab o) throws Exception {
    return null;
  }


  @Override
  public String delete(Long id) throws Exception {
    return null;
  }


  @Override
  public boolean local() throws Exception {
    ResultSet rs;
    PreparedStatement ps = cn.prepareStatement("SELECT [Filtro]\n" +
        ",[Codigo]\n" +
        ",[Opcion]\n" +
        ",[condicional]"+
        "FROM [dbo].[Desplegables] \n" +
        "ORDER BY Codigo");
    rs = ps.executeQuery();

    while (rs.next()) {
      DT.add(gift(rs));
    }

    String contenido = new Gson().toJson(DT);

    return ja.WriteJson(path, nombre, contenido);
  }

  @Override
  public List<String> group() {
    List<String> nombresdesp = new ArrayList<>();
    try {
      ResultSet rs;
      PreparedStatement ps = cn.prepareStatement("SELECT [Filtro]\n" +
          "FROM [dbo].[Desplegables]\n" +
          "Group by [Filtro]");
      rs = ps.executeQuery();

      while (rs.next()) {
        nombresdesp.add(rs.getString("Filtro"));
      }
      return nombresdesp;
    } catch (Exception EX) {
      return null;
    }
  }

  @Override
  public boolean traerDesplegable(String nombreD) {

    DT.clear();
    try {
      ResultSet rs;
      PreparedStatement ps = cn.prepareStatement("SELECT id_Desplegable, Filtro, Codigo, Opcion, condicional\n" +
          "FROM  Desplegables\n" +
          "WHERE [Filtro]='" + nombreD + "' ORDER BY Codigo");
      rs = ps.executeQuery();

      while (rs.next()) {
        DT.add(gift(rs));
      }

      return ja.WriteJson(path, nombre, new Gson().toJson(DT));
    } catch (Exception EX) {
      Log.i("CONSULTA",""+EX.toString());
      return false;
    }
  }

  @Override
  public List<DesplegableTab> all()  throws Exception{
    Gson gson = new Gson();
    DT = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<DesplegableTab>>() {
    }.getType());
    return DT;
  }

  @Override
  public String json(DesplegableTab o) throws Exception {
    Gson gson = new Gson();
    return gson.toJson(o);
  }

  public DesplegableTab gift(ResultSet rs) throws Exception {
    Log.i("CONSULTA","Filtro : "+rs.getString("Filtro")+" condi : "+rs.getString("condicional"));
    return new DesplegableTab(
        rs.getString("Filtro"),
        rs.getString("Codigo"),
        rs.getString("Opcion"),
        rs.getString("condicional")
    );
  }

}
