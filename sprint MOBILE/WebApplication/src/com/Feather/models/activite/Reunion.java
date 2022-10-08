/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.models.activite;

import com.Feather.models.sprint.Sprint;
import com.Feather.models.user.User;
import com.Feather.utils.enumeration.TypeImportance;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Freddy
 */
public class Reunion extends Activite
{
    private String themeDuJour;
    private TypeImportance importance;
    private User coordonateur;
    private Sprint sprint;
    private ArrayList<Objectif> objectifs;
    private ArrayList<Integer> participants;
    
    public Reunion() {
    }

    public Reunion(String themeDuJour, TypeImportance importance, User coordonateur, Sprint sprint, ArrayList<Objectif> objectifs, int id, String titre, Date dateDebut, Date dateFin, String description,Date dateRappel) {
        super(id, titre, dateDebut, dateFin, description,dateRappel);
        this.themeDuJour = themeDuJour;
        this.importance = importance;
        this.coordonateur = coordonateur;
        this.sprint = sprint;
        this.objectifs = objectifs;
    }

    public Reunion(String themeDuJour, TypeImportance importance, User coordonateur, Sprint sprint, ArrayList<Objectif> objectifs, String titre, Date dateDebut, Date dateFin, String description,Date dateRappel) {
        super(titre, dateDebut, dateFin, description,dateRappel);
        this.themeDuJour = themeDuJour;
        this.importance = importance;
        this.coordonateur = coordonateur;
        this.sprint = sprint;
        this.objectifs = objectifs;
    }

    public String getThemeDuJour() {
        return themeDuJour;
    }

    public Reunion setThemeDuJour(String themeDuJour) {
        this.themeDuJour = themeDuJour;
        return this;
    }

    public TypeImportance getImportance() {
        return importance;
    }

    public Reunion setImportance(TypeImportance importance) {
        this.importance = importance;
        return this;
    }

    public ArrayList<Objectif> getObjectifs() {
        return objectifs;
    }

    public Reunion setObjectifs(ArrayList<Objectif> objectifs) {
        this.objectifs = objectifs;
        return this;
    }
    public User getCoordonateur() {
        return coordonateur;
    }

    public Reunion setCoordonateur(User coordonateur) {
        this.coordonateur = coordonateur;
        return this;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public Reunion setSprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }
    
    /**
     * Get the value of participants
     *
     * @return the value of participants
     */
    public ArrayList<Integer> getParticipants() {
        return participants;
    }

    /**
     * Set the value of participants
     *
     * @param participants new value of participants
     */
    public void setParticipants(ArrayList<Integer> participants) {
        this.participants = participants;
    }


    @Override
    public String toString() {
        return "Reunion{"+ super.toString() + "themeDuJour=" + themeDuJour + ", importance=" + importance.getText() + ", coordonateur=" + (coordonateur==null?" ":coordonateur.toString()) + ", sprint=" + ((sprint==null)?" ":sprint.toString()) + ", objectifs=" + objectifs.toString() + '}';
    }
    
    
    
    
}
