/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.utils.enumeration;

/**
 *
 * @author Freddy
 */
public enum TypeImportance {
    GRANDE("Tres important",5), MOYENNE("Moin important",3) , FAIBLE("Pas important",0);
    
    private String text;
    private int value;

    private TypeImportance(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getText();
    }
    
    
    
}
