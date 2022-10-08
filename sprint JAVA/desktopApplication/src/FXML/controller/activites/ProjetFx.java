/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.activites;

import entity.pojet.Projet;

/**
 *
 * @author Freddy
 */
class ProjetFx extends Projet{
    private Boolean Selected;
    private Projet projet;

    public ProjetFx(Projet p) {
        super(p.getIdProjet(),p.getNom());
        projet = new Projet(p.getIdProjet(),p.getNom());
        this.Selected = false;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean Selected) {
        this.Selected = Selected;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }
    
}
