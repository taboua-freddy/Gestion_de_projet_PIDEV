/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.activiteService;

import service.activiteService.ObjectifService;
import connexiondb.DataSource;
import entity.activite.Objectif;
import entity.activite.Reunion;
import entity.Sprint;
import entity.user.User;
import enumeration.TypeImportance;
import functions.Functions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.sprintService.SprintService;
import service.userService.UserService;

/**
 *
 * @author Freddy
 */
public class ReunionService {
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet res;
    private int exc;
    private ObjectifService objectifService;
    private SprintService sprintService;
    private UserService userService;

    public ReunionService() {
        con = DataSource.getInstance().getConnection();
        objectifService = new ObjectifService();
        sprintService = new SprintService();
        userService = new UserService();
    }
    
    public int insert(Reunion r)
    {
        int idReunion = 0;
        String req = "insert into reunion (titre,coordonateur,idSprint,dateDebut,dateFin,description,themeDuJour,importance,dateRappel) values(?,?,?,?,?,?,?,?,?)";
        
        try {
            ps = con.prepareStatement(req,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, r.getTitre());
            if(Objects.isNull(r.getCoordonateur()))
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, r.getCoordonateur().getIdUser());
            
            if(Objects.isNull(r.getSprint()))
                ps.setNull(3, Types.INTEGER);
            else
                ps.setInt(3, r.getSprint().getID());
            
            ps.setTimestamp(4, Functions.dateTimeToTimestamp(r.getDateDebut()));
            ps.setTimestamp(5, Functions.dateTimeToTimestamp(r.getDateFin()));
            ps.setString(6, r.getDescription());
            ps.setString(7, r.getThemeDuJour());
            ps.setInt(8, r.getImportance().getValue());
            if(Objects.isNull(r.getDateRappel()))
                ps.setNull(9, Types.INTEGER);
            else
                ps.setTimestamp(9, Functions.dateTimeToTimestamp(r.getDateRappel()));
            
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("ajouté");
            }
            else{
                System.err.println("non ajouté");
            }
            res = ps.getGeneratedKeys();	
            if(res.next()){
                idReunion=res.getInt(1);
                for (Objectif objectif : r.getObjectifs()) {
                    System.out.println(objectif);
                     objectifService.insert(objectif, idReunion);
                }
                return idReunion;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        return idReunion;
    }
    
    public void insertUserToReunion(int idReunion,User u,int presence)
    {
        String req = "insert into participe_reunion (idUser,idReunion,presence) values(?,?,?)";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, u.getIdUser());
            ps.setInt(2, idReunion);
            ps.setInt(3, presence);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("ajouté");
            }
            else{
                System.err.println("non ajouté");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(Reunion r)
    {
        String req = "update reunion set titre=?,coordonateur=?,idSprint=?,dateDebut=?,dateFin=?,description=?,themeDuJour=?,importance=?,dateRappel=? where idReunion=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setString(1, r.getTitre());
            
            if(Objects.isNull(r.getCoordonateur()))
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, r.getCoordonateur().getIdUser());
            
            if(Objects.isNull(r.getSprint()))
                ps.setNull(3, Types.INTEGER);
            else
                ps.setInt(3, r.getSprint().getID());
            
            ps.setTimestamp(4, Functions.dateTimeToTimestamp(r.getDateDebut()));
            ps.setTimestamp(5, Functions.dateTimeToTimestamp(r.getDateFin()));
            ps.setString(6, r.getDescription());
            ps.setString(7, r.getThemeDuJour());
            ps.setInt(8, r.getImportance().getValue());
           if(Objects.isNull(r.getDateRappel()))
                ps.setNull(9, Types.INTEGER);
            else
                ps.setTimestamp(9, Functions.dateTimeToTimestamp(r.getDateRappel()));
            ps.setInt(10, r.getId());
            exc = ps.executeUpdate();
            if(exc>0){
                objectifService.deleteByContainer(r.getId());
                for (Objectif objectif : r.getObjectifs()) {
                     objectifService.insert(objectif, r.getId());
                }
                System.err.println("mise à jour");
            }
            else{
                System.err.println("non mise à jour");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updatePresenceReunion(int idReunion,int idUser,int presence)
    {
        String req = "update participe_reunion set presence=? where (idReunion=? and idUser=?)";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, presence);
            ps.setInt(2, idReunion);
            ps.setInt(3, idUser);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("mise à jour");
            }
            else{
                System.err.println("non mise à jour");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void delete(int idReunion)
    {
        String req = "delete from reunion where idReunion=?";
        
        try {
            /**
             * suppression des dépendances
             */
            objectifService.deleteByContainer(idReunion);
            this.deleteAllUsersToReunion(idReunion);
            
            ps = con.prepareStatement(req);
            ps.setInt(1, idReunion);
            //ps.setString(2, titre);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("supprimé");
            }
            else{
                System.err.println("non supprimé");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteUserToReunion(int idReunion,int idUser)
    {
        String req = "delete from participe_reunion where idUser=? and idReunion=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idUser);
            ps.setInt(2, idReunion);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("supprimé");
            }
            else{
                System.err.println("non supprimé");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ObjectifService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteCoordonateurToReunion(int idReunion)
    {
        String req = "update reunion set idUser=? where idReunion=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setNull(1, Types.INTEGER);
            ps.setInt(2, idReunion);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("supprimé");
            }
            else{
                System.err.println("non supprimé");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteSprintToReunion(int idReunion)
    {
        String req = "update reunion set idSprint=? where idReunion=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setNull(1, Types.INTEGER);
            ps.setInt(2, idReunion);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("supprimé");
            }
            else{
                System.err.println("non supprimé");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteAllUsersToReunion(int idReunion)
    {
        String req = "delete from participe_reunion where idReunion=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idReunion);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("supprimé");
            }
            else{
                System.err.println("non supprimé");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean existe(int idReunion,String titre)
    {   
        return !Objects.isNull(display(idReunion,titre));
    }
    
    public boolean existeUserReunion(int idReunion,int idUser)
    {
        String req = "select * from participe_reunion where idUser=? and idReunion=?";
        boolean existe = false;
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idUser);
            ps.setInt(2, idReunion);
            res = ps.executeQuery();
            while (res.next()) { 
                existe=true;
                break;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return existe;
    }
    
    public Reunion display(int idReunion,String titre)
    {
        Reunion reu = null;
        String req = "select * from reunion where idReunion=? or titre=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idReunion);
            ps.setString(2, titre);
            res = ps.executeQuery();
            while (res.next()) { 
                int id = res.getInt("idReunion");
                LocalDateTime dateDebut = Functions.TimestampToDateTime(res.getTimestamp("dateDebut"));
                LocalDateTime dateFin = Functions.TimestampToDateTime(res.getTimestamp("dateFin"));
                LocalDateTime dateRappel = null;
                if(res.getTimestamp("dateRappel")!=null)
                    dateRappel = Functions.TimestampToDateTime(res.getTimestamp("dateRappel"));
                TypeImportance importance = Functions.getImportance(res.getInt("importance"));
                User coordonateur = userService.displayUser(res.getInt("coordonateur")); 
                Sprint sprint = sprintService.displaySprint(res.getInt("idSprint"));
                ArrayList<Objectif> objectifs = objectifService.displayAll(idReunion);
                
                reu = new Reunion(res.getString("themeDuJour"), importance , coordonateur, sprint, objectifs, id, res.getString("titre"),dateDebut,dateFin,res.getString("description"),dateRappel);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reu;
    }
    /*public ArrayList<User> absentUsers()
    {
        ArrayList<User> list = new ArrayList<User>() ;
        String req = "select * from reunion";
        
        try {
            ps = con.prepareStatement(req);
            res = ps.executeQuery();
            while (res.next()) { 
                int idReunion = res.getInt("idReunion");
                LocalDateTime dateDebut = Functions.TimestampToDateTime(res.getTimestamp("dateDebut"));
                LocalDateTime dateFin = Functions.TimestampToDateTime(res.getTimestamp("dateFin"));
                LocalDateTime dateRappel = null;
                if(res.getTimestamp("dateRappel")!=null)
                    dateRappel = Functions.TimestampToDateTime(res.getTimestamp("dateRappel"));     
                TypeImportance importance = Functions.getImportance(res.getInt("importance")); 
                User coordonateur = userService.displayUser(res.getInt("idUser")); 
                Sprint sprint = sprintService.displaySprint(res.getInt("idSprint"));
                ArrayList<Objectif> objectifs = objectifService.displayAll(idReunion);
                
                list.add(new Reunion(res.getString("themeDuJour"), importance , coordonateur, sprint, objectifs, idReunion, res.getString("titre"),dateDebut,dateFin,res.getString("description"),dateRappel));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return list;
    }*/
    
    public ArrayList<Reunion> displayAll()
    {
        ArrayList<Reunion> list = new ArrayList<Reunion>() ;
        String req = "select * from reunion";
        
        try {
            ps = con.prepareStatement(req);
            res = ps.executeQuery();
            while (res.next()) { 
                int idReunion = res.getInt("idReunion");
                LocalDateTime dateDebut = Functions.TimestampToDateTime(res.getTimestamp("dateDebut"));
                LocalDateTime dateFin = Functions.TimestampToDateTime(res.getTimestamp("dateFin"));
                LocalDateTime dateRappel = null;
                if(res.getTimestamp("dateRappel")!=null)
                    dateRappel = Functions.TimestampToDateTime(res.getTimestamp("dateRappel"));     
                TypeImportance importance = Functions.getImportance(res.getInt("importance")); 
                User coordonateur = userService.displayUser(res.getInt("Coordonateur")); 
                Sprint sprint = sprintService.displaySprint(res.getInt("idSprint"));
                ArrayList<Objectif> objectifs = objectifService.displayAll(idReunion);
                
                list.add(new Reunion(res.getString("themeDuJour"), importance , coordonateur, sprint, objectifs, idReunion, res.getString("titre"),dateDebut,dateFin,res.getString("description"),dateRappel));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public Map<User,Integer> displayUsersReunion(int idReunion)
    {
        Map<User,Integer> listMap = new HashMap<User,Integer>() ;
        String req = "select u.*,p.presence as presence from user u inner join participe_reunion p"
                + " on u.idUser=p.idUser inner join reunion r on r.idReunion=p.idReunion where r.idReunion=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idReunion);
            res = ps.executeQuery();
            while (res.next()) { 
                listMap.put(new User(res.getInt("idUser"),res.getString("nom"),res.getString("prenom"),res.getString("email")), res.getInt("presence")); // a revoir
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listMap;
    }
    
    
    public ArrayList<Reunion> displayReunionsUser(int idUser)
    {
        ArrayList<Reunion> list = new ArrayList<Reunion>() ;
        String req = "select r.* from reunion r inner join participe_reunion p on p.idReunion=r.idReunion inner join "
                + " user u on p.idUser=u.idUser where u.idUser=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idUser);
            res = ps.executeQuery();
            while (res.next()) { 
                int idReunion = res.getInt("idReunion");
                LocalDateTime dateDebut = Functions.TimestampToDateTime(res.getTimestamp("dateDebut"));
                LocalDateTime dateFin = Functions.TimestampToDateTime(res.getTimestamp("dateFin"));
                LocalDateTime dateRappel = null;
                if(res.getTimestamp("dateRappel")!=null)
                    dateRappel = Functions.TimestampToDateTime(res.getTimestamp("dateRappel"));
                TypeImportance importance = Functions.getImportance(res.getInt("importance")); 
                                  
                User coordonateur = userService.displayUser(res.getInt("coordonateur")); 
                Sprint sprint = sprintService.displaySprint(res.getInt("idSprint"));
                ArrayList<Objectif> objectifs = objectifService.displayAll(idReunion);
                
                list.add(new Reunion(res.getString("themeDuJour"), importance , coordonateur, sprint, objectifs, idReunion, res.getString("titre"),dateDebut,dateFin,res.getString("description"),dateRappel));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public ArrayList<Reunion> RechercheReunion(LocalDateTime date)
    {
        ArrayList<Reunion> list = (ArrayList<Reunion>) this.displayAll();
        
        ArrayList<Reunion> listMatch = new ArrayList<>();
        
        for (Reunion reunion : list) {
            if(Functions.matchDate(reunion.getDateDebut(), reunion.getDateFin(), date))
                listMatch.add(reunion);
        }
        
        return listMatch;
    }
    
    public boolean getDisponibilite(LocalDateTime dateDebut,LocalDateTime dateFin, int idUser)
    {
        boolean dispo = true;
        
        ArrayList<Reunion> listReunions = (ArrayList)this.displayReunionsUser(idUser);
        
        for (Reunion listReunion : listReunions) {
            if(Functions.confitDate(listReunion.getDateDebut(), listReunion.getDateFin(), dateDebut, dateFin))
                return false;
        }
        
        return dispo;
    }
      
}
