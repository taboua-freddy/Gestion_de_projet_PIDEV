/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.reclamation;

import connexiondb.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import entity.reclamation.Reclamation;
import entity.user.User;
import entity.reclamation.TypeReclamation;
import interfaces.ReclamationInterfa;


/**
 *
 * @author Legion
 */
public class ReclamationServices implements ReclamationInterfa{
     private Connection cnx ;
    private Statement st;
    private PreparedStatement pst ;
    private ResultSet rs ;

    public ReclamationServices() {
        cnx= DataSource.getInstance().getConnection();
    }
    
    
 @Override
    public void addReclation(Reclamation rec) {
        String request = "insert into `reclamation` SET idUser=?,description=?,etat=?,dateRec=?,reponse=?,typeRec=?";
        try {
            System.err.println("000000 ici bien");
         PreparedStatement st = cnx.prepareStatement(request);
        st.setInt(1,rec.getUser().getIdUser());
        st.setString(2,rec.getDescription());
         System.err.println("111111 ici bien");
        st.setInt(3,rec.getEtat());
        st.setDate(4,rec.getDateRec());
        st.setString(5,rec.getReponse());
            System.err.println("22222 ici bien");
        st.setString(6,rec.getTr().getText());
        st.executeUpdate();
     } catch (Exception ex) {
          System.out.println("Error in methode addreclamation"+ex.getMessage());
     }
    }

    @Override
    public void deleteReclamation(int id_rec) {
       
           String request = "delete from reclamation where idRec=?";
          
      try{
          PreparedStatement st = cnx.prepareStatement(request);
          st.setInt(1, id_rec);
          st.executeUpdate();
       }catch(Exception ex){
           System.out.println("erreur in delete reclamation"+ex);
       }
    }

    

   public  TypeReclamation getType(String TypeRec)
    {
        TypeReclamation type1 =null ;
        for(TypeReclamation t : TypeReclamation.values()){
            if(TypeRec.equals(t.getText()))
            {
                type1 = t;
            }
        }
        return type1;
    }
     @Override
    public ObservableList<Reclamation> displayAll() {
        
        ObservableList <Reclamation> list = FXCollections.observableArrayList();
        
        String req="select * From reclamation " ;
        try {
            
            
            st= cnx.createStatement();
            rs=st.executeQuery(req);
            
            while(rs.next()){
                
                User u = new User(rs.getInt(2));
                list.add(new Reclamation(rs.getInt(1),u,rs.getString(3),rs.getInt(4),
                        rs.getDate(5),rs.getString(6),this.getType(rs.getString("typeRec"))));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReclamationServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public int nbReclamByIdUsr(int id){
      int nb=0;
       String request = "SELECT count(*) FROM reclamation WHERE idUser=?";
       try{
           PreparedStatement st;
            st = cnx.prepareStatement(request);
              st.setInt(1, id);
              
            ResultSet rs = st.executeQuery();
             while(rs.next()){
              nb = rs.getInt(1);
            
            }
       }catch(Exception ex){
           System.out.println("exeptionn in nb reclamation"+ex);
       }
      return nb;
    }
    
    
     @Override
    public ObservableList<Reclamation> findReclamById(int id) {
       ObservableList <Reclamation> list = FXCollections.observableArrayList();
        Reclamation rec = null;
        String request = "SELECT * FROM reclamation WHERE idUser= "+id+";";
         try{
            st= cnx.createStatement();
            ResultSet rs = st.executeQuery(request);
            while(rs.next()){
                            User u = new User(rs.getInt(2));
             rec = new Reclamation(rs.getInt(1),u,rs.getString(3),rs.getInt(4),
                        rs.getDate(5),rs.getString(6),this.getType(rs.getString("typeRec")));
              list.add(rec);
            }
        }catch(Exception ex ){
            System.out.println("Error find ReclamById  " +ex);
        }
        return list;
    }  
    
    
    @Override
    public ObservableList<Reclamation> rechercherRec(Date dt) {
        ObservableList<Reclamation> list=FXCollections.observableArrayList();
        Reclamation rec = null;
        String req = "select * from reclamation where (dateRec like '"+dt+"%')";
        try {
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while(rs.next()){
                User u = new User(rs.getInt(2));
             rec = new Reclamation(rs.getInt(1),u,rs.getString(3),rs.getInt(4),
                        rs.getDate(5),rs.getString(6),this.getType(rs.getString("typeRec")));
              list.add(rec);
            }
        } catch (SQLException ex) {
            System.out.println("Error find Reclamation  " +ex);
        }
        return list;
    }
    
    
    public int nbReclamTotal(){
      int nb=0;
       String request = "SELECT count(*) FROM reclamation";
       try{
           PreparedStatement st;
            st = cnx.prepareStatement(request);
            ResultSet rs = st.executeQuery();
             while(rs.next()){
              nb = rs.getInt(1);
            }
       }catch(Exception ex){
           System.out.println("exeptionn in nombre reclamation"+ex);
       }
      return nb;
    }
     @Override
     public void updateReclamation(Reclamation Rec, int id ) {
         try {
            Statement statement= cnx.createStatement();
            String requete="update reclamation set reponse='"+Rec.getReponse()+"' where idRec= '"+id+"'";
            statement.executeUpdate(requete);
            System.out.print("Updated !!");
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        
    }
     public void updateEtat(Reclamation Rec, int id ) {
         try {
            Statement statement= cnx.createStatement();
            String requete="update reclamation set etat='"+Rec.getEtat()+"' where idRec= '"+id+"'";
            statement.executeUpdate(requete);
           
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        
    }
}
