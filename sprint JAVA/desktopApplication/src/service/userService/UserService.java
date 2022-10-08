/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.userService;

import connexiondb.DataSource;
import connexiondb.StaticValue;
import entity.groupe.Groupe;
import entity.user.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.activiteService.ReunionService;

/**
 *
 * @author Freddy
 */
public class UserService  {

    private Connection con;
    private PreparedStatement ps;
    private Statement ste;
    private ResultSet res;
    private int exc;

    public UserService() {
        con = DataSource.getInstance().getConnection();

    }

    public User existUser(String email, String motPass) {
        User ut = null;
        try {
            String sql = "SELECT * FROM user WHERE email = ?";// AND password = ?
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
           // ps.setString(2, motPass);
            res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("idUser");
                String noms = res.getString("nom");
                String prenoms = res.getString("prenom");
                String type = res.getString("typeuser");
                String emails = res.getString("email");
                String password = res.getString("password");
                ut = new User(id, noms, prenoms, type, emails, password);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ut;
    }

    public void connect(User u) {
        StaticValue.utilisateur = u;
    }

    public User displayUser(int idUser) {
        User u = null;
        String req = "select * from User where idUser=?";

        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idUser);
            res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("idUser");

                u = new User(id, res.getString("nom"), res.getString("prenom"), res.getString("email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public User displayUser(String nom, String prenom) {
        User u = null;
        String req = "select * from User where nom=? and prenom=?";

        try {
            ps = con.prepareStatement(req);
            ps.setString(1, nom);
            ps.setString(2, prenom);
            res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("idUser");

                u = new User(id, res.getString("nom"), res.getString("prenom"), res.getString("email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public ArrayList<User> displayUsers() {
        ArrayList<User> list = new ArrayList<User>();
        String req = "select * from User";

        try {
            ps = con.prepareStatement(req);
            res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("idUser");

                list.add(new User(id, res.getString("nom"), res.getString("prenom"), res.getString("email")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ArrayList<User> displayUsersProjet(int idProjet) {
        ArrayList<User> list = new ArrayList<User>();
        String req = "select u.* from User u inner join user_usergroup ug on u.iduser=ug.user_id inner join "
                + " groupe_travail g on g.idGroupe=ug.usergroup_id "
                + "where g.id_projet_id=? order by nom,prenom asc";

        try {
            ps = con.prepareStatement(req);
            ps.setInt(1, idProjet);
            res = ps.executeQuery();
            while (res.next()) {
                int id = res.getInt("idUser");

                list.add(new User(id, res.getString("nom"), res.getString("prenom"), res.getString("email")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReunionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void ajouter(User t) throws SQLException {
        ste = con.createStatement();
        String requeteInsert = "INSERT INTO user (`idUser`,`email` , `password` , `nom`, `prenom`, `typeuser`) VALUES (NULL, '" + t.getEmail() + "','" + t.getPassword() + "','" + t.getNom() + "', '" + t.getPrenom() + "', '" + t.getTypeUser()+ "');";
        ste.executeUpdate(requeteInsert);
    }

    public void ajouter1(User p) throws SQLException {
        PreparedStatement pre = con.prepareStatement("INSERT INTO user ( `email`,`password`,`nom`, `prenom`, `typeuser`) VALUES ( ?, ?, ?, ?, ?);");
        pre.setString(1, p.getEmail());
        pre.setString(2, p.getPassword());
        pre.setString(3, p.getNom());
        pre.setString(4, p.getPrenom());
        pre.setString(5, p.getTypeUser());

        pre.executeUpdate();
    }

    public void updatepassword(User p) throws SQLException {
        PreparedStatement pr = con.prepareStatement("UPDATE user SET password=? WHERE email=?");
        pr.setString(1, p.getPassword());
        pr.setString(2, p.getEmail());

        pr.execute();
    }

    public boolean delete(User u) throws SQLException {

        try {
            String req1 = "delete from user where idUser=?";

            ps = con.prepareStatement(req1);
            ps.setInt(1, u.getIdUser());
            ps.executeUpdate();
            System.out.println("Delete Complete");
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean update(User u) {

        try {

            String req5 = "UPDATE user SET nom=?,prenom=?,typeuser=?, email=?, password=? WHERE idUser= ?";

            PreparedStatement pre = con.prepareStatement(req5);

            pre.setString(1, u.getNom());
            pre.setString(2, u.getPrenom());
            pre.setString(3, u.getTypeUser());
            pre.setString(5, u.getEmail());
            pre.setString(6, u.getPassword());
            pre.setInt(8, u.getIdUser());

            pre.executeUpdate();
            System.out.println("successfully modified!");
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.err.println(" error modification!!");
        }
        return false;
    }


    public List<User> readAll() throws SQLException {
        List<User> arr = new ArrayList<>();
        ste = con.createStatement();
        ResultSet rs = ste.executeQuery("select * from user");
        while (rs.next()) {
            int id = rs.getInt(1);
            String nom = rs.getString("nom");
            String prenom = rs.getString(3);
            String type = rs.getString("typeuser");
            String email = rs.getString("email");
            String password = rs.getString("password");
            int idGroupe = rs.getInt("Groupe");
            Groupe clGroupe = new Groupe(idGroupe);
            User u = new User(id, nom, prenom, type, email, password);
            arr.add(u);
        }
        return arr;
    }


    public User rechercher(String emaill) throws SQLException {
        User u = new User();
        ste = con.createStatement();
        ResultSet rs = ste.executeQuery("SELECT * FROM user WHERE email= '" + emaill + "'");
        while (rs.next()) {
            int id = rs.getInt("idUser");
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            String type = rs.getString("typeuser");
            String email = rs.getString("email");
            String password = rs.getString("password");
            u = new User(id, nom, prenom, type, email, password);
        }

        return u;

    }


    public boolean resetPass(User u) {
        try {

            String req5 = "UPDATE user SET password=? WHERE email= ?";

            PreparedStatement pre = con.prepareStatement(req5);

            pre.setString(1, u.getPassword());
            pre.setString(2, u.getEmail());

            pre.executeUpdate();
            System.out.println("successfully modified!");
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.err.println(" error modification!!");
        }
        return false;

    }

    public boolean checkcode(User u, int code) {
        if (u.generateCode() == code) {
            return true;
        }
        return false;
    }

}
