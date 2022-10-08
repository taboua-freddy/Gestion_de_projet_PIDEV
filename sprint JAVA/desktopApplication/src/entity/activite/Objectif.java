/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.activite;


/**
 *
 * @author Freddy
 */
public class Objectif {
    private int idObjectif;
    private String objectif;

    public Objectif() {
    }

    public Objectif(int idObjectif, String objectif) {
        this.idObjectif = idObjectif;
        this.objectif = objectif;
    }

    public Objectif(String objectif) {
        this.objectif = objectif;
    }

    public int getIdObjectif() {
        return idObjectif;
    }

    public Objectif setIdObjectif(int idObjectif) {
        this.idObjectif = idObjectif;
        return this;
    }

    public String getObjectif() {
        return objectif;
    }

    public Objectif setObjectif(String objectif) {
        this.objectif = objectif;
        return this;
    }
 
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.idObjectif;
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
        final Objectif other = (Objectif) obj;
        if (this.idObjectif != other.idObjectif) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Objectif{" + "idObjectif=" + idObjectif + ", objectif=" + objectif + '}';
    }
    
}
