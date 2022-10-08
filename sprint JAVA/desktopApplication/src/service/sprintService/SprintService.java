/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.sprintService;

import connexiondb.DataSource;
import entity.Sprint;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.activiteService.ReunionService;

/**
 *
 * @author Freddy
 */
public class SprintService implements services.ISprint<Sprint>{
    private Connection cnx;
    private Statement st;
    private PreparedStatement pst;
    private PreparedStatement ps;
    private ResultSet rs;
    private int exc;

    public SprintService() {
        cnx = DataSource.getInstance().getConnection();
    }
    
    @Override
    public void insert(Sprint sp) throws SQLException {

        String req = "INSERT INTO sprint (idSprint,idProjet,nomSprint,dateDebut,dateFin) " +
                "VALUES(?,?,?,?,?)";

        pst = cnx.prepareStatement(req);
        pst.setInt(1,sp.getID());
        pst.setInt(2,sp.getIdProjet());
        pst.setString(3,sp.getNomSprint());
        pst.setDate(4,java.sql.Date.valueOf(sp.getDateDebut()));
        pst.setDate(5,java.sql.Date.valueOf(sp.getDateFin()));
        pst.executeUpdate();
        System.out.println("Successfully Done!");

    }

    @Override
    public List<Sprint> displayAll() {
        List<Sprint> list = new ArrayList<>();
        String req = "SELECT * FROM sprint";

        try {
            st = cnx.createStatement();
            rs=st.executeQuery(req);
            System.out.println("Getting DATA from DATABASE!!!!!!");
            while(rs.next()){
                list.add(new Sprint(rs.getInt(1),rs.getInt(2),rs.getString(3),
                        rs.getDate(4).toString(),rs.getDate(5).toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void delete(int id) throws SQLException {

        String req = "DELETE FROM sprint WHERE idSprint=?";

        pst = cnx.prepareStatement(req);
        pst.setInt(1,id);
        pst.executeUpdate();
        System.out.println("Successfully Deleted!");

    }

    @Override
    public void update(Sprint sprint) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Give a new Date");
        String a = sc.nextLine();
        String req = "UPDATE sprint SET dateDebut=? WHERE idSprint=?";

        pst = cnx.prepareStatement(req);
        pst.setDate(1, Date.valueOf(a));
        pst.setInt(2,1);
        pst.executeUpdate();
        System.out.println("Successfully Updated!");

    }
    
    public Sprint displaySprint(int idSprint)
    {
        Sprint s = null;
        String req = "select * from sprint where idSprint=?";
        
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, idSprint);
            rs = ps.executeQuery();
            while (rs.next()) { 
                
                s = new Sprint(rs.getInt("idSprint"),rs.getInt("idprojet"),rs.getString("nomSprint"),rs.getString("dateDebut"),rs.getString("dateFin"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
    
    public ArrayList<Sprint> displaySprintProjet(int idProjet)
    {
        ArrayList<Sprint> list = new ArrayList<Sprint>() ;
        String req = "select * from Sprint"
                + " where idProjet=? ";
        
        try {
            ps = cnx.prepareStatement(req);
            ps.setInt(1, idProjet);
            rs = ps.executeQuery();
            while (rs.next()) { 
                
                list.add(new Sprint(rs.getInt("idSprint"),rs.getInt("idprojet"),rs.getString("nomSprint"),rs.getString("dateDebut"),rs.getString("dateFin")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
