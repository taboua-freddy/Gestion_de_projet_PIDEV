/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.activite;

import entity.Sprint;
import entity.user.User;
import enumeration.TypeImportance;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

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
    
    public Reunion() {
    }

    public Reunion(String themeDuJour, TypeImportance importance, User coordonateur, Sprint sprint, ArrayList<Objectif> objectifs, int id, String titre, LocalDateTime dateDebut, LocalDateTime dateFin, String description,LocalDateTime dateRappel) {
        super(id, titre, dateDebut, dateFin, description,dateRappel);
        this.themeDuJour = themeDuJour;
        this.importance = importance;
        this.coordonateur = coordonateur;
        this.sprint = sprint;
        this.objectifs = objectifs;
    }

    public Reunion(String themeDuJour, TypeImportance importance, User coordonateur, Sprint sprint, ArrayList<Objectif> objectifs, String titre, LocalDateTime dateDebut, LocalDateTime dateFin, String description,LocalDateTime dateRappel) {
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

    @Override
    public String toString() {
        return "Reunion{"+ super.toString() + "themeDuJour=" + themeDuJour + ", importance=" + importance.getText() + ", coordonateur=" + (Objects.isNull(coordonateur)?" ":coordonateur.toString()) + ", sprint=" + (Objects.isNull(sprint)?" ":sprint.toString()) + ", objectifs=" + objectifs.toString() + '}';
    }
    
    
    
    
}
