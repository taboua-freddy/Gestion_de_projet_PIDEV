/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.activites;

import FXML.controller.HomeController;
import animatefx.animation.FadeInDownBig;
import animatefx.animation.FadeInRightBig;
import animatefx.animation.FadeOutRightBig;
import animatefx.animation.FadeOutUpBig;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import connexiondb.StaticValue;
import entity.activite.Objectif;
import entity.activite.Reunion;
import entity.user.User;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import service.activiteService.ReunionService;
import service.projetService.ProjetService;
import service.sprintService.SprintService;
import service.userService.UserService;

/**
 * FXML Controller class
 *
 * @author Freddy
 */
public class AfficherReunionEmpController implements Initializable {

    private ReunionService rs;
    private UserService us;
    private SprintService ss;
    private ProjetService ps;
    private User user;
    private HomeController homeController;
    private ReunionFx r = null;
    private HashMap<User, Integer> participants = null;
    
    @FXML
    private StackPane mainPage;

    @FXML
    private AnchorPane affichageDetailsPart;

    @FXML
    private TreeTableView<UserP> listPartDetails;

    @FXML
    private TreeTableColumn<UserP, String> nomUserD;

    @FXML
    private TreeTableColumn<UserP, String> prenomUserD;

    @FXML
    private TreeTableColumn<UserP, String> presenceUserD;

    @FXML
    private Pane labelInstruction;

    @FXML
    private TreeTableView<ReunionFx> listReunion;

    @FXML
    private TreeTableColumn<ReunionFx, String> titreCol;

    @FXML
    private TreeTableColumn<ReunionFx, String> dateDebutCol;

    @FXML
    private TreeTableColumn<ReunionFx, String> dateFinCol;

    @FXML
    private TreeTableColumn<ReunionFx, String> etatCol;

    @FXML
    private Pane detailsPane;

    @FXML
    private JFXTextField titreDetails;

    @FXML
    private TextArea descriptionTextDetails;

    @FXML
    private JFXTextField themeDuJourDetails;

    @FXML
    private JFXTextField projetDetails;

    @FXML
    private JFXTextField coordonateurDetails;

    @FXML
    private JFXTextField sprintDetails;

    @FXML
    private JFXTextField importanceDetails;

    @FXML
    private JFXTextField dateRappelDetails;

    @FXML
    private JFXListView<String> listObjectifsDetails;

    @FXML
    private JFXButton presenceReunion;

    @FXML
    void afficherParticipants(ActionEvent event) {
        displayModalVertivally(affichageDetailsPart);
        displayListPart(participants);
    }

    @FXML
    void hideModalPart(ActionEvent event) {
        displayModalVertivally(affichageDetailsPart);
    }

    @FXML
    void trierReunions(ActionEvent event) {
        initTable(listReunion, trierReunions(rs.displayReunionsUser(user.getIdUser())));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        affichageDetailsPart.setVisible(false);
        affichageDetailsPart.toBack();
        detailsPane.setVisible(false);
        listReunion.getStylesheets().add("/FXML/css/fullpackstyling.css");

        rs = new ReunionService();
        ss = new SprintService();
        ps = new ProjetService();
        us = new UserService();
        user = StaticValue.utilisateur;

        initTable(listReunion, rs.displayReunionsUser(user.getIdUser()));

        listReunion.setOnMouseClicked((event) -> {
            r = getSelectedRow(listReunion);
            if (r != null) {
                participants = (HashMap<User, Integer>) rs.displayUsersReunion(r.getId());
                insertDetailsValues(r.getReunion());
                displayModalHorizontally(detailsPane);
            } else {
                hideModalHorizontally(detailsPane);
            }

        });
        presenceReunion.setOnAction((event) -> {
            System.err.println(r);
            rs.updatePresenceReunion(r.getId(), user.getIdUser(), getPresenceUser(participants)==1?0:1);
            participants = (HashMap<User, Integer>) rs.displayUsersReunion(r.getId());
            updateButtonPresence();
        });
    }   
    
     private void displayModalVertivally(Node node) {
        if (node.isVisible()) {
            FadeOutUpBig animation = new FadeOutUpBig(node);
            animation.setDelay(Duration.ONE);
            animation.setOnFinished((event) -> {
                node.setVisible(false);
                node.toBack();
            });
            animation.play();
        } else {
            node.setVisible(true);
            node.toFront();
            new FadeInDownBig(node).setDelay(Duration.ONE).play();
        }

    }
    private void displayModalHorizontally(Node node) {
        if (!node.isVisible()) {
            node.setVisible(true);
            node.getParent().toFront();
            node.toFront();
            new FadeInRightBig(node).setDelay(Duration.ONE).play();
        }
    }
    private void hideModalHorizontally(Node node) {
        if (node.isVisible()) {
            FadeOutRightBig animation = new FadeOutRightBig(node);
            animation.setDelay(Duration.ONE);
            animation.setOnFinished((event) -> {
                node.setVisible(false);
                node.toBack();
                node.getParent().toBack();
            });
            animation.play();
        }
    }

    public void initTable(TreeTableView t1, ArrayList<Reunion> data) {

        titreCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, String>("titre"));
        dateDebutCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, String>("dateD"));
        dateFinCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, String>("dateF"));
        etatCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, String>("etat"));

        TreeItem<ReunionFx> root = new TreeItem<>(new ReunionFx(new Reunion()));
        for (Reunion reunion : data) {
            root.getChildren().add(new TreeItem<ReunionFx>(new ReunionFx(reunion)));
        }

        t1.setRoot(root);
        t1.setShowRoot(false);
    }
    
    private ReunionFx getSelectedRow(TreeTableView<ReunionFx> table) {
        ReunionFx r = null;
        if (table.getSelectionModel() != null) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                r = (ReunionFx) table.getSelectionModel().getSelectedItem().getValue();
            }
        }

        return r;
    }
    private int getPresenceUser(HashMap<User, Integer> list)
    {
        for (Map.Entry<User, Integer> entry : list.entrySet()) {
            User key = entry.getKey();
            Integer value = entry.getValue();
            if(key.equals(user))
            {
                return value;
            }  
        }
        return 0;
    }
    private void updateButtonPresence()
    {
        String presenceText = "Signaler Mon Absecnce";
        if(getPresenceUser(participants)==0)
            presenceText = "Signaler Ma presence";
        presenceReunion.setText(presenceText);
    }
    private void insertDetailsValues(Reunion r) {
        updateButtonPresence(); 
        titreDetails.setText(r.getTitre());
        themeDuJourDetails.setText(r.getThemeDuJour());
        if (r.getCoordonateur() != null) {
            coordonateurDetails.setText(r.getCoordonateur().getNom() + " " + r.getCoordonateur().getPrenom());
        }
        if (r.getSprint() != null) {
            sprintDetails.setText(r.getSprint().getNomSprint());
        }
        projetDetails.setText("");
        descriptionTextDetails.setText(r.getDescription());
        importanceDetails.setText(r.getImportance().getText());
        dateRappelDetails.setText("");
        if (r.getDateRappel() != null) {
            String date = r.getDateRappel().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.FRENCH));
            String heure = r.getDateRappel().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(Locale.FRENCH));
            dateRappelDetails.setText(date + " à " + heure);
        }

        listObjectifsDetails.setItems(FXCollections.observableArrayList(r.getObjectifs().stream().map(o -> o.getObjectif()).collect(Collectors.toList())));

        if (participants != null) {
            displayListPart(participants);
        }

    }
    private void displayListPart(HashMap<User, Integer> list)
    {
        nomUserD.setCellValueFactory(new TreeItemPropertyValueFactory<UserP, String>("nom"));
        prenomUserD.setCellValueFactory(new TreeItemPropertyValueFactory<UserP, String>("prenom"));
        presenceUserD.setCellValueFactory(new TreeItemPropertyValueFactory<UserP, String>("presence"));
        TreeItem<UserP> root = new TreeItem<UserP>();
        for (Map.Entry<User, Integer> entry : list.entrySet()) {
            root.getChildren().add(new TreeItem<UserP>(new UserP(entry)));
        }
        listPartDetails.setRoot(root);
        listPartDetails.setShowRoot(false);
    }
    private ArrayList<Reunion> trierReunions(ArrayList<Reunion> data)
    {
        LocalDateTime dateActuelle = LocalDateTime.now();
        Collections.sort(data, Comparator.comparing((reunion) -> reunion.getDateDebut()));
        ArrayList<Reunion> inComing = new ArrayList<>(data.stream().filter((Reunion t) -> {
            return t.getDateDebut().isAfter(dateActuelle); 
        }).collect(Collectors.toList()));
        data.removeAll(inComing);
        inComing.addAll(data);
        return inComing;
    }
}
