package com.example.eliteCapture;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
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

import com.example.eliteCapture.Config.Util.ControlViews.CconteosCheck;
import com.example.eliteCapture.Config.Util.ControlViews.CconteosEditar;
import com.example.eliteCapture.Config.Util.ControlViews.CdespelgableQ;
import com.example.eliteCapture.Config.Util.ControlViews.Cdesplegable;
import com.example.eliteCapture.Config.Util.ControlViews.Cetalf;
import com.example.eliteCapture.Config.Util.ControlViews.Cetnum;
import com.example.eliteCapture.Config.Util.ControlViews.CfilAuto;
import com.example.eliteCapture.Config.Util.ControlViews.Cfiltro;
import com.example.eliteCapture.Config.Util.ControlViews.Cscanner;
import com.example.eliteCapture.Config.Util.ControlViews.Ctextview;
import com.example.eliteCapture.Config.Util.ControlViews.Cconteos;
import com.example.eliteCapture.Config.Util.ControlViews.CradioButton;
import com.example.eliteCapture.Config.Util.formAdmin;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.Data.ionLine;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContador;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.Model.View.iRespuestas;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.example.eliteCapture.R.drawable.ic_cloud;
import static com.example.eliteCapture.R.drawable.ic_cloud_noti;
import static com.example.eliteCapture.R.drawable.ic_cloud_upload;
import static com.example.eliteCapture.R.drawable.ic_cloud_upload_white_18dp;
import static com.example.eliteCapture.R.drawable.ic_save;
import static com.example.eliteCapture.R.drawable.ic_star;
import static java.lang.String.valueOf;

public class genated extends AppCompatActivity {

    TextView EncabTitulo, contcc, scrollcomplete, txtCalificacion, btnEnvioGuardar, btnCalificar;
    LinearLayout linearBodypop, linearPrinc;
    ScrollView scrollForm, scrollCalificacion;
    Dialog mypop, popcalificacion, popvalidar, popregla;
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

    formAdmin formA;

    ionLine ion;


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
            popregla = new Dialog(this);

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
            ion = new ionLine(path);

            btnEnvioGuardar.setText(ion.all().equals("onLine") ?"Enviar":"Guardar");
            btnEnvioGuardar.setCompoundDrawablesWithIntrinsicBounds( 0, 0, ion.all().equals("onLine") ? ic_cloud_upload : ic_save, 0);
            btnCalificar.setCompoundDrawablesWithIntrinsicBounds( 0, 0, ic_star, 0);

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
            formA = new formAdmin(linearPrinc, linearBodypop,this, path, inicial, 0);
            CrearEncabezado();
            CrearCuerpo();
        } catch (Exception ex) {
            Toast.makeText(this, "exc crearform" + ex.toString(), Toast.LENGTH_SHORT).show();
            Log.i("exc crearform", ex.toString());
        }
    }

    public void CrearEncabezado() throws Exception {
        //CrearHeader(contenedor.getHeader());//crea los elemtos del header y pasa datos correspondientes
        formA.CrearHeader("H");
    }

    public void CrearCuerpo() throws Exception {
        /*CrearForm(contenedor.getQuestions(), "Q");//crea los elemtos del body (formulario) y pasa datos correspondientes
        if (footer) {
            CrearForm(contenedor.getFooter(), "F");//crea los elemtos del body (formulario) y pasa datos correspondientes
        }*/

        formA.CrearForm("Q");
        if(footer) {
            formA.CrearForm("F");
        }
    }

    //INSTANCIA CONTROLES VIEWS
    public void insView() {
        mypop.setContentView(R.layout.popupcumstom);
        popcalificacion.setContentView(R.layout.popupcalificacion);
        popvalidar.setContentView(R.layout.popupvalidar);
        popregla.setContentView(R.layout.popreglaconteos);
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
        btnEnvioGuardar = findViewById(R.id.btnEnvioGuardar);
        btnCalificar = findViewById(R.id.btnCalificar);

        popvalidar.setCancelable(false);
        mypop.setCancelable(false);

        Window window = mypop.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        window = popregla.getWindow();
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
                        linearBodypop.addView(new Cetnum(genated.this, path, "H", r, inicial).tnumerico());
                        break;
                    case "ETA":
                        linearBodypop.addView(new Cetalf(genated.this, path, "H", r, inicial).talfanumerico());
                        break;
                    case "CBX":
                        linearBodypop.addView(new Cdesplegable(genated.this, path, "H", r, inicial).desplegable());
                        break;
                    case "FIL":
                        linearBodypop.addView(new Cfiltro(genated.this, path, "H", r, inicial).filtro());
                        break;
                    case "SCA":
                        linearBodypop.addView(new Cscanner(genated.this, path, "H", r, inicial).scanner());
                        break;
                    case "AUT":
                        linearBodypop.addView(new CfilAuto(genated.this, path, "H", r, inicial).autocompletado());
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
                        linearPrinc.addView(new Cconteos(genated.this, path, ubicacion, r, inicial).Cconteo());
                        break;
                    case "RSE":
                        linearPrinc.addView(new CconteosEditar(genated.this, path, ubicacion, r, inicial, popregla).CconteoEditar());
                        break;
                    case "RSC":
                        linearPrinc.addView(new CconteosCheck(genated.this, path, ubicacion, r, inicial).Cconteo());
                        break;
                    case "RB":
                        linearPrinc.addView(new CradioButton(genated.this, path, ubicacion, r, inicial).Tradiobtn());
                        break;
                    case "CBX":
                        linearPrinc.addView(new CradioButton(genated.this, path, ubicacion, r, inicial).Tradiobtn());
                        break;
                    case "DES":
                        linearPrinc.addView(new CdespelgableQ(genated.this, r, path, inicial, ubicacion, linearPrinc, contenedor).Cdesp());
                        break;
                }
            }
        } catch (Exception ex) {
            Log.i("exc_crearForm", ex.toString());
        }
    }

    // metodo para extraer del SharedPreferences el id del proceso
    public void getcodProceso() {
        pro = new Gson().fromJson(sp.getString("proceso", ""), new TypeToken<ProcesoTab>() {}.getType());
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
        try {
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            String deviceName = myDevice.getName();
            return deviceName;
        } catch (Exception e) {
            return "Neron_Navarrete";
        }
    }

    //MUESTRA EL POP CON LOS CAMPOS DEL HEADER
    public void Showpop(View v) {
        mypop.show();
    }

    //OCULTA EL POP CON LOS CAMPOS DEL HEADER
    public void ocultarPop(View v) {

        String splitS = obtenerNulos(0);
        Log.i("vaciosDD", splitS);

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
                contenedor = contenedorLimipio();
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
                contador.update(usu.getId_usuario(), pro.getCodigo_proceso());
                cargarContador();

                inicial = false;
                killChildrens(nuevo);

                if(ion.all().equals("onLine")) {
                    if (iCon.enviar()) {
                        Toast.makeText(this, "Insertado con exito!" + vacios, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Agregado a local" + vacios, Toast.LENGTH_LONG).show();
                    }
                }else{
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
