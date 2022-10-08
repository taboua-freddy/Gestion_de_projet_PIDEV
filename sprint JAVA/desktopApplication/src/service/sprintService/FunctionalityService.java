package service.sprintService;

import connexiondb.DataSource;
import entity.Functionnality;
import entity.Sprint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FunctionalityService implements IFunctionality<Functionnality> {

    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;
    private Connection cnx;


    public FunctionalityService() throws SQLException {
        cnx = DataSource.getInstance().getConnection();
    }

    public void insert(Functionnality fn) throws SQLException {
        String req = "INSERT INTO fonctionnalite (idFn,idSprint,nomFn,priorite) " +
                "VALUES(?,?,?,?)";

        pst = cnx.prepareStatement(req);
        pst.setInt(1,fn.getIdFn());
        pst.setInt(2,fn.getIdSprint());
        pst.setString(3,fn.getNomFn());
        pst.setInt(4,fn.getPriorite());
        pst.executeUpdate();
        System.out.println("Successfully Done!");
    }
    public List<Functionnality> displayAll() {
        List<Functionnality> list = new ArrayList<>();
        String req = "SELECT * FROM fonctionalite";

        try {
            st = cnx.createStatement();
            rs=st.executeQuery(req);
            System.out.println("Getting DATA from DATABASE!!!!!!");
            while(rs.next()){
                list.add(new Functionnality(rs.getInt(1),rs.getInt(2),rs.getString(3),
                        rs.getDate(4).toString(),rs.getDate(5).toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void delete(int id) throws SQLException {

    }

    public void update(Functionnality functionnality) throws SQLException {

    }
}
