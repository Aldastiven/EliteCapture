package com.example.procesos2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.example.procesos2.Config.sqlConect;
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
import java.util.Iterator;
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
    iDetalles iD = null;

    public Conexion con = null;

    int idres=0;
    int cont = 0;


    public boolean Temporal;


    ArrayList<String> al = new ArrayList<String>(200);
    ArrayList<String> res = new ArrayList<String>(200);
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


        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        try {

            path = getExternalFilesDir(null)+ File.separator;
            con = new Conexion(path, getApplicationContext());

            if(con.getPath(path)){
                Log.i("resultado con g ",""+con.getPath(path));
                iD = new iDetalles(con.getConexion(),path);
                ir = new iRespuestas(con.getConexion(),path);
                id = new iDesplegable(con.getConexion(),path);

                iP = iD.all();
            }else {
                Log.i("resultado con x",""+con.getPath(path));
                iD = new iDetalles(null,path);
                ir = new iRespuestas(null,path);
                id = new iDesplegable(null,path);

                iP = iD.all();
            }


            String nom = sp.getString("nom_proceso", "");
            EncabTitulo.setText(nom);

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("dataedittext", "");
            edit.apply();



            CrearHeader();
            onckickBTNfloating();
            crearJsonRes();
            contarNull();
            //validarNull();
        }catch (Exception ex){
               Log.i("Error onCreate", ex.toString());
        }

    }

    @Override protected void onResume() {
        super.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        getState();
        Log.i("Temporal ", String.valueOf(Temporal));

        if (Temporal){
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("Temporal", Temporal);
            edit.commit();
            edit.apply();
        }else{
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("Temporal", Temporal);
            edit.commit();
            edit.apply();
        }
    }

    @Override protected void onRestart(){
        super.onRestart();
        //Toast.makeText(this,"Restart",Toast.LENGTH_SHORT).show();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"onDestroy generated",Toast.LENGTH_SHORT).show();
        Temporal = false;

        if (Temporal){
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("Temporal", Temporal);
            edit.commit();
            edit.apply();
        }else{
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
        try{
            ir.all();
            ir.local();
        }catch (Exception ex){
            ir.local();
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
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    CrearTextViewFecha(modulo, id, pregunta);


                } else if (d.getTipoDetalle().equals(tipo4) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    CrearEditTextNumeric(modulo, id, pregunta);


                } else if (d.getTipoDetalle().equals(tipo5) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    CrearEditTextAlfanumeric(modulo, id, pregunta);

                } else if (d.getTipoDetalle().equals(tipo6) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {
                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    String desplegable = d.getListaDesplegable();
                    Float porcentaje = d.getPorcentaje();
                    CrearSpinner(modulo, id, pregunta, desplegable, porcentaje);

                }else if (d.getTipoDetalle().equals(tipo7) && d.getIdProceso() == cod && d.getTipoModulo().equals("H")) {

                    Long id = d.getCodDetalle();
                    String pregunta = d.getQuesDetalle();
                    String modulo = d.getTipoModulo();
                    String desplegable = d.getListaDesplegable();
                    Float porcen = d.getPorcentaje();

                    CrearFiltro(modulo, id, pregunta, desplegable);
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
                CrearSumRes(modulo, id, pregunta, porce);

            } else if (d.getTipoDetalle().equals(tipo2) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                CrearSwicht(modulo, id, pregunta);

            } else if (d.getTipoDetalle().equals(tipo4) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                CrearEditTextNumericQ(modulo, id, pregunta);

            } else if (d.getTipoDetalle().equals(tipo5) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                CrearEditTextAlfaQ(modulo, id, pregunta);

            } else if (d.getTipoDetalle().equals(tipo6) && d.getIdProceso() == cod && d.getTipoModulo().equals("Q")) {
                Long idCons = d.getIdConsecutivo();
                Long id = d.getCodDetalle();
                String pregunta = d.getQuesDetalle();
                String modulo = d.getTipoModulo();
                String desplegable = d.getListaDesplegable();
                Float porcen = d.getPorcentaje();
                CrearEditTextSpinnerQ(modulo, id, pregunta, desplegable, porcen);

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
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyy HH:mm:ss");
        String fecha = sdf.format(cal.getTime());
        return fecha;
    }

    public int getcodProceso(){
        int idProceso = sp.getInt("cod_proceso", 0);
        return idProceso;
    }


    //ENCABEZADO

    public String CrearTextViewFecha(String modulo, Long id, String pregunta){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
        String fecha = sdf.format(cal.getTime());

        for(int i=0; i<1; i++){
            ArrayList<TVF> lista = new ArrayList<>();
            lista.add(new TVF(id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

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
                String respuesta = "NULL";

                al.add(modulo +"--"+quest+"--"+respuesta+"--0");

                String alData = modulo+"--"+quest+"--"+respuesta+"--0";
                al.set((tvp.getId())-1,alData);
            }
        }
        return null;
    }

    public void CrearEditTextNumeric(final String modulo, final Long id, String pregunta){
        for(int i=0; i<1; i++){
            ArrayList<ET> lista = new ArrayList<>();
            lista.add(new ET(id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

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
                al.add(modulo +"--"+quest+"--"+"NULL"+"--0");


                etxtN.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String alData = modulo+"--"+quest+"--"+charSequence+"--0";

                        al.set((tvp.getId())-1,alData);

                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("data", charSequence.toString());
                        edit.apply();

                        saveState();

                        if(charSequence.equals("")){
                            //desabilitarBTN();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }
        }
    }

    public void CrearEditTextAlfanumeric(final String modulo, Long id, String pregunta){
        for(int i=0; i<1; i++){
            ArrayList<ET> lista = new ArrayList<>();
            lista.add(new ET(id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

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
                al.add(modulo +"--"+quest+"--"+"NULL"+"--0");

                etxtA.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        charSequence = " ";
                        String alData = modulo+"--"+quest+"--"+charSequence+"--0";
                        al.set((tvp.getId())-1,alData);
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String alData = modulo+"--"+quest+"--"+charSequence+"--0";

                        al.set((tvp.getId())-1,alData);
                        saveState();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        }
    }

    public void CrearSpinner(final String modulo, Long id, String pregunta, String desplegable, Float pocentaje){
        try {
            for (int i = 0; i < 1; i++) {
                ArrayList<SPINNER> list = new ArrayList<>();

                list.add(new SPINNER( id.intValue(), pregunta, iP.get(i).getTipoDetalle(),desplegable, pocentaje));


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

                    Conexion cn = new Conexion(path,this);

                    iDesplegable iDES = new iDesplegable(null,path);
                    iDES.nombre = desplegable;


                    iDES.all();
                    ArrayList<String> OptionArray = new ArrayList<>(200);

                    OptionArray.add("selecciona");

                    for(DesplegableTab ds : iDES.all()){
                        if(ds.getFiltro().equals(desplegable)){
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


                                    if (!seleccionado.equals("selecciona")) {
                                        int idd=(spinner.getId())-1;
                                        String quest = String.valueOf(spinner.getId());
                                        al.set(idd,modulo +"--"+spinner.getId()+"--"+seleccionado+"--0");

                                        saveState();
                                    } else {
                                        int idd=(spinner.getId())-1;
                                        String quest = String.valueOf(spinner.getId());
                                        al.set(idd,modulo +"--"+spinner.getId()+"--NULL--0");
                                    }
                                }catch (Exception ex){
                                    Toast.makeText(genated.this, "Error "+ex.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            public void onNothingSelected(AdapterView<?> spn) {
                            }
                        });

                    String quest = String.valueOf(spinner.getId());
                    String respuesta = "NULL";
                    al.add(modulo +"--"+spinner.getId()+"--"+respuesta+"--0");
                }
            }
        }catch (Exception ex){
            /*Toast.makeText(this,"Se genero un exception \n " +
                    "al crear el tipo de respuesta \n" +
                    "en el metodo SPINNER \n \n"+ex.toString(),Toast.LENGTH_LONG).show();*/
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

    public void CrearFiltro(final String modulo, Long id, String pregunta , final String desplegable){
        try{

            for(int i = 0; i<1; i++){
                ArrayList<FILTRO> lista = new ArrayList<>();
                lista.add(new FILTRO(id.intValue(), pregunta, desplegable ));

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
                    al.add(modulo +"--"+quest+"--"+"NULL"+"--0");


                    final iDesplegable iDES = new iDesplegable(con.getConexion(),path);
                    iDES.nombre = desplegable;


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

                                            String alData = modulo+"--"+quest+"--"+dato[1].trim();
                                            al.set((tv.getId())-1,alData+"--0");

                                            saveState();

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

    private void CrearEditTextNumericQ(final String modulo, Long id, String pregunta) {
        try {
            for(int i=0; i<1; i++){
                ArrayList<ET> lista = new ArrayList<>();
                lista.add(new ET(id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

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
                    al.add(modulo +"--"+quest+"--"+"NULL");

                    edt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            charSequence = edt.getText().toString();
                            String alData =modulo+"--"+quest+"--"+charSequence;
                            al.set((tvp.getId())-1,alData);
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String alData = modulo+"--"+quest+"--"+charSequence;
                            al.set((tvp.getId())-1,alData);
                            saveState();
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

    private void CrearEditTextAlfaQ( final String modulo, Long id, String pregunta) {
        try {
            for(int i=0; i<1; i++){
                ArrayList<ET> lista = new ArrayList<>();
                lista.add(new ET(id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

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
                    al.add(modulo +"--"+quest+"--"+"NULL");

                    edt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            charSequence = edt.getText().toString();
                            String alData = modulo+"--"+quest+"--"+charSequence;
                            al.set((tvp.getId())-1,alData);
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            String alData = modulo+"--"+quest+"--"+charSequence;

                            al.set((tvp.getId())-1,alData);
                            saveState();
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

    private void CrearEditTextSpinnerQ( final String modulo, Long id, String pregunta ,String desplegable, Float porcen) {
        try {
            for (int i = 0; i < 1; i++) {
                ArrayList<SPINNER> list = new ArrayList<>();

                list.add(new SPINNER(id.intValue(), pregunta, iP.get(i).getTipoDetalle(), desplegable , porcen));

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

                    iDesplegable iDES = new iDesplegable(null,path);
                    iDES.nombre = desplegable;

                    iDES.all();
                    ArrayList<String> OptionArray = new ArrayList<>(200);

                    OptionArray.add("selecciona");

                    for(DesplegableTab ds : iDES.all()){
                        if(ds.getFiltro().equals(desplegable)){
                            OptionArray.add(ds.getOptions());
                        }else {}
                    }

                    final Spinner spinner = new Spinner(getApplicationContext());
                    spinner.setId(id.intValue());



                    ArrayAdapter<String> spinnerArray = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item_personal, OptionArray);
                    spinner.setAdapter(spinnerArray);
                    spinner.setSelection(0);
                    spinner.setLayoutParams(llparams);

                    linearPrinc.addView(llpregunta);
                    linearPrinc.addView(spinner);

                    if(spinner.getSelectedItem().toString().equals("selecciona")){
                        tvpor.setText("resultado: \n");
                    }

                    spinner.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> spn, android.view.View v, int posicion, long id) {
                                    try {
                                        String seleccionado = spn.getItemAtPosition(posicion).toString();
                                        String [] Afirmacion = seleccionado.split(" ");


                                            int idd=(spinner.getId())-1;
                                            String quest = String.valueOf(spinner.getId());

                                        if(Afirmacion[0].equals("SI")){
                                            int valor = 0;
                                            tvpor.setText("resultado: \n"+OperacionPorcentaje(spi.porcentaje,valor));

                                            String porcen = tvpor.getText().toString();


                                            String [] ddd = porcen.split(":");

                                            al.set(idd,modulo +"--"+quest+"--"+Afirmacion[0]+"--"+ddd[1].trim());
                                            cal.set(tvpor.getId(),ddd[1].trim());

                                            saveState();

                                        }else if(Afirmacion[0].equals("NO")){
                                            int valor = 9;
                                            tvpor.setText("resultado: \n"+OperacionPorcentaje(spi.porcentaje,valor));

                                            String porcen = tvpor.getText().toString();
                                            String[] ddd = porcen.split(":");

                                            al.set(idd,modulo +"--"+quest+"--"+Afirmacion[0]+"--"+ddd[1].trim());
                                            cal.set(tvpor.getId(),ddd[1].trim());

                                            saveState();

                                        }else if (seleccionado.equals("N/A")){
                                            tvpor.setText("resultado: \n");

                                            String porcen = tvpor.getText().toString();
                                            String[] ddd = porcen.split(":");

                                            al.set(idd,modulo +"--"+quest+"--"+"NULL"+"--0");
                                            cal.set(tvpor.getId(),"");
                                        }
                                        else if (seleccionado.equals("selecciona")){
                                        tvpor.setText("resultado: \n");

                                        String porcen = tvpor.getText().toString();
                                        String[] ddd = porcen.split(":");

                                        al.set(idd,modulo +"--"+quest+"--"+"NULL"+"--0");
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
                    String respuesta = "NULL";
                    String porce = tvpor.getText().toString();
                    String[] ddd = porce.split(":");

                    al.add(modulo +"--"+quest+"--"+"NULL"+"--0");
                    cal.add(ddd[1].trim());
                }
            }
        }catch (Exception ex){
            /*Toast.makeText(this,"Se genero un exception \n " +
                    "al crear el tipo de respuesta \n" +
                    "en el metodo SPINNER \n \n"+ex.toString(),Toast.LENGTH_LONG).show();*/
        }
    }

    public void CrearSumRes(final String modulo, Long id, String pregunta, final Float porcen){
        try{
            for(int i=0; i<1; i++) {
                final ArrayList<buttonsSumRes> lista = new ArrayList<>();
                lista.add(new buttonsSumRes(id, pregunta, iP.get(i).getTipoDetalle(), porcen));

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

                                        String porcen = tvpor.getText().toString();
                                        String [] ddd = porcen.split(":");
                                        String alData = modulo+"--"+id+"--"+resultado+"--"+ddd[1].trim();

                                        al.set((tvp.getId())-1,alData);
                                        cal.set(tvpor.getId(),ddd[1].trim());

                                        saveState();

                                    }

                                    String porcen = tvpor.getText().toString();
                                    String [] ddd = porcen.split(":");
                                    String alData = modulo+"--"+id+"--"+resultado+"--"+ddd[1].trim();

                                    al.set((tvp.getId())-1,alData);
                                    cal.set(tvpor.getId(),ddd[1].trim());

                                    saveState();
                                }
                                else if(n==-1){
                                    Long id = (long) tvp.getId();
                                    String porcen = tvpor.getText().toString();
                                    String [] ddd = porcen.split(":");
                                    String alData = modulo+"--"+id+"--"+"NULL"+"--"+ddd[1].trim();

                                    al.set((tvp.getId())-1,alData);
                                    cal.set(tvpor.getId(),ddd[1].trim());
                                }else{
                                    Long id = (long) tvp.getId();
                                    String porcen = tvpor.getText().toString();
                                    String [] ddd = porcen.split(":");
                                    String alData = modulo+"--"+id+"--"+"NULL"+"--"+ddd[1].trim();

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

                                    String alData = modulo+"--"+id+"--"+resultado+"--"+ddd[1].trim();
                                    al.set((tvp.getId())-1,alData);
                                    cal.set(tvpor.getId(),ddd[1].trim());

                                    saveState();
                                }
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                        String porce = tvpor.getText().toString();
                        String [] ddd = porce.split(":");
                        String quest = String.valueOf(tvp.getId());
                        al.add(modulo +"--"+quest+"--NULL--0");
                        cal.add(ddd[1]);
                }

            }
        }catch (Exception ex){
            Toast.makeText(this,"Se genero un exception \n " +
                                            "al crear el tipo de respuesta \n" +
                                            "en el metodo CrearSumRes \n \n"+ex,Toast.LENGTH_LONG).show();
        }
    }

    public void CrearSwicht(final String modulo, Long id, String pregunta){
        try {
            for(int i=0; i<1; i++){
                ArrayList<SwichtQ> lista = new ArrayList<>();
                lista.add(new SwichtQ(id.intValue(),pregunta,iP.get(i).getTipoDetalle()));

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

                            String alData = modulo+"--"+id+"--"+respuesta;

                            al.set((tvp.getId())-1,alData);

                            saveState();
                        }
                    });

                    String quest = String.valueOf(tvp.getId());
                    String respuesta = "NULL";
                    al.add(modulo +"--"+quest+"--"+respuesta);

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
            contarNull();
            validarNull();
        }catch (Exception ex){
            Log.i("Error registro json\n",ex.toString());
        }
    }

    public void contarNull(){
        cont=0;
        for(String i : al){
            String cadena[] = i.split("--");

            if(cadena[2].equals("NULL")){
                cont++;
            }else{ }
        }

    }

    public void validarNull(){

        if(cont != 0){
            Toast.makeText(this, "No puedes dejar campos vacios", Toast.LENGTH_SHORT).show();
        }else{
            for(String resdata : al){
                String cadena[] = resdata.split("--");
                RegisterFinal(Long.parseLong(cadena[1]),cadena[2],cadena[3]);
            }

            Toast.makeText(this, "Se ha guardado exitosamente", Toast.LENGTH_SHORT).show();
            validaQ();

        }
    }

    public void validaQ(){
        for(String i : al){
            String cadena[] = i.split("--");
            while (cadena[0].equals("Q")){
                al.remove(Integer.parseInt(cadena[1])-1);
            }
        }

        eliminarHijos();
        CrearForm();
    }

    public void RegisterFinal(Long idDetalle,String respuesta,String porcentaje){
        try {
            int idusuario = sp.getInt("codigo",0);
            ir.nombre = "Respuestas";

            RespuestasTab rt = new RespuestasTab();

            if(ir.all().size()<=0) {
                rt.setIdreg((long) ir.all().size() + 1);
            }else{
                rt.setIdreg(rt.getIdreg());
            }
            rt.setFecha(getFecha());
            rt.setIdProceso((long) getcodProceso());
            rt.setIdPregunta(idDetalle);
            rt.setRespuesta(respuesta);
            rt.setPorcentaje(Double.valueOf(porcentaje));
            rt.setTerminal(getPhoneName());
            rt.setIdUsuario(idusuario);

            Log.i("insert ",ir.insert(rt));

            Temporal = false;

        }catch (Exception ee){
            Toast.makeText(this, ""+ee, Toast.LENGTH_SHORT).show();
        }
    }

    public void Enviar(View v){
        try {
            iRespuestas irr = new iRespuestas(con.getConexion(),path);

            String nomres = sp.getString("nom_proceso", "");
            irr.nombre="Respuestas";


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
                                rc.setIdUsuario(rc.getIdUsuario());

                                irr.Record(rc);
                                irr.delete(rc.getIdreg());
                        }
                        //progressBar("Enviando...","Se envio exitosamente.",5);
                        Toast.makeText(this, "Se envio exitosamente.", Toast.LENGTH_SHORT).show();
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
                    Dialog(SumarCalificacion().toString()+"");
                    Log.i("data final",al.toString());
                    contarNull();
                    Log.i("data contador",""+cont);
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


    public void onBackPressed() {
        //Toast.makeText(this, "Oprime el boton de menu en la parte superior de la pantalla", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, Index.class);
        startActivity(i);
    }

    //guardar estado de la vista
    public boolean saveState(){
        try{
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("estadoActividad", al.toString());
            edit.commit();
            edit.apply();

            return Temporal = true;
        }catch (Exception ex){
            Toast.makeText(this,"se genero una exception al guardar el estado del formulario \n \n"+ex.toString(),Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public void getState(){
        try{
            String arreglo = sp.getString("estadoActividad", "");
            //Toast.makeText(this,""+arreglo,Toast.LENGTH_SHORT).show();

            //Toast.makeText(this,""+Temporal,Toast.LENGTH_SHORT).show();

        }catch (Exception ex){
            Toast.makeText(this,"Se genero una exception al traer el estado del formulario \n \n"+ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    protected class Conexion extends sqlConect{
        Connection cn = getConexion();
        String path = null;
        Context context;

        public Conexion(String path, Context context) {
            this.path = path;
            this.context = context;
            getPath(path);
        }

        public boolean getPath(String path){
            this.path = path;
            if(cn != null){
                return true;
            }else {
                return false;
            }
        }
    }


    class buttonsSumRes{
        private Long codDetalle;
        private String pregunta;
        private String tipoRespuesta;
        private  Float porcen;

        public buttonsSumRes(Long codDetalle, String pregunta, String tipoRespuesta, Float porcen) {
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
            this.porcen = porcen;
        }


    }
    class SwichtQ{
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;

        public SwichtQ(int codDetalle, String pregunta, String tipoRespuesta) {
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
        }
    }
    class TVF{
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;

        public TVF(int codDetalle, String pregunta, String tipoRespuesta) {
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
        }
    }
    class ET{
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;

        public ET(int codDetalle, String pregunta, String tipoRespuesta) {
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
        }
    }
    class SPINNER{
        private int codDetalle;
        private String pregunta;
        private String tipoRespuesta;
        private String desplegable;
        private Float porcentaje;

        public SPINNER(int codDetalle, String pregunta, String tipoRespuesta, String desplegable, Float porcentaje) {
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            this.tipoRespuesta = tipoRespuesta;
            this.desplegable = desplegable;
            this.porcentaje = porcentaje;
        }
    }
    class FILTRO{
        private int codDetalle;
        private String pregunta;
        private String Desplagable;

        public FILTRO(int codDetalle, String pregunta, String desplagable) {
            this.codDetalle = codDetalle;
            this.pregunta = pregunta;
            Desplagable = desplagable;
        }
    }
}
