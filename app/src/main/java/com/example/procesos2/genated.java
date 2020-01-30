package com.example.procesos2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.procesos2.Chead.Cdesplegable;
import com.example.procesos2.Chead.Cetalf;
import com.example.procesos2.Chead.Cetnum;
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
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.RespuestasTab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.String.valueOf;

public class genated extends AppCompatActivity {

  TextView EncabTitulo, contcc;
  LinearLayout linearHeader;
  LinearLayout linearBodypop;
  LinearLayout linearPrinc;
  LinearLayout LLprincipal;
  Button btnGuardar,editarEncab;
  ScrollView scrollForm;
  FloatingActionButton fBtn;

  String path = null;
  List<DetallesTab> iP = new ArrayList<>();

  private ProgressDialog progress;
  SharedPreferences sp = null;
  iRespuestas ir = null;
  iDesplegable id = null;
  iDetalles iD = null;

  Dialog mypop;

  public Conexion con = null;

  int idres = 0, cont = 0, contConsec = 1;

  public boolean Temporal;


  ArrayList<String> al = new ArrayList<String>(200);
  ArrayList<String> cal = new ArrayList<String>(1000);
  ArrayList<Integer> arraypuntos = new ArrayList<>(10000);
  ArrayList<Integer> sumporespuesta = new ArrayList<>(10000);

  CheckedConexion Cc = new CheckedConexion();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_genated);
    getSupportActionBar().hide();

    mypop = new Dialog(this);
    mypop.setContentView(R.layout.popupcumstom);
    linearPrinc = findViewById(R.id.LinearCheck);
    //btnGuardar = findViewById(R.id.guardar);
    EncabTitulo = findViewById(R.id.EncabTitulo);
    scrollForm = findViewById(R.id.scrollForm);
    //fBtn = findViewById(R.id.fBtn);
    contcc = findViewById(R.id.contcc);
    editarEncab = findViewById(R.id.editarEncab);

    linearBodypop = mypop.findViewById(R.id.linearbodypop);

    Window window = mypop.getWindow();
    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

    try {

      path = getExternalFilesDir(null) + File.separator;
      con = new Conexion(path, getApplicationContext());

      if (con.getPath(path)) {
        Log.i("resultado con g ", "" + con.getPath(path));
        iD = new iDetalles(con.getConexion(), path);
        ir = new iRespuestas(con.getConexion(), path);
        id = new iDesplegable(con.getConexion(), path);

        iP = iD.all();
      } else {
        Log.i("resultado con x", "" + con.getPath(path));
        iD = new iDetalles(null, path);
        ir = new iRespuestas(null, path);
        id = new iDesplegable(null, path);

        iP = iD.all();
      }


      String nom = sp.getString("nom_proceso", "");
      EncabTitulo.setText(nom);

      SharedPreferences.Editor edit = sp.edit();
      edit.putString("dataedittext", "");
      edit.apply();

      contcc.setText("1");

      CrearHeader();
      onckickBTNfloating();
      crearJsonRes();

      //abre el encabezado
      mypop.show();

    } catch (Exception ex) {
      Log.i("Error onCreate", ex.toString());
    }

  }


  @Override
  protected void onPause() {
    super.onPause();
    getState();
    Log.i("Temporal ", String.valueOf(Temporal));

    if (Temporal) {
      SharedPreferences.Editor edit = sp.edit();
      edit.putBoolean("Temporal", Temporal);
      edit.commit();
      edit.apply();
    } else {
      SharedPreferences.Editor edit = sp.edit();
      edit.putBoolean("Temporal", Temporal);
      edit.commit();
      edit.apply();
    }
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    //Toast.makeText(this,"onDestroy generated",Toast.LENGTH_SHORT).show();
    Temporal = false;

    if (Temporal) {
      SharedPreferences.Editor edit = sp.edit();
      edit.putBoolean("Temporal", Temporal);
      edit.commit();
      edit.apply();
    } else {
      SharedPreferences.Editor edit = sp.edit();
      edit.putBoolean("Temporal", Temporal);
      edit.commit();
      edit.apply();
    }

    al.clear();

  }

  //CREAR EL JSON SINO EXISTE
  private void crearJsonRes() throws Exception {
    ir.nombre = "Respuestas";
    try {
      ir.all();
      ir.local();
    } catch (Exception ex) {
      ir.local();
    }
  }

  public void Showpop(View v){
    mypop.show();
  }


  public void CrearHeader() {
    try {

      path = getExternalFilesDir(null) + File.separator;

      for (DetallesTab d : iP) {
        int cod = sp.getInt("cod_proceso", 0);

        String tipo1 = "RS"; //BOTON DE CANTIDAD
        String tipo2 = "SWH"; //BOTON DE SI O NO
        String tipo3 = "TV"; //TEXTVIEW
        String tipo4 = "ETN"; //EDITTEXT NUMERICO
        String tipo5 = "ETA"; //EDITTEXT ALFANUMERICO
        String tipo6 = "CBX"; //SPINNER
        String tipo7 = "FIL"; //FLITROS JSON
        String tipo8 = "SCA"; //SCANNER BAR

        if (d.getTipoDetalle().equals(tipo3) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {

          Long id = d.getCodDetalle();
          String pregunta = d.getQuesDetalle();
          String modulo = d.getTipoModulo();
          //CrearTextViewFecha(modulo, id, pregunta);

          Ctextview ct = new Ctextview();
          linearBodypop.addView(ct.textview(this, id ,pregunta));

        } else if (d.getTipoDetalle().equals(tipo4) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
          Long id = d.getCodDetalle();
          String pregunta = d.getQuesDetalle();
          String modulo = d.getTipoModulo();
          //CrearEditTextNumeric(modulo, id, pregunta);

          Cetnum cen = new Cetnum();
          linearBodypop.addView(cen.tnumerico(genated.this, id, pregunta));

        } else if (d.getTipoDetalle().equals(tipo5) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
          Long id = d.getCodDetalle();
          String pregunta = d.getQuesDetalle();
          String modulo = d.getTipoModulo();
          //CrearEditTextAlfanumeric(modulo, id, pregunta);

          Cetalf cal = new Cetalf();
          linearBodypop.addView(cal.talfanumerico(this, id, pregunta));

        } else if (d.getTipoDetalle().equals(tipo6) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
          Long id = d.getCodDetalle();
          String pregunta = d.getQuesDetalle();
          String modulo = d.getTipoModulo();
          String desplegable = d.getListaDesplegable();
          Float porcentaje = d.getPorcentaje();
          //CrearSpinner(modulo, id, pregunta, desplegable, porcentaje);

          Cdesplegable cd = new Cdesplegable();
          cd.Carga(path);
          linearBodypop.addView(cd.desplegable(this, id, pregunta, desplegable));

        } else if (d.getTipoDetalle().equals(tipo7) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {

          Long id = d.getCodDetalle();
          String pregunta = d.getQuesDetalle();
          String modulo = d.getTipoModulo();
          String desplegable = d.getListaDesplegable();
          Float porcen = d.getPorcentaje();

          //CrearFiltro(modulo, id, pregunta, desplegable);

          Cfiltro cf = new Cfiltro();
          cf.Carga(path);
          linearBodypop.addView(cf.filtro(this, id, pregunta, desplegable));


        }else if(d.getTipoDetalle().equals(tipo8) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")){

          Long id = d.getCodDetalle();
          String pregunta = d.getQuesDetalle();

          Cscanner cs = new Cscanner();
          linearBodypop.addView(cs.scanner(this,id,pregunta));
        }


      }
    } catch (Exception exception) {
      Toast.makeText(this, "exception en generated \n \n" + exception.toString(), Toast.LENGTH_SHORT).show();
      Log.i("Excep on create",exception.toString());
    }

    CrearForm();
  }

  public void CrearForm() {

    scrollForm.fullScroll(View.FOCUS_UP);

    for (DetallesTab d : iP) {
      int cod = sp.getInt("cod_proceso", 0);

      String tipo1 = "RS"; //BOTON DE CANTIDAD
      String tipo2 = "SWH"; //BOTON DE SI O NO
      String tipo3 = "TV"; //TEXTVIEW
      String tipo4 = "ETN"; //EDITTEXT NUMERICO
      String tipo5 = "ETA"; //EDITTEXT ALFANUMERICO
      String tipo6 = "CBX"; //SPINNER
      String tipo7 = "RB"; //RADIO BUTTONS


      if (d.getTipoDetalle().equals(tipo1) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
        Long idCons = d.getIdConsecutivo();
        Long id = d.getCodDetalle();
        String pregunta = d.getQuesDetalle();
        String modulo = d.getTipoModulo();
        Float porce = d.getPorcentaje();
        //CrearSumRes(modulo, id, pregunta, porce);
        Cconteos cc = new Cconteos();
        linearPrinc.addView(cc.Cconteo(this,id,pregunta,porce));

      } else if (d.getTipoDetalle().equals(tipo2) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
        Long idCons = d.getIdConsecutivo();
        Long id = d.getCodDetalle();
        String pregunta = d.getQuesDetalle();
        String modulo = d.getTipoModulo();

      } else if (d.getTipoDetalle().equals(tipo4) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
        Long idCons = d.getIdConsecutivo();
        Long id = d.getCodDetalle();
        String pregunta = d.getQuesDetalle();
        String modulo = d.getTipoModulo();

      } else if (d.getTipoDetalle().equals(tipo5) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
        Long idCons = d.getIdConsecutivo();
        Long id = d.getCodDetalle();
        String pregunta = d.getQuesDetalle();
        String modulo = d.getTipoModulo();

      } else if (d.getTipoDetalle().equals(tipo6) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
        Long idCons = d.getIdConsecutivo();
        Long id = d.getCodDetalle();
        String pregunta = d.getQuesDetalle();
        String modulo = d.getTipoModulo();
        String desplegable = d.getListaDesplegable();
        Float porcen = d.getPorcentaje();

      } else if(d.getTipoDetalle().equals(tipo7) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")){
        Long id = d.getCodDetalle();
        String pregunta = d.getQuesDetalle();
        String desplegable = d.getListaDesplegable();
        Float porcentaje = d.getPorcentaje();

        CradioButton cb = new CradioButton();
        cb.Carga(path);
        linearPrinc.addView(cb.Tradiobtn(this,id,pregunta,desplegable,porcentaje));
      }
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

  //BAJAR TECLADO

  public void KeyDown(EditText et) {
    InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
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

  public void progressBar(String msgCarga, final String msgCompletado, final int tiempoSalteado) {
    try {
      progress = new ProgressDialog(this, R.style.MyProgressDialogDespues);
      progress.setMessage(msgCarga);
      progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      progress.setProgress(0);
      progress.show();

      final int totalProgressTime = 100;
      final Thread t = new Thread() {
        @Override
        public void run() {
          int jumpTime = 0;

          while (jumpTime < totalProgressTime) {
            try {
              jumpTime += tiempoSalteado;
              progress.setProgress(jumpTime);
              sleep(200);
            } catch (InterruptedException e) {
              Toast.makeText(genated.this, "Progress Bar \n" + e.toString(), Toast.LENGTH_SHORT).show();
            }
          }

          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              progress.setMessage(msgCompletado);
              progress.setProgressStyle(R.style.MyProgressDialogDespues);
              int dur = 2500;
              new Handler().postDelayed(new Runnable() {
                public void run() {
                  progress.dismiss();
                }
              }, dur);

            }
          });

        }
      };
      t.start();

    } catch (Exception ex) {
      Toast.makeText(this, "" + ex.toString(), Toast.LENGTH_SHORT).show();
    }
  }

  public void Dialog(String msg) {
    try {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);

      LinearLayout linearLayout = new LinearLayout(this);
      linearLayout.setOrientation(LinearLayout.VERTICAL);

      TextView tvv = new TextView(this);
      tvv.setText(msg);
      tvv.setTextSize(50);
      tvv.setTextColor(Color.parseColor("#5DADE2"));
      tvv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

      linearLayout.addView(tvv);

      builder.setTitle("Calificación");
      builder.setCancelable(true);
      builder.setView(linearLayout);
      //builder.setIcon(R.drawable.ok);

      final AlertDialog dialog = builder.create();

      dialog.show();

      final Timer timer2 = new Timer();
      timer2.schedule(new TimerTask() {
        public void run() {
          dialog.dismiss();
          timer2.cancel();
        }
      }, 5000);
    } catch (Exception ex) {
      Toast.makeText(this, "Dialog" + ex, Toast.LENGTH_SHORT).show();
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
    fBtn.setOnClickListener(new View.OnClickListener() {
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
    });
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


  public void onBackPressed() {
    //Toast.makeText(this, "Oprime el boton de menu en la parte superior de la pantalla", Toast.LENGTH_LONG).show();
    Intent i = new Intent(this, Index.class);
    startActivity(i);
  }

  //guardar estado de la vista
  public boolean saveState() {
    try {
      SharedPreferences.Editor edit = sp.edit();
      edit.putString("estadoActividad", al.toString());
      edit.commit();
      edit.apply();

      return Temporal = true;
    } catch (Exception ex) {
      Toast.makeText(this, "se genero una exception al guardar el estado del formulario \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
    }
    return false;
  }

  public void getState() {
    try {
      String arreglo = sp.getString("estadoActividad", "");
      //Toast.makeText(this,""+arreglo,Toast.LENGTH_SHORT).show();

      //Toast.makeText(this,""+Temporal,Toast.LENGTH_SHORT).show();

    } catch (Exception ex) {
      Toast.makeText(this, "Se genero una exception al traer el estado del formulario \n \n" + ex.toString(), Toast.LENGTH_SHORT).show();
    }
  }

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
