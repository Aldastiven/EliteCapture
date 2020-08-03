package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.CompoundButtonCompat;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.R;

import java.util.ArrayList;
import java.util.List;

public class RS_RSE_RSC {
    Context context;
    RespuestasTab rt;
    String ubicacion, path;
    boolean vacio, initial;

    TextView respuestaPonderado, campConteo;
    LinearLayout contenedorCamp;
    containerAdmin ca;
    textAdmin ta;
    GIDGET pp;

    int n = 0;

    iDesplegable iDesp;

    public RS_RSE_RSC(Context context, String ubicacion, RespuestasTab rt, String path, boolean initial) {
        this.context = context;
        this.rt = rt;
        this.ubicacion = ubicacion;
        this.path = path;
        this.vacio = rt.getRespuesta() != null;
        this.initial = initial;

        iDesp = new iDesplegable(null, path);

        ca = new containerAdmin(context);
        pp = new GIDGET(context, ubicacion, rt, path);
        ta = new textAdmin(context);

        respuestaPonderado = (TextView) pp.resultadoPonderado();
        respuestaPonderado.setText(vacio ? "Resultado : "+rt.getPonderado() : "Resultado :");

    }

    public View crear(){//GENERA EL CONTENEDOR DEL ITEM
        contenedorCamp = ca.container();
        contenedorCamp.setOrientation(LinearLayout.VERTICAL);
        contenedorCamp.setPadding(10, 0, 10, 0);
        contenedorCamp.setGravity(Gravity.CENTER_HORIZONTAL);

        contenedorCamp.addView(pp.Line(respuestaPonderado));//Crea la seccion de pregunta ponderado y resultado
        contenedorCamp.addView(campo());
        pp.validarColorContainer(contenedorCamp, vacio, initial);//pinta el contenedor del item si esta vacio o no

        return contenedorCamp;
    }

    public LinearLayout campo() {//CAMPO DE USUARIO
        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.VERTICAL);
        if(rt.getTipo().equals("RSC")) line.addView(multiSelect());
        if(rt.getTipo().equals("RS") && rt.getDesplegable() != null) line.addView(despSelect());
        if(rt.getReglas() == 0) line.addView(ta.textColor("No hay asignado limite de conteo (REGLA)","rojo",15,"l"));
        line.addView(conteos());
        return line;
    }


    //CONTROL Y FUNCION CONTEOS
    public LinearLayout conteos(){
        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setWeightSum(3);

        campConteo = (TextView) pp.campoEdtable("TextView", "grisClear");
        campConteo.setLayoutParams(params( rt.getTipo().equals("RSE") ? (float) 0.7 : (float) 1));
        campConteo.setTextSize(25);

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
        return line;
    }
    public void FunContador(View btn, final String tipo){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {

                    if(tipo.equals("s")) {
                        if (n < rt.getReglas()) n++;
                    }else {
                        if (n >= 0) n--;
                    }

                    campConteo.setText(String.valueOf(n));

                }catch (Exception e){
                    Log.i("CONTEO",e.toString());
                }
            }
        });
    }


    //CONTROL Y FUNCION DE RS CON DESPLEGABLE
    public Spinner despSelect(){
        Spinner campSpin = new Spinner(context);
        campSpin.setAdapter(getAdapter(getDesp()));
        campSpin.setSelection((vacio ? getDesp().indexOf(rt.getCausa()) : 0));
        campSpin.setBackgroundResource(R.drawable.myspinner);

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


    //CONTROL Y FUNCION RSE
    public ImageButton btnRegla(){
        ImageButton btnRegla = new ImageButton(context);
        btnRegla.setId(rt.getId().intValue());
        btnRegla.setBackgroundColor(Color.TRANSPARENT);
        btnRegla.setImageResource(R.drawable.lapiz);
        btnRegla.setPadding(10, 10, 10, 10);
        btnRegla.setLayoutParams(params((float) 0.9));

        return btnRegla;
    }


    //CONTROL Y FUNCION RSC
    public LinearLayout multiSelect(){
        final LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.VERTICAL);

        TextView btnDesp = (TextView) pp.campoEdtable("TextView","grisClear");
        btnDesp.setText("Elige una opción");
        btnDesp.setTextSize(15);
        btnDesp.setPadding(0, 25, 0, 25);

        final LinearLayout lineDespliegue = ca.container();
        lineDespliegue.setOrientation(LinearLayout.VERTICAL);
        lineDespliegue.setVisibility(View.GONE);


        btnDesp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                lineDespliegue.setVisibility(lineDespliegue.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        line.addView(btnDesp);
        if(rt.getDesplegable() != null) getMultiOption(lineDespliegue);
        else line.addView(ta.textColor("¡El campo no tiene desplegable asignada!", "rojo", 15, "l"));
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

                int states[][] = {{android.R.attr.state_checked}, {}};
                int colors[] = {Color.rgb(88, 214, 141),
                        Color.rgb(39, 55, 70)};
                CompoundButtonCompat.setButtonTintList(cB, new ColorStateList(states, colors));

                lineDespliegue.addView(cB);
            }
        }catch (Exception e){
            Toast.makeText(context, "Error desplegable : "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public LinearLayout.LayoutParams params(float i){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(2,2,2,2);
        params.weight = i;

        return params;
    }

}
