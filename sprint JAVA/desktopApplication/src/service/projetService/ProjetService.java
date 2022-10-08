/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.projetService;

import connexiondb.DataSource;
import entity.groupe.Groupe;
import entity.pojet.Projet;
import static entity.pojet.Projet.status.enumToString;
import static entity.pojet.Projet.status.stringToEnum;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.groupeService.GroupeService;

/**
 *
 * @author Aymen Touihri
 */
public class ProjetService implements IService<Projet> {

    private Connection cnx;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;

    public ProjetService() {
        cnx = DataSource.getInstance().getConnection();

    }

    
    public void insert(Projet t) {
        String req = "insert into projet (idProjet,nom,datedebut,datefin,description,status) values (?,?,?,?,?,?,?)";
        try {
            pst = cnx.prepareStatement(req);
            pst.setInt(1, t.getIdProjet());
            pst.setString(2, t.getNom());
            pst.setDate(3, t.getDateDebut());
            pst.setDate(4, t.getDateFin());
            pst.setString(6, t.getDescription());

            if (t.getDateDebut().equals(new Date(LocalDate.now().getYear() - 1900, LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getDayOfMonth()))) {
                pst.setString(7, enumToString(entity.pojet.Projet.status.INPROGRESS));
            } else {
                pst.setString(7, enumToString(entity.pojet.Projet.status.NOTSTARTED));
            }

            pst.executeUpdate();
            System.out.printf("Inserted with success.");

        } catch (SQLException ex) {
            Logger.getLogger(ProjetService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public List<Projet> displayAll() {
        List<Projet> list = new ArrayList<>();
        String req = "select * from projet";
        GroupeService gs = new GroupeService();
        try {
            st = cnx.createStatement();
            rs = st.executeQuery(req);
            while (rs.next()) {
                Projet t = new Projet();
                t.setIdProjet(rs.getInt("idProjet"));
                t.setNom(rs.getString("nom"));
                t.setDateDebut(rs.getDate("dateDebut"));
                t.setDateFin(rs.getDate("dateFin"));
                //t.setGroupe(gs.getGroupeById(rs.getInt("idGroupe")));
                t.setDescription(rs.getString("description"));
                t.setStatus(stringToEnum(rs.getString("status")));

                list.add(t);
                System.out.println(rs.getInt(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProjetService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    @Override
    public void delete(int idProjet) {
        try {
            PreparedStatement ps = cnx.prepareStatement("DELETE FROM projet WHERE idProjet=?");
            ps.setInt(1, idProjet);
            ps.executeUpdate();
            System.out.println(idProjet + " deleted.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Projet t) {
        try {
            PreparedStatement ps = cnx.prepareStatement("UPDATE projet SET nom=? , dateDebut=? , dateFin=?, description=? , status=? WHERE idProjet=?");

            ps.setString(1, t.getNom());
            ps.setDate(2, t.getDateDebut());
            ps.setDate(3, t.getDateFin());
            ps.setString(5, t.getDescription());
            ps.setString(6, enumToString(t.getStatus()));
            ps.setInt(7, t.getIdProjet());
            ps.executeUpdate();
            System.out.println(t.getIdProjet() + " updated.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Projet getProjetByID(int idProjet) {
        Projet t = new Projet();
        GroupeService gs = new GroupeService();

        try {
            st = cnx.createStatement();
            ResultSet res = st.executeQuery(("SELECT * FROM projet WHERE idProjet = " + idProjet + ";"));
            while (res.next()) {
                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                //t.setGroupe(gs.getGroupeById(res.getInt("idGroupe")));
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return t;

    }
    public Projet getProjetByIdSprint(int Sprint) {
        Projet t = new Projet();
        GroupeService gs = new GroupeService();

        try {
            st = cnx.createStatement();
            ResultSet res = st.executeQuery(("SELECT * FROM projet WHERE idSprint = " + Sprint + ";"));
            while (res.next()) {
                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                //t.setGroupe(gs.getGroupeById(res.getInt("idGroupe")));
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return t;

    }

    public Projet getLastProjet() {
        Projet t = new Projet();
        GroupeService gs = new GroupeService();

        try {
            st = cnx.createStatement();
            ResultSet res = st.executeQuery(("SELECT * FROM projet "
                    + "ORDER BY idProjet DESC "
                    + "LIMIT 1; "));
            while (res.next()) {
                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                //t.setGroupe(gs.getGroupeById(res.getInt("idGroupe")));
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return t;

    }

    public Projet getProjetByNom(String nom) {
        Projet t = new Projet();
        GroupeService gs = new GroupeService();

        try {
            st = cnx.createStatement();
            ResultSet res = st.executeQuery(("SELECT * FROM projet WHERE nom Like '" + nom + "';"));

            while (res.next()) {

                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return t;

    }

   /* public List<Projet> getProjetByIdGroupe(Groupe groupe) {
        Projet t;
        GroupeService gs = new GroupeService();
        List<Projet> projets = new ArrayList<Projet>();
        try {
            st = cnx.createStatement();
            ResultSet res = st.executeQuery(("SELECT * FROM projet WHERE idGroupe = " + groupe.getIdGroupe() + ";"));

            while (res.next()) {
                t = new Projet();
                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));
                projets.add(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return projets;

    }*/

    public List<Projet> getProjetByUser(int idUser) {
        Projet t;
        GroupeService gs = new GroupeService();
        List<Projet> projets = new ArrayList<Projet>();
        try {
            st = cnx.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM projet p inner join groupe_travail gt on gt.id_projet_id=p.idprojet "
                    + "inner JOIN user_usergroup ug on ug.usergroup_id=gt.idgroupe inner join user u ON u.iduser = ug.user_id WHERE u.iduser= '"+idUser+"';");

            while (res.next()) {
                t = new Projet();
                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));
                projets.add(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return projets;

    }

    public List<Projet> getUserProjetsByKey(String key,int idUser)
    {
        Projet t;
        List<Projet> projets = new ArrayList<Projet>();
        try {
            st = cnx.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM projet p inner join groupe_travail gt on gt.id_projet_id=p.idprojet "
                    + "inner JOIN user_usergroup ug on ug.usergroup_id=gt.idgroupe "
                    + "inner join user u ON u.iduser = ug.user_id WHERE u.iduser=  '"+idUser+"' "
                    + "And gt.nom Like '%"+key + "%'"
                    + " Or p.`nom` Like '%"+key + "%' "
                    + " Or p.`dateDebut` Like '%"+key+ "%' "
                    + " Or p.`dateFin` Like '%"+key+ "%' "
                    + " Or p.`status` Like '%"+key+ "%' "
                    + " Or p.`description` Like '%"+key+ "%' "
                    + " group by p.`idProjet`;");

            while (res.next()) {
                t = new Projet();
                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));
                projets.add(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
       
        //SELECT Distinct * FROM `projet` p JOIN `user_groupe` ug ON ug.`idGroupe` = p.`idGroupe` JOIN groupe_travail gt on gt.idGroupe = p.idGroupe WHERE ug.`idUser` = 1 And gt.nom Like '%desc%' Or p.`nom` Like '%desc%' Or p.`dateDebut` Like '%desc%' Or p.`dateFin` Like'%desc%' Or p.`status` Like '%desc%' Or p.`description` Like '%desc%' group by p.`idProjet`
        return projets;
        
    }
    public List<Projet> getProjetsByKey(String key) {
        Projet t;
        GroupeService gs = new GroupeService();
        List<Projet> projets = new ArrayList<Projet>();
        try {
            st = cnx.createStatement();
            ResultSet res
                    = st.executeQuery(("SELECT * FROM `projet` INNER JOIN `groupe_travail` on `projet`.`idprojet` = `groupe_travail`.`id_projet_id` "
                            + "WHERE `groupe_travail`.`nom` Like '%" + key + "%'"
                            + "Or `projet`.`nom` Like '%" + key + "%'"
                            + "Or `projet`.`dateDebut` Like '%" + key + "%'"
                            + "Or `projet`.`dateFin` Like '%" + key + "%'"
                            + "Or `projet`.`status` Like '%" + key + "%'"
                            + "Or `projet`.`description` Like '%" + key + "%'"));

            while (res.next()) {
                t = new Projet();
                t.setIdProjet(res.getInt("idProjet"));
                t.setNom(res.getString("nom"));
                t.setDateDebut(res.getDate("dateDebut"));
                t.setDateFin(res.getDate("dateFin"));
                t.setDescription(res.getString("description"));
                t.setStatus(stringToEnum(res.getString("status")));
                projets.add(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return projets;

    }

    @Override
    public void insert(Projet t, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int insertIdReturn(Projet t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteByContainer(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Projet display(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Projet> displayAll(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean existe(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}


//SELECT * FROM `projet` INNER JOIN `user` on `projet`.`idGroupe` = `user`.`idGroupe` where `projet`.`idGroupe` = 2

//SELECT * FROM `projet` Left Join user_groupe On user_groupe.idGroupe = projet.idGroupe Where user_groupe.idUser = 2