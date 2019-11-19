package com.example.procesos2;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.procesos2.Conexion.CheckedConexion;
import com.example.procesos2.Config.Util.JsonAdmin;
import com.example.procesos2.Model.iConteo;
import com.example.procesos2.Model.tab.procesosTap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    public String path = null;
    public String datosw1 = null;
    public String datosw2 = null;
    public String datosw3 = null;

    List<procesosTap> clc = new ArrayList<>();
    //DECLARANDO ELEMTOS XML A OBJETOS JAVA
    EditText campoCODIGO, campoBLOQUE, campoCAMA;
    TextView campoFECHA;
    Spinner SpinnerFINCA;
    //DECLARANDO ELEMNTOS DEL FRAGMENT QUESTION
    TextView conteo1, conteo2, conteo3, conteo5, conteo6, conteo7, conteo8, conteo11, conteo12, conteo13, conteo14;
    Button btnplus1, btnplus2, btnplus3,  btnplus5, btnplus6, btnplus7, btnplus8,  btnplus11, btnplus12, btnplus13, btnplus14;
    Button btnrest1, btnrest2, btnrest3, btnrest5, btnrest6, btnrest7, btnrest8,  btnrest11, btnrest12, btnrest13, btnrest14;
    Switch switch1, switch2, switch3;
    ScrollView scrollQuestion;
    CheckedConexion Cc = null;
    JsonAdmin Ja = null;
    iConteo iC = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//linea de codigo para dejar la actividad vertical
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        //DECLARANDO E INSTANCIANDO ELEMTOS XML A OBJETOS JAVA
        campoFECHA = findViewById(R.id.campoFECHA);
        SpinnerFINCA = findViewById(R.id.SpinnerFINCA);
        scrollQuestion = findViewById(R.id.scrollQuestion);
        campoCODIGO = findViewById(R.id.campoCODIGO);
        campoBLOQUE = findViewById(R.id.campoBLOQUE);
        campoCAMA = findViewById(R.id.campoCAMA);

        conteo1 = findViewById(R.id.conteo1);
        btnrest1 = findViewById(R.id.btnrest1);
        btnplus1 = findViewById(R.id.btnplus1);
        conteo2 = findViewById(R.id.conteo2);
        btnrest2 = findViewById(R.id.btnrest2);
        btnplus2 = findViewById(R.id.btnplus2);
        conteo3 = findViewById(R.id.conteo3);
        btnrest3 = findViewById(R.id.btnrest3);
        btnplus3 = findViewById(R.id.btnplus3);
        conteo5 = findViewById(R.id.conteo5);
        btnrest5 = findViewById(R.id.btnrest5);
        btnplus5 = findViewById(R.id.btnplus5);
        conteo6 = findViewById(R.id.conteo6);
        btnrest6 = findViewById(R.id.btnrest6);
        btnplus6 = findViewById(R.id.btnplus6);
        conteo7 = findViewById(R.id.conteo7);
        btnrest7 = findViewById(R.id.btnrest7);
        btnplus7 = findViewById(R.id.btnplus7);
        conteo8 = findViewById(R.id.conteo8);
        btnrest8 = findViewById(R.id.btnrest8);
        btnplus8 = findViewById(R.id.btnplus8);
        conteo11 = findViewById(R.id.conteo11);
        btnrest11 = findViewById(R.id.btnrest11);
        btnplus11 = findViewById(R.id.btnplus11);
        conteo12 = findViewById(R.id.conteo12);
        btnrest12 = findViewById(R.id.btnrest12);
        btnplus12 = findViewById(R.id.btnplus12);
        conteo13 = findViewById(R.id.conteo13);
        btnrest13 = findViewById(R.id.btnrest13);
        btnplus13 = findViewById(R.id.btnplus13);
        conteo14 = findViewById(R.id.conteo14);
        btnrest14 = findViewById(R.id.btnrest14);
        btnplus14 = findViewById(R.id.btnplus14);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);


        try {

            loadSpinner(); //llama al metodo que rellena los datos (FINCA) al spinner
            fragmentHeader();  //llama al metodo header del estilo XML
            getDate();  //obtine fecha actual
            IniciaConteos();  //inializa conteos
            CargaRecursos();  //realiza la instancia de lo que nesecita la app


            path = getExternalFilesDir(null) + File.separator;

            //comprueba la coneccion de red
            if (Cc.checkedConexionValidate(this)) {
                Toast.makeText(this, "TIENES CONEXION DE RED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "NO TIENES CONEXION", Toast.LENGTH_LONG).show();
            }


            //FUNCION DE LOS SWITCH
            switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        //Toast.makeText(getApplicationContext(), "SI", Toast.LENGTH_SHORT).show();
                        datosw1 = "SI";
                    } else if (!isChecked) {
                        //Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                        datosw1 = "NO";
                    }
                }
            });

            switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        //Toast.makeText(getApplicationContext(), "SI", Toast.LENGTH_SHORT).show();
                        datosw2 = "SI";
                    } else if (!isChecked) {
                        //Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                        datosw2 = "NO";
                    }
                }
            });

            switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        //Toast.makeText(getApplicationContext(), "SI", Toast.LENGTH_SHORT).show();
                        datosw3 = "SI";
                    } else if (!isChecked) {
                        //Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
                        datosw3 = "NO";
                    }
                }
            });

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Ocurrio al iniciar en OnCreate \n \n" + ex, Toast.LENGTH_LONG).show();
        }
    }

    //INICIALIZA LOS CONTEOS
    public void IniciaConteos() {
        conteo1.setText("0");
        conteo2.setText("0");
        conteo3.setText("0");
        conteo5.setText("0");
        conteo6.setText("0");
        conteo7.setText("0");
        conteo8.setText("0");
        conteo11.setText("0");
        conteo12.setText("0");
        conteo13.setText("0");
        conteo14.setText("0");

        campoCODIGO.setText("");
        campoBLOQUE.setText("");
        campoCAMA.setText("");
        SpinnerFINCA.setSelection(0);

        switch1.setChecked(false);
        switch2.setChecked(false);
        switch3.setChecked(false);

        scrollQuestion.fullScroll(ScrollView.FOCUS_UP);

        campoCODIGO.requestFocus();
    }

    //CARGA DE RECURSOS
    public void CargaRecursos() throws Exception {
        path = getExternalFilesDir(null) + File.separator;
        Cc = new CheckedConexion();
        Ja = new JsonAdmin();
        iC = new iConteo(path);

        iC.nombre = "procesos_2";
    }


    //METODO HEADER FRAGMENT (XML)
    public void fragmentHeader() {
        HeaderFragment headerFragment = new HeaderFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Header, headerFragment, null);
        fragmentTransaction.commit();
    }


    //METODO PARA OBTENER FECHA ACTUAL
    public void getDate() {
        String fecha;

        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        fecha = sdf.format(calendarDate.getTime());
        campoFECHA.setText(fecha);
    }

    //RELLENA EL SPINNER CON LAS FINCAS
    public void loadSpinner() {
        // asociar arreglo postcosecha al desplegable spinner

        String[] finca = {"Circasia", "Chuzaca e.u", "El respiro", "Las Margaritas", "La Esmeralda", "Marly", "Mercedes", "Normandia", "Palmas", "Rosas Col", "San Carlos", "San Mateo", "San Pedro", "Tinsuque", "Valentina", "Guacari", "Vista", "Wayuu suesca", "Wayuu guasca"};
        ArrayAdapter<String> cuadroArray = new ArrayAdapter<>(this, R.layout.spinner_item_personal, finca);
        SpinnerFINCA.setAdapter(cuadroArray);
    }

    //REALIZA LOS CONTEOS SEGUN LA PREGUNTA


    // Aumentar conteo de pregunta 1
    public void sumFen1(View v) {
        int n = Integer.parseInt(conteo1.getText().toString());
        if (n < 9) {
            conteo1.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 1
    public void resFen1(View v) {
        int n = Integer.parseInt(conteo1.getText().toString());
        if (n > 0) {
            conteo1.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de pregunta 2
    public void sumFen2(View v) {
        int n = Integer.parseInt(conteo2.getText().toString());
        if (n < 9) {
            conteo2.setText(valueOf(n + 1));
        }

    }

    // Disminuir conteo de pregunta 2
    public void resFen2(View v) {
        int n = Integer.parseInt(conteo2.getText().toString());
        if (n > 0) {
            conteo2.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de pregunta 3
    public void sumFen3(View v) {
        int n = Integer.parseInt(conteo3.getText().toString());
        if (n < 9) {
            conteo3.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 3
    public void resFen3(View v) {
        int n = Integer.parseInt(conteo3.getText().toString());
        if (n > 0) {
            conteo3.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de pregunta 5
    public void sumFen5(View v) {
        int n = Integer.parseInt(conteo5.getText().toString());
        if (n < 9) {
            conteo5.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 5
    public void resFen5(View v) {
        int n = Integer.parseInt(conteo5.getText().toString());
        if (n > 0) {
            conteo5.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de pregunta 6
    public void sumFen6(View v) {
        int n = Integer.parseInt(conteo6.getText().toString());
        if (n < 9) {
            conteo6.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 6
    public void resFen6(View v) {
        int n = Integer.parseInt(conteo6.getText().toString());
        if (n > 0) {
            conteo6.setText(valueOf(n - 1));

        }
    }


    // Aumentar conteo de pregunta 7
    public void sumFen7(View v) {
        int n = Integer.parseInt(conteo7.getText().toString());
        if (n < 9) {
            conteo7.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 7
    public void resFen7(View v) {
        int n = Integer.parseInt(conteo7.getText().toString());
        if (n > 0) {
            conteo7.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de pregunta 8
    public void sumFen8(View v) {
        int n = Integer.parseInt(conteo8.getText().toString());
        if (n < 9) {
            conteo8.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 8
    public void resFen8(View v) {
        int n = Integer.parseInt(conteo8.getText().toString());
        if (n > 0) {
            conteo8.setText(valueOf(n - 1));

        }
    }


    // Aumentar conteo de pregunta 11
    public void sumFen11(View v) {
        int n = Integer.parseInt(conteo11.getText().toString());
        if (n < 9) {
            conteo11.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 11
    public void resFen11(View v) {
        int n = Integer.parseInt(conteo11.getText().toString());
        if (n > 0) {
            conteo11.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de pregunta 12
    public void sumFen12(View v) {
        int n = Integer.parseInt(conteo12.getText().toString());
        if (n < 9) {
            conteo12.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 12
    public void resFen12(View v) {
        int n = Integer.parseInt(conteo12.getText().toString());
        if (n > 0) {
            conteo12.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de pregunta 13
    public void sumFen13(View v) {
        int n = Integer.parseInt(conteo13.getText().toString());
        if (n < 9) {
            conteo13.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 13
    public void resFen13(View v) {
        int n = Integer.parseInt(conteo13.getText().toString());
        if (n > 0) {
            conteo13.setText(valueOf(n - 1));

        }
    }

    // Aumentar conteo de pregunta 14
    public void sumFen14(View v) {
        int n = Integer.parseInt(conteo14.getText().toString());
        if (n < 9) {
            conteo14.setText(valueOf(n + 1));
        }
    }

    // Disminuir conteo de pregunta 14
    public void resFen14(View v) {
        int n = Integer.parseInt(conteo14.getText().toString());
        if (n > 0) {
            conteo14.setText(valueOf(n - 1));

        }
    }

    //BOTON PARA GUARDAR JSON
    public void registrar(View v) {
        try {

            if (campoCODIGO.getText().toString().equals("") || campoCAMA.getText().toString().equals("") || campoBLOQUE.getText().toString().equals("")) {
                Toast.makeText(this, "por favor verifica que los campos no esten vacios", Toast.LENGTH_SHORT).show();
            } else {

                if (datosw1 == null) {
                    datosw1 = "NO";
                }
                if (datosw2 == null) {
                    datosw2 = "NO";
                }
                if (datosw3 == null) {
                    datosw3 = "NO";
                }

                procesosTap p = new procesosTap();

                p.setFecha(campoFECHA.getText().toString());
                p.setCodigo(Integer.parseInt(campoCODIGO.getText().toString()));
                p.setBloque(Integer.parseInt(campoBLOQUE.getText().toString()));
                p.setCama(Integer.parseInt(campoCAMA.getText().toString()));
                p.setFinca(SpinnerFINCA.getSelectedItem().toString());
                p.setPr1(Integer.parseInt(conteo1.getText().toString()));
                p.setPr2(Integer.parseInt(conteo2.getText().toString()));
                p.setPr3(Integer.parseInt(conteo3.getText().toString()));
                p.setPr4(datosw1);
                p.setPr5(Integer.parseInt(conteo5.getText().toString()));
                p.setPr6(Integer.parseInt(conteo6.getText().toString()));
                p.setPr7(Integer.parseInt(conteo7.getText().toString()));
                p.setPr8(Integer.parseInt(conteo8.getText().toString()));
                p.setPr9(datosw2);
                p.setPr10(datosw3);
                p.setPr11(Integer.parseInt(conteo11.getText().toString()));
                p.setPr12(Integer.parseInt(conteo12.getText().toString()));
                p.setPr13(Integer.parseInt(conteo13.getText().toString()));
                p.setPr14(Integer.parseInt(conteo14.getText().toString()));
                p.setTerminal(getPhoneName());

                Toast.makeText(this, "" + iC.insert(p), Toast.LENGTH_SHORT).show();
                IniciaConteos();
            }

        } catch (Exception ex) {
            Toast.makeText(this, "btn insert Exception" + ex, Toast.LENGTH_SHORT).show();
        }
    }

    //BOTON SUBIR DATOS
    public void btn_subir(View v) {
        try {
            if (Cc.checkedConexionValidate(this)) {

                List<procesosTap> cl = iC.all();
                    for (procesosTap c : cl) {
                        Toast.makeText(this,""+ iC.record(c), Toast.LENGTH_SHORT).show();
                    }

                    if(cl.size()==0){
                            Toast.makeText(this, "Por el momento no hay registros guardados en el dispositivo", Toast.LENGTH_LONG).show();
                    }

            }else if(!Cc.checkedConexionValidate(this)){
                Toast.makeText(this,"No tienes conexion, \npor el momento guarda los datos en el dispositivo",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No tienes conexion, \npor el momento guarda los datos en el dispositivo", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception ex) {
            Toast.makeText(this, "exc btn_subir \n" + ex, Toast.LENGTH_SHORT).show();
        }

    }

    //OBTIENE EL NOMBRE DEL DISPOSITIVO POR MEDIO DEL BLUETHOOT
    public String getPhoneName() {

        String msj = "";
        try {

            //OBTIENE EL NOMBRE DEL BLUETHOOT
             BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
             String deviceName = myDevice.getName();
             return deviceName;

        }catch (Exception e){
            msj="Ex \n"+e;
            return msj;
        }
    }

}
