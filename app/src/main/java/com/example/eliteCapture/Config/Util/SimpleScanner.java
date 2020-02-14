package com.example.eliteCapture.Config.Util;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.example.eliteCapture.genated;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScanner extends genated implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    public List<String> hr = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
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
