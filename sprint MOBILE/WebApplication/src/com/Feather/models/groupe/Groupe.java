/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.models.groupe;

/**
 *
 * @author Aymen Touihri
 */
public class Groupe {
    private int idGroupe;
    
    private String nomGroupe;

    public Groupe() {
    }

    public Groupe(int idGroupe) {
        this.idGroupe=idGroupe;
    }

    public Groupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }
    

    public int getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        this.idGroupe = idGroupe;
    }
    
    /**
     * Get the value of nomGroupe
     *
     * @return the value of nomGroupe
     */
    public String getNomGroupe() {
        return nomGroupe;
    }

    /**
     * Set the value of nomGroupe
     *
     * @param nomGroupe new value of nomGroupe
     */
    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }
    
    
}
