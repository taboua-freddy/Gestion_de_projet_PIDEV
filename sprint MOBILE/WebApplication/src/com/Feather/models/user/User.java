/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.models.user;

import com.Feather.models.groupe.Groupe;


/**
 *
 * @author Freddy
 */
public class User {
    private int idUser;
    private String nom;
    private String prenom;
    private String password;
    private String email;
    private String typeUser;
    private Groupe idGroupe;
    
    private int presence;

    /**
     * Get the value of presence
     *
     * @return the value of presence
     */
    public int getPresence() {
        return presence;
    }

    /**
     * Set the value of presence
     *
     * @param presence new value of presence
     */
    public void setPresence(int presence) {
        this.presence = presence;
    }


    public User() {
    }

    public User(int idUser) {
        this.idUser = idUser;
    }

    public User(int idUser, String nom, String prenom, String email) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }
    public User(int id, String nom, String prenom, Groupe idGroupe, String type, String email, String password) {
        this.idUser = id;
        this.nom = nom;
        this.prenom = prenom;
        this.typeUser = type;
        this.idGroupe =  idGroupe;
        this.email= email;
        this.password= password;
    }

    public User(String email,String Pass ) {
     
        this.email= email;
        this.password= Pass;
      
    }
    
    public User(int id , String pass){
        this.idUser= id;
        this.password= pass;
    } 
    
    
    public User(String nom, String prenom,String type, Groupe idGroupe, String email, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.typeUser = type;
        this.idGroupe =  idGroupe;
        this.email= email;
        this.password= password;
    }

    public User(int id, String nom, String prenom, Groupe idGroupe, String type ) {
        this.idUser = id;
        this.nom = nom;
        this.prenom = prenom;
        this.typeUser = type;
        this.idGroupe =  idGroupe;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Groupe getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Groupe idGroupe) {
        this.idGroupe = idGroupe;
    }

    
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }
    
    public boolean isAdmin()
    {
        return this.typeUser.equals("admin");
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
        final User other = (User) obj;
        return this.idUser == other.idUser;
    }

    @Override
    public String toString() {
        //return "User{" + "idUser=" + idUser + ", nom=" + nom + ", prenom=" + prenom + ", password=" + password + ", email=" + email + ", typeUser=" + typeUser + ", idGroupe=" + idGroupe + '}';
        return this.nom + " " + this.prenom;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.idUser;
        return hash;
    }
    
     public int generateCode()
    {
        int code=25002;
        code=code*2 + this.idUser;
        return code;
    }
    
    
    
}
