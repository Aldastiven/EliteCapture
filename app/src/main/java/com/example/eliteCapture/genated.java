package com.example.eliteCapture;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.ControlViews.Cdesplegable;
import com.example.eliteCapture.Config.Util.ControlViews.Cetalf;
import com.example.eliteCapture.Config.Util.ControlViews.Cetnum;
import com.example.eliteCapture.Config.Util.ControlViews.CfilAuto;
import com.example.eliteCapture.Config.Util.ControlViews.Cfiltro;
import com.example.eliteCapture.Config.Util.ControlViews.Cscanner;
import com.example.eliteCapture.Config.Util.ControlViews.Ctextview;
import com.example.eliteCapture.Config.Util.ControlViews.Cconteos;
import com.example.eliteCapture.Config.Util.ControlViews.CradioButton;
import com.example.eliteCapture.Config.Util.Dialogo;
import com.example.eliteCapture.Model.Data.Admin;
import com.example.eliteCapture.Model.Data.Tab.ProcesoTab;
import com.example.eliteCapture.Model.View.Interfaz.Respuestas;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.Model.View.iRespuestas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

import static java.lang.String.valueOf;

public class genated extends AppCompatActivity {

    TextView EncabTitulo, contcc, scrollcomplete;
    LinearLayout linearBodypop, linearPrinc;
    ScrollView scrollForm;
    Dialog mypop, popcalificacion, popvalidar;
    Admin adm = null;
    iContenedor iCon = null;
    ContenedorTab contenedor = null;
    SharedPreferences sp = null;
    Button popSi, popNo;

    String path = null;

    int contConsec = 1;
    ProcesoTab pro = null;

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
            getcodProceso();//obtiene los datos del proceso
            iCon = new iContenedor(path);//intancia funcionalidad de la entidad contenedort
            contenedor = validarTemporal();//valida si hay datos temporales de los formularios


            if (temporal) {
                popvalidar.show();
            } else {
                crearform();
            }

            iCon.crearTemporal(contenedor);//crea el json temporal con los datos correspondientes

            EncabTitulo.setText(pro.getNombre_proceso());//asigna el nombre del encabezado

            contcc.setText("1");//inicializa el conteo de formularios guardados o enviados


        } catch (Exception ex) {
            Log.i("Error_onCreate", ex.toString());
        }

    }

    public void crearform() {
        try {
            CrearHeader(contenedor.getHeader());//crea los elemtos del header y pasa datos correspondientes
            CrearForm(contenedor.getQuestions());//crea los elemtos del body (formulario) y pasa datos correspondientes
            mypop.show();
        } catch (Exception ex) {
            Toast.makeText(this, "exc crearform" + ex.toString(), Toast.LENGTH_SHORT).show();
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
        ///editarEncab = findViewById(R.id.);
        linearBodypop = mypop.findViewById(R.id.linearbodypop);
        scrollcomplete = findViewById(R.id.complete);

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
                    return iCon.generarContenedor(getcodUsuario(), adm.getDetalles().forDetalle(pro.getCodigo_proceso()));
                }
            }

            return iCon.generarContenedor(getcodUsuario(), adm.getDetalles().forDetalle(pro.getCodigo_proceso()));
        } catch (Exception e) {
            Log.i("Contenedor_Error", e.toString());
            temporal = false;
        }
        return iCon.generarContenedor(getcodUsuario(), adm.getDetalles().forDetalle(pro.getCodigo_proceso()));
    }

    //REALIZA VALIDACION DEL POP (SI O NO EN FORMULARIO PENDIENTE)
    public void popSI(View v) {
        ok = true;
        try {
            crearform();
        } catch (Exception ex) {
            Log.i("Contenedor", "" + ex.toString());
        }
        popvalidar.dismiss();
    }

    public void popNo(View v) {
        ok = false;

        try {
            ContenedorTab conTemp = iCon.generarContenedor(getcodUsuario(), adm.getDetalles().forDetalle(pro.getCodigo_proceso()));
            contenedor = conTemp;
            crearform();
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
                        Cetnum cen = new Cetnum(genated.this ,path ,r.getId() , r.getPregunta(),"H", r);
                        linearBodypop.addView(cen.tnumerico());
                        break;
                    case "ETA":
                        Cetalf cal = new Cetalf(genated.this ,path ,r.getId() , r.getPregunta(),"H", r);
                        linearBodypop.addView(cal.talfanumerico());
                        break;
                    case "CBX":
                        Cdesplegable cd = new Cdesplegable(genated.this,path , r.getId(), r.getPregunta(), r.getDesplegable(),"H", r);
                        linearBodypop.addView(cd.desplegable());
                        break;
                    case "FIL":
                        Cfiltro cf = new Cfiltro(genated.this,path , r.getId(), r.getPregunta(), r.getDesplegable(),"H", r);
                        linearBodypop.addView(cf.filtro());
                        break;
                    case "SCA":
                        Cscanner cs = new Cscanner(genated.this, r.getId(), r.getPregunta());
                        linearBodypop.addView(cs.scanner());
                        break;
                    case "AUT":
                        CfilAuto ca = new CfilAuto(genated.this, r.getId(), r.getPregunta(), r.getDesplegable());
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
    public void CrearForm(List<RespuestasTab> questions) throws Exception {
        scrollForm.fullScroll(View.FOCUS_UP); //funcion que sube el scroll al inicio

        for (RespuestasTab r : questions) {

            Log.i("Error_Crs", "" + new iRespuestas().json(r));


            switch (r.getTipo()) {
                case "RS":
                    Cconteos cc = new Cconteos(genated.this, path, "Q", r);
                    linearPrinc.addView(cc.Cconteo());
                    break;
                case "RB":
                    CradioButton cb = new CradioButton(genated.this, path, "Q", r.getId(), r.getPregunta(), r.getPonderado(), r.getDesplegable(), r);
                    linearPrinc.addView(cb.Tradiobtn());
                    break;
            }
        }
    }


    //CALIFICACIÓN POP
    public void onCalificar(View v) {
        popcalificacion.show();
    }

    // metodo para extraer del SharedPreferences el id del proceso
    public void getcodProceso() {
        pro = new Gson().fromJson(sp.getString("proceso", ""), new TypeToken<ProcesoTab>() {
        }.getType());
        Log.i("ProContenedor:", sp.getString("proceso", ""));

    }

    // metodo para extraer del SharedPreferences el id del usuario
    public int getcodUsuario() {
        return sp.getInt("codigo", 0);
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
        mypop.dismiss();
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

    //trastear estos datos------------------------------------------------------------------------------
    //trastear estos datos------------------------------------------------------------------------------
    //trastear estos datos------------------------------------------------------------------------------
    //trastear estos datos------------------------------------------------------------------------------
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
}
