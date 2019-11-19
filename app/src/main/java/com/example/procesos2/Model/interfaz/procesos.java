package com.example.procesos2.Model.interfaz;

import com.example.procesos2.Config.DAO;
import com.example.procesos2.Model.tab.procesosTap;

import java.util.List;

public interface procesos extends DAO <Long, procesosTap, String> {
    String send(List<procesosTap> ls);
}
