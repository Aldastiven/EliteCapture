package com.example.procesos2.Config.Util;

import android.os.Bundle;

import com.example.procesos2.genated;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScanner extends genated implements ZBarScannerView.ResultHandler {

    public ZBarScannerView mScannerView;
    ArrayList<String> hr = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mScannerView.setResultHandler(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume(){
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        hr.add(rawResult.getContents());
        hr.add(rawResult.getBarcodeFormat().getName());
        mScannerView.resumeCameraPreview(this);
    }
}
