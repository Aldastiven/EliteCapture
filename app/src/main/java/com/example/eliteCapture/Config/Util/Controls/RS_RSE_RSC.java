package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.CompoundButtonCompat;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Modal.modal_editar_regla_conteo;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class RS_RSE_RSC {
    Context context;
    TextView respuestaPonderado, campConteo;
    LinearLayout contenedorCamp, noti;
    containerAdmin ca;
    textAdmin ta;
    GIDGET pp;

    RespuestasTab rt;

    iDesplegable iDesp;

    String ubicacion, path, valor = "", rta = "", causa = "";
    boolean vacio, initial;
    List<String> checkList = new ArrayList<>();
    int n, regla;

    modal_editar_regla_conteo modalEditarRegla;

    public RS_RSE_RSC(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;
        this.valor = rt.getValor();
        this.rta = rt.getRespuesta();
        this.causa = rt.getCausa();
        this.n = rta != null ? Integer.parseInt(rta) : -1;
        this.regla = rt.getReglas();

        iDesp = new iDesplegable(null, path);

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : "+ (rt.getValor().equals("-1") ? "N/A" : rt.getValor()) : "Resultado :");

        noti = new LinearLayout(context);
        noti.setOrientation(LinearLayout.VERTICAL);
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

    public LinearLayout campo() {//CAMPO DE USUARIO
        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.VERTICAL);
        if (rt.getTipo().equals("RSC")) {
            line.addView(multiSelect());
        }
        if (regla == 0) {
            line.addView(ta.textColor("No hay asignado limite de conteo (REGLA)","rojo",15,"l"));
        }
        line.addView(noti);
        line.addView(conteos());

        modalEditarRegla = new modal_editar_regla_conteo(context, path, regla, n, ubicacion, rt, campConteo, respuestaPonderado, pp);
        return line;
    }


    //CONTROL Y FUNCION CONTEOS
    public LinearLayout conteos(){
        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setWeightSum(3);

        campConteo = (TextView) pp.campoEdtable(rt.getTipo().equals("RSE") ? "Edit" : "TextView", "grisClear");
        campConteo.setLayoutParams(params( rt.getTipo().equals("RSE") ? (float) 0.7 : (float) 1));
        campConteo.setText(vacio ? rt.getRespuesta() : "");
        campConteo.setTextSize(25);
        campConteo.setRawInputType(Configuration.KEYBOARD_QWERTY);

        TextView btnRes = (Button) pp.boton("-", "rojo");
        btnRes.setLayoutParams(params(rt.getTipo().equals("RSE") ? (float) 0.7 : (float) 1));
        btnRes.setTextSize(25);

        TextView btnSum = (Button) pp.boton("+", "verde");
        btnSum.setLayoutParams(params(rt.getTipo().equals("RSE") ? (float) 0.7 : (float) 1));
        btnSum.setTextSize(25);

        line.addView(btnRes);
        line.addView(campConteo);
        line.addView(btnSum);
        if(rt.getTipo().equals("RSE"))line.addView(btnRegla());

        FunContador(btnRes, "r");
        FunContador(btnSum, "s");
        funCampChange();
        funEraser();
        return line;
    }

    public void funEraser(){
        campConteo.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_DEL){
                String res = campConteo.getText().toString();
                if(res.equals("-1")){
                    campConteo.setText(res.substring(0, res.length()-2 ));
                }
            }
            return false;
        });
    }

    public void funCampChange(){
        campConteo.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String res = reemplazarDecimal();
                    n = !StringUtils.isEmpty(res) ? Integer.parseInt(res) : 0;

                    if (n > modalEditarRegla.getRegla()) {
                        campConteo.setText(res.substring(0, res.length() - 1));
                    }

                    valor = valor(n);
                    respuestaPonderado.setText(valor.equals("-1") ? "Resultado : N/A" : "Resultado : " + valor);
                    registro(rta, valor, causa);
                }catch (Exception e){
                    Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    public String reemplazarDecimal(){
        String d = campConteo.getText().toString();

        //elimina punto si el campo es un entero
        if(d.contains(".") && rt.getDecimales() == 0 || campConteo.getText().length() == 1){
            d = d.replaceAll("\\.", "");
        }

        //valida si digita mas de un punto
        if(getNumerCountCharacter(d, (char)46) == 2){
            d = d.substring(0, d.length() - 1);
            campConteo.setText(d);
        }

        //valida si digita mas de un coma
        if(getNumerCountCharacter(d, (char)44) > 0){
            d = d.substring(0, d.length() - 1);
            campConteo.setText(d);
        }

        //valida si digita mas de un espacio
        if(getNumerCountCharacter(d, (char)32) > 0){
            d = d.substring(0, d.length() - 1);
            campConteo.setText(d);
        }

        //valida si digita mas de un quion
        if(getNumerCountCharacter(d, (char)45) > 0 && d.length() == 1){
            d = d.substring(0, d.length() - 1);
            campConteo.setText(d);
        }

        //valida cantidad de digitos despues del punto
        if(d.contains(".")) {
            String[] r = d.split("\\.");
            d = r[0] + "." + r[1].substring(0, r[1].length() == rt.getDecimales() ? r[1].length() : r[1].length() - (r[1].length() - rt.getDecimales()));
        }

        //imprime en el campo si el dato registrado es diferente al inicial
        if(d.length() != campConteo.getText().length()) {
            campConteo.setText(d);
        }
        return d;
    }

    public int getNumerCountCharacter(String txt, char _toCompare){
        int c = 0;
        char []caracteres = txt.toCharArray();
        for(int i = 0; i <= caracteres.length - 1; i++){
            if(_toCompare == caracteres[i]){
                c++;
            }
        }
        return c;
    }
    public void FunContador(View btn, final String tipo){

        btn.setOnClickListener(v -> {
            try {
                if(StringUtils.isEmpty(rt.getDesplegable())){
                    n =  contar(tipo);
                    rta = n+"";
                    campConteo.setText(n+"");
                }else{
                    if(StringUtils.isEmpty(causa)) {
                        noti.removeAllViews();
                        noti.addView(ta.textColor("¡Debes seleccionar al menos una opción!", "rojo", 15, "l"));
                        temporizador(5000);
                        n = -1;
                        rta = n + "";
                        campConteo.setText("");
                    }else {
                        n = contar(tipo);
                        rta = n + "";
                        campConteo.setText(""+n);
                    }
                }

            }catch (Exception e){
                Log.i("CONTEO",e.toString());
            }
        });
    }
    public int contar(String tipo){
        n = campConteo.getText().toString() == null ?  -1 : n;
        if(tipo.equals("s")) {
            if (n < modalEditarRegla.getRegla()) n++;
        }else{
            if (n >= 0) n--;
        }
        return  n;
    }
    public String valor(int rta) {//CALCULA VALOR SEGUN LA CANTIDAD
        try {
            DecimalFormatSymbols separator = new DecimalFormatSymbols();
            separator.setDecimalSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("#.##",separator);
            float operacion = Float.parseFloat((rta < 0) ? "-1" : String.valueOf((rt.getPonderado() / modalEditarRegla.getRegla()) * (modalEditarRegla.getRegla() - rta)));

            //REVISAR CON FERNANDO
            /*if(rt.getTipo().equals("RSE")){
                operacion = operacion;
            }*/

            return ""+decimalFormat.format(operacion);
        }catch (Exception ex){
            Toast.makeText(context, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }


    //CONTROL Y FUNCION DE RS CON DESPLEGABLE
    public Spinner despSelect(){

        Spinner campSpin = new Spinner(context);
        campSpin.setAdapter(getAdapter(getDesp()));
        campSpin.setSelection(vacio ? getDesp().indexOf(rt.getCausa()) : 0);
        campSpin.setBackgroundResource(R.drawable.myspinner);

        Funspinner(campSpin);
        return campSpin;
    }
    public ArrayAdapter<String> getAdapter(List<String> listaCargada){
        int resource =  R.layout.items_des;
        ArrayAdapter<String> autoArray = new ArrayAdapter<>(context,resource , listaCargada);
        return autoArray;
    }
    public List<String> getDesp(){
        try {
            List<String> Loptions = new ArrayList<>();
            iDesp.nombre = rt.getDesplegable();
            Loptions.add("Selecciona");
            for (DesplegableTab desp : iDesp.all()) {
                Loptions.add(desp.getOpcion());
            }
            return Loptions;
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    public void Funspinner(final Spinner spn){//FUNCION DEL SPINNER
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String data = spn.getItemAtPosition(position).toString();
                    causa = !data.equals("Selecciona") ? data : "";
                    registro(rta,valor,causa);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    //CONTROL Y FUNCION RSE
    public ImageButton btnRegla(){
        ImageButton btnRegla = new ImageButton(context);
        btnRegla.setId(rt.getId().intValue());
        btnRegla.setBackgroundColor(Color.TRANSPARENT);
        btnRegla.setImageResource(R.drawable.lapiz);
        btnRegla.setPadding(10, 10, 10, 10);
        btnRegla.setLayoutParams(params((float) 0.9));

        btnRegla.setOnClickListener(v -> {
            try {
                modalEditarRegla.showModal();
            }catch (Exception e){
                Toast.makeText(context, ""+e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        return btnRegla;
    }


    //CONTROL Y FUNCION RSC
    public LinearLayout multiSelect(){
        final LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.VERTICAL);

        TextView btnDesp = (TextView) pp.campoEdtable("TextView","grisClear");
        btnDesp.setText("Elige varias opciones");
        btnDesp.setTextSize(15);
        btnDesp.setPadding(0, 25, 0, 25);

        final LinearLayout lineDespliegue = ca.container();
        lineDespliegue.setOrientation(LinearLayout.VERTICAL);
        lineDespliegue.setVisibility(View.GONE);


        btnDesp.setOnClickListener(v -> lineDespliegue.setVisibility(lineDespliegue.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));

        line.addView(btnDesp);
        if(rt.getDesplegable() != null) {
            getMultiOption(lineDespliegue);
        } else {
            line.addView(ta.textColor("¡El campo no tiene desplegable asignada!", "rojo", 15, "l"));
        }
        line.addView(lineDespliegue);
        return line;
    }
    public void getMultiOption(LinearLayout lineDespliegue){
        try {
            iDesp.nombre = rt.getDesplegable();
            for (DesplegableTab desp : iDesp.all()) {
                CheckBox cB = new CheckBox(context);
                cB.setText(desp.getOpcion());
                cB.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                if(rt.getCausa() != null && vacio)recorrerSeleccion(cB);

                int states[][] = {{android.R.attr.state_checked}, {}};
                int colors[] = {Color.rgb(88, 214, 141),
                        Color.rgb(39, 55, 70)};
                CompoundButtonCompat.setButtonTintList(cB, new ColorStateList(states, colors));

                FunmultiSelect(cB);
                lineDespliegue.addView(cB);
            }

            causa = respuestaLimpia();
            registro(rta,valor,causa);
        }catch (Exception e){
            Toast.makeText(context, "Error desplegable : "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void FunmultiSelect(final CheckBox cb){
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(cb.isChecked()){
                checkList.add(cb.getText().toString());
            }if(!cb.isChecked()) {
                deleteObjList(cb.getText().toString());
            }
            causa = respuestaLimpia();
            registro(rta,valor,causa);
        });
    }
    public void deleteObjList(String obj){
        for(int i = 0; i < checkList.size(); i++){
            if(checkList.get(i).equals(obj)){
                checkList.remove(obj);
            }
        }
    }
    public String respuestaLimpia(){
        String d = checkList.toString().replace("[", "").replace("]", "");
        return d;
    }
    public void recorrerSeleccion(CheckBox cB){
        String[] causaL = rt.getCausa().split(",");
        for (String c : causaL) {
            if(c.trim().equals(cB.getText().toString().trim())) {
                cB.setChecked(true);
                checkList.add(cB.getText().toString());
                break;
            }
        }

    }


    public LinearLayout.LayoutParams params(float i){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(2,2,2,2);
        params.weight = i;

        return params;
    }

    public void temporizador(int duracion){
        if(duracion > 0 ) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    noti.removeAllViews();
                }
            }, duracion);
        }
    }

    public void registro(String rta, String valor, String causa) {//REGISTRO
        int regla = rt.getTipo().equals("RSE") ? modalEditarRegla.getRegla() : rt.getReglas();

        String respuestaConcat ="tipoCampo : "+rt.getTipo()+" Rta : "+rta+", valor : "+valor+", causa : "+causa;

        //Toast.makeText(context,respuestaConcat , Toast.LENGTH_SHORT).show();
        Log.i("repuestaRSC", respuestaConcat);

        new iContenedor(path).editarTemporal(ubicacion, rt.getId().intValue(), rta, rt.getTipo().equals("RSC") ? causa : valor, causa, regla);
    }

}
