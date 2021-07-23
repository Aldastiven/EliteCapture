package com.example.eliteCapture.Model.Data;

import android.util.Log;
import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Tab.jsonPlanTab;
import com.example.eliteCapture.Model.Data.Tab.listFincasTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class iJsonPlan {

    String path, nombre = "planJSON", nameFincas = "listFincas";
    int usuario;

    Connection cn;
    JsonAdmin ja;
    listFincasTab.fincasTab farm;

    public iJsonPlan(String path, int usuario, Connection cn) {
        this.path = path;
        this.usuario = usuario;
        this.cn = cn;
        ja = new JsonAdmin();
    }

    public iJsonPlan(String path, int usuario, Connection cn, listFincasTab.fincasTab... farm) {
        this.path = path;
        this.farm = farm[0];
        this.cn = cn;
        this.usuario = usuario;
        ja = new JsonAdmin();
    }

    public iJsonPlan(String path) {
        this.path = path;
        ja = new JsonAdmin();
    }

    public List<jsonPlanTab> all(){
        return new Gson().fromJson(
                ja.ObtenerLista(path, nombre),
                new TypeToken<List<jsonPlanTab>>() {
        }.getType());
    }

    public boolean local(int usuario) throws Exception{
        ResultSet rs;
        String q = "SELECT * FROM loginFinca WHERE codigo = "+usuario;

        Log.i("consultaFincasQuery", q);

        PreparedStatement ps = cn.prepareStatement(q);
        rs = ps.executeQuery();

        List<listFincasTab> listFincas = new ArrayList<>();
        List<listFincasTab.fincasTab> fincas = new ArrayList<>();

        while (rs.next()) {
            Log.i("consultaFincas", "encontron : "+rs.getString(2));
            fincas.add(new listFincasTab.fincasTab(
                    Integer.parseInt(rs.getString(2)),
                    rs.getString(3)
            ));
        }

        listFincas.add(new listFincasTab(
                usuario,
                fincas
        ));
        return ja.WriteJson(path, nameFincas, new Gson().toJson(listFincas));
    }

    public List<listFincasTab> allListFincas(){
        return new Gson().fromJson(
                ja.ObtenerLista(path, nameFincas),
                new TypeToken<List<listFincasTab>>() {
                }.getType());
    }

    public boolean localListFincas(int idFinca) throws Exception{
        ResultSet rs;
        String q = "SELECT  * FROM Plano_json_Bloque WHERE codigo = "+idFinca;

        Log.i("descaga", q);

        PreparedStatement ps = cn.prepareStatement(q);
        rs = ps.executeQuery();

        String data = "";
        while (rs.next()) {
            data = rs.getString(2) + data;
            Log.i("descaga", data);
        }
        return ja.WriteJson(path, "Finca_"+idFinca, data);
    }

    public Date getDateUpdate(int idFinca, Connection cn) throws Exception{
        ResultSet rs;
        String q = "SELECT  * FROM Plano_json_Bloque WHERE codigo = "+idFinca;
        Log.i("nextFinca", "idFinca : "+q);
        PreparedStatement ps = cn.prepareStatement(q);
        rs = ps.executeQuery();

        Date fecha = null;
        while (rs.next()) {
            fecha = rs.getDate(3);
            Log.i("nextFinca", "ultima fecha de modificacion : "+fecha);
        }

        return fecha;
    }

    public String modify(int idFinca){
        String pathFarm = path +"listFarms/"+idFinca+"/Finca_"+idFinca + ".json";
        return  new SimpleDateFormat("yyyy-MM-dd").format(new File(pathFarm).getAbsoluteFile().lastModified());
    }

    public boolean validateDateFile(int idFinca, Connection cn) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaParseada = sdf.parse(modify(idFinca));

        return fechaParseada.before(getDateUpdate(idFinca, cn));
    }
}
