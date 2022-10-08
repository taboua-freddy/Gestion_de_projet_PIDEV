/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.activites;

import entity.user.User;
import java.util.Map;

/**
 *
 * @author Freddy
 */
public class UserP
{
    public String nom;
    public String prenom;
    public String presence;

    public UserP(Map.Entry<User, Integer> entry) {
        User user = entry.getKey();
        this.presence = ( entry.getValue()==1 )?"Sera présent":"Ne sera pas présent";
        this.nom = user.getNom();
        this.prenom = user.getPrenom();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    @Override
    public String toString() {
        return "UserP{" + "nom=" + nom + ", prenom=" + prenom + ", presence=" + presence + '}';
    }
        
}
