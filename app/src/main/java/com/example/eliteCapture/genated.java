package com.example.eliteCapture;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eliteCapture.Config.Util.Controls.JSO;
import com.example.eliteCapture.Config.Util.formAdmin;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.Data.Tab.UsuarioTab;
import com.example.eliteCapture.Model.Data.ionLine;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContador;
import com.example.eliteCapture.Model.View.iContenedor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.eliteCapture.R.drawable.ic_cloud_upload;
import static com.example.eliteCapture.R.drawable.ic_save;
import static com.example.eliteCapture.R.drawable.ic_star;

@RequiresApi(api = Build.VERSION_CODES.O)
public class genated extends AppCompatActivity {

    TextView EncabTitulo, contcc, scrollcomplete, txtCalificacion, btnEnvioGuardar, btnCalificar;
    LinearLayout linearBodypop, linearPrinc;
    ScrollView scrollForm, scrollCalificacion;
    Dialog mypop, popcalificacion, popvalidar, popregla, itemLayout;
    Button popSi, popNo;

    ProcesoTab pro = null;
    UsuarioTab usu = null;
    ContenedorTab contenedor = null;

    iContenedor iCon = null;
    iContador contador = null;
    ionLine ion;

    String path = null, dataCamera;
    int contConsec = 0;
    boolean camera, inicial = true , ok, temporal, footer, JSO = false;

    formAdmin formA;
    Admin adm = null;
    SharedPreferences sp = null;

    RespuestasTab rt = null;

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
            itemLayout = new Dialog(this);

            insView(); //instancia los elementos del layout

            //Toast.makeText(this, "MAC : "+getMacAddress(), Toast.LENGTH_SHORT).show();


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
                String ubicacion = bundle.getString("ubicacion");
                rt = (RespuestasTab) getIntent().getSerializableExtra("respuestaTab");
                ok = true;
                crearform();

                if(ubicacion.equals("H"))mypop.show();
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

    @Override protected void onResume() {
        super.onResume();
        Log.i("cycleLife", "onResume");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            camera = bundle.getBoolean("camera");
            rt = (RespuestasTab) getIntent().getSerializableExtra("respuestaTab");
        }

        if(rt != null && rt.getTipo().equals("JSO")){
            Log.i("cycleLife", "llego respuesta data : "+rt.getTipo());
            JSO = true;
        }
        crearform();
    }

    public void cargarContador() {
        EncabTitulo.setText(" "+pro.getNombre_proceso());//asigna el nombre del encabezado
        contConsec = contador.getCantidad(usu.getId_usuario(), pro.getCodigo_proceso()) + 1;
        contcc.setText(String.valueOf(contConsec));//inicializa el conteo de formularios guardados o enviados
        int regla = (pro.getPersonalizado5() != null) ? Integer.parseInt(pro.getPersonalizado5()) : 0;
        validarFooter(contConsec, regla);
    }

    public void validarFooter(int contador, int regla) {
        try {
            footer = (regla != 0 && (contador % regla) == 0);
        } catch (NumberFormatException en) {
            footer = false;
        }
    }

    public void crearform() {
        try {
            Log.i("cycleLife", "llego a crear formulario");
            formA = new formAdmin(linearPrinc, linearBodypop,this, this, path, inicial, 0, 0, JSO);
            CrearEncabezado();
            CrearCuerpo();
        } catch (Exception ex) {
            Toast.makeText(this, "exc crearform" + ex.toString(), Toast.LENGTH_SHORT).show();
            Log.i("exc crearform", ex.toString());
        }
    }


    public void CrearEncabezado() {
        linearBodypop.removeAllViews();
        formA.CrearForm("H");
    }

    public void CrearCuerpo(){
        scrollForm.fullScroll(0);
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
            Log.i("Contenedor_ErrorData", e.toString());
            temporal = false;
        }
        return contenedorLimipio();
    }

    public ContenedorTab contenedorLimipio() throws Exception {
        return iCon.generarContenedor(
                usu.getId_usuario(),
                getMacAddress(),
                adm.getDetalles()
                        .forDetalle(pro.getCodigo_proceso()));
    }

    public ContenedorTab contenedorEncabezado(ContenedorTab temporal) {
        return new ContenedorTab(
                pro.getCodigo_proceso(),
                contador.getCantidad(temporal.getIdUsuario(), temporal.getIdProceso()),
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
            inicial = true;
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
            inicial = true;
            contenedor = contenedorLimipio();
            iCon.crearTemporal(contenedor);
            crearform();
            mypop.show();
        } catch (Exception ex) {
        }
        popvalidar.dismiss();
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
            return myDevice.getName();
        } catch (Exception e) {
            return "Neron_Navarrete";
        }
    }

    public String getMacAddress() {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
            }
            return "02:00:00:00:00:00";
    }

    //MUESTRA EL POP CON LOS CAMPOS DEL HEADER
    public void Showpop(View v) {
        mypop.show();
    }

    //OCULTA EL POP CON LOS CAMPOS DEL HEADER
    public void ocultarPop(View v) {
        mypop.getWindow().getDecorView().clearFocus();//limpia el foco de la ventana
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
            linearBodypop.removeAllViews();
            try {
                contenedor = contenedorLimipio();
                iCon.crearTemporal(contenedorEncabezado(temporal));
                crearform();
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
        try {
        ContenedorTab nuevo = iCon.optenerTemporal();

        boolean full = true;
            for (Map.Entry<Integer, List<Long>> entry : iCon.validarVacios(nuevo, footer).entrySet()) {
                if (entry.getValue().size() > 0) {
                    full = false;
                }
            }

            nuevo.setConsecutivo(contConsec);

            if (full) {
                boolean envio = iCon.enviarInmediato2(nuevo, contConsec);

                Toast.makeText(this,
                        !ion.all().equals("onLine") || !envio ? "Agregado a local" : "Insertado con exito!", Toast.LENGTH_LONG).show();

                inicial = true;

                if(ion.all().equals("offLine")) {
                    nuevo.setEstado(0);
                    iCon.insert(nuevo);
                }

                killChildrens(nuevo);

                contador.update(usu.getId_usuario(), pro.getCodigo_proceso());
                cargarContador();
            } else {
                inicial = false;
                Toast.makeText(this, "¡tienes campos vacios! ", Toast.LENGTH_LONG).show();
                killChildrens2(nuevo);
            }

        } catch (Exception e) {
            Log.i("Enviar_error", e.toString());
        }
    }
}
