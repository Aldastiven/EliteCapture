package com.example.eliteCapture.Model.Data;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Interfaz.Detalle;
import com.example.eliteCapture.Model.Data.Tab.DetalleTab;
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

public class iDetalle implements Detalle {

	public List<DetalleTab> D1 = new ArrayList<>();
	Connection cn;
	String path = null;
	JsonAdmin ja = null;

	public String nombre = "Detalle";

	public String all = "SELECT [id_detalle]\n" +
			"      ,[id_proceso]\n" +
			"      ,[codigo_detalle]\n" +
			"      ,[nombre_detalle]\n" +
			"      ,[tipo]\n" +
			"      ,[lista_desp]\n" +
			"      ,[tipo_M]\n" +
			"      ,[porcentaje]\n" +
			"      ,[capitulo]\n" +
			"      ,[item]\n" +
			"      ,[Capitulo_Nombre]\n" +
			"      ,[grupo1]\n" +
			"      ,[reglas]\n" +
			"      ,[tip]\n" +
			"      ,[desde_hasta]\n" +
			"      ,[decimales]" +
			"      ,[obligatorio]" +
			"  FROM [Formularios].[dbo].[Procesos_Detalle]\n" +
			"  ORDER BY [id_proceso], [codigo_detalle]";

	public iDetalle(Connection cn, String path) {
		this.cn = cn;
		getPath(path);
	}

	public void getPath(String path) {
		ja = new JsonAdmin();
		this.path = path;
	}

	@Override
	public List<DetalleTab> forDetalle(long id_proceso) throws Exception {
		List<DetalleTab> detalles = new ArrayList<>();
		for (DetalleTab det : all()) {
			if (det.getId_proceso() == id_proceso) {
				detalles.add(det);
			}
		}
		return detalles;
	}


	@Override
	public String insert(DetalleTab o) throws Exception {
		return null;
	}


	@Override
	public String delete(Long id) throws Exception {
		return null;
	}

	@Override
	public boolean local() throws Exception {
		return false;
	}


	public boolean localDesp(int idFinca) throws Exception {
		ResultSet rs;
		String q = "SELECT  * FROM Plano_json_Bloque WHERE codigo = "+idFinca;

		Log.i("nextFinca", "query : "+q);

		PreparedStatement ps = cn.prepareStatement(q);
		rs = ps.executeQuery();

		String data = "";
		while (rs.next()) {
			data = rs.getString(2) + data;
			Log.i("nextFinca", data);
		}
		return ja.WriteJson(path, "Detalle", data);
	}

	@Override
	public List<DetalleTab> all(){

		Gson gson = new Gson();
		D1 = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<DetalleTab>>() {
		}.getType());

		return D1;
	}

	@Override
	public String json(DetalleTab o){
		Gson gson = new Gson();
		return gson.toJson(o);
	}


	public Date getDateJson(){
		try {
			Log.i("getDateJson", "validando query");
			ResultSet rs;
			PreparedStatement ps = cn.prepareStatement("SELECT  * FROM Plano_json_Bloque WHERE codigo = 0");
			rs = ps.executeQuery();

			Date fecha = null;
			while (rs.next()){
				fecha = rs.getDate(3);
				Log.i("nextFinca", "ultima fecha de modificacion formularios : "+fecha);
			}
			Log.i("getDateJson", "fecha : "+fecha);
			return fecha;
		}catch (Exception e){
			Log.i("getDateJson", "Error : "+e.toString());
			return null;
		}
	}

	public boolean validateDateFile() throws Exception {
		try {
			String dateJson = new SimpleDateFormat("yyyy-MM-dd").format(new File(path + "/Detalle.json").getAbsoluteFile().lastModified());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaParseada = sdf.parse(dateJson);

			return fechaParseada.before(getDateJson());
		}catch (Exception e){
			Log.i("getDateJson", "Error : "+e.toString());
			return false;
		}
	}

	public void setTxtNotification(Activity act, TextView txt){
		act.runOnUiThread(() -> {
			txt.setText("Validando Actualizaciones espere un momento porfavor...");
		});
	}
}
