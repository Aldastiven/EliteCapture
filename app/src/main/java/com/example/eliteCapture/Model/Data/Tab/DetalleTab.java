package com.example.eliteCapture.Model.Data.Tab;

public class DetalleTab {

	private Long id_detalle;
	private int id_proceso;
	private int codigo_detalle;
	private String nombre_detalle;
	private String tipo;
	private String lista_desp;
	private String tipo_M;
	private Float porcentaje;
	private String capitulo;
	private String item;
	private String capitulo_Nombre;
	private String grupo1;
	private int reglas;
	private String tip;
	private String desde_hasta;
	private  int decimales;
	int obligatorio;


	public DetalleTab(Long id_detalle, int id_proceso, int codigo_detalle, String nombre_detalle, String tipo, String lista_desp, String tipo_M, Float porcentaje, String capitulo, String item, String capitulo_Nombre, String grupo1, int reglas, String tip, String desde_hasta, int decimales, int obligatorio) {
		this.setId_detalle(id_detalle);
		this.setId_proceso(id_proceso);
		this.setCodigo_detalle(codigo_detalle);
		this.setNombre_detalle(nombre_detalle);
		this.setTipo(tipo);
		this.setLista_desp(lista_desp);
		this.setTipo_M(tipo_M);
		this.setPorcentaje(porcentaje);
		this.setCapitulo(capitulo);
		this.setItem(item);
		this.setCapitulo_Nombre(capitulo_Nombre);
		this.setGrupo1(grupo1);
		this.setReglas(reglas);
		this.setTip(tip);
		this.setDecimales(decimales);
		this.setObligatorio(obligatorio);
	}


	public Long getId_detalle() {
		return id_detalle;
	}

	public void setId_detalle(Long id_detalle) {
		this.id_detalle = id_detalle;
	}

	public int getId_proceso() {
		return id_proceso;
	}

	public void setId_proceso(int id_proceso) {
		this.id_proceso = id_proceso;
	}

	public int getCodigo_detalle() {
		return codigo_detalle;
	}

	public void setCodigo_detalle(int codigo_detalle) {
		this.codigo_detalle = codigo_detalle;
	}

	public String getNombre_detalle() {
		return nombre_detalle;
	}

	public void setNombre_detalle(String nombre_detalle) {
		this.nombre_detalle = nombre_detalle;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getLista_desp() {
		return lista_desp;
	}

	public void setLista_desp(String lista_desp) {
		this.lista_desp = lista_desp;
	}

	public String getTipo_M() {
		return tipo_M;
	}

	public void setTipo_M(String tipo_M) {
		this.tipo_M = tipo_M;
	}

	public Float getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Float porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getCapitulo() {
		return capitulo;
	}

	public void setCapitulo(String capitulo) {
		this.capitulo = capitulo;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getCapitulo_Nombre() {
		return capitulo_Nombre;
	}

	public void setCapitulo_Nombre(String capitulo_Nombre) {
		this.capitulo_Nombre = capitulo_Nombre;
	}

	public String getGrupo1() {
		return grupo1;
	}

	public void setGrupo1(String grupo1) {
		this.grupo1 = grupo1;
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

	public String getDesde_hasta() {
		return desde_hasta;
	}

	public void setDesde_hasta(String desde_hasta) {
		this.desde_hasta = desde_hasta;
	}

	public int getDecimales() {
		return decimales;
	}

	public void setDecimales(int decimales) {
		this.decimales = decimales;
	}

	public int getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(int obligatorio) {
		this.obligatorio = obligatorio;
	}
}
