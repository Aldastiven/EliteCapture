package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.R;

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
        String txtPregunta = ubicacion.equals("H") ? rt.getPregunta() : rt.getId()+ ". "+rt.getPregunta();
        return (TextView) ta.textColor(txtPregunta, "negro",15,"l");
    }

    public View ponderado(){
        String txtPonderado = "Ponderado : "+ rt.getPonderado();
        return (TextView) ta.textColor(txtPonderado, "gris",13,"l");
    }

    public View resultadoPonderado(){
        String txtResPonderado = rt.getValor()!=null ? "Resultado : \n"+ rt.getValor() : "Resultado : ";
        TextView tv = (TextView) ta.textColor(txtResPonderado, "negro",13,"l");
        tv.setBackgroundColor(Color.parseColor("#EAEDED"));
        return tv;
    }

    public View Line(View resultadoPonderado){

        LinearLayout lineContainer = ca.container();
        lineContainer.setOrientation(LinearLayout.HORIZONTAL);
        lineContainer.setWeightSum(2);

        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.VERTICAL);
        line.setLayoutParams(params((float) 2.3));
        line.setGravity(Gravity.CENTER);

        line.addView(pregunta());
        if(ubicacion.equals("Q")) line.addView(ponderado());


        lineContainer.addView(line);
        if(ubicacion.equals("Q")) lineContainer.addView(resultadoPonderado);


        return lineContainer;
    }

    public LinearLayout.LayoutParams params(float i){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = i;

        return params;
    }

    public void validarColorContainer(LinearLayout contenedorCamp, boolean vacio, boolean inicial){
        if(vacio) {
            contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
        }else if(!vacio && inicial ){
            contenedorCamp.setBackgroundResource(R.drawable.bordercontainerred);
        }else {
            contenedorCamp.setBackgroundResource(R.drawable.bordercontainer);
        }
    }

    public EditText campoEdtable(){
        EditText camp = new EditText(context);
        camp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        camp.setTextColor(Color.parseColor("#515A5A"));
        camp.setBackgroundColor(Color.parseColor("#E5E7E9"));
        camp.setTypeface(null, Typeface.BOLD);
        camp.setBackgroundColor(Color.parseColor("#eeeeee"));
        camp.setSingleLine();

        return camp;
    }
}
