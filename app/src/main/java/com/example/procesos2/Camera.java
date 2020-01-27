package com.example.procesos2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class Camera extends AppCompatActivity {

    public ZBarScannerView vbc;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        vbc = new ZBarScannerView(this);
        vbc.setResultHandler(new barcodeimp());
        setContentView(vbc);
    }

    public void onResume(){
        super.onResume();
        vbc.startCamera();
    }

    public void onPause(){
        super.onPause();
        vbc.stopCamera();
    }

    private class barcodeimp implements ZBarScannerView.ResultHandler {

        @Override
        public void handleResult(Result rawResult) {
            try{
                String bc = rawResult.getContents();

                if(bc != null){
                    Intent i = new Intent(Camera.this,genated.class);
                    i.putExtra("codigo",bc);
                    i.putExtra("id",ID());
                    startActivityForResult(i,0);
                    vbc.stopCamera();
                }else{
                    Toast.makeText(Camera.this, "No se pudo leer el codigo \n no se encontraron resultados", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception ex){
                Toast.makeText(Camera.this, "Error al leer el codigo \n \n"+ex.toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Camera.this,genated.class);
                startActivity(i);
            }
        }
    }

    public long ID(){
        Long id = null;
        try{
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                id = bundle.getLong("id");
            }
        }catch (Exception ex){

        }
        return id;
    }

    public void onBackPressed(){
        Intent i = new Intent(Camera.this, genated.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        vbc.stopCamera();
    }
}
