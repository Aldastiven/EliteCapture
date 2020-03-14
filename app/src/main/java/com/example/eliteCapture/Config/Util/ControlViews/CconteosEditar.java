package com.example.eliteCapture.Config.Util.ControlViews;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.ContenedorTab;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class CconteosEditar {
    View ControlView;

    private Context context;
    private String path;
    private String ubicacion;
    private RespuestasTab rt;
    private boolean vacio;
    private Boolean inicial;
    private int id;
    private Dialog dialog;

    Button btnAceptar, btnDenegar;
    ControlGnr Cgnr;
    String respuestaSpin;

    public CconteosEditar(Context context, String path, String ubicacion, RespuestasTab rt, Boolean inicial, Dialog dialog) {
        this.context = context;
        this.path = path;
        this.ubicacion = ubicacion;
        this.rt = rt;
        this.id = rt.getId().intValue() + 1;
        this.vacio = rt.getRespuesta() != null;
        this.inicial = inicial;
        this.dialog = dialog;
    }

    //metodo que crea el control del conteo
    public View CconteoEditar() {

        LinearLayout.LayoutParams llparamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 2.3;

        LinearLayout.LayoutParams llparamsTextpo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llparamsText.weight = (float) 0.7;

        final TextView tvp = new TextView(context);
        tvp.setId(rt.getId().intValue());
        tvp.setText(id + ". " + rt.getPregunta() + "\nponderado: " + rt.getPonderado());
        tvp.setTextColor(Color.parseColor("#979A9A"));
        tvp.setPadding(5, 5, 5, 5);
        tvp.setTypeface(null, Typeface.BOLD);
        tvp.setLayoutParams(llparamsText);

        String valor;
        if(rt.getValor()==null){
            valor = "Resultado : \n";
        }else {
            if(rt.getValor().equals("-1")){
                valor = "Resultado :\nNA";
            }else {
                valor = "Resultado : \n"+rt.getValor();
            }
        }

        final TextView tvpor = new TextView(context);
        tvpor.setId(rt.getId().intValue());
        tvpor.setText((rt.getValor() == null || rt.getValor().equals("null") ? "Resultado : \n" : valor));
        tvpor.setTextColor(Color.parseColor("#979A9A"));
        tvpor.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        tvpor.setPadding(10, 10, 10, 10);
        tvpor.setTypeface(null, Typeface.BOLD);
        tvpor.setLayoutParams(llparamsTextpo);

        Cgnr = new ControlGnr(context, rt.getId(), pp(Cdesp(llparamsText), tvpor), Cbotones(tvpor), null, "vx2");
        ControlView = Cgnr.Contenedor(vacio, inicial, rt.getTipo());

        return ControlView;
    }

    public View Cdesp(LinearLayout.LayoutParams llparamsText) {
        //metodo que crea el desplegable o la pregunta segun la BD

        Log.i("desplegabledata",rt.getDesplegable());

        String splitS = rt.getDesplegable().replaceAll("[^\\dA-Za-z]", "");

        if (!splitS.isEmpty()) {

            ArrayList soloOpciones = soloOpciones(rt.getDesplegable());

            ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(context, R.layout.spinner_item_personal, soloOpciones);
            final Spinner spinner = new Spinner(context);
            spinner.setId(rt.getId().intValue());
            spinner.setAdapter(spinnerArray);
            spinner.setSelection((vacio ? soloOpciones.indexOf(rt.getCausa()) : 0));
            spinner.setLayoutParams(llparamsText);
            Funspinner(spinner);
            return spinner;

        } else {

            final TextView tvp = new TextView(context);
            tvp.setId(rt.getId().intValue());
            tvp.setText(id + ". " + rt.getPregunta() + "\nponderado: " + rt.getPonderado());
            tvp.setTextColor(Color.parseColor("#979A9A"));
            tvp.setPadding(5, 5, 5, 5);
            tvp.setTypeface(null, Typeface.BOLD);
            tvp.setLayoutParams(llparamsText);
            return tvp;

        }
    }

    //opciones del desplegable
    public ArrayList soloOpciones(String opcion) {
        try {
            ArrayList<String> opc = new ArrayList<>();

            iDesplegable iDesp = new iDesplegable(null, path);
            iDesp.nombre = opcion;
            opc.add("Selecciona");
            for (DesplegableTab des : iDesp.all()) {
                opc.add(des.getOpcion());
            }
            return opc;
        } catch (Exception ex) {
            Toast.makeText(context, "" + ex, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //funcion del spinner
    public void Funspinner(final Spinner spn) {
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                RespuestasTab rtab = datosVivos(spn.getId());

                try {
                    String rta = spn.getItemAtPosition(position).toString();
                    if (rta.equals("Selecciona")) {
                        rta = "null";
                        registro(rtab.getRespuesta(), rtab.getValor(), rta, rtab.getReglas());
                    } else {
                        Cgnr.getViewtt().setBackgroundResource(R.drawable.bordercontainer);
                        registro(rtab.getRespuesta(), rtab.getValor(), rta, rtab.getReglas());
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //metodo que crea los botones de conteos
    public View Cbotones(final TextView tvpor) {
        View viewTotal;

        LinearLayout llbtn = new LinearLayout(context);
        llbtn.setWeightSum(3);
        llbtn.setOrientation(LinearLayout.HORIZONTAL);
        llbtn.setVerticalGravity(Gravity.CENTER);
        llbtn.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        LinearLayout.LayoutParams LLcmpweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LLcmpweight.weight = (float) 1.5;

        final TextView tvr = new TextView(context);
        tvr.setText((rt.getRespuesta() == null) ? "-1" : rt.getRespuesta());
        tvr.setId(rt.getId().intValue());
        tvr.setTextSize(40);
        tvr.setTextColor(Color.parseColor("#5d6d7e"));
        tvr.setTypeface(null, Typeface.BOLD);
        tvr.setGravity(Gravity.CENTER);
        tvr.setLayoutParams(LLcmpweight);

        LinearLayout.LayoutParams LLbtnweight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LLbtnweight.weight = (float) 0.5;

        final Button bntres = new Button(context);
        bntres.setId(rt.getId().intValue());
        bntres.setText("-");
        bntres.setTextSize(20);
        bntres.setTextColor(Color.parseColor("#ffffff"));
        bntres.setBackgroundResource(R.drawable.btnres);
        bntres.setLayoutParams(LLbtnweight);

        final Button bntsum = new Button(context);
        bntsum.setId(rt.getId().intValue());
        bntsum.setText("+");
        bntsum.setTextSize(20);
        bntsum.setTextColor(Color.parseColor("#ffffff"));
        bntsum.setBackgroundResource(R.drawable.btnsum);
        bntsum.setLayoutParams(LLbtnweight);

        LinearLayout.LayoutParams LLbtnregla = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LLbtnregla.weight = (float) 0.5;

        final ImageButton btnRegla = new ImageButton(context);
        btnRegla.setId(rt.getId().intValue());
        btnRegla.setBackgroundColor(Color.TRANSPARENT);
        btnRegla.setImageResource(R.drawable.lapiz);
        btnRegla.setPadding(10, 10, 10, 10);
        btnRegla.setLayoutParams(LLbtnregla);

        btnRegla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insDialog(tvr,tvpor);
                dialog.show();
            }
        });

        llbtn.addView(bntres);
        llbtn.addView(tvr);
        llbtn.addView(bntsum);
        llbtn.addView(btnRegla);

        viewTotal = llbtn;

        int data = Integer.parseInt(tvr.getText().toString());
        ResSum(data, rt.getRespuesta() == null, tvr, rt.getId().intValue());

        //btn de suma
        bntsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RespuestasTab rtab = datosVivos(bntsum.getId());
                int n = Integer.parseInt(tvr.getText().toString());

                if (rt.getCausa() == null) {
                    suma(bntsum, n, tvr, tvpor);
                } else {
                    if (rtab.getCausa().equals("null")) {
                        Toast.makeText(context, "Debes escoger una opcion", Toast.LENGTH_SHORT).show();
                    } else if (!rtab.getCausa().equals("null")) {
                        suma(bntsum, n, tvr, tvpor);
                    }
                }
            }
        });

        //btn de resta
        bntres.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                RespuestasTab rtab = datosVivos(bntres.getId());

                if (rt.getCausa() == null) {

                    int n = Integer.parseInt(tvr.getText().toString());
                    resta(bntres, n, tvr, tvpor);
                    visibilidad(n, tvr, tvpor);
                } else {
                    if (rtab.getCausa().equals("null")) {
                        Toast.makeText(context, "Debes escoger una opcion", Toast.LENGTH_SHORT).show();
                    } else {

                        int n = Integer.parseInt(tvr.getText().toString());
                        resta(bntres, n, tvr, tvpor);
                        visibilidad(n, tvr, tvpor);
                    }
                }

            }
        });

        return viewTotal;
    }

    public void suma(Button bntsum, int n, TextView tvr, TextView tvpor) {
        try {
            RespuestasTab rtab = datosVivos(bntsum.getId());
            if (n < (rtab.getReglas())) {
                int rta = n + 1;
                String data = ResSum(rta, false, tvr, rt.getId().intValue());
                tvr.setText(data);

                String vlr = valor(rta, rtab.getReglas());
                tvpor.setText((rta < 0) ? "Resultado: " : "Resultado: \n" + vlr);

                registro(data, vlr, rtab.getCausa(), rtab.getReglas());
            }
        } catch (Exception ex) {
            Toast.makeText(context, "suma \n" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void resta(Button btnres, int n, TextView tvr, TextView tvpor) {
        try {
            RespuestasTab rtab = datosVivos(btnres.getId());
            if (n >= 0) {
                int rta = n - 1;
                String data = ResSum(rta, false, tvr, rt.getId().intValue());
                tvr.setText(data);

                String vlr = valor(rta, rtab.getReglas());
                tvpor.setText((rta < 0) ? "Resultado: \n NA" : "Resultado: \n" + vlr);

                registro(data, (rta < 0) ? "-1" : vlr, rtab.getCausa(), rtab.getReglas());
            }
        } catch (Exception e) {
            Log.i("Error_Crs", e.toString());
        }
    }

    public void visibilidad(int n, TextView tvr, TextView tvpor) {
        RespuestasTab rtab = datosVivos(tvr.getId());
        if (tvr.getVisibility() == View.INVISIBLE && n < 0) {

            tvr.setVisibility(View.VISIBLE);
            tvpor.setText("Resultado: \n NA");

            int rta = n;
            String vlr = valor(rta, rtab.getReglas());
            String data = ResSum(rta, false, tvr, rt.getId().intValue());
            try {
                registro(data, (rta < 0) ? "-1" : vlr, rtab.getCausa(), rtab.getReglas());
            } catch (Exception e) {
                Log.i("Error_Crs", e.toString());
            }
        }
    }

    public RespuestasTab datosVivos(int id) {
        iContenedor icon = new iContenedor(path);
        ContenedorTab ct = icon.optenerTemporal();

        return ct.getQuestions().get(id);
    }

    public String ResSum(int rta, boolean inicial, TextView tvr, int id) {
        RespuestasTab rtab = datosVivos(id);

        String data = String.valueOf(rta);
        try {
            if (inicial && data.equals("-1")) {
                tvr.setVisibility(View.INVISIBLE);
            } else if (!inicial && rta > -1) {
                tvr.setVisibility(View.VISIBLE);
            } else if (!inicial && rta <= 0) {
                tvr.setVisibility(View.VISIBLE);
                registro("-1", "-1", respuestaSpin, rtab.getReglas());
            }
        } catch (Exception ex) {
            Toast.makeText(context, "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

        return data;
    }

    public String valor(int rta, int regla) {
        try {
            DecimalFormatSymbols separator = new DecimalFormatSymbols();
            separator.setDecimalSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("#.##",separator);
            float operacion = Float.parseFloat((rta < 0) ? "-1" : String.valueOf((rt.getPonderado() / regla) * (regla - rta)));

            return String.valueOf(decimalFormat.format(operacion));
        }catch (Exception ex){
            Toast.makeText(context, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    public void registro(String rta, String valor, String causa, int regla) throws Exception {
        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), causa, regla);
    }

    //retorna el layout del encabezado pregunta porcentaje
    public LinearLayout pp(View v1, View v2) {
        LinearLayout.LayoutParams llparamspregunta = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout llpregunta = new LinearLayout(context);
        llpregunta.setLayoutParams(llparamspregunta);
        llpregunta.setWeightSum(3);
        llpregunta.setOrientation(LinearLayout.HORIZONTAL);
        llpregunta.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        llpregunta.addView(v1); //retorna pregunta
        llpregunta.addView(v2); //retorna pocentaje

        return llpregunta;
    }

    public void insDialog(final TextView tvr, final TextView tvpor) {
        try {

            LinearLayout lineDialog = dialog.findViewById(R.id.linearDialog);
            lineDialog.removeAllViews();
            TextView vtxt = new TextView(context);
            vtxt.setText("Digita el limite de conteo maximo para este campo. \nPor defecto trae un limite de : "+rt.getReglas()+"\n");
            vtxt.setTextSize(15);
            vtxt.setPadding(10, 0, 10, 0);

            LinearLayout.LayoutParams lletxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lletxt.setMargins(0, 10, 0, 10);
            final EditText etxt = new EditText(context);
            etxt.setId(rt.getId().intValue());
            etxt.setBackgroundColor(Color.parseColor("#e5e7e9"));
            etxt.setTypeface(null, Typeface.BOLD);
            etxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            etxt.setHint("                   ");
            etxt.setRawInputType(Configuration.KEYBOARD_QWERTY);
            etxt.setSingleLine();
            etxt.setLayoutParams(lletxt);

            LinearLayout linearLayoutBtns = new LinearLayout(context);
            linearLayoutBtns.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutBtns.setWeightSum(2);
            linearLayoutBtns.setPadding(10, 10, 10, 10);

            LinearLayout.LayoutParams llbtn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            llbtn.weight = 1;
            llbtn.setMargins(5,0,5,0);

            btnDenegar = new Button(context);
            btnDenegar.setId(rt.getId().intValue());
            btnDenegar.setText("Cancelar");
            btnDenegar.setLayoutParams(llbtn);
            btnDenegar.setBackgroundColor(Color.parseColor("#85929e"));
            btnDenegar.setTypeface(null, Typeface.BOLD);
            btnDenegar.setTextColor(Color.WHITE);
            btnDenegar.setPadding(0, 10, 0, 10);

            btnAceptar = new Button(context);
            btnAceptar.setId(rt.getId().intValue());
            btnAceptar.setText("Aceptar");
            btnAceptar.setBackgroundColor(Color.parseColor("#27ae60"));
            btnAceptar.setLayoutParams(llbtn);
            btnAceptar.setTypeface(null, Typeface.BOLD);
            btnAceptar.setTextColor(Color.WHITE);
            btnAceptar.setPadding(0, 10, 0, 10);

            linearLayoutBtns.addView(btnDenegar);
            linearLayoutBtns.addView(btnAceptar);

            lineDialog.addView(vtxt);
            lineDialog.addView(etxt);
            lineDialog.addView(linearLayoutBtns);

            btnAceptar.setOnClickListener(new View.OnClickListener() {

                RespuestasTab rtab = datosVivos(btnAceptar.getId());

                @Override
                public void onClick(View v) {
                    try {
                        registro(rt.getRespuesta(), rt.getValor(), rtab.getCausa(), Integer.parseInt(etxt.getText().toString()));
                            tvr.setText("-1");
                            tvr.setVisibility(View.INVISIBLE);
                            tvpor.setText("Resultado: ");
                            bajarTeclado(etxt);
                            dialog.dismiss();

                    } catch (Exception ex) {
                        Toast.makeText(context, "Debes poner un digito numerio \n" + ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnDenegar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }catch (NumberFormatException ex){
            Toast.makeText(context, "Debes poner un digito mayor a 0", Toast.LENGTH_SHORT).show();
        }
    }

    public void bajarTeclado(EditText txt) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(txt.getWindowToken(),0);
    }
}
