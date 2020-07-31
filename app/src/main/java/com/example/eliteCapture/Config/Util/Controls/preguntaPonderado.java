package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.R;

import static com.example.eliteCapture.R.drawable.*;

public class preguntaPonderado {

    Context context;
    String ubicacion;
    RespuestasTab rt;

    textAdmin ta;
    containerAdmin ca;

    public preguntaPonderado(Context context, String ubicacion, RespuestasTab rt) {
        this.context = context;
        this.ubicacion = ubicacion;
        this.rt = rt;

        ta = new textAdmin(context);
        ca = new containerAdmin(context);
    }

    public View pregunta(){
        String txtPregunta = ubicacion.equals("H") ? rt.getPregunta() : (rt.getId()+1)+ ". "+rt.getPregunta();
        return (TextView) ta.textColor(txtPregunta, "negro",15,"l");
    }

    public View ponderado(){
        String txtPonderado = "Ponderado : "+ rt.getPonderado();
        TextView tv = (TextView) ta.textColor(txtPonderado, "gris",13,"l");
        tv.setLayoutParams(params(1));
        return tv;
    }

    public View resultadoPonderado(){

        String txtResPonderado = rt.getValor()!=null ? " Resultado : "+ rt.getValor() : " Resultado : ";
        TextView tv = (TextView) ta.textColor(txtResPonderado, "darkGray",13,"l");
        tv.setLayoutParams(params(1));
        return tv;
    }

    public View Line(View resultadoPonderado){

        LinearLayout lineContainer = ca.container();
        lineContainer.setOrientation(LinearLayout.VERTICAL);

        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutParams(ca.params());
        lineContainer.setWeightSum(2);
        line.setGravity(Gravity.LEFT);

        lineContainer.addView(pregunta());

        if(ubicacion.equals("Q")) line.addView(ponderado());
        if(ubicacion.equals("Q")) line.addView(resultadoPonderado);

        lineContainer.addView(line);

        return lineContainer;
    }

    public LinearLayout.LayoutParams params(float i){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT;
        params.weight = i;

        return params;
    }

    public void validarColorContainer(LinearLayout contenedorCamp, boolean vacio, boolean inicial){
        Log.i("INITIAL","ini : "+inicial+" vac : "+vacio);
        if(!inicial){
            contenedorCamp.setBackgroundResource(!vacio ? bordercontainerred : bordercontainer);
        }else{
            contenedorCamp.setBackgroundResource(bordercontainer);
        }
    }

    public View campoEdtable(String tipo){
        View v = null;
        switch (tipo){
            case "Edit":
                EditText edit = new EditText(context);
                edit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                edit.setTextColor(Color.parseColor("#515A5A"));
                edit.setBackgroundColor(Color.parseColor("#E5E7E9"));
                edit.setTypeface(null, Typeface.BOLD);
                edit.setBackgroundColor(Color.parseColor("#eeeeee"));
                edit.setSingleLine(true);
                v =  edit;
                break;
            case "Auto":
                AutoCompleteTextView auto = new AutoCompleteTextView(context);
                auto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                auto.setTextColor(Color.parseColor("#515A5A"));
                auto.setBackgroundColor(Color.parseColor("#E5E7E9"));
                auto.setTypeface(null, Typeface.BOLD);
                auto.setBackgroundColor(Color.parseColor("#eeeeee"));
                auto.setSingleLine(true);
                v =  auto;
                break;
        }
        return v;
    }
}
