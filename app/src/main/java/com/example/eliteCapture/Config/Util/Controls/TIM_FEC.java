package com.example.eliteCapture.Config.Util.Controls;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import java.util.Calendar;

public class TIM_FEC {
    Context context;
    RespuestasTab rt;
    String ubicacion, path, rta;
    boolean vacio, initial;

    TextView respuestaPonderado;
    TextView camp;
    LinearLayout contenedorCamp;
    containerAdmin ca;
    GIDGET pp;

    TimePickerDialog tp;
    int hour, minutes, year, month, day;

    public TIM_FEC(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {//CONSTRUCTOR
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        Calendar cldr = Calendar.getInstance();
        hour = cldr.get(Calendar.HOUR_OF_DAY);
        minutes = cldr.get(Calendar.MINUTE);
        year = cldr.get(Calendar.YEAR);
        month = cldr.get(Calendar.MONTH)+1;
        day = cldr.get(Calendar.DAY_OF_MONTH);

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : "+rt.getPonderado() : "Resultado :");
    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        try {
            contenedorCamp = ca.container();
            contenedorCamp.setOrientation(LinearLayout.VERTICAL);
            contenedorCamp.setPadding(10, 0, 10, 0);
            contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

            contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
            contenedorCamp.addView(campo());
            pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

            return contenedorCamp;
        }catch (Exception e){
            return new GIDGET(context, "", null, path).problemCamp(rt.getTipo(), e.toString());
        }
    }

    public TextView campo(){//CAMPO DE USUARIO
        camp = (TextView) new textAdmin(context).textColor("Digita la hora o la fecha", "gris", 20, "c");

        LinearLayout.LayoutParams llparams = ca.params();
        llparams.weight = 1;
        llparams.setMargins(5 ,2, 5 ,10);

        String TIMDte = rt.getTipo().equals("FEC") ? (year + "-" + month + "-" + day) : (hour + ":" + minutes);
        camp.setText(vacio ? rt.getRespuesta() : TIMDte);
        camp.setLayoutParams(llparams);

        camp.setOnClickListener(v -> {
            switch (rt.getTipo()) {
                case "TIM":
                    timePickerView();
                    break;
                case "FEC":
                    datePickerView();
                    break;
            }
        });
        funEdt();
        obtenerRespuesta();
        return camp;
    }

    public void funEdt(){
        camp.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rta = camp.getText().toString();

                respuestaPonderado.setText(!rta.isEmpty() ? "Resultado : " + rt.getPonderado() : "Resultado :");
                contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
            }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    public void registro(String rta, String valor) {//REGISTRO
        try{
            new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, String.valueOf(valor), null, rt.getReglas());
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void timePickerView(){
        tp = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hora, int minuto) {
                        try {
                            camp.setText(String.format("%02d", hora) + ":" + String.format("%02d", minuto));
                            obtenerRespuesta();
                        }catch (Exception e){
                            Toast.makeText(context, "Error conversion : "+e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("TIMEDATE", e.toString());
                        }
                    }
                }, hour, minutes, false);
        tp.show();
    }

    public void datePickerView(){

        DatePickerDialog.OnDateSetListener on = (view, yearS, monthS, dayS) -> {
            String dateElegido = yearS + "-" + (monthS+1) + "-" + dayS;
            camp.setText(dateElegido);
            obtenerRespuesta();
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogPickerDate, on, year, (month - 1), day);
        datePickerDialog.updateDate(year, (month - 1), day);
        Window w = datePickerDialog.getWindow();
        w.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        datePickerDialog.show();
    }

    public void obtenerRespuesta() {
        rta = camp.getText().toString();
        rta = rta.replace(" ", "");
        registro(!rta.isEmpty() ? rta : null, !rta.isEmpty() ? rt.getPonderado() + "" : null);
    }
}
