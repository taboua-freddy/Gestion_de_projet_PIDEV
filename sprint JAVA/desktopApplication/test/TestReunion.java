

import entity.activite.Objectif;
import entity.activite.Reunion;

import entity.Sprint;
import entity.user.User;
import enumeration.TypeImportance;
import functions.SendMail;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import service.activiteService.EventService;
import service.activiteService.ReunionService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Freddy
 */
public class TestReunion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReunionService rs = new ReunionService();
        
        
        Objectif o1= new Objectif("manger");
        Objectif o2= new Objectif("dormir");
        Objectif o3= new Objectif("courrir");
        
        ArrayList<Objectif> objectifs = new ArrayList<Objectif>();
        
        objectifs.add(o1);
        objectifs.add(o2);
        objectifs.add(o3);
        
        User u1 = new User(1);
        User u2 = new User(2);
        
        Sprint sp1 = new Sprint(1,"sprint1 ");
        Sprint sp2 = new Sprint(2,"sprint2 ");
        
        Reunion r1 = new Reunion("presentation du projet", TypeImportance.GRANDE, u2, sp2, objectifs, "reunion", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "description",null);
        
        Reunion r2 = new Reunion("presentation du projet 2", TypeImportance.FAIBLE, u1, sp1, objectifs, "reunion2", LocalDateTime.now(), LocalDateTime.now().plusDays(5), "ma description",null);
        
        System.out.println();
        int idReunion1 = 0;
        int idReunion2 = 0;
             
        
        
        if(!rs.existe(0, r1.getTitre()))
            idReunion1 = rs.insert(r1);
        else
            System.out.println("cet evenement existe deja");
        
        if(!rs.existe(0, r2.getTitre()))
            idReunion2 = rs.insert(r2);
        else
            System.out.println("cet evenement existe deja");
        
        rs.displayAll().forEach(e->{
            System.out.println(e);
        });
        
        
        if(idReunion1!=0)
            rs.delete(idReunion1);
        
        idReunion2 = 8;
        /*if (rs.existe(idReunion2, null)) {
            Reunion r3 = rs.display(idReunion2, null);
            r3.setCoordonateur(null);
            r3.setImportance(TypeImportance.MOYENNE);
            for (Objectif objectif : r3.getObjectifs()) {
                objectif.setObjectif("mon nouvel objectif");
                break;
            }
            rs.update(r3);
        }
        
        rs.displayAll().forEach(e->{
            System.out.println(e);
        });
        
        
        if(!rs.existeUserReunion(idReunion2, 1))
            rs.insertUserToReunion(idReunion2, 1, 0);
        else
            System.out.println("cet utilisateur est deja convié a la reunion");
        if(!rs.existeUserReunion(idReunion2, 2))
            rs.insertUserToReunion(idReunion2, 2, 0);
        else
            System.out.println("cet utilisateur est deja convié a la reunion");
        
        rs.displayUsersReunion(idReunion2).forEach((u,p)->{
            System.out.println(u + " presence = " + p);
        });
        
        rs.deleteUserToReunion(idReunion2, 1);
        
        rs.updatePresenceReunion(idReunion2, 2, 1);
        
        rs.deleteSprintToReunion(8);
        
        rs.displayUsersReunion(idReunion2).forEach((u,p)->{
            System.out.println(u + " presence = " + p);
        });
        */
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        boolean dispo = rs.getDisponibilite(LocalDateTime.parse("2020-02-10 00:00:00" , formatter), LocalDateTime.parse("2020-02-12 19:44:48", formatter), 2);
        
        boolean dispo1 = rs.getDisponibilite(LocalDateTime.parse("2020-02-17 20:44:48", formatter), LocalDateTime.parse("2020-02-18 19:44:48", formatter), 2);
        
        //System.err.println(dispo + " " + dispo1 );

        
        //rs.RechercheReunion(LocalDateTime.parse("2020-02-14 00:00:00" , formatter)).forEach(System.out::println);
        
        //SendMail.sendMail("tabouaf@gmail.com", "bonjour", "nouveaux message");
    
    
    }
    
}
