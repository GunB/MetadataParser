/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author hangarita
 */
public class Recurso {
    
    String strNombre;
    String sttrID;
    String strDesc;
    
    ArrayList<String> arrData = new ArrayList<>();

    public Recurso(String strNombre, String sttrID, String strDesc) {
        this.strNombre = strNombre;
        this.sttrID = sttrID;
        this.strDesc = strDesc;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getSttrID() {
        return sttrID;
    }

    public void setSttrID(String sttrID) {
        this.sttrID = sttrID;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public ArrayList<String> getArrData() {
        return arrData;
    }

    public void setArrData(ArrayList<String> arrData) {
        this.arrData = arrData;
    }
    
    
    
}
