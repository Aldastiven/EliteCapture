package com.example.eliteCapture.Config.Util.Controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.Data.Tab.DesplegableTab;
import com.example.eliteCapture.Model.Data.iDesplegable;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;

import static com.example.eliteCapture.R.drawable.bordercontainer;
import static com.example.eliteCapture.R.drawable.bordercontainerred;

public class GIDGET {

    Context context;
    String ubicacion, path;
    RespuestasTab rt;

    textAdmin ta;
    containerAdmin ca;
    iDesplegable iDesp;

    public GIDGET(Context context, String ubicacion, RespuestasTab rt, String path) {
        this.context = context;
        this.ubicacion = ubicacion;
        this.rt = rt;

        ta = new textAdmin(context);
        ca = new containerAdmin(context);
        iDesp = new iDesplegable(null, path);
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
        tv.setLayoutParams(ca.params3());
        return tv;
    }

    public View resultadoFiltro(){
        LinearLayout.LayoutParams p = ca.params();
        p.setMargins(0,-5,0,-5);
        LinearLayout line = ca.container();
        line.setLayoutParams(p);

        return line;
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

        LinearLayout linePon = ca.container();
        linePon.setLayoutParams(ca.params3());
        linePon.setLayoutParams(params(1));
        linePon.addView(resultadoPonderado);

        if(ubicacion.equals("Q")) line.addView(ponderado());
        if(ubicacion.equals("Q")) line.addView(linePon);

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
        Log.i("inicialValue", "llego data : "+inicial);
        contenedorCamp.setBackgroundResource(!inicial && !vacio ? bordercontainerred : bordercontainer);
    }

    public View campoEdtable(String tipo, String color){
        View v = null;
        switch (tipo){
            case "Edit":
                EditText edit = new EditText(context);
                edit.setTextColor(Color.parseColor("#515A5A"));
                edit.setTypeface(null, Typeface.BOLD);
                edit.setSingleLine(true);
                v =  edit;
                break;
            case "Auto":
                AutoCompleteTextView auto = new AutoCompleteTextView(context);
                auto.setTextColor(Color.parseColor("#515A5A"));
                auto.setTypeface(null, Typeface.BOLD);
                auto.setSingleLine(true);
                v =  auto;
                break;
            case "TextView":
                TextView textV = new TextView(context);
                textV.setTextColor(Color.parseColor("#515A5A"));
                textV.setTypeface(null, Typeface.BOLD);
                textV.setSingleLine(true);
                textV.setGravity(Gravity.CENTER);
                v =  textV;
                break;
        }
        v.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        v.setBackgroundColor(colorBack(color));
        return v;
    }

    public View boton(String nombre, String color){
        Button btn = new Button(context);
        btn.setBackgroundColor(colorBack(color));
        btn.setText(nombre);
        btn.setTextColor(Color.WHITE);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setAllCaps(false);

        return btn;
    }

    public int colorBack(String color){
        int C = 0;
        switch (color){
            case "verde":
                C = Color.parseColor("#2ecc71");
                break;
            case "gris":
                C = Color.parseColor("#85929E");
                break;
            case "negro":
                C = Color.parseColor("#17202A");
                break;
            case "rojo":
                C = Color.parseColor("#E74C3C");
                break;
            case "darkGray":
                C = Color.parseColor("#154360");
                break;
            case "grisClear":
                C = Color.parseColor("#eeeeee");
                break;
        }
        return C;
    }

    public DesplegableTab busqueda(String data){
        try {
            iDesp.nombre = rt.getDesplegable();
            DesplegableTab d = null;
            for (DesplegableTab desp : iDesp.all()) {
                if (desp.getCodigo().equals(data)) {
                    d = desp;
                    break;
                }else if(desp.getOpcion().equals(data)) {
                    d = desp;
                    break;
                }
            }
            return d;
        }catch (Exception e){
            Toast.makeText(context, "DESPLEGABLE : "+e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public LinearLayout problemCamp(String campo, String error){
        LinearLayout line = new containerAdmin(context).container();
        TextView txt = new TextView(context);
        txt.setText("Problemas campo : "+campo+"\nError : "+error);
        txt.setTextSize(15);
        txt.setTextColor(Color.parseColor("#F1948A"));

        line.addView(txt);
        return line;
    }
}
