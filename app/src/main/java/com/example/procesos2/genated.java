package com.example.procesos2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.Chead.Cdesplegable;
import com.example.procesos2.Chead.Cetalf;
import com.example.procesos2.Chead.Cetnum;
import com.example.procesos2.Chead.CfilAuto;
import com.example.procesos2.Chead.Cfiltro;
import com.example.procesos2.Chead.Cscanner;
import com.example.procesos2.Chead.Ctextview;
import com.example.procesos2.Conexion.CheckedConexion;
import com.example.procesos2.Config.sqlConect;
import com.example.procesos2.Cquest.Cconteos;
import com.example.procesos2.Cquest.CradioButton;
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Model.iDetalles;
import com.example.procesos2.Model.iRespuestas;
import com.example.procesos2.Model.iTemporal;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.RespuestasTab;
import com.example.procesos2.Model.tab.TemporalTab;

import java.io.File;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.String.valueOf;

public class genated extends AppCompatActivity {

  TextView EncabTitulo, contcc, scrollcomplete;
  LinearLayout linearBodypop, linearPrinc;
  Button editarEncab;
  ScrollView scrollForm;
  Dialog mypop, popcalificacion;


  List<DetallesTab> iP = new ArrayList<>();

  SharedPreferences sp = null;
  iRespuestas ir = null;
  iDesplegable id = null;
  iDetalles iD = null;
  iTemporal iT = null;

  String path = null, nombreproc = null;
  public Conexion con = null;

  int idres = 0, cont = 0, contConsec = 1;

  public boolean Temporal;

  ArrayList<String> al = new ArrayList<String>(200);
  ArrayList<String> cal = new ArrayList<String>(1000);
  ArrayList<Integer> arraypuntos = new ArrayList<>(10000);
  ArrayList<Integer> sumporespuesta = new ArrayList<>(10000);
  ArrayList<String> OptionArray = new ArrayList<>(200);

  CheckedConexion Cc = new CheckedConexion();

  @SuppressLint("ClickableViewAccessibility")
  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_genated);
    getSupportActionBar().hide();

    mypop = new Dialog(this);
    popcalificacion = new Dialog(this);

    insView();
    PC();

    sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

    try {
      path = getExternalFilesDir(null) + File.separator;
      con = new Conexion(path, getApplicationContext());


      //ENCABEZADO ASIGNA EL NOMBRE DEL PROCESO
      String nom = sp.getString("nom_proceso", "");
      nombreproc = nom;
      EncabTitulo.setText(nom);

      contcc.setText("1");

      CrearHeader();
      mypop.show();

      onckickBTNfloating();

    } catch (Exception ex) {
      Log.i("Error onCreate", ex.toString());
    }

  }


  //INSTANCIA CONTROLES VIEWS
  public void insView(){
    mypop.setContentView(R.layout.popupcumstom);
    popcalificacion.setContentView(R.layout.popupcalificacion);
    linearPrinc = findViewById(R.id.LinearCheck);
    EncabTitulo = findViewById(R.id.EncabTitulo);
    scrollForm = findViewById(R.id.scrollForm);
    contcc = findViewById(R.id.contcc);
    editarEncab = findViewById(R.id.editarEncab);
    linearBodypop = mypop.findViewById(R.id.linearbodypop);
    scrollcomplete = findViewById(R.id.complete);

    Window window = mypop.getWindow();
    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
  }

  //TRAE Y ENVIA PAHT Y CONEXION PARA LAS FUNCIONES DE LAS ENTIDADES
  public void PC(){
    try {

      path = getExternalFilesDir(null) + File.separator;
      con = new Conexion(path, getApplicationContext());

      if (con.getPath(path)) {
        Log.i("resultado con g ", "" + con.getPath(path));
        iD = new iDetalles(con.getConexion(), path);
        ir = new iRespuestas(con.getConexion(), path);
        id = new iDesplegable(con.getConexion(), path);
        iT = new iTemporal(path);

        iP = iD.all();
      } else {
        Log.i("resultado con x", "" + con.getPath(path));
        iD = new iDetalles(null, path);
        ir = new iRespuestas(null, path);
        id = new iDesplegable(null, path);
        iT = new iTemporal(path);

        iP = iD.all();
      }
    }catch (Exception ex){
      Toast.makeText(this, "Exception al traer path y conexion en metodo PC \n"+ex.toString(), Toast.LENGTH_SHORT).show();
    }
  }

  //MUESTRA O OCULTA EL POP CON LOS CAMPOS DEL HEADER
  public void Showpop(View v){
    mypop.show();
  }

  public void ocultarPop(View v){
    mypop.dismiss();
  }

  //CREA LOS CONTROLES DEL HEADER EN EL POP
  @RequiresApi(api = Build.VERSION_CODES.M)
  public void CrearHeader() {
    try {
        for (DetallesTab d : iP) {
            String campo = d.getTipoDetalle();
            Long id = d.getCodDetalle();
            String pregunta = d.getQuesDetalle();
            String desplegable = d.getListaDesplegable();

            if (d.getIdProceso() == getcodProceso() && d.getTipoModulo().equals("H")) {
                switch (campo){
                    case "TV":
                      Ctextview ct = new Ctextview();
                      linearBodypop.addView(ct.textview(genated.this, id ,pregunta));
                      insertTemp(id.intValue(),getcodProceso());
                      break;
                    case "ETN":
                      Cetnum cen = new Cetnum(genated.this, id, pregunta);
                      linearBodypop.addView(cen.tnumerico());
                      insertTemp(id.intValue(),getcodProceso());
                      break;
                    case "ETA":
                      Cetalf cal = new Cetalf(genated.this, id, pregunta);
                      linearBodypop.addView(cal.talfanumerico());
                      insertTemp(id.intValue(),getcodProceso());
                      break;
                    case "CBX":
                      Cdesplegable cd = new Cdesplegable(genated.this,id,pregunta,opciones(desplegable));
                      linearBodypop.addView(cd.desplegable());
                      insertTemp(id.intValue(),getcodProceso());
                      break;
                    case "FIL":
                      Cfiltro cf = new Cfiltro(genated.this, id, pregunta, desplegable);
                      cf.Carga(path);
                      linearBodypop.addView(cf.filtro());
                      insertTemp(id.intValue(),getcodProceso());
                      break;
                    case "SCA":
                      Cscanner cs = new Cscanner(genated.this, id, pregunta);
                      linearBodypop.addView(cs.scanner());
                      insertTemp(id.intValue(),getcodProceso());
                      break;
                    case "AUT":
                      CfilAuto ca = new CfilAuto(genated.this,id,pregunta,desplegable);
                      ca.Carga(path);
                      linearBodypop.addView(ca.autocompletado());
                      insertTemp(id.intValue(),getcodProceso());
                      break;
                    default:
                      Toast.makeText(this, "ocurrio un error al crear ", Toast.LENGTH_SHORT).show();
                      break;
                }
            }
        }
    } catch (Exception exception) {
      Toast.makeText(this, "exception en generated \n \n" + exception.toString(), Toast.LENGTH_SHORT).show();
      Log.i("Excepcreate",exception.toString());
    }

    CrearForm();
  }

  //CREA CONTROLES DEL FORMULARIO
  public void CrearForm() {
    scrollForm.fullScroll(View.FOCUS_UP); //funcion que sube el scroll al inicio
      for (DetallesTab d : iP) {

        String campo = d.getTipoDetalle();
        String pregunta = d.getQuesDetalle();
        String desplegable = d.getListaDesplegable();
        Float porcentaje = d.getPorcentaje();
        Long id = d.getCodDetalle();

        path = getExternalFilesDir(null) + File.separator;;

          if (d.getIdProceso() == getcodProceso() && d.getTipoModulo().equals("Q")) {
              switch (campo){
                case "RS":
                  Cconteos cc = new Cconteos(genated.this,id,pregunta,porcentaje);
                  linearPrinc.addView(cc.Cconteo());
                  insertTemp(id.intValue(),getcodProceso());
                  break;
                case "RB":
                  CradioButton cb = new CradioButton();
                  cb.Carga(path);
                  linearPrinc.addView(cb.Tradiobtn(this,id,pregunta,desplegable,porcentaje,path,nombreproc,getcodProceso(),getcodUsuario()));
                  insertTemp(id.intValue(),getcodProceso());
                  break;
              }
          }
      }
  }

  //CONSULTA LOS DATOS DEL SPINNER
  public ArrayList opciones(String desplegable)throws Exception{

    OptionArray.clear();

    id = new iDesplegable(null, path);
    id.nombre = desplegable;
    id.all();

    OptionArray.add("selecciona");

    for (DesplegableTab ds : id.all()) {
      if (ds.getFiltro().equals(desplegable)) {
        OptionArray.add(ds.getOptions());
      } else {
      }
    }

    return OptionArray;
  }


  //METODO PARA INSERTAR EN JSON TEMPORAL
  public void insertTemp(int idItem, int codpro){
    try {
      iT.path = path;
      iT.nombre = "Temp"+nombreproc;
      TemporalTab tt = new TemporalTab();
      tt.setItem(idItem);
      tt.setProceso(codpro);
      tt.setUsuario(getcodUsuario());
      tt.setRespuesta(null);
      tt.setPorcentaje(null);
      iT.insert(tt);
      iT.local();
    }catch (Exception ex){
      Toast.makeText(this, "Exc al insertar en en el json \n \n "+ex.toString(), Toast.LENGTH_LONG).show();
    }
  }

  //METODOS PARA OBTENER DATOS
  public String getPhoneName() {
    BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
    String deviceName = myDevice.getName();
    return deviceName;
  }
  public String getFecha() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyy HH:mm:ss");
    String fecha = sdf.format(cal.getTime());
    return fecha;
  }
  public int getcodProceso() {
    int idProceso = sp.getInt("cod_proceso", 0);
    return idProceso;
  }
  public int getcodUsuario(){
    int idusuario = sp.getInt("codigo", 0);
    return idusuario;
  }


  //REGISTRO DE JSON
  public void registroJson(View v) {
    try {
      //realiza el conteo de si encuentra alguno con null en el arreglo
      cont = 0;
      for (String i : al) {
        String cadena[] = i.split("--");

        if (cadena[2].equals("NULL")) {
          cont++;
        } else {
        }

      }
      validarNull();

    } catch (Exception ex) {
      Log.i("Error registro json\n", ex.toString());
    }
  }

  public void validarNull() {
    try {
      if (cont != 0) {
        Toast.makeText(this, "No puedes dejar campos vacios", Toast.LENGTH_SHORT).show();
      } else {
        for (String resdata : al) {
          String cadena[] = resdata.split("--");
          RegisterFinal(Long.parseLong(cadena[1]), cadena[2], cadena[3]);
          Log.i("data al", "" + resdata);
        }

        contador1_5();

        Log.i("data for", "zzzz");
        Toast.makeText(this, "Se guardo correctamente", Toast.LENGTH_SHORT).show();
        EraserQ();

      }
    } catch (Exception ex) {

    }
  }

  public void contador1_5() {
    if (Integer.parseInt(contcc.getText().toString()) < 6) {
      contConsec++;
      contcc.setText(valueOf(contConsec));
      contcc.setTextColor(Color.parseColor("#aab7b8"));
    }
    if (Integer.parseInt(contcc.getText().toString()) == 6) {
      contConsec = 1;
      contcc.setText(valueOf(contConsec));
    }
    if (Integer.parseInt(contcc.getText().toString()) == 5) {
      contcc.setTextColor(Color.parseColor("#ec7063"));
    }
  }

  public void EraserQ() {
    try {
      for (String i : al) {
        String cadena[] = i.split("--");
        while (cadena[0].equals("Q")) {
          al.remove(Integer.parseInt(cadena[1]) - 1);
        }
      }
      Log.i("data Eraser ", "llego");

    } catch (Exception ex) {
      //sumporespuesta.clear();
      sumporespuesta.clear();
      eliminarHijos();
      idres = 0;
      CrearForm();
    }
  }

  public void RegisterFinal(Long idDetalle, String respuesta, String porcentaje) {
    try {
      int idusuario = sp.getInt("codigo", 0);
      ir.nombre = "Respuestas";

      RespuestasTab rt = new RespuestasTab();

      if (ir.all().size() <= 0) {
        rt.setIdreg((long) ir.all().size() + 1);
      } else {
        rt.setIdreg(rt.getIdreg());
      }
      rt.setFecha(getFecha());
      rt.setIdProceso((long) getcodProceso());
      rt.setIdPregunta(idDetalle);
      rt.setRespuesta(respuesta);
      rt.setPorcentaje(Double.valueOf(porcentaje));
      rt.setTerminal(getPhoneName());
      rt.setIdUsuario(idusuario);
      rt.setConsecutivo(Integer.parseInt(contcc.getText().toString()));

      ir.insert(rt);

      Temporal = false;

    } catch (Exception ee) {
      Toast.makeText(this, "" + ee, Toast.LENGTH_SHORT).show();
    }
  }

  public void Enviar(View v) {
    try {
      iRespuestas irr = new iRespuestas(con.getConexion(), path);

      irr.nombre = "Respuestas";


      List<RespuestasTab> rtt = irr.all();


      if (rtt.isEmpty()) {
        Toast.makeText(this, "No hay registros pendientes para enviar", Toast.LENGTH_LONG).show();
      } else {
        if (Cc.checkedConexionValidate(this)) {
          for (RespuestasTab rc : rtt) {
            rc.setFecha(rc.getFecha());
            rc.setIdProceso(rc.getIdProceso());
            rc.setIdPregunta(rc.getIdPregunta());
            rc.setRespuesta(rc.getRespuesta());
            rc.setPorcentaje(rc.getPorcentaje());
            rc.setTerminal(rc.getTerminal());
            rc.setIdUsuario(rc.getIdUsuario());
            rc.setConsecutivo(rc.getConsecutivo());

            Log.i("Data Enviar", irr.Record(rc));

          }
          irr.delete(null);
          Toast.makeText(this, "Se envio correctamente", Toast.LENGTH_SHORT).show();

        } else {
          Toast.makeText(this, "No tienes conexión ", Toast.LENGTH_SHORT).show();
        }
      }
    } catch (Exception ex) {
      Toast.makeText(this, "Exception al subir los datos \n" + ex.toString(), Toast.LENGTH_SHORT).show();
    }
  }

  //elimina los controles dentro del linear del formulario
  public void eliminarHijos() {
    if (linearPrinc.getChildCount() > 0) {
      linearPrinc.removeAllViews();
    }
  }

  public void DesabilitarTeclado(View v) {

    v.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
          InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
          return true;
        }
        return false;
      }
    });

  }

  public float OperacionPorcentaje(float i, int r) {

    DecimalFormatSymbols separador = new DecimalFormatSymbols();
    separador.setDecimalSeparator('.');

    DecimalFormat format = new DecimalFormat("#.##", separador);

    Float operacion = (i / 9) * (9 - r);

    return Float.valueOf(format.format(operacion));
  }


  //cuenta las respuestas dadas
  public int contarporcenescogido() {

    int intcont = 0;
    int intTot = 0;
    for (int i = 0; i < sumporespuesta.size(); i++) {
      if (sumporespuesta.get(i) >= 0) {
        intcont += sumporespuesta.get(i);
        intTot += arraypuntos.get(i);
      }
    }
    Log.i("prueba onClick", "" + intcont + " / " + intTot + " = " + intcont*100 / intTot);
    return (intcont*100 / intTot);
  }

  private void onckickBTNfloating() {
    /*fBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          //int dato = contarporcenescogido();

          //Dialog(dato + " %");
          //Log.i("prueba onClick", "" + contarporcenescogido());
          //Log.i("prueba onClick", "" + al.toString());
          //Log.i("prueba null",""+cont);
        } catch (Exception ex) {
          Toast.makeText(genated.this, "Exception \n " + ex, Toast.LENGTH_SHORT).show();
        }
      }
    });*/
  }

  public Double SumarCalificacion() {
    double suma = 0;

    for (int i = 0; i < cal.size(); i++) {
      double valor = 0;
      try {
        valor = Double.parseDouble(cal.get(i));
      } catch (Exception ex) {
        valor = 0;
      }
      suma += valor;
    }

    DecimalFormatSymbols separador = new DecimalFormatSymbols();
    separador.setDecimalSeparator('.');

    DecimalFormat format = new DecimalFormat("#.##", separador);
    return Double.parseDouble(format.format(suma));
  }

  //SI OPRIME EL BOTON DE RETROCESO
  public void onBackPressed() {
    Intent i = new Intent(this, Index.class);
    startActivity(i);
  }

  public void onBackPressedform(View v){
    Intent i = new Intent(this, Index.class);
    startActivity(i);
  }

  //CALIFICACIÓN POP
  public void  onCalificar(View v){
    popcalificacion.show();
  }

  //CONTRUCTOR QUE RETORNA FALSE O TRUE A LA CONEXION DE LA BD
  protected class Conexion extends sqlConect {
    Connection cn = getConexion();
    String path = null;
    Context context;

    public Conexion(String path, Context context) {
      this.path = path;
      this.context = context;
      getPath(path);
    }

    public boolean getPath(String path) {
      this.path = path;
      if (cn != null) {
        return true;
      } else {
        return false;
      }
    }
  }

}
