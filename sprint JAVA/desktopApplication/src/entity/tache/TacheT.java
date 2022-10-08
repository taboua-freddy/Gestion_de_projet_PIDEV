/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.tache;

import java.sql.Date;

/**
 *
 * @author Freddy
 */
public class TacheT {
     private int idTache;
  private int idStory;
  private int idUser;
  private Date dateDebut;
  private Date dateFin;
  private int priorite;
  private String nom;
  private String etat;
  private String description ;

  
  
  
        public TacheT() {
    }
        public TacheT(int idTache, int idStory, int idUser, Date dateDebut, Date dateFin,int priorite, String nom,String etat, String description) {
        this.idTache = idTache;
        this.idStory = idStory;
        this.idUser = idUser;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.priorite = priorite;
        this.nom = nom;
        this.etat = etat;
        this.description = description;
    }
        public TacheT(int idTache, int idStory, Date dateDebut, Date dateFin,int priorite, String nom,String etat, String description) {
        this.idTache = idTache;
        this.idStory = idStory;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.priorite = priorite;
        this.nom = nom;
        this.etat = etat;
        this.description = description;
    }

    public TacheT(Date dateDebut, Date dateFin, int priorite, String nom, String etat, String description) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.priorite = priorite;
        this.nom = nom;
        this.etat = etat;
        this.description = description;
    }
  public TacheT(int priorite, String nom,String description) {
      
        this.priorite = priorite;
        this.nom = nom;
        this.etat = etat;
        this.description = description;
    }

   

   

    
  

    public int getIdTache() {
        return idTache;
    }

    public int getIdStory() {
        return idStory;
    }

    public int getIdUser() {
        return idUser;
    }
    

   public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public int getPriorite() {
        return priorite;
    }

    public String getNom() {
        return nom;
    }

    public String getEtat() {
        return etat;
    }

    public String getDescription() {
        return description;
    }

    public void setIdTache(int idTache) {
        this.idTache = idTache;
    }

    public void setIdStory(int idStory) {
        this.idStory = idStory;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
