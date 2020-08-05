package com.example.eliteCapture.Config.Util.Modal;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteCapture.Config.Util.Container.containerAdmin;
import com.example.eliteCapture.Config.Util.Controls.GIDGET;
import com.example.eliteCapture.Config.Util.text.textAdmin;
import com.example.eliteCapture.Model.View.Tab.RespuestasTab;
import com.example.eliteCapture.Model.View.iContenedor;
import com.example.eliteCapture.R;

public class modal_editar_regla_conteo {

    Context context;
    String ubicacion, path;
    int regla, n;
    RespuestasTab rt;
    TextView tvr, tvporc;
    
    containerAdmin ca;
    textAdmin ta;
    GIDGET pp;
    Dialog d;

    EditText edt;

    public modal_editar_regla_conteo(Context context, String path, int regla, int n, String ubicacion, RespuestasTab rt, TextView tvr, TextView tvporc, GIDGET pp) {
        this.context = context;
        this.ubicacion = ubicacion;
        this.path = path;
        this.regla = regla;
        this.n = n;
        this.rt = rt;
        this.tvr = tvr;
        this.tvporc = tvporc;
        this.pp = pp;

        ca = new containerAdmin(context);
        ta = new textAdmin(context);
    }

    public void showModal(){
        d = new Dialog(context, R.style.TransparentDialog);
        d.setContentView(dialog());
        d.show();
    }

    public View dialog(){
        LinearLayout line = ca.container();
        line.setOrientation(LinearLayout.VERTICAL);
        line.setLayoutParams(ca.params());
        
        String txt = "Digita conteo maximo para este campo. \nPor defecto trae un limite de : "+rt.getReglas();
        TextView msg = (TextView) ta.textColor(txt,"negro",15,"l");
        msg.setLayoutParams(margins(10,5,10,5));

        edt = (EditText) pp.campoEdtable("Edit","grisClear");
        edt.setRawInputType(Configuration.KEYBOARD_QWERTY);
        edt.setLayoutParams(margins(10,5,10,10));

        line.addView(msg);
        line.addView(edt);
        line.addView(footer());
        return line;
    }

    public View footer(){
        LinearLayout lineBtn = ca.container();
        lineBtn.setOrientation(LinearLayout.HORIZONTAL);
        lineBtn.setLayoutParams(ca.params());
        lineBtn.setWeightSum(2);

        Button btnCancel = (Button) pp.boton("Cancelar", "negro");
        btnCancel.setLayoutParams(params(1));
        btnCancel.setTextSize(15);

        Button btnAcep = (Button) pp.boton("Aceptar", "verde");
        btnAcep.setLayoutParams(params(1));
        btnAcep.setTextSize(15);

        FunFooter(btnCancel, "cancelar");
        FunFooter(btnAcep, "aceptar");

        lineBtn.addView(btnCancel);
        lineBtn.addView(btnAcep);
        return lineBtn;
    }

    public void FunFooter(Button btn, final String tipo){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (tipo) {
                        case "aceptar":
                            int nuevaRegla = Integer.parseInt(edt.getText().toString());
                            tvr.setText("");
                            tvporc.setText("Resultado: ");
                            regla = nuevaRegla;
                            setRegla(nuevaRegla);
                            break;
                        case "cancelar":
                            break;
                    }
                    bajarTeclado();
                    d.dismiss();
                }catch (Exception e){
                    Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public LinearLayout.LayoutParams params(float i){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(2,2,2,2);
        params.weight = i;

        return params;
    }

    public LinearLayout.LayoutParams margins(int l, int t, int r, int b){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(l,t,r,b);
        return params;
    }

    public void bajarTeclado() {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(edt.getWindowToken(),0);
    }

    public int getRegla() {
        return regla;
    }

    public void setRegla(int regla) {
        this.regla = regla;
    }
}
