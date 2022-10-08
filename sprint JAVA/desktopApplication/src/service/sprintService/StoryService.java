package service.sprintService;

import connexiondb.DataSource;
import entity.Story;
import java.util.Scanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public  class StoryService implements IStory<Story> {
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;
    private Connection cnx;




    public StoryService() throws SQLException {
        cnx = DataSource.getInstance().getConnection();
    }
    
    public void insert(Story s) throws SQLException {
        String req = "INSERT INTO user_story (idProjet,nomStory,bv,c,priorite,complexite,description,ROI) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        pst = cnx.prepareStatement(req);
        pst.setInt(1,s.getIdProjet());
        pst.setString(2,s.getNomStory());
        pst.setInt(3,s.getBV());
        pst.setInt(4,s.getCap());
        pst.setInt(5,s.getPriorite());
        pst.setFloat(6,s.getComplexite());
        pst.setString(7,s.getDescription());
        pst.setFloat(8,s.getROI());
        pst.executeUpdate();
    }

    public List<Story> displayAll() {
        List<Story> list = new ArrayList<>();
        String req = "SELECT * FROM user_story";

        try {
            st = cnx.createStatement();
            rs=st.executeQuery(req);
            System.out.println("Getting DATA from DATABASE!!!!!!");
            while(rs.next()){
                list.add(new Story(rs.getInt(2),rs.getString("nomStory"),
                        rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getFloat(7),
                        rs.getString("description"),rs.getFloat(9)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void delete(int id) throws SQLException {

        String req = "DELETE FROM user_story WHERE idStory=?";

        pst = cnx.prepareStatement(req);
        pst.setInt(1,id);
        pst.executeUpdate();
        System.out.println("Done");
    }
    public void update(Story s) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Give a new Story");
        String a = sc.nextLine();
        String req = "UPDATE user_story SET nomStory=? WHERE idStory=?";

        pst = cnx.prepareStatement(req);
        pst.setString(1,a);
        pst.setInt(2,2);
        pst.executeUpdate();
        System.out.println("Done");


    }
}
