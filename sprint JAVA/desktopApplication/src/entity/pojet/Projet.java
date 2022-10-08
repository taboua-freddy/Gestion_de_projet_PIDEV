/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.pojet;

import java.sql.Date;

/**
 *
 * @author Aymen Touihri
 */
public class Projet {

    public enum status {
        INPROGRESS,
        FINISHED,
        NOTSTARTED,
        ONHOLD;

        public static String enumToString(status pType) {
            return pType.toString();
        }

        public String enumToString() {
            return this.toString();
        }

        public static status stringToEnum(String pTypeStr) {
            switch (pTypeStr.toUpperCase()) {
                case "INPROGRESS":
                    return status.INPROGRESS;
                case "FINISHED":
                    return status.FINISHED;
                case "ONHOLD":
                    return status.ONHOLD;
                case "NOTSTARTED":
                    return status.NOTSTARTED;
                default:
                    return status.INPROGRESS;
            }
        }
    }
    private int idProjet;
    private String Nom;
    private Date dateDebut;
    private Date dateFin;
    private String description;
    private status Status;

    public Projet(int idProjet, String Nom, Date dateDebut, Date dateFin, String description, status Status) {
        this.idProjet = idProjet;
        this.Nom = Nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.Status = Status;
    }

     public Projet(int idProjet,String nom) {
        this.idProjet = idProjet;
        this.Nom = nom;
    }
     
    public Projet() {
    }

    public int getIdProjet() {
        return idProjet;
    }

    public status getStatus() {
        return Status;
    }

    public void setStatus(status Status) {
        this.Status = Status;
    }

    public String getNom() {
        return Nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "projet{" + "idProjet=" + idProjet + ", Nom=" + Nom + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", description=" + description + '}';
    }

}
