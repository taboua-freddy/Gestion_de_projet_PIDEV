/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.groupe;

/**
 *
 * @author Aymen Touihri
 */
public class Groupe {
    public int idGroupe;
        
    public Groupe() {
    }

    public Groupe(int idGroupe) {
        this.idGroupe=idGroupe;
    }

    public int getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
