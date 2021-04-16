package com.example.eliteCapture.Model.Data.Tab;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;

public class farmSelectDownloadTab {

    Activity act;
    TextView txtNotification;
    String folderPath;
    listFincasTab.fincasTab farm;
    LinearLayout lineDrawable;
    Connection conexion;


    public farmSelectDownloadTab(Activity act, TextView txtNotification, String folderPath, listFincasTab.fincasTab farm, LinearLayout lineDrawable, Connection conexion) {
        this.act = act;
        this.txtNotification = txtNotification;
        this.folderPath = folderPath;
        this.farm = farm;
        this.lineDrawable = lineDrawable;
        this.conexion = conexion;
    }

    public Activity getAct() {
        return act;
    }

    public TextView getTxtNotification() {
        return txtNotification;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public listFincasTab.fincasTab getFarm() {
        return farm;
    }

    public LinearLayout getLineDrawable() {
        return lineDrawable;
    }

    public Connection getConexion() {
        return conexion;
    }
}
