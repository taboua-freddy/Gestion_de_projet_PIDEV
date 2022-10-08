/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.reclamation;

import entity.user.User;
import java.sql.Date;

/**
 *
 * @author Legion
 */
public class Reclamation {
    private int idRec ;
    private User user ;
    private String description ;
    private int etat ;
    private Date dateRec ;
    private String reponse ;
    private TypeReclamation tr;
    

    public Reclamation() {
    }

    public Reclamation(int idRec, User user, String description, int etat, Date dateRec, String reponse,TypeReclamation tr ) {
        this.idRec = idRec;
        this.user = user;
        this.description = description;
        this.etat = etat;
        this.dateRec = dateRec;
        this.reponse = reponse;
        this.tr=tr;
        
    }

    public int getIdRec() {
        return idRec;
    }

    public void setIdRec(int idRec) {
        this.idRec = idRec;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public Date getDateRec() {
        return dateRec;
    }

    public void setDateRec(Date dateRec) {
        this.dateRec = dateRec;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public TypeReclamation getTr() {
        return tr;
    }

    public void setTr(TypeReclamation tr) {
        this.tr = tr;
    }

    @Override
    public String toString() {
        return "Reclamation{" + "idRec=" + idRec + ", user=" + user + ", description=" + description + ", etat=" + etat + ", dateRec=" + dateRec + ", reponse=" + reponse + ", tr=" + tr + '}';
    }
    
    
    
}
