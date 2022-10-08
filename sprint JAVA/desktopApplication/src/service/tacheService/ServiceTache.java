/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.tacheService;

import java.util.List;
import connexiondb.DataSource;
import entity.tache.TacheT;
import enumeration.TypeEtat;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.UIManager.getString;
import service.tache.Tservice;

/**
 *
 * @author maysa
 */

public class ServiceTache implements Tservice<TacheT>{
    private Connection cnx;
    private Statement st;
    private Statement ste;

    private PreparedStatement pst;
    private ResultSet rs;
      public ServiceTache(){
        cnx = DataSource.getInstance().getConnection();
    }
    public int counttodo(){
        try {
            int rowCount=0;
            String req="SELECT COUNT(*) FROM tache where etat='todo'";
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while(rs.next()){
             rowCount = rs.getInt(1);}
              rs.close();
            return rowCount;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    return 0;
    } 
    public int countdoing(){
        try {
            int rowCount=0;
            String req="SELECT COUNT(*) FROM tache where etat='doing'";
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while(rs.next()){
             rowCount = rs.getInt(1);}
              rs.close();
            return rowCount;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    return 0;
    } 
    public int countdone(){
        try {
            int rowCount=0;
            String req="SELECT COUNT(*) FROM tache where etat='done'";
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while(rs.next()){
             rowCount = rs.getInt(1);}
              rs.close();
            return rowCount;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    return 0;
    } 
   public void insertPST(TacheT t){
        String req="insert into tache (dateDebut, dateFin,priorite,etat,nom,description) values (?,?,?,?,?,?)";
        try {
            pst = cnx.prepareStatement(req);
            pst.setDate(1, t.getDateDebut());
            pst.setDate(2,t.getDateFin());
            pst.setInt(3, t.getPriorite());
            pst.setString(4,t.getEtat().toString());
            pst.setString(5, t.getNom());
            pst.setString(6, t.getDescription());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    public void updateddoing( int id){
               String String ="doing";

        String req="update tache set etat='doing' where idTache='"+id+"' ";
        try {
            pst = cnx.prepareStatement(req);
                        pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
      public void updateddone( int id){
               String String ="doing";

        String req="update tache set etat='done' where idTache='"+id+"' ";
        try {
            pst = cnx.prepareStatement(req);
                        pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
 public void insert(TacheT t){
        String req="insert into tache (priorite,nom,description) values (?,?,?)";
        try {
            pst = cnx.prepareStatement(req);
         
            pst.setInt(1, t.getPriorite());
            pst.setString(2, t.getNom());
            pst.setString(3, t.getDescription());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
   
  

    @Override
    public List<TacheT> displayAll() {
       List<TacheT> list=new ArrayList<>();
        String req = "select * from tache ";
        try {
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while(rs.next()){
           list.add(new TacheT(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getDate(4),rs.getDate(5), rs.getInt(6),rs.getString(7),  rs.getString(8),rs.getString(9) ));
                //(int idTache, int idStory, int idUser, java.util.Date dateDebut, java.util.Date dateFin,int priorite, String nom, TypeEtat etat, String description)
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list; //To change body of generated methods, choose Tools | Templates.
    }
    public List<TacheT> displayAlltodo() {
       List<TacheT> list=new ArrayList<>();
        String req = "select * from tache where etat='todo'";
        try {
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while(rs.next()){
           list.add(new TacheT(rs.getInt(1), rs.getInt(2),rs.getDate(3),rs.getDate(4), rs.getInt(5),rs.getString(6), rs.getString(7),rs.getString(8) ));
                //(int idTache, int idStory, int idUser, java.util.Date dateDebut, java.util.Date dateFin,int priorite, String nom, TypeEtat etat, String description)
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list; //To change body of generated methods, choose Tools | Templates.
    }
      public List<TacheT> displayAlldoing() {
       List<TacheT> list=new ArrayList<>();
        String req = "select * from tache where etat='doing'";
        try {
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while(rs.next()){
           list.add(new TacheT(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getDate(4),rs.getDate(5), rs.getInt(6),rs.getString(7),  rs.getString(8),rs.getString(9) ));
                //(int idTache, int idStory, int idUser, java.util.Date dateDebut, java.util.Date dateFin,int priorite, String nom, TypeEtat etat, String description)
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list; //To change body of generated methods, choose Tools | Templates.
    }
        public List<TacheT> displayAlldone() {
       List<TacheT> list=new ArrayList<>();
        String req = "select * from tache where etat='done'";
        try {
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while(rs.next()){
           list.add(new TacheT(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getDate(4),rs.getDate(5), rs.getInt(6),rs.getString(7),  rs.getString(8),rs.getString(9) ));
                //(int idTache, int idStory, int idUser, java.util.Date dateDebut, java.util.Date dateFin,int priorite, String nom, TypeEtat etat, String description)
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(int t) {
        try {
         pst = cnx.prepareStatement("delete from tache where idTache=?");
        pst.setInt(1,t);
        pst.execute(); 
    } catch (SQLException ex) {
        Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    public void update(TacheT t , int id) {
         { 
 
    
   String req = "update tache set  dateDebut=?,dateFin=?,priorite=?,etat=?,nom=?,description=? where idTache= '"+id+"'";
        
        try {
            pst = cnx.prepareStatement(req);
            pst.setDate(1, new java.sql.Date(t.getDateDebut().getDate()));
            pst.setDate(2,new java.sql.Date(t.getDateFin().getDate()));
            pst.setInt(3, t.getPriorite());
            pst.setString(4,t.getEtat().toString());
            pst.setString(5, t.getNom());
            pst.setString(6, t.getDescription());
            int exc = pst.executeUpdate();
            if(exc>0){
                System.err.println("mise à jour");
            }
            else{
                System.err.println("non mise à jour");
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    }
    /* private int idTache;
  private int idStory;
  private int idUser;
  private Date dateDebut;
  private Date dateFin;
  private int priorite;
  private String nom;
  private TypeEtat etat;
  private String description ;*/
/*lel front front ychouf b date wel bkia w bel user story */
    public List<TacheT> Findtache (String text) {
               List<TacheT> list=new ArrayList<>();

     try {
        Statement st = cnx.createStatement();
        String query="SELECT * FROM tache WHERE CONCAT(`dateDebut`, `dateFin`, `priorite`,`nom`,`etat`,`description`) LIKE '%"+text+"%'";
        ResultSet  rs =st.executeQuery(query);
        while(rs.next())
        {   
       list.add(new TacheT(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getDate(4),rs.getDate(5), rs.getInt(5),rs.getString("etat"),rs.getString(6),rs.getString(7)));        }  
        } catch (SQLException ex) {
          Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
    return list;
    } 
    
/* lel back
    lezem scrum master 
    ycherchi bl user w x bel user stories  bl bkya 
    
    */
     public List<TacheT> Findtachescrummaster (int text) {
               List<TacheT> list=new ArrayList<>();

     try {
        Statement st = cnx.createStatement();
        String query="SELECT t.idTache, t.nom, t.description FROM tache t, user u WHERE t.idUser=u.idUser  LIKE '%"+text+"%'";
        ResultSet  rs =st.executeQuery(query);
        while(rs.next())
        {   
       list.add(new TacheT(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getDate(4),rs.getDate(5), rs.getInt(6),rs.getString(7),  rs.getString(8),rs.getString(9) ));        }  
        } catch (SQLException ex) {
          Logger.getLogger(ServiceTache.class.getName()).log(Level.SEVERE, null, ex);
        }
    return list;
    } 

    @Override
    public void update(TacheT t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
