/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.activiteService;

import connexiondb.DataSource;
import entity.activite.Objectif;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Freddy
 */
public class ObjectifService implements IService<Objectif>
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet res;
    private int exc;

    public ObjectifService() {
        con = DataSource.getInstance().getConnection();
    }
    
    @Override
    public void insert(Objectif o,int idReunion)
    {
        String req = "insert into objectif (objectif,idReunion) values(?,?)";
        
        try {
            ps = con.prepareStatement(req);
            ps.setString(1, o.getObjectif());
            ps.setInt(2, idReunion);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("ajouté");
            }
            else{
                System.err.println("non ajouté");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ObjectifService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @Override
    public void update(Objectif o)
    {
        String req = "update objectif set objectif=? where idObjectif=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setString(1, o.getObjectif());
            ps.setInt(2, o.getIdObjectif());
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("mise à jour");
            }
            else{
                System.err.println("non mise à jour");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ObjectifService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updateByContainer(Objectif o,int idReunion)
    {
        String req = "update objectif set objectif=? where idObjectif=? and idReunion=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setString(1, o.getObjectif());
            ps.setInt(2, o.getIdObjectif());
            ps.setInt(3, idReunion);
            exc = ps.executeUpdate();
            if(exc>0){
                System.err.println("mise à jour");
            }
            else{
                System.err.println("non mise à jour");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ObjectifService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void deleteByContainer(int idReunion)
    {
        String req = "delete from objectif where idReunion=?";
        
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
            Logger.getLogger(ObjectifService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void delete(int idObjectif)
    {
        String req = "delete from objectif where idObjectif=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idObjectif);
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
    
    @Override
    public boolean existe(int idObjectif)
    {
        return !Objects.isNull(display(idObjectif));
    }
    
    @Override
    public Objectif display(int idObjectif)
    {
        Objectif obj = null ;
        String req = "select * from objectif  where idObjectif=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idObjectif);
            res = ps.executeQuery();
            while (res.next()) { 
                obj.setIdObjectif(res.getInt("idObjectif")).setObjectif(res.getString("objectif"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }
    
    @Override
    public ArrayList<Objectif> displayAll(int idReunion)
    {
        ArrayList<Objectif> list = new ArrayList<Objectif>() ;
        String req = "select * from objectif where idReunion=?";
        
        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idReunion);
            res = ps.executeQuery();
            while (res.next()) { 
                list.add(new Objectif(res.getInt("idObjectif"), res.getString("objectif")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    

    @Override
    public void insert(Objectif t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int insertIdReturn(Objectif t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Objectif> displayAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
