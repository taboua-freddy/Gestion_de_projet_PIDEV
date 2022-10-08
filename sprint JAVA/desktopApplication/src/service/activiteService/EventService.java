/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.activiteService;

import connexiondb.DataSource;
import entity.activite.Evenement;
import entity.pojet.Projet;
import static entity.pojet.Projet.status.stringToEnum;
import functions.Functions;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.groupeService.GroupeService;

/**
 *
 * @author Freddy
 */
public class EventService 
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet res;
    GroupeService gs;
    private int exc;

    public EventService() {
        con = DataSource.getInstance().getConnection();
        gs = new GroupeService();
    }

    
    public int insertIdReturn(Evenement e,File file) {
        int idEvent = 0;
        String req = "insert into evenement (titre,dateDebut,dateFin,description,lieu,affiche,dateRappel) values(?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(req,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getTitre());
            ps.setTimestamp(2, Functions.dateTimeToTimestamp(e.getDateDebut()));
            ps.setTimestamp(3, Functions.dateTimeToTimestamp(e.getDateFin()));
            if(Objects.isNull(e.getDescription()))
                ps.setNull(4, Types.INTEGER);
            else
                ps.setString(4, e.getDescription());
            if(Objects.isNull(e.getLieu()))
                ps.setNull(5, Types.INTEGER);
            else
                 ps.setString(5, e.getLieu());
            if(Objects.isNull(e.getAffiche())|| file==null)
                ps.setNull(6, Types.INTEGER);
            else
                ps.setBinaryStream(6, e.getAffiche(),file.length());
            if(Objects.isNull(e.getDateRappel()))
                ps.setNull(7, Types.INTEGER);
            else
                ps.setTimestamp(7, Functions.dateTimeToTimestamp(e.getDateRappel()));
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("ajouté");
            }
            else{
                System.err.println("non ajouté");
            }
            res = ps.getGeneratedKeys();	
            if(res.next()){
                idEvent=res.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        return idEvent;
    }
    
    public void insertProjetToEvent(int idProjet,int idEvent)
    {
        String req = "insert into expose_projet (idProjet,idEvent) values(?,?)";
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idProjet);
            ps.setInt(2, idEvent);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("ajouté");
            }
            else{
                System.err.println("non ajouté");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void delete(int idEvent) {

        String req = "delete from evenement where idEvent=?";
        
        try {
            /**
             * suppression des dependances
             */
            this.deleteAllProjetToEvent(idEvent);
            
            ps = con.prepareStatement(req);
            ps.setInt(1, idEvent);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("supprimé");
            }
            else{
                System.err.println("non supprimé");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public void deleteProjetToEvent(int idEvent,int idprojet) {

        String req = "delete from expose_projet where idEvent=? and idProjet=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idEvent);
            ps.setInt(2, idprojet);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("supprimé");
            }
            else{
                System.err.println("non supprimé");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public void deleteAllProjetToEvent(int idEvent) {

        String req = "delete from expose_projet where idEvent=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idEvent);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("supprimé");
            }
            else{
                System.err.println("non supprimé");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    
    

    public void update(Evenement e,File file) {
        String req = "update evenement set titre=?,dateDebut=?,dateFin=?,description=?,lieu=?,affiche=?,dateRappel=? where idEvent=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setString(1, e.getTitre());
            ps.setTimestamp(2, Functions.dateTimeToTimestamp(e.getDateDebut()));
            ps.setTimestamp(3, Functions.dateTimeToTimestamp(e.getDateFin()));
            ps.setString(4, e.getDescription());
            ps.setString(5, e.getLieu());
            ps.setBinaryStream(6, e.getAffiche(),file.length());
            if(Objects.isNull(e.getDateRappel()))
                ps.setNull(7, Types.INTEGER);
            else
                ps.setTimestamp(7, Functions.dateTimeToTimestamp(e.getDateRappel()));
            ps.setInt(8, e.getId()); 
            
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("mise à jour");
            }
            else{
                System.err.println("non mise à jour");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }


    public Evenement display(int idEvent) {
        Evenement event = null;
        String req = "select * from evenement where idEvent=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idEvent);
            res = ps.executeQuery();
            while (res.next()) { 
                int id = res.getInt("idEvent");
                LocalDateTime dateDebut = Functions.TimestampToDateTime(res.getTimestamp("dateDebut"));
                LocalDateTime dateFin = Functions.TimestampToDateTime(res.getTimestamp("dateFin"));
                LocalDateTime dateRappel = null;
                if(res.getTimestamp("dateRappel")!=null)
                    dateRappel = Functions.TimestampToDateTime(res.getTimestamp("dateRappel"));
                event = new Evenement(res.getString("lieu"), id, res.getString("titre"),dateDebut,dateFin,res.getString("description"),res.getBinaryStream("affiche"),dateRappel);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return event;    
    }


    public List<Evenement> displayAll() {
        ArrayList<Evenement> list = new ArrayList<Evenement>() ;
        String req = "select * from evenement";
        
        try {
            ps = con.prepareStatement(req);
            res = ps.executeQuery();
            while (res.next()) { 
                int idEvent = res.getInt("idEvent");
                LocalDateTime dateDebut = Functions.TimestampToDateTime(res.getTimestamp("dateDebut"));
                LocalDateTime dateFin = Functions.TimestampToDateTime(res.getTimestamp("dateFin"));
                LocalDateTime dateRappel = null;
                if(res.getTimestamp("dateRappel")!=null)
                    dateRappel = Functions.TimestampToDateTime(res.getTimestamp("dateRappel"));
                list.add(new Evenement(res.getString("lieu"), idEvent, res.getString("titre"),dateDebut,dateFin,res.getString("description"),res.getBinaryStream("affiche"),dateRappel));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }    
    
    public List<Projet> displayProjetsEvenement(int idEvent) {
        ArrayList<Projet> list = new ArrayList<Projet>() ;
        String req = "select * from projet where idProjet in (select idProjet from expose_projet where idEvent=?)";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idEvent);
            res = ps.executeQuery();
            while (res.next()) { 
                Projet t = new Projet();
                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));
                list.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean existe(int idEvent) {
        return !Objects.isNull(display(idEvent));   
    }
    
    public boolean existeProjetEvent(int idEvent,int idProjet) {
        boolean existe = false;
        String req = "select * expose_projet where idEvent=? and idProjet=?)";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idEvent);
            ps.setInt(2, idProjet);
            res = ps.executeQuery();
            while (res.next()) { 
                existe =true;
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return  existe;
    }
}
