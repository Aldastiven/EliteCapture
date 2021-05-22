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
	public boolean local(Long id_proceso) throws Exception {
		return false;
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
		ResultSet rs;
		PreparedStatement ps = cn.prepareStatement("SELECT  * FROM Plano_json_Bloque WHERE codigo = 0");
		rs = ps.executeQuery();

		String dataJson = "";

		while (rs.next()) {
			//D1.add(gift(rs));
			dataJson = dataJson + rs.getString(2);
		}

		String contenido = new Gson().toJson(dataJson);
		return ja.WriteJson(path, nombre, contenido);
	}

	@Override
	public List<DetalleTab> all() throws Exception {

		Gson gson = new Gson();
		D1 = gson.fromJson(ja.ObtenerLista(path, nombre), new TypeToken<List<DetalleTab>>() {
		}.getType());

		return D1;
	}

	@Override
	public String json(DetalleTab o) throws Exception {
		Gson gson = new Gson();
		return gson.toJson(o);
	}

	private DetalleTab gift(ResultSet rs) throws Exception {
		Log.i("ErrorSplashDetalle",rs.getInt("id_proceso")+" COD. DETALLE : "+rs.getString("codigo_detalle"));

		return new DetalleTab(
				rs.getLong("id_detalle"),
				rs.getInt("id_proceso"),
				rs.getInt("codigo_detalle"),
				rs.getString("nombre_detalle"),
				rs.getString("tipo"),
				rs.getString("lista_desp"),
				rs.getString("tipo_M"),
				rs.getFloat("porcentaje"),
				rs.getString("capitulo"),
				rs.getString("item"),
				rs.getString("Capitulo_Nombre"),
				rs.getString("grupo1"),
				rs.getInt("reglas"),
				rs.getString("tip"),
				rs.getString("desde_hasta"),
				rs.getInt("decimales"),
				rs.getInt("obligatorio"));
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
