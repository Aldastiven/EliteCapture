package com.example.eliteCapture.Model.View;

import com.example.eliteCapture.Config.Util.JsonAdmin;
import com.example.eliteCapture.Model.View.Interfaz.Respuestas;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class iRespuestas  implements Respuestas {


	public String ins = "INSERT INTO Datos_Procesos_Detalle (Fecha, Id_Procesos, Id_Procesos_Detalle, Valor_Resp_D, Porc_Resp_D, Id_Terminal, Id_usuario, consec_json)\n" +
						"VALUES (?,?,?,?,?,?,?,?)";


	@Override
	public List<RespuestasTab> forProceso(long id, String respuesta) throws Exception {
		return null;
	}

	@Override
	public boolean local(Long id) throws Exception {
		return false;
	}

	@Override
	public String insert(RespuestasTab o) throws Exception {
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

	@Override
	public List<RespuestasTab> all() throws Exception {
		return null;
	}

	@Override
	public String json(RespuestasTab o) throws Exception {
		Gson gson = new Gson();
		return gson.toJson(o);
	}

}
