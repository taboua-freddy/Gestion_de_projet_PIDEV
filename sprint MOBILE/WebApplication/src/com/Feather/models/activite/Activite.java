/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.models.activite;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Freddy
 */
abstract class Activite {
    protected int id;
    protected String titre;
    protected Date dateDebut;
    protected Date dateFin;
    protected String description;
    protected Date dateRappel;

    public Activite() {
    }

    public Activite(int id, String titre, Date dateDebut, Date dateFin, String description,Date dateRappel) {
        this.id = id;
        this.titre = titre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.dateRappel = dateRappel;
    }

    public Activite(String titre, Date dateDebut, Date dateFin, String description,Date dateRappel) {
        this.titre = titre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.dateRappel = dateRappel;
    }

    public int getId() {
        return id;
    }

    public Activite setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateRappel() {
        return dateRappel;
    }

    public void setDateRappel(Date dateRappel) {
        this.dateRappel = dateRappel;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.titre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Activite other = (Activite) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.titre, other.titre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Activite{" + "id=" + id + ", titre=" + titre + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", description=" + description + ", dateRappel=" + dateRappel + '}';
    }

    
    
    
}
