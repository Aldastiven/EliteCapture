package com.example.procesos2.Config;


import java.util.List;


public interface DAO <K, O, S> {
    String insert(O o) throws Exception;
    String update(O o, K id) throws Exception;
    String delete(K id) throws Exception;
    String limpiar(O o) throws Exception;
    O oneId(K id)throws Exception;
    boolean local()throws Exception;
    List<O> all() throws Exception;
}
