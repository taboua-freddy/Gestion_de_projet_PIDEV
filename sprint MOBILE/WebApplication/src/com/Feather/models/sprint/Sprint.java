package com.Feather.models.sprint;

public class Sprint {

    private int ID;
    private int idProjet;
    private String nomSprint;
    private String dateDebut;
    private String dateFin;

    public Sprint() {
    }

    public Sprint(int idProjet, String nomSprint) {
        this.idProjet = idProjet;
        this.nomSprint = nomSprint;
    }
    
    public Sprint(int ID, int idProjet, String nomSprint1, String dateDebut, String dateFin) {
        this.nomSprint = nomSprint1;
        this.ID = ID;
        this.idProjet = idProjet;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getNomSprint() {
        return nomSprint;
    }

    public void setNomSprint(String nomSprint) {
        this.nomSprint = nomSprint;
    }

    @Override
    public String toString() {
        /*return "Sprint{" +
                "ID=" + ID +
                ", idProjet=" + idProjet +
                ", nomSprint='" + nomSprint + '\'' +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                '}';*/
        
        return nomSprint;
    }

}
