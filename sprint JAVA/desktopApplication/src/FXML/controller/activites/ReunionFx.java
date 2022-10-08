/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.activites;

import entity.activite.Reunion;
import functions.Functions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 *
 * @author Freddy
 */
public class ReunionFx extends Reunion {

    public Boolean Selected;
    public Reunion reunion;
    public String etat;
    private String dateD;
    private String dateF;

    public ReunionFx() {
    }


    public ReunionFx(Reunion r) {
        super(r.getThemeDuJour(),r.getImportance(),r.getCoordonateur(),r.getSprint(),r.getObjectifs(),r.getId(),r.getTitre(),r.getDateDebut(),r.getDateFin(),r.getDescription(),r.getDateRappel());
        reunion = new Reunion(r.getThemeDuJour(),r.getImportance(),r.getCoordonateur(),r.getSprint(),r.getObjectifs(),r.getId(),r.getTitre(),r.getDateDebut(),r.getDateFin(),r.getDescription(),r.getDateRappel());
        this.Selected = false;
        if(r.getDateDebut()!=null)
        {
            this.etat = (getDateDebut().isBefore(LocalDateTime.now()))?"Déjà passée":"A venir";
            if(Functions.matchDate(getDateDebut(), getDateFin(), LocalDateTime.now()))
                this.etat = "En cours";
            
            String date = r.getDateDebut().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.FRENCH));
            String heure = r.getDateDebut().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(Locale.FRENCH));
            this.dateD = date + " à " +heure;
            
            date = r.getDateFin().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.FRENCH));
            heure = r.getDateFin().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(Locale.FRENCH));
            this.dateF = date + " à " +heure;
        }

    }

    public ReunionFx(Reunion r, boolean selected) {
        super(r.getThemeDuJour(),r.getImportance(),r.getCoordonateur(),r.getSprint(),r.getObjectifs(),r.getId(),r.getTitre(),r.getDateDebut(),r.getDateFin(),r.getDescription(),r.getDateRappel());
        reunion = new Reunion(r.getThemeDuJour(),r.getImportance(),r.getCoordonateur(),r.getSprint(),r.getObjectifs(),r.getId(),r.getTitre(),r.getDateDebut(),r.getDateFin(),r.getDescription(),r.getDateRappel());
        this.Selected = false;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean Selected) {
        this.Selected = Selected;
    }

    public Reunion getReunion() {
        return reunion;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDateD() {
        return dateD;
    }

    public void setDateD(String dateD) {
        this.dateD = dateD;
    }

    public String getDateF() {
        return dateF;
    }

    public void setDateF(String dateF) {
        this.dateF = dateF;
    }
    
}
