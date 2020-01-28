package com.example.procesos2.Config.Util;

import android.os.Bundle;

import com.example.procesos2.genated;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScanner extends genated implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    public List<String> hr = new ArrayList<>();

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume(){
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public  void onPause(){
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
