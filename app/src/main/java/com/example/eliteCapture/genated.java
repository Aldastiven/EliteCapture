package com.example.eliteCapture;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.ControlViews.Cdesplegable;
import com.example.eliteCapture.Config.Util.ControlViews.Cetalf;
import com.example.eliteCapture.Config.Util.ControlViews.Cetnum;
import com.example.eliteCapture.Config.Util.ControlViews.CfilAuto;
import com.example.eliteCapture.Config.Util.ControlViews.Cfiltro;
import com.example.eliteCapture.Config.Util.ControlViews.ControlGnr;
import com.example.eliteCapture.Config.Util.ControlViews.Cscanner;
import com.example.eliteCapture.Config.Util.ControlViews.Ctextview;
import com.example.eliteCapture.Config.Util.ControlViews.Cconteos;
import com.example.eliteCapture.Config.Util.ControlViews.CradioButton;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.View.Interfaz.Contenedor;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContador;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.Model.View.iRespuestas;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

public class genated extends AppCompatActivity {

    TextView EncabTitulo, contcc, scrollcomplete, txtCalificacion;
    LinearLayout linearBodypop, linearPrinc;
    ScrollView scrollForm, scrollCalificacion;
    Dialog mypop, popcalificacion, popvalidar;
    Admin adm = null;
    iContenedor iCon = null;
    ContenedorTab contenedor = null;
    SharedPreferences sp = null;
    Button popSi, popNo;

    String path = null;
    String dataCamera;
    boolean footer;

    int contConsec = 0;
    ProcesoTab pro = null;
    UsuarioTab usu = null;
    iContador contador = null;

    boolean camera;
    boolean inicial = false;

    public boolean ok, temporal; //retorna la respuesta de un formulario pendiente

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genated);
        getSupportActionBar().hide();


        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        try {

            //instancia dialogogos (modales)
            mypop = new Dialog(this);
            popcalificacion = new Dialog(this);
            popvalidar = new Dialog(this);

            insView(); //instancia los elementos del layout

            path = getExternalFilesDir(null) + File.separator; //path
            adm = new Admin(null, path);//administra la conexion de las entidades
            getcodProceso(); //obtiene los datos del proceso
            getcodUsuario(); //obtiene los datos del usuario

            iCon = new iContenedor(path);//intancia funcionalidad de la entidad contenedort
            contador = new iContador(path);

            cargarContador();


            contenedor = validarTemporal();//valida si hay datos temporales de los formularios

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                camera = bundle.getBoolean("camera");
                dataCamera = bundle.getString("codigo");
                ok = true;
                crearform();
                mypop.show();
            } else {
                if (temporal) {
                    popvalidar.show();
                } else {
                    crearform();
                    mypop.show();
                }
            }

            iCon.crearTemporal(contenedor);//crea el json temporal con los datos correspondientes

        } catch (Exception ex) {
            Log.i("Error_onCreate", ex.toString());
        }

    }

    public void cargarContador() {

        EncabTitulo.setText(pro.getNombre_proceso());//asigna el nombre del encabezado
        contConsec = contador.getCantidad(usu.getId_usuario(), pro.getCodigo_proceso()) + 1;
        contcc.setText(String.valueOf(contConsec));//inicializa el conteo de formularios guardados o enviados
        int regla = (pro.getPersonalizado5() != null) ? Integer.parseInt(pro.getPersonalizado5()) : 0;
        validarFooter(contConsec, regla);
    }

    public void validarFooter(int contador, int regla) {
        Log.i("Footer", "Contador: " + contador + " Regla: " + regla);
        try {
            footer = (regla != 0 && (contador % regla) == 0);

        } catch (NumberFormatException en) {
            footer = false;
        }
    }

    public void crearform() {
        try {
            CrearEncabezado();
            CrearCuerpo();
        } catch (Exception ex) {
            Toast.makeText(this, "exc crearform" + ex.toString(), Toast.LENGTH_SHORT).show();
            Log.i("exc crearform", ex.toString());
        }
    }

    public void CrearEncabezado() throws Exception {
        CrearHeader(contenedor.getHeader());//crea los elemtos del header y pasa datos correspondientes
    }

    public void CrearCuerpo() throws Exception {
        CrearForm(contenedor.getQuestions(), "Q");//crea los elemtos del body (formulario) y pasa datos correspondientes
        if (footer) {
            CrearForm(contenedor.getFooter(), "F");//crea los elemtos del body (formulario) y pasa datos correspondientes
        }
    }

    //INSTANCIA CONTROLES VIEWS
    public void insView() {
        mypop.setContentView(R.layout.popupcumstom);
        popcalificacion.setContentView(R.layout.popupcalificacion);
        popvalidar.setContentView(R.layout.popupvalidar);
        popSi = findViewById((R.id.popSi));
        popNo = findViewById((R.id.popNo));
        linearPrinc = findViewById(R.id.LinearCheck);
        EncabTitulo = findViewById(R.id.EncabTitulo);
        scrollForm = findViewById(R.id.scrollForm);
        contcc = findViewById(R.id.contcc);
        scrollCalificacion = findViewById(R.id.scrollCalificacion);
        linearBodypop = mypop.findViewById(R.id.linearbodypop);
        scrollcomplete = findViewById(R.id.complete);
        txtCalificacion = popcalificacion.findViewById(R.id.txtCalificacion);

        popvalidar.setCancelable(false);
        mypop.setCancelable(false);

        Window window = mypop.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    //VALIDA SI HAY FORMULARIOS PENDIENTES DESDE EL JSON TEMP
    public ContenedorTab validarTemporal() throws Exception {

        ContenedorTab conTemp = iCon.optenerTemporal();
        try {
            if (conTemp != null) {
                if (conTemp.getIdProceso() == pro.getCodigo_proceso()) {

                    temporal = true;
                    return conTemp;

                } else {
                    temporal = false;
                    return contenedorLimipio();
                }
            }

            return contenedorLimipio();
        } catch (Exception e) {
            Log.i("Contenedor_Error", e.toString());
            temporal = false;
        }
        return contenedorLimipio();
    }

    public ContenedorTab contenedorLimipio() throws Exception {
        return iCon.generarContenedor(
                usu.getId_usuario(),
                getPhoneName(),
                adm.getDetalles()
                        .forDetalle(pro.getCodigo_proceso()));
    }

    public ContenedorTab contenedorEncabezado(ContenedorTab temporal) {
        return new ContenedorTab(
                pro.getCodigo_proceso(),
                temporal.getHeader(),
                contenedor.getQuestions(),
                contenedor.getFooter(),
                temporal.getIdUsuario(),
                temporal.getTerminal()
        );
    }

    //REALIZA VALIDACION DEL POP (SI O NO EN FORMULARIO PENDIENTE)
    public void popSI(View v) {
        ok = true;
        try {
            crearform();
            mypop.show();
        } catch (Exception ex) {
            Log.i("Contenedor", "" + ex.toString());
        }
        popvalidar.dismiss();
    }

    public void popNo(View v) {
        ok = false;

        try {
            contenedor = contenedorLimipio();
            iCon.crearTemporal(contenedor);
            crearform();
            mypop.show();
        } catch (Exception ex) {
        }
        popvalidar.dismiss();
    }

    //CREA LOS CONTROLES DEL HEADER EN EL POP
    public void CrearHeader(List<RespuestasTab> header) {
        try {
            for (RespuestasTab r : header) {

                switch (r.getTipo()) {
                    case "TV":
                        Ctextview ct = new Ctextview();
                        linearBodypop.addView(ct.textview(genated.this, r.getId(), r.getPregunta()));
                        break;
                    case "ETN":
                        Log.i("validar", "" + r.getRespuesta());

                        Cetnum cen = new Cetnum(genated.this, path, r.getId(), r.getPregunta(), "H", r, (r.getRespuesta() != null), inicial);
                        linearBodypop.addView(cen.tnumerico());
                        break;
                    case "ETA":
                        Cetalf cal = new Cetalf(genated.this, path, r.getId(), r.getPregunta(), "H", r, (r.getRespuesta() != null ? true : false), inicial);
                        linearBodypop.addView(cal.talfanumerico());
                        break;
                    case "CBX":
                        Cdesplegable cd = new Cdesplegable(genated.this, path, r.getId(), r.getPregunta(), r.getDesplegable(), "H", r, (r.getRespuesta() != null ? true : false), inicial);
                        linearBodypop.addView(cd.desplegable());
                        break;
                    case "FIL":
                        Cfiltro cf = new Cfiltro(genated.this, path, r.getId(), r.getPregunta(), r.getDesplegable(), "H", r, (r.getRespuesta() != null ? true : false), inicial);
                        linearBodypop.addView(cf.filtro());
                        break;
                    case "SCA":
                        Cscanner cs = new Cscanner(genated.this, path, r.getId(), r.getPregunta(), "H", r, (dataCamera != null ? dataCamera : ""), (r.getRespuesta() != null ? true : false), inicial,sp);
                        linearBodypop.addView(cs.scanner());
                        break;
                    case "AUT":
                        CfilAuto ca = new CfilAuto(genated.this, path, r.getId(), r.getPregunta(), "H", r.getDesplegable(), r, (r.getRespuesta() != null ? true : false), inicial);
                        linearBodypop.addView(ca.autocompletado());
                        break;
                    default:
                        Toast.makeText(this, "ocurrio un error al crear ", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        } catch (Exception exception) {
            Toast.makeText(this, "exception en generated \n \n" + exception.toString(), Toast.LENGTH_SHORT).show();
            Log.i("Excepcreate", exception.toString());
        }

    }

    //CREA CONTROLES DEL FORMULARIO
    public void CrearForm(List<RespuestasTab> questions, String ubicacion) {
        try {
            scrollForm.fullScroll(View.FOCUS_UP); //funcion que sube el scroll al inicio

            for (RespuestasTab r : questions) {

                Log.i("Error_Crs", "" + new iRespuestas().json(r));

                switch (r.getTipo()) {
                    case "RS":
                        Cconteos cc = new Cconteos(genated.this, path, "Q", r, (r.getRespuesta() != null), inicial);
                        linearPrinc.addView(cc.Cconteo());
                        break;
                    case "RB":
                        CradioButton cb = new CradioButton(genated.this, path, "Q", r.getId(), r.getPregunta(), r.getPonderado(), r.getDesplegable(), r, (r.getRespuesta() != null), inicial);
                        linearPrinc.addView(cb.Tradiobtn());
                        break;
                }
            }
        } catch (Exception ex) {
            Log.i("exc_crearForm", ex.toString());
        }
    }


    // metodo para extraer del SharedPreferences el id del proceso
    public void getcodProceso() {
        pro = new Gson().fromJson(sp.getString("proceso", ""), new TypeToken<ProcesoTab>() {
        }.getType());
        Log.i("ProContenedor:", sp.getString("proceso", ""));

    }

    // metodo para extraer del SharedPreferences el id del usuario
    public void getcodUsuario() {
        usu = new Gson()
                .fromJson(
                        sp.getString("usuario", ""),
                        new TypeToken<UsuarioTab>() {
                        }.getType());
    }

    //METODOS PARA OBTENER DATOS
    public String getPhoneName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        return deviceName;
    }

    //MUESTRA EL POP CON LOS CAMPOS DEL HEADER
    public void Showpop(View v) {
        mypop.show();
    }

    //OCULTA EL POP CON LOS CAMPOS DEL HEADER
    public void ocultarPop(View v) {

        String splitS = obtenerNulos(0);

        if (splitS.isEmpty()) {
            mypop.dismiss();
        } else {
            Toast.makeText(this, "¡No puedes dejar campos vacios!", Toast.LENGTH_SHORT).show();
        }

    }

    public void regresarMenu(View v) {
        Intent i = new Intent(this, Index.class);
        startActivity(i);
    }

    //CALIFICACIÓN POP
    public void onCalificar(View v) {
        try {

            String splitS = obtenerNulos(1);

            if (splitS.isEmpty()) {

                String cal = String.valueOf(iCon.calcular(iCon.optenerTemporal(), footer));
                if (!cal.equals("NaN")) {
                    txtCalificacion.setText(cal + "%");
                    popcalificacion.show();
                } else {
                    Toast.makeText(this, "No se puede dar una calificación, \n por favor diligencia el formulario", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No se puede dar una calificación, \n por favor completa el formulario", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.i("onCalificar", "" + ex.toString());
        }
    }

    public String obtenerNulos(int ubicacion) {
        ContenedorTab nuevo = iCon.optenerTemporal();
        Map<Integer, List<Long>> ArrMap = iCon.validarVacios(nuevo, footer);

        String encabezado = "";

        for (Map.Entry<Integer, List<Long>> entry : ArrMap.entrySet()) {
            if (entry.getKey() == ubicacion) {
                encabezado += entry.getValue().toString();
            }
        }

        String splitS = encabezado.replaceAll("[^\\dA-Za-z]", "");
        Log.i("Enviar_Array", "vacios = " + splitS);
        return splitS;
    }

    //SI OPRIME EL BOTON DE RETROCESO
    public void onBackPressed() {
        Intent i = new Intent(this, Index.class);
        startActivity(i);
    }

    public void onBackPressedform(View v) {
        Intent i = new Intent(this, Index.class);
        startActivity(i);
    }


    public void killChildrens(ContenedorTab temporal) {
        if (linearPrinc.getChildCount() > 0) {
            linearPrinc.removeAllViews();
            try {
                iCon.crearTemporal(contenedorEncabezado(temporal));
                CrearCuerpo();
                mypop.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void killChildrens2(ContenedorTab temporal) {
        if (linearPrinc.getChildCount() > 0 && linearBodypop.getChildCount() > 0) {
            linearPrinc.removeAllViews();
            linearBodypop.removeAllViews();

            try {
                contenedor = temporal;
                crearform();
                contenedor = contenedorLimipio();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //SUBIR DATOS DEL FORMULARIO
    public void UpData(View v) {
        ContenedorTab nuevo = iCon.optenerTemporal();
        Map<Integer, List<Long>> ArrMap = iCon.validarVacios(nuevo, footer);

        boolean full = true;
        String[] area = {"\nEncabezado: ", "\n Preguntas: ", "\n Pie: "};
        String vacios = "";

        try {

            for (Map.Entry<Integer, List<Long>> entry : ArrMap.entrySet()) {
                Log.i("Enviar_Array", "clave=" + entry.getKey() + ", valor=" + entry.getValue());
                if (entry.getValue().size() > 0) {
                    vacios += area[entry.getKey()] + entry.getValue().toString();
                    full = false;
                }
            }

            if (full) {

                iCon.insert(nuevo);
                cargarContador();
                inicial = false;

                if (iCon.enviar()) {
                    killChildrens(nuevo);
                    Toast.makeText(this, "Insertado con exito!" + vacios, Toast.LENGTH_LONG).show();
                } else {
                    killChildrens(nuevo);
                    Toast.makeText(this, "Agregado a local" + vacios, Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "¡tienes campos vacios! ", Toast.LENGTH_LONG).show();
                inicial = true;
                killChildrens2(nuevo);
            }

        } catch (Exception e) {

            Log.i("Enviar_error", e.toString());

        }
    }
}
