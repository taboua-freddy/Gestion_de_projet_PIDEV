/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.activite;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 *
 * @author Freddy
 */
public class Evenement extends Activite
{
    private String lieu;
    private InputStream affiche;

    public Evenement() {
    }

    public Evenement(String lieu, int id, String titre, LocalDateTime dateDebut, LocalDateTime dateFin, String description,InputStream affiche,LocalDateTime dateRappel) {
        super(id, titre, dateDebut, dateFin, description,dateRappel);
        this.lieu = lieu;
        this.affiche = affiche;
    }

    public Evenement(String lieu, String titre, LocalDateTime dateDebut, LocalDateTime dateFin, String description,InputStream affiche,LocalDateTime dateRappel) {
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

    public InputStream getAffiche() {
        return affiche;
    }

    public void setAffiche(InputStream affiche) {
        this.affiche = affiche;
    }

    @Override
    public String toString() {
        return "Evenement{" +super.toString() + "lieu=" + lieu + ", affiche=" + affiche + '}';
    }

    
    
    
    
    
    
}
