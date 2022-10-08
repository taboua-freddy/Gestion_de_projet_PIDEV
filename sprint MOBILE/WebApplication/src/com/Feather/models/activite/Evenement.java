/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.models.activite;

import java.io.InputStream;
import java.util.Date;

/**
 *
 * @author Freddy
 */
public class Evenement extends Activite
{
    private String lieu;
    private String affiche;

    public Evenement() {
    }

    public Evenement(String lieu, int id, String titre, Date dateDebut, Date dateFin, String description,String affiche,Date dateRappel) {
        super(id, titre, dateDebut, dateFin, description,dateRappel);
        this.lieu = lieu;
        this.affiche = affiche;
    }

    public Evenement(String lieu, String titre, Date dateDebut, Date dateFin, String description,String affiche,Date dateRappel) {
        super(titre, dateDebut, dateFin, description,dateRappel);
        this.lieu = lieu;
        this.affiche = affiche;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getAffiche() {
        return affiche;
    }

    public void setAffiche(String affiche) {
        this.affiche = affiche;
    }

    @Override
    public String toString() {
        return "Evenement{" +super.toString() + "lieu=" + lieu + ", affiche=" + affiche + '}';
    }

    
    
    
    
    
    
}
