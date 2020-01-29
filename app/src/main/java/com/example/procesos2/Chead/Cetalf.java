package com.example.procesos2.Chead;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.procesos2.R;
import com.example.procesos2.genated;

import java.util.ArrayList;
import static androidx.core.content.ContextCompat.getSystemService;

public class Cetalf {

    private static final Context INPUT_METHOD_SERVICE = null;
    View ControlView;

    //metodo que crea el control edittext alfanumerico
    public View talfanumerico(Context context, Long id, String contenido){
        int i;
        for(i=0; i<=1; i++){
            ArrayList<consCetalf> lista = new ArrayList<>();
            lista.add(new consCetalf(context, id, contenido));

            for(consCetalf ea : lista){
                LinearLayout.LayoutParams llparams = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llparams.weight = 1;
                llparams.setMargins(5, 10, 5, 10);

                final TextView tvp = new TextView(context);
                tvp.setId(id.intValue());
                tvp.setText(ea.contenido);
                tvp.setTextSize(20);
                tvp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvp.setTextColor(Color.parseColor("#979A9A"));
                tvp.setTypeface(null, Typeface.BOLD);
                tvp.setLayoutParams(llparams);

                EditText etxtA = new EditText(context);
                etxtA.setId(id.intValue());
                etxtA.setTextSize(20);
                etxtA.setHint("NULL");
                etxtA.setHintTextColor(Color.TRANSPARENT);
                etxtA.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                etxtA.setTextColor(Color.parseColor("#515A5A"));
                etxtA.setBackgroundColor(Color.parseColor("#E5E7E9"));
                etxtA.setTypeface(null, Typeface.BOLD);
                etxtA.setLayoutParams(llparams);
                etxtA.setBackgroundResource(R.drawable.bordertext);

                ControlView = CrearLinearLayoutHeader(tvp, etxtA, context);
            }

        }
        return ControlView;
    }

    public LinearLayout CrearLinearLayoutHeader(View v1, View v2, Context context) {
        LinearLayout.LayoutParams llparamsPrincipal = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout LLprincipal = new LinearLayout(context);
        LLprincipal.setLayoutParams(llparamsPrincipal);
        LLprincipal.setBackgroundColor(Color.parseColor("#ffffff"));
        LLprincipal.setWeightSum(2);
        LLprincipal.setOrientation(LinearLayout.HORIZONTAL);
        LLprincipal.setPadding(5, 5, 5, 5);
        LLprincipal.setGravity(Gravity.CENTER_HORIZONTAL);

        LLprincipal.addView(v1);
        LLprincipal.addView(v2);

        return LLprincipal;
    }


    //constructor
    class consCetalf{
        Context context;
        Long id;
        String contenido;

        public consCetalf(Context context, Long id, String contenido) {
            this.context = context;
            this.id = id;
            this.contenido = contenido;
        }
    }
}