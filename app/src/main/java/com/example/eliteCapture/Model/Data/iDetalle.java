package com.example.eliteCapture.Model.Data;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.Data.Interfaz.Detalle;
import com.example.eliteCapture.Model.Data.Tab.DetalleTab;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class iDetalle implements Detalle {

	public List<DetalleTab> D1 = new ArrayList<>();
	Connection cn = null;
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
			"  FROM [Formularios].[dbo].[Procesos_Detalle]\n" +
			"  ORDER BY [id_proceso], [capitulo] , [item];";

	public iDetalle(Connection cn, String path) throws Exception {
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
		PreparedStatement ps = cn.prepareStatement(all);
		rs = ps.executeQuery();

		while (rs.next()) {
			D1.add(gift(rs));
		}

		//closeConexion(cn,rs);
		String contenido = new Gson().toJson(D1);

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
		return new DetalleTab(
				rs.getLong("id_detalle"),
				rs.getInt("id_proceso"),
				rs.getLong("codigo_detalle"),
				rs.getString("nombre_detalle").trim(),
				rs.getString("tipo").trim(),
				rs.getString("lista_desp"),
				rs.getString("tipo_M"),
				rs.getFloat("porcentaje"),
				rs.getString("capitulo"),
				rs.getString("item"),
				rs.getString("Capitulo_Nombre"),
				rs.getString("grupo1"),
				rs.getInt("reglas"),
				rs.getString("tip"));

	}
}
