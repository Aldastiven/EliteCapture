package com.example.eliteCapture.Model.View.Tab;

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;

import java.util.List;

public class RespuestasTab {
	private Long id;
	private int idProceso;
	private Long idPregunta;
	private String tipo;
	private String pregunta;
	private float ponderado;
	private String respuesta;
	private String valor;
	private List<DesplegableTab> desplegable;
	private int reglas;
	private String tip;

	public RespuestasTab(Long id, int idProceso, Long idPregunta, String tipo, String pregunta, float ponderado, String respuesta, String valor, List<DesplegableTab> desplegable, int reglas, String tip) {
		this.setId(id);
		this.setIdProceso(idProceso);
		this.setIdPregunta(idPregunta);
		this.setTipo(tipo);
		this.setPregunta(pregunta);
		this.setPonderado(ponderado);
		this.setRespuesta(respuesta);
		this.setValor(valor);
		this.setDesplegable(desplegable);
		this.setReglas(reglas);
		this.setTip(tip);
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(int idProceso) {
		this.idProceso = idProceso;
	}

	public Long getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(Long idPregunta) {
		this.idPregunta = idPregunta;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public float getPonderado() {
		return ponderado;
	}

	public void setPonderado(float ponderado) {
		this.ponderado = ponderado;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public List<DesplegableTab> getDesplegable() {
		return desplegable;
	}

	public void setDesplegable(List<DesplegableTab> desplegable) {
		this.desplegable = desplegable;
	}

	public int getReglas() {
		return reglas;
	}

	public void setReglas(int reglas) {
		this.reglas = reglas;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
}
