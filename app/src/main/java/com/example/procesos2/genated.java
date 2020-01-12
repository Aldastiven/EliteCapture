package com.example.procesos2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import com.example.procesos2.Conexion.CheckedConexion;
import com.example.procesos2.Model.iDesplegable;
import com.example.procesos2.Model.iDetalles;
import com.example.procesos2.Model.iRespuestas;
import com.example.procesos2.Model.tab.DesplegableTab;
import com.example.procesos2.Model.tab.DetallesTab;
import com.example.procesos2.Model.tab.RespuestasTab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
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

    TextView EncabTitulo;
    LinearLayout linearHeader;
    LinearLayout linearPrinc;
    LinearLayout LLprincipal;
    Button btnGuardar;
    ScrollView scrollForm;
    FloatingActionButton fBtn;


    String path = null;
    List<DetallesTab> iP = new ArrayList<>();

    private ProgressDialog progress;
    SharedPreferences sp = null;
    iRespuestas ir = null;
    iDesplegable id = null;

    int idres=0;

    ArrayList<String> al = new ArrayList<String>(200);
    ArrayList<String> cal = new ArrayList<String>(1000);

    CheckedConexion Cc = new CheckedConexion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genated);
        getSupportActionBar().hide();


        linearHeader = findViewById(R.id.LinearHeader);
        linearPrinc = findViewById(R.id.LinearCheck);
        btnGuardar = findViewById(R.id.guardar);
        EncabTitulo = findViewById(R.id.EncabTitulo);
        scrollForm = findViewById(R.id.scrollForm);
        fBtn = findViewById(R.id.fBtn);

        path = getExternalFilesDir(null)+ File.separator;
        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        try {
            cargarPlanos();
            String nom = sp.getString("nom_proceso", "");
            EncabTitulo.setText(nom);

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("dataedittext", "");
            edit.apply();


                iDetalles iD = new iDetalles(path);


                if(Cc.checkedConexionValidate(this)){
                    iD.nombre = "Detalles";
                    iD.local();
                    iP = iD.all();
                }else {
                    iD.nombre = "Detalles";
                    iP = iD.all();
                }

                CrearHeader();
                onckickBTNfloating();


        }catch (Exception ex){
                Toast.makeText(getApplicationContext(),"Error onCreate \n" +ex,Toast.LENGTH_SHORT).show();
        }

    }


    //VALIDACIÓN PARA LA CREACIÓN DINAMICA
    public void cargarPlanos(){
        path = getExternalFilesDir(null) + File.separator;
        try{
            ir = new iRespuestas(path);
            id = new iDesplegable(path);

            String nom = sp.getString("nom_proceso", "");
            ir.nombre = nom;

            String desp=id.group();


        }catch (Exception ex){
            Toast.makeText(this, "Error al cargar planos "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    public void CrearHeader(){

        try {

            for (DetallesTab d : iP) {
                int cod = sp.getInt("cod_proceso", 0);

                String tipo1 = "RS"; //BOTON DE CANTIDAD
                String tipo2 = "SWH"; //BOTON DE SI O NO
                String tipo3 = "TV"; //TEXTVIEW
                String tipo4 = "ETN"; //EDITTEXT NUMERICO
                String tipo5 = "ETA"; //EDITTEXT ALFANUMERICO
                String tipo6 = "CBX"; //SPINNER
                String tipo7 = "FIL"; //FLITROS JSON

                if (d.getTipoDetalle().equals(tipo3) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
                    Long idCons = d.getIdConsecutivo();
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    CrearTextViewFecha(idCons, modulo, id, pregunta);


                } else if (d.getTipoDetalle().equals(tipo4) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
                    Long idCons = d.getIdConsecutivo();
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    CrearEditTextNumeric(idCons,modulo, id, pregunta);


                } else if (d.getTipoDetalle().equals(tipo5) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
                    Long idCons = d.getIdConsecutivo();
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    CrearEditTextAlfanumeric(idCons, modulo, id, pregunta);

                } else if (d.getTipoDetalle().equals(tipo6) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
                    Long idCons = d.getIdConsecutivo();
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    String desplegable = d.getListaDesplegable();
                    Float porcentaje = d.getPorcentaje();
                    CrearSpinner(idCons,modulo, id, pregunta, desplegable, porcentaje);

                }else if (d.getTipoDetalle().equals(tipo7) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
                    Long idCons = d.getIdConsecutivo();
                    Toast.makeText(this,"id \n"+idCons,Toast.LENGTH_LONG).show();
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    String desplegable = d.getListaDesplegable();
                    Float porcen = d.getPorcentaje();

                    CrearFiltro(idCons,modulo, id, pregunta, desplegable);
                }
            }
        }catch (Exception exception){
            Toast.makeText(this, "exception en generated \n \n"+exception.toString(), Toast.LENGTH_SHORT).show();
        }

        CrearForm();
    }
    public void CrearForm(){

        scrollForm.fullScroll(View.FOCUS_UP);

        for (DetallesTab d : iP) {
            int cod = sp.getInt("cod_proceso", 0);

            String tipo1 = "RS"; //BOTON DE CANTIDAD
            String tipo2 = "SWH"; //BOTON DE SI O NO
            String tipo3 = "TV"; //TEXTVIEW
            String tipo4 = "ETN"; //EDITTEXT NUMERICO
            String tipo5 = "ETA"; //EDITTEXT ALFANUMERICO
            String tipo6 = "CBX"; //SPINNER


            if (d.getTipoDetalle().equals(tipo1) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                Float porce = d.getPorcentaje();
                CrearSumRes(idCons,modulo, id, pregunta, porce);

            } else if (d.getTipoDetalle().equals(tipo2) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                CrearSwicht(idCons,modulo, id, pregunta);

            } else if (d.getTipoDetalle().equals(tipo4) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                CrearEditTextNumericQ(idCons,modulo, id, pregunta);

            } else if (d.getTipoDetalle().equals(tipo5) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                CrearEditTextAlfaQ(idCons,modulo, id, pregunta);

            } else if (d.getTipoDetalle().equals(tipo6) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                String desplegable = d.getListaDesplegable();
                Float porcen = d.getPorcentaje();
                CrearEditTextSpinnerQ(idCons,modulo, id, pregunta, desplegable, porcen);

            }
        }
    }


    //METODOS PARA OBTENER DATOS

    public String getPhoneName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        return deviceName;
    }

    public String getFecha(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
        String fecha = sdf.format(cal.getTime());
        return fecha;
    }

    public String getnomProceso(){
        String nom = sp.getString("nom_proceso", "");
        return nom;
    }

    public int getcodProceso(){
        int idProceso = sp.getInt("cod_proceso", 0);
        return idProceso;
    }


    //ENCABEZADO

    public String CrearTextViewFecha(Long idcons, String modulo, Long id, String pregunta){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
        String fecha = sdf.format(cal.getTime());

        for(int i=0; i<1; i++){
            ArrayList<TVF> lista = new ArrayList<>();
            lista.add(new TVF(idcons, id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

            for(TVF tv: lista){

                LinearLayout.LayoutParams llparamsTXT1 =new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                llparamsTXT1.weight = 1;
                llparamsTXT1.setMargins(5,10,5,5);

                TextView tvp = new TextView(getApplicationContext());
                tvp.setId(id.intValue());
                tvp.setText(tv.pregunta);
                tvp.setTextSize(20);
                tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(llparamsTXT1);


                TextView tvr = new TextView(getApplicationContext());
                tvr.setId(id.intValue());
                tvr.setText(fecha);
                tvr.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvr.setTextSize(20);
                tvr.setTextColor(Color.parseColor("#979A9A"));
                tvr.setTypeface(null, Typeface.BOLD);
                tvr.setLayoutParams(llparamsTXT1);

                linearHeader.addView(CrearLinearLayoutHeader(tvp,tvr));

                String quest = String.valueOf(tvp.getId());
                String respuesta = tvr.getText().toString();

                al.add(idcons+"--"+modulo +"--"+quest+"--"+respuesta+"--0");

                String alData = idcons+"--"+modulo+"--"+quest+"--"+respuesta+"--0";
                al.set((tvp.getId())-1,alData);
            }
        }
        return null;
    }

    public void CrearEditTextNumeric(final Long idcons,final String modulo, final Long id, String pregunta){
        for(int i=0; i<1; i++){
            ArrayList<ET> lista = new ArrayList<>();
            lista.add(new ET(idcons ,id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

            for (ET et : lista){

                LinearLayout.LayoutParams llparams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparams.weight= 1;
                llparams.setMargins(5,10,5,5);

                final TextView tvp = new TextView(getApplicationContext());
                tvp.setId(id.intValue());
                tvp.setText(et.pregunta);
                tvp.setTextSize(20);
                tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(llparams);

                final EditText etxtN = new EditText(getApplicationContext());
                etxtN.setId(id.intValue());
                etxtN.setTextSize(20);
                etxtN.setHint("NULL");
                etxtN.setHintTextColor(Color.TRANSPARENT);
                etxtN.setRawInputType (Configuration.KEYBOARD_QWERTY);
                etxtN.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                etxtN.setTextColor(Color.parseColor("#515A5A"));
                etxtN.setBackgroundColor(Color.parseColor("#E5E7E9"));
                etxtN.setTypeface(null, Typeface.BOLD);
                etxtN.setLayoutParams(llparams);

                DesabilitarTeclado(etxtN);

                linearHeader.addView(CrearLinearLayoutHeader(tvp,etxtN));

                final String quest = String.valueOf(tvp.getId());
                final String resp = etxtN.getHint().toString();
                al.add(idcons+"--"+modulo +"--"+quest+"--"+resp+"--0");


                etxtN.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String alData = idcons+"--"+modulo+"--"+quest+"--"+charSequence+"--0";

                        al.set((tvp.getId())-1,alData);

                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("data", charSequence.toString());
                        edit.apply();

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }
        }
    }

    public void CrearEditTextAlfanumeric(final Long idcons, final String modulo, Long id, String pregunta){
        for(int i=0; i<1; i++){
            ArrayList<ET> lista = new ArrayList<>();
            lista.add(new ET(idcons,id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

            for (ET et : lista){

                LinearLayout.LayoutParams llparams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparams.weight=1;
                llparams.setMargins(5,10,5,5);

                final TextView tvp = new TextView(getApplicationContext());
                tvp.setId(id.intValue());
                tvp.setText(et.pregunta);
                tvp.setTextSize(20);
                tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(llparams);

                EditText etxtA = new EditText(getApplicationContext());
                etxtA.setId(id.intValue());
                etxtA.setTextSize(20);
                etxtA.setHint("NULL");
                etxtA.setHintTextColor(Color.TRANSPARENT);
                etxtA.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                etxtA.setTextColor(Color.parseColor("#515A5A"));
                etxtA.setBackgroundColor(Color.parseColor("#E5E7E9"));
                etxtA.setTypeface(null, Typeface.BOLD);
                etxtA.setLayoutParams(llparams);

                DesabilitarTeclado(etxtA);

                linearHeader.addView(CrearLinearLayoutHeader(tvp,etxtA));

                final String quest = String.valueOf(tvp.getId());
                String resp = etxtA.getHint().toString();
                al.add(idcons+"--"+modulo +"--"+quest+"--"+resp+"--0");

                etxtA.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        charSequence = " ";
                        String alData = idcons+"--"+modulo+"--"+quest+"--"+charSequence+"--0";
                        al.set((tvp.getId())-1,alData);
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String alData = idcons+"--"+modulo+"--"+quest+"--"+charSequence+"--0";

                        al.set((tvp.getId())-1,alData);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        }
    }

    public void CrearSpinner(final Long idcons, final String modulo, Long id, String pregunta, String desplegable, Float pocentaje){
        try {
            for (int i = 0; i < 1; i++) {
                ArrayList<SPINNER> list = new ArrayList<>();

                list.add(new SPINNER(idcons, id.intValue(), pregunta, iP.get(i).getTipoDetalle(),desplegable, pocentaje));


                for (SPINNER spi : list) {
                    LinearLayout.LayoutParams llparams = new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    llparams.weight = 1;
                    llparams.setMargins(5, 10, 5, 5);

                    final TextView tvp = new TextView(getApplicationContext());
                    tvp.setId(id.intValue());
                    tvp.setText(spi.pregunta);
                    tvp.setTextSize(20);
                    tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tvp.setTextColor(Color.parseColor("#979A9A"));
                    tvp.setTypeface(null, Typeface.BOLD);
                    tvp.setLayoutParams(llparams);

                    CheckedConexion Cc = new CheckedConexion();

                    iDesplegable iDES = new iDesplegable(path);
                    iDES.nombre = desplegable;

                    if (Cc.checkedConexionValidate(this)){
                        Toast.makeText(this,""+iDES.group(),Toast.LENGTH_LONG).show();
                    }
                    iDES.all();
                    ArrayList<String> OptionArray = new ArrayList<>(200);
                    OptionArray.add("selecciona");
                    for(DesplegableTab ds : iDES.all()){
                        if(ds.getFiltro().equals(spi.desplegable)){
                            OptionArray.add(ds.getOptions());
                        }else {}
                    }

                    final Spinner spinner = new Spinner(getApplicationContext());
                    spinner.setId(id.intValue());

                    ArrayAdapter<String> spinnerArray = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item_personal, OptionArray);
                    spinner.setAdapter(spinnerArray);
                    spinner.setLayoutParams(llparams);

                    linearHeader.addView(CrearLinearLayoutHeader(tvp, spinner));

                     spinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> spn, android.view.View v, int posicion, long id) {
                                try {
                                    String seleccionado = spn.getItemAtPosition(posicion).toString();


                                    if (!seleccionado.equals("Selecciona")) {
                                        int idd=(spinner.getId())-1;
                                        String quest = String.valueOf(spinner.getId());
                                        al.set(idd,idcons+"--"+modulo +"--"+quest+"--"+seleccionado+"--0");
                                    } else {
                                    }
                                }catch (Exception ex){
                                    Toast.makeText(genated.this, "Error "+ex.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            public void onNothingSelected(AdapterView<?> spn) {
                            }
                        });

                    String quest = String.valueOf(spinner.getId());
                    String respuesta = "Selecciona";
                    al.add(idcons+"--"+modulo +"--"+quest+"--"+respuesta+"--0");


                }
            }
        }catch (Exception ex){
            Toast.makeText(this,"Se genero un exception \n " +
                    "al crear el tipo de respuesta \n" +
                    "en el metodo SPINNER \n \n"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public LinearLayout CrearLinearLayoutHeader(View v1, View v2){
        LinearLayout.LayoutParams llparamsPrincipal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout LLprincipal = new LinearLayout(getApplicationContext());
        LLprincipal.setLayoutParams(llparamsPrincipal);
        LLprincipal.setBackgroundColor(Color.parseColor("#ffffff"));
        LLprincipal.setWeightSum(2);
        LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
        LLprincipal.setPadding(5,5,5,5);
        LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

        LLprincipal.addView(v1);
        LLprincipal.addView(v2);

        return LLprincipal;
    }

    public void CrearFiltro(final Long idcons, final String modulo, Long id, String pregunta , final String desplegable){
        try{


            for(int i = 0; i<1; i++){
                ArrayList<FILTRO> lista = new ArrayList<>();
                lista.add(new FILTRO(idcons, id.intValue(), pregunta, desplegable ));

                for(final FILTRO fl : lista){

                    final TextView tv = new TextView(getApplicationContext());
                    tv.setId(id.intValue());
                    tv.setText("Resultados :");
                    tv.setTextColor(Color.parseColor("#979A9A"));
                    tv.setPadding(5,5,5,5);
                    tv.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv.setTypeface(null, Typeface.BOLD);

                    LinearLayout.LayoutParams llparamsPrincipal = new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    LinearLayout LLprincipal = new LinearLayout(getApplicationContext());
                    LLprincipal.setLayoutParams(llparamsPrincipal);
                    LLprincipal.setWeightSum(2);
                    LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
                    LLprincipal.setPadding(5,5,5,5);
                    LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

                    LinearLayout.LayoutParams llparamsTXT1 =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    llparamsTXT1.weight =(float) 0.5;
                    llparamsTXT1.setMargins(5,10,5,5);

                    final EditText edt = new EditText(getApplicationContext());
                    edt.setHint(""+fl.pregunta);
                    edt.setHintTextColor(Color.parseColor("#626567"));
                    edt.setBackgroundColor(Color.parseColor("#ffffff"));
                    edt.setTextColor(Color.parseColor("#1C2833"));
                    edt.setLayoutParams(llparamsTXT1);
                    edt.setTypeface(null, Typeface.BOLD);
                    edt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    edt.setRawInputType (Configuration.KEYBOARD_QWERTY);

                    LinearLayout.LayoutParams llparamsBtn =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    llparamsBtn.weight =(float) 1.5;
                    llparamsBtn.setMargins(5,10,5,5);


                    final Button btn = new Button(getApplicationContext());
                    btn.setBackgroundColor(Color.TRANSPARENT);
                    btn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.lupa,0);
                    btn.setPadding(10,10,10,10);
                    btn.setLayoutParams(llparamsBtn);

                    LLprincipal.addView(edt);
                    LLprincipal.addView(btn);

                    linearHeader.addView(tv);
                    linearHeader.addView(LLprincipal);

                    final String quest = String.valueOf(tv.getId());
                    al.add(idcons+"--"+modulo +"--"+quest+"--"+"NULL"+"--0");

                    CheckedConexion Cc = new CheckedConexion();

                    final iDesplegable iDES = new iDesplegable(path);
                    iDES.nombre = desplegable;

                    if (Cc.checkedConexionValidate(this)){
                        Toast.makeText(this,""+iDES.traerDesplegable(desplegable),Toast.LENGTH_SHORT).show();
                    }
                    iDES.all();

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {

                                KeyDown(edt);

                                for (DesplegableTab ds : iDES.all()) {
                                    try {
                                        if (ds.getCod().equals(valueOf(edt.getText()))) {
                                            tv.setText("Resultado :    "+ds.getOptions());
                                            tv.setTextColor(Color.parseColor("#2ECC71"));


                                            String[] dato = tv.getText().toString().split(":");

                                            String alData = idcons+"--"+modulo+"--"+quest+"--"+dato[1].trim();
                                            al.set((tv.getId())-1,alData+"--0");

                                        } else {
                                            //Toast.makeText(genated.this, "no se encontraron resultados", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (Exception ex){
                                        Toast.makeText(genated.this, "Exception en resultado \n \n"+ex.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception ex){
                                Toast.makeText(genated.this, "Exeption al filtrar \n \n"+ex.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        }catch (Exception ex) {
            Toast.makeText(this, "Exception al crear el filtro \n \n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //BAJAR TECLADO

    public void KeyDown(EditText et){
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }


    //FORMULARIO

    private void CrearEditTextNumericQ(final Long idcons,final String modulo, Long id, String pregunta) {
        try {
            for(int i=0; i<1; i++){
                ArrayList<ET> lista = new ArrayList<>();
                lista.add(new ET(idcons, id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

                for(ET et : lista){

                    final  TextView tvp = new TextView(getApplicationContext());
                    tvp.setId(id.intValue());
                    tvp.setText(et.pregunta);
                    tvp.setTextColor(Color.parseColor("#979A9A"));
                    tvp.setPadding(5,5,5,5);
                    tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvp.setTypeface(null, Typeface.BOLD);

                    LinearLayout.LayoutParams llparamsTXT1 =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    llparamsTXT1.setMargins(30,10,30,10);

                    final  EditText edt = new EditText(getApplicationContext());
                    edt.setId(id.intValue());
                    edt.setTextSize(20);
                    edt.setHint("null");
                    edt.setHintTextColor(Color.TRANSPARENT);
                    edt.setRawInputType (Configuration.KEYBOARD_QWERTY);
                    edt.setTextColor(Color.parseColor("#515A5A"));
                    edt.setBackgroundColor(Color.parseColor("#E5E7E9"));
                    edt.setTypeface(null, Typeface.BOLD);
                    edt.setLayoutParams(llparamsTXT1);

                    DesabilitarTeclado(edt);

                    linearPrinc.addView(tvp);
                    linearPrinc.addView(edt);

                    final String quest = String.valueOf(tvp.getId());
                    String resp = edt.getHint().toString();
                    al.add(idcons +"--"+modulo +"--"+quest+"--"+resp);

                    edt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            charSequence = edt.getText().toString();
                            String alData =idcons+"--"+ modulo+"--"+quest+"--"+charSequence;
                            al.set((tvp.getId())-1,alData);
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String alData = idcons+"--"+modulo+"--"+quest+"--"+charSequence;

                            al.set((tvp.getId())-1,alData);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }
            }
        }catch (Exception ex){
            Toast.makeText(this, "Error al crear el EditTex en el formulario" + ex, Toast.LENGTH_LONG).show();
        }
    }

    private void CrearEditTextAlfaQ(final Long idcon, final String modulo, Long id, String pregunta) {
        try {
            for(int i=0; i<1; i++){
                ArrayList<ET> lista = new ArrayList<>();
                lista.add(new ET(idcon,id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

                for(ET et : lista){

                    final  TextView tvp = new TextView(getApplicationContext());
                    tvp.setId(id.intValue());
                    tvp.setText(et.pregunta);
                    tvp.setTextColor(Color.parseColor("#979A9A"));
                    tvp.setPadding(5,5,5,5);
                    tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvp.setTypeface(null, Typeface.BOLD);

                    LinearLayout.LayoutParams llparamsTXT1 =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    llparamsTXT1.setMargins(30,10,30,10);

                    final  EditText edt = new EditText(getApplicationContext());
                    edt.setId(id.intValue());
                    edt.setTextSize(20);
                    edt.setHint("null");
                    edt.setHintTextColor(Color.TRANSPARENT);
                    edt.setTextColor(Color.parseColor("#515A5A"));
                    edt.setBackgroundColor(Color.parseColor("#E5E7E9"));
                    edt.setTypeface(null, Typeface.BOLD);
                    edt.setLayoutParams(llparamsTXT1);

                    DesabilitarTeclado(edt);

                    linearPrinc.addView(tvp);
                    linearPrinc.addView(edt);

                    final String quest = String.valueOf(tvp.getId());
                    String resp = edt.getHint().toString();
                    al.add(idcon+"--"+modulo +"--"+quest+"--"+resp);

                    edt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            charSequence = edt.getText().toString();
                            String alData = idcon+"--"+modulo+"--"+quest+"--"+charSequence;
                            al.set((tvp.getId())-1,alData);
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String alData = idcon+"--"+modulo+"--"+quest+"--"+charSequence;

                            al.set((tvp.getId())-1,alData);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }
            }
        }catch (Exception ex){
            Toast.makeText(this, "Error al crear el EditTex en el formulario" + ex, Toast.LENGTH_LONG).show();
        }
    }

    private void CrearEditTextSpinnerQ(final Long idcon,  final String modulo, Long id, String pregunta ,String desplegable, Float porcen) {
        try {
            for (int i = 0; i < 1; i++) {
                ArrayList<SPINNER> list = new ArrayList<>();

                list.add(new SPINNER(idcon, id.intValue(), pregunta, iP.get(i).getTipoDetalle(), desplegable , porcen));

                for (final SPINNER spi : list) {
                    LinearLayout.LayoutParams llparams = new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    llparams.weight = 1;
                    llparams.setMargins(5, 10, 5, 5);

                    LinearLayout.LayoutParams llparamspregunta = new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    LinearLayout llpregunta = new LinearLayout(getApplicationContext());
                    llpregunta.setLayoutParams(llparamspregunta);
                    llpregunta.setWeightSum(3);
                    llpregunta.setOrientation(LinearLayout.HORIZONTAL);

                    LinearLayout.LayoutParams llparamsText =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    llparamsText.weight =(float) 2.3;

                    LinearLayout.LayoutParams llparamsTextpo =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    llparamsText.weight =(float) 0.7;

                    final TextView tvp = new TextView(getApplicationContext());
                    tvp.setId(id.intValue());
                    tvp.setText(spi.pregunta+"  ("+porcen+"%)");
                    tvp.setTextColor(Color.parseColor("#979A9A"));
                    tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvp.setPadding(10,10,10,10);
                    tvp.setTypeface(null, Typeface.BOLD);
                    tvp.setLayoutParams(llparamsText);

                    final TextView tvpor = new TextView(getApplicationContext());
                    tvpor.setId(idres++);
                    tvpor.setText("resultado: \n"+spi.porcentaje.toString());
                    tvpor.setTextColor(Color.parseColor("#979A9A"));
                    tvpor.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvpor.setPadding(10,10,10,10);
                    tvpor.setTypeface(null, Typeface.BOLD);
                    tvpor.setLayoutParams(llparamsTextpo);

                    //Toast.makeText(this, "spinner  "+tvpor.getId(), Toast.LENGTH_SHORT).show();

                    llpregunta.addView(tvp);
                    llpregunta.addView(tvpor);

                    CheckedConexion Cc = new CheckedConexion();

                    iDesplegable iDES = new iDesplegable(path);
                    iDES.nombre = desplegable;

                    if (Cc.checkedConexionValidate(this)){
                        iDES.traerDesplegable(desplegable);
                    }
                    iDES.all();
                    ArrayList<String> OptionArray = new ArrayList<>(200);
                    for(DesplegableTab ds : iDES.all()){
                        if(ds.getFiltro().equals(desplegable)){
                            OptionArray.add(ds.getOptions());
                        }else {}
                    }

                    final Spinner spinner = new Spinner(getApplicationContext());
                    spinner.setId(id.intValue());



                    ArrayAdapter<String> spinnerArray = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item_personal, OptionArray);
                    spinner.setAdapter(spinnerArray);
                    spinner.setSelection(1);
                    spinner.setLayoutParams(llparams);

                    linearPrinc.addView(llpregunta);
                    linearPrinc.addView(spinner);

                    if(spinner.getSelectedItem().toString().equals("N/A")){
                        tvpor.setText("resultado: \n");
                    }

                    spinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> spn, android.view.View v, int posicion, long id) {
                                    try {
                                        String seleccionado = spn.getItemAtPosition(posicion).toString();

                                            int idd=(spinner.getId())-1;
                                            String quest = String.valueOf(spinner.getId());

                                        if(seleccionado.equals("SI")){
                                            int valor = 0;
                                            tvpor.setText("resultado: \n"+OperacionPorcentaje(spi.porcentaje,valor));

                                            String porcen = tvpor.getText().toString();
                                            String[] ddd = porcen.split(":");

                                            al.set(idd,idcon+"--"+modulo +"--"+quest+"--"+seleccionado+"--"+ddd[1].trim());
                                            cal.set(tvpor.getId(),ddd[1].trim());

                                        }else if(seleccionado.equals("NO")){
                                            int valor = 9;
                                            tvpor.setText("resultado: \n"+OperacionPorcentaje(spi.porcentaje,valor));

                                            String porcen = tvpor.getText().toString();
                                            String[] ddd = porcen.split(":");

                                            al.set(idd,idcon+"--"+modulo +"--"+quest+"--"+seleccionado+"--"+ddd[1].trim());
                                            cal.set(tvpor.getId(),ddd[1].trim());

                                        }else if (seleccionado.equals("N/A")){
                                            tvpor.setText("resultado: \n");

                                            String porcen = tvpor.getText().toString();
                                            String[] ddd = porcen.split(":");

                                            al.set(idd,idcon+"--"+modulo +"--"+quest+"--"+seleccionado+"--0");
                                            cal.set(tvpor.getId(),"");
                                        }

                                    }catch (Exception ex){
                                        Toast.makeText(genated.this, "Error "+ex.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                                public void onNothingSelected(AdapterView<?> spn) {
                                }
                            });

                    String quest = String.valueOf(spinner.getId());
                    String respuesta = "Selecciona";
                    String porce = tvpor.getText().toString();
                    String[] ddd = porce.split(":");

                    al.add(idcon+"--"+modulo +"--"+quest+"--"+respuesta+"--0");
                    cal.add(ddd[1].trim());


                }
            }
        }catch (Exception ex){
            Toast.makeText(this,"Se genero un exception \n " +
                    "al crear el tipo de respuesta \n" +
                    "en el metodo SPINNER \n \n"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void CrearSumRes(final Long idcon, final String modulo, Long id, String pregunta, final Float porcen){
        try{
            for(int i=0; i<1; i++) {
                final ArrayList<buttonsSumRes> lista = new ArrayList<>();
                lista.add(new buttonsSumRes(idcon,id, pregunta, iP.get(i).getTipoDetalle(), porcen));

                    for (final buttonsSumRes btnsr : lista) {

                        LinearLayout.LayoutParams llparamspregunta = new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        LinearLayout llpregunta = new LinearLayout(getApplicationContext());
                        llpregunta.setLayoutParams(llparamspregunta);
                        llpregunta.setWeightSum(3);
                        llpregunta.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout.LayoutParams llparamsText =new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                        llparamsText.weight =(float) 2.3;

                        LinearLayout.LayoutParams llparamsTextpo =new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                        llparamsText.weight =(float) 0.7;

                        final TextView tvp = new TextView(getApplicationContext());
                        tvp.setId(id.intValue());
                        tvp.setText(btnsr.pregunta+"  ("+btnsr.porcen+"%)");
                        tvp.setTextColor(Color.parseColor("#979A9A"));
                        tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                        tvp.setPadding(10,10,10,10);
                        tvp.setTypeface(null, Typeface.BOLD);
                        tvp.setLayoutParams(llparamsText);

                        final TextView tvpor = new TextView(getApplicationContext());
                        tvpor.setId(idres++);
                        tvpor.setText("resultado: \n");
                        tvpor.setTextColor(Color.parseColor("#979A9A"));
                        tvpor.setBackgroundColor(Color.parseColor("#ffffff"));
                        tvpor.setPadding(10,10,10,10);
                        tvpor.setTypeface(null, Typeface.BOLD);
                        tvpor.setLayoutParams(llparamsTextpo);

                        //Toast.makeText(this, "sr "+tvpor.getId(), Toast.LENGTH_SHORT).show();

                        llpregunta.addView(tvp);
                        llpregunta.addView(tvpor);


                        LinearLayout.LayoutParams llparamsPrincipal = new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        LLprincipal = new LinearLayout(getApplicationContext());
                        LLprincipal.setLayoutParams(llparamsPrincipal);
                        LLprincipal.setWeightSum(3);
                        LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
                        LLprincipal.setPadding(5,5,5,5);
                        llparamsPrincipal.setMargins(0,0,0,25);


                        LinearLayout.LayoutParams llparamsTXT =new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        llparamsTXT.weight = 1;
                        llparamsTXT.setMargins(5,5,5,5);

                        final TextView tvr = new TextView(getApplicationContext());
                        tvr.setText("-1");
                        tvr.setId(id.intValue());
                        tvr.setTextSize(25);
                        tvr.setTextColor(Color.parseColor("#ffffff"));
                        tvr.setBackgroundColor(Color.parseColor("#CCD1D1"));
                        tvr.setTypeface(null, Typeface.BOLD);
                        tvr.setGravity(Gravity.CENTER);

                        tvr.setLayoutParams(llparamsTXT);

                        LinearLayout.LayoutParams llparamsBTN2 =new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        llparamsBTN2.weight = 1;
                        llparamsBTN2.setMargins(5,5,5,5);

                        Button btn2 = new Button(getApplicationContext());
                        btn2.setBackgroundColor(Color.parseColor("#F1948A"));
                        btn2.setText("-");
                        btn2.setTextSize(25);
                        btn2.setTypeface(null, Typeface.BOLD);
                        btn2.setLayoutParams(llparamsBTN2);
                        btn2.setId(id.intValue());

                        LinearLayout.LayoutParams llparamsBTN3 =new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                        llparamsBTN3.weight = 1;
                        llparamsBTN3.setMargins(5,5,5,5);

                        Button btn3 = new Button(getApplicationContext());
                        btn3.setBackgroundColor(Color.parseColor("#7DCEA0"));
                        btn3.setText("+");
                        tvr.setTypeface(null, Typeface.BOLD);
                        btn3.setTextSize(25);
                        btn3.setLayoutParams(llparamsBTN2);
                        btn3.setId(id.intValue());


                        LLprincipal.addView(tvr);
                        LLprincipal.addView(btn2);
                        LLprincipal.addView(btn3);

                        //agregando check dinamicos
                        linearPrinc.addView(llpregunta);
                        linearPrinc.addView(LLprincipal);

                     btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int n = Integer.parseInt(tvr.getText().toString());

                                if(n>-1){

                                    Long id = (long) tvp.getId();
                                    tvr.setText(valueOf(n - 1));
                                    String resultado = tvr.getText().toString();

                                    tvpor.setText("resultado: \n" + OperacionPorcentaje(btnsr.porcen, Integer.parseInt(resultado)));

                                    if(tvr.getText().equals("-1")){
                                        tvpor.setText("resultado: \n");
                                    }

                                    String porcen = tvpor.getText().toString();
                                    String [] ddd = porcen.split(":");
                                    String alData = idcon+"--"+modulo+"--"+id+"--"+resultado+"--"+ddd[1].trim();

                                    al.set((tvp.getId())-1,alData);
                                    cal.set(tvpor.getId(),ddd[1].trim());
                                }
                            }
                        });

                    btn3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int n = Integer.parseInt(tvr.getText().toString());
                            try {
                                if(n<9){
                                    Long id = (long) tvp.getId();
                                    tvr.setText(valueOf(n + 1));
                                    String resultado = tvr.getText().toString();


                                    tvpor.setText("resultado: \n" + OperacionPorcentaje(btnsr.porcen, Integer.parseInt(resultado)));

                                    String porcen = tvpor.getText().toString();
                                    String [] ddd = porcen.split(":");

                                    String alData = idcon+"--"+modulo+"--"+id+"--"+resultado+"--"+ddd[1].trim();
                                    al.set((tvp.getId())-1,alData);
                                    cal.set(tvpor.getId(),ddd[1].trim());
                                }
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                        String porce = tvpor.getText().toString();
                        String [] ddd = porce.split(":");
                        String quest = String.valueOf(tvp.getId());
                        String respuesta = tvr.getText().toString();
                        al.add(idcon+"--"+modulo +"--"+quest+"--"+respuesta+"--0");
                        cal.add(ddd[1]);
                }

            }
        }catch (Exception ex){
            Toast.makeText(this,"Se genero un exception \n " +
                                            "al crear el tipo de respuesta \n" +
                                            "en el metodo CrearSumRes \n \n"+ex,Toast.LENGTH_LONG).show();
        }
    }

    public void CrearSwicht(final Long idcon, final String modulo, Long id, String pregunta){
        try {
            for(int i=0; i<1; i++){
                ArrayList<SwichtQ> lista = new ArrayList<>();
                lista.add(new SwichtQ(idcon,id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

                for(SwichtQ sw : lista){

                    final TextView tvp = new TextView(getApplicationContext());
                    tvp.setId(id.intValue());
                    tvp.setText(sw.pregunta);
                    tvp.setTextColor(Color.parseColor("#979A9A"));
                    tvp.setPadding(5,5,5,5);
                    tvp.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvp.setTypeface(null, Typeface.BOLD);


                    LinearLayout.LayoutParams llparamsPrincipal = new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    LinearLayout LLprincipal = new LinearLayout(getApplicationContext());
                    LLprincipal.setLayoutParams(llparamsPrincipal);
                    LLprincipal.setWeightSum(2);
                    LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
                    LLprincipal.setPadding(5,5,5,5);
                    LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);


                    LinearLayout.LayoutParams llparamsTXT1 =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    llparamsTXT1.weight = 1;
                    llparamsTXT1.setMargins(5,10,5,5);

                    final TextView tvres = new TextView(getApplicationContext());
                    tvres.setText("NO");
                    tvres.setId(id.intValue());
                    tvres.setTextSize(30);
                    tvres.setTextColor(Color.parseColor("#ABB2B9"));
                    tvres.setTypeface(null, Typeface.BOLD);
                    tvres.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    tvres.setLayoutParams(llparamsTXT1);



                    LinearLayout.LayoutParams llparamsSWC =new
                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    llparamsSWC.weight = 1;
                    //llparamsSWC.setMargins(5,0,5,0)

                    final Switch  swit = new Switch(getApplicationContext());
                    swit.setHeight(100);
                    swit.setId(id.intValue());

                    swit.setLayoutParams(llparamsSWC);

                    LLprincipal.addView(tvres);
                    LLprincipal.addView(swit);

                    //agregando check dinamicos
                    linearPrinc.addView(tvp);
                    linearPrinc.addView(LLprincipal);

                    swit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(tvres.getText().toString().equals("SI")){
                                tvres.setText("NO");
                            }else {
                                tvres.setText("SI");
                            }
                            Long id = (long)tvp.getId();
                            String respuesta = tvres.getText().toString();

                            String alData = idcon+"--"+modulo+"--"+id+"--"+respuesta;

                            al.set((tvp.getId())-1,alData);
                        }
                    });

                    String quest = String.valueOf(tvp.getId());
                    String respuesta = tvres.getText().toString();
                    al.add(idcon+"--"+modulo +"--"+quest+"--"+respuesta);
                }
            }
        }catch (Exception ex){
            Toast.makeText(this,"Se genero un exception \n " +
                                            "al crear el tipo de respuesta \n" +
                                            "en el metodo CrearSwicht" +ex,Toast.LENGTH_LONG).show();
        }
     }




     //REGISTRO DE JSON

    public void registroJson(View v){
        try {

            for(String i : al){

                String cadena[] = i.split("--");
                RegisterRespuestas(Long.parseLong(cadena[0]),cadena[1],Long.parseLong(cadena[2]),cadena[3],cadena[4]);
                //Toast.makeText(this, ""+cadena[0]+"\n"+cadena[1]+"\n"+cadena[2]+"\n"+cadena[3], Toast.LENGTH_SHORT).show();
            }
            progressBar( "Guardando", "Se ha guardado exitosamente",50);
            eliminarHijos();
            CrearForm();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),"Error registro Json\n"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public  void RegisterRespuestas(Long idcon,String modulo,Long idDetalle,String respuesta,String porcentaje){
        try {
            RegisterFinal(idcon,modulo,idDetalle,respuesta,porcentaje);
        }catch (Exception ex){
            Toast.makeText(this, "Exception ResgisterRespuestas--generated.Java \n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void RegisterFinal(Long idcon,String modulo,Long idDetalle,String respuesta,String porcentaje){
        try {
            String nom = sp.getString("nom_proceso", "");
            ir.nombre = "Send " + nom;

            RespuestasTab rt = new RespuestasTab();
            rt.setFecha(getFecha());
            rt.setIdProceso((long) getcodProceso());
            rt.setIdcons(idcon);
            rt.setIdPregunta(idDetalle);
            rt.setRespuesta(respuesta);
            rt.setPorcentaje(Double.valueOf(porcentaje));
            rt.setTerminal(getPhoneName());
            ir.insert(rt);

        }catch (Exception ee){
            Toast.makeText(this, ""+ee, Toast.LENGTH_SHORT).show();
        }
    }

    public void Enviar(View v){
        try {
            iRespuestas irr = new iRespuestas(path);

            String nomres = sp.getString("nom_proceso", "");
            irr.nombre="Send "+nomres;


            List<RespuestasTab> rtt = irr.all();


            if(rtt.isEmpty()) {
                Toast.makeText(this, "No hay registros pendientes para enviar", Toast.LENGTH_LONG).show();
            }else {
                    if(Cc.checkedConexionValidate(this)) {
                        for (RespuestasTab rc : rtt) {
                                rc.setFecha(rc.getFecha());
                                rc.setIdProceso(rc.getIdProceso());
                                rc.setIdPregunta(rc.getIdPregunta());
                                rc.setRespuesta(rc.getRespuesta());
                                rc.setPorcentaje(rc.getPorcentaje());
                                rc.setTerminal(rc.getTerminal());

                                //irr.Record(rc);
                                //irr.delete(rc.getIdreg() - 1);
                        }
                        progressBar("Enviando...","Se envio exitosamente.",5);
                    }else {
                        Toast.makeText(this, "No tienes conexión ", Toast.LENGTH_SHORT).show();
                    }
            }
        }catch (Exception ex){
            Toast.makeText(this, "Exception al subir los datos \n"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void progressBar(String msgCarga, final String msgCompletado, final int tiempoSalteado){
        try {
            progress = new ProgressDialog(this,R.style.MyProgressDialogDespues);
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
                        public void run()
                        {
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

        }catch (Exception ex){
            Toast.makeText(this, ""+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Dialog(String msg){
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
        }catch (Exception ex){
            Toast.makeText(this, "Dialog"+ex, Toast.LENGTH_SHORT).show();
        }

    }

    //elimina los controles dentro del linear del formulario
    public void eliminarHijos(){
        if(linearPrinc.getChildCount() > 0){
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

    public float OperacionPorcentaje(float i , int r){

        DecimalFormatSymbols separador = new DecimalFormatSymbols();
        separador.setDecimalSeparator('.');

        DecimalFormat format = new DecimalFormat("#.##",separador);

        Float operacion = (i/9)*(9-r);

        return Float.valueOf(format.format(operacion));
    }

    private void onckickBTNfloating() {
        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Dialog(SumarCalificacion().toString()+"  /100");
                    Toast.makeText(genated.this,""+al.toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(genated.this, ""+SumarCalificacion(), Toast.LENGTH_SHORT).show();

                    //Dialog dd = new Dialog();
                    //dd.Dialog("hola mundo");
                }catch (Exception ex){
                    Toast.makeText(genated.this, "Exception \n " + ex, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Double SumarCalificacion(){
        double suma = 0;

            for (int i = 0; i < cal.size(); i++) {
                double valor = 0;
                try {
                    valor = Double.parseDouble(cal.get(i));
                }catch (Exception ex){
                    valor=0;
                }
                suma+=valor;
            }

        DecimalFormatSymbols separador = new DecimalFormatSymbols();
        separador.setDecimalSeparator('.');

        DecimalFormat format = new DecimalFormat("#.##",separador);
        return Double.parseDouble(format.format(suma));
    }

    //PARA VOLVER A LA ACTIVIDAD ANTERIOR(INDEX)
    public void onBackPressed() {
        Intent i = new Intent(this, Index.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    class buttonsSumRes{
        private Long idconsecutivo;
        private Long codDetalle;
        private String pregunta;
        private String tipoRespuesta;
        private  Float porcen;

        public buttonsSumRes(Long idconsecutivo, Long codDetalle, String pregunta, String tipoRespuesta, Float porcen) {
            this.idconsecutivo = idconsecutivo;
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
            this.porcen = porcen;
        }


    }
    class SwichtQ{
        private Long idconsecutivo;
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;

        public SwichtQ(Long idconsecutivo, int codDetalle, String pregunta, String tipoRespuesta) {
            this.idconsecutivo = idconsecutivo;
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
        }
    }
    class TVF{
        private Long idconsecutivo;
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;

        public TVF(Long idconsecutivo, int codDetalle, String pregunta, String tipoRespuesta) {
            this.idconsecutivo = idconsecutivo;
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
        }
    }
    class ET{
        private Long idconsecutivo;
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;

        public ET(Long idconsecutivo, int codDetalle, String pregunta, String tipoRespuesta) {
            this.idconsecutivo = idconsecutivo;
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
        }
    }
    class SPINNER{
        private Long idconsecutivo;
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;
        private String desplegable;
        private Float porcentaje;

        public SPINNER(Long idconsecutivo, int codDetalle, String pregunta, String tipoRespuesta, String desplegable, Float porcentaje) {
            this.idconsecutivo = idconsecutivo;
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
            this.desplegable = desplegable;
            this.porcentaje = porcentaje;
        }
    }
    class FILTRO{
        private Long idconsecutivo;
        private int codDetalle;
        private String pregunta;
        private String Desplagable;

        public FILTRO(Long idconsecutivo, int codDetalle, String pregunta, String desplagable) {
            this.idconsecutivo = idconsecutivo;
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            Desplagable = desplagable;
        }
    }
}
