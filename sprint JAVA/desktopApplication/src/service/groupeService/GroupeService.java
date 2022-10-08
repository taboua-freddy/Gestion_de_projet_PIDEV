/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.groupeService;


import connexiondb.DataSource;
import entity.groupe.Groupe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import service.projetService.IService;


/**
 *
 * @author Aymen Touihri
 */
public class GroupeService implements IService{

     private Connection cnx;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;

    public GroupeService() {    
         cnx= DataSource.getInstance().getConnection();
    }
    
    
    
    @Override
    public void insert(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List displayAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   public Groupe getGroupeById(int idGroupe){
       
       Groupe gp = new Groupe();

        try {
            st = cnx.createStatement();
            ResultSet res = st.executeQuery(("SELECT * FROM groupe_travail WHERE idGroupe = " + idGroupe + ";"));
            while(res.next()){
                gp.setIdGroupe(res.getInt("idGroupe"));
 
        }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

       return gp;
   }

    @Override
    public void insert(Object t, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int insertIdReturn(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteByContainer(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object display(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List displayAll(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean existe(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
