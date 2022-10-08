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
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import entity.activite.Objectif;
import entity.activite.Reunion;
import entity.pojet.Projet;
import entity.Sprint;
import entity.user.User;
import enumeration.TypeImportance;
import enumeration.TypeSprint;
import functions.Functions;
import functions.SendMail;
import java.io.IOException;
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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.spreadsheet.StringConverterWithFormat;
import service.activiteService.ReunionService;
import service.projetService.ProjetService;
import service.sprintService.SprintService;
import service.userService.UserService;

/**
 * FXML Controller class
 *
 * @author Freddy
 */
public class AfficherReunionController implements Initializable {

    private ReunionService rs;
    private UserService us;
    private SprintService ss;
    private ProjetService ps;
    private HomeController homeController;
    private ReunionFx r = null;
    private HashMap<User, Integer> participants = null;

    @FXML
    private JFXCheckBox autreTitre;
    @FXML
    private ComboBox<String> titreCombo;
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
    private TreeTableColumn<ReunionFx, Boolean> selectedCol;

    @FXML
    private AnchorPane afficaheModif;
    @FXML
    private JFXButton buttonHideModal;

    @FXML
    private JFXDatePicker dateDebut;

    @FXML
    private JFXTimePicker heureDebut;

    @FXML
    private JFXDatePicker dateFin;

    @FXML
    private JFXTimePicker heureFin;

    @FXML
    private JFXCheckBox rappelCheckBox;

    @FXML
    private Spinner<Integer> tempsRappel;

    @FXML
    private JFXComboBox<String> uniteTemps;

    @FXML
    private JFXTextField titre;

    @FXML
    private TextArea descriptionText;

    @FXML
    private JFXTextField themeDuJour;

    @FXML
    private JFXComboBox<Sprint> sprint;

    @FXML
    private JFXComboBox<TypeImportance> importance;

    @FXML
    private JFXComboBox<Projet> projet;

    @FXML
    private JFXTextField coordonateur;

    @FXML
    private JFXTextField dateRappelDetails;

    @FXML
    private TreeTableView<UserFx> tableViewPart;

    @FXML
    private JFXTextArea objectif;

    @FXML
    private JFXListView<Objectif> listObjectifs;

    @FXML
    private TreeTableView<UserP> listPartDetails;
    @FXML
    private TreeTableColumn<UserP, String> nomUserD;

    @FXML
    private TreeTableColumn<UserP, String> prenomUserD;

    @FXML
    private TreeTableColumn<UserP, String> presenceUserD;

    @FXML
    private AnchorPane affichageInfo1;
    @FXML
    private AnchorPane affichageDetailsPart;

    @FXML
    private JFXTextField filtre;

    @FXML
    private TreeTableView<UserFx> t1;

    @FXML
    private TreeTableColumn<UserFx, Boolean> c1;
    @FXML
    private TreeTableColumn<UserFx, String> c2;
    @FXML
    private TreeTableColumn<UserFx, String> c3;
    @FXML
    private TreeTableColumn<UserFx, String> c4;

    @FXML
    private JFXButton buttonAjouterMulti;

    @FXML
    private Pane labelInstruction;

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
    private JFXListView<String> listObjectifsDetails;

    @FXML
    private StackPane stackPane;
    @FXML
    private StackPane mainPage;

    @FXML
    void afficherParticipants(ActionEvent event) {
        displayModalVertivally(affichageDetailsPart);
    }

    @FXML
    void trierReunions(ActionEvent event) {
        initTable(listReunion, trierReunions(rs.displayAll()));
    }
    @FXML
    void autreTitre(ActionEvent event) {
        if(autreTitre.isSelected())
        {
            displayTextTitreSprint();
            titreCombo.setVisible(false);
        }
        else
        {
            displayComboTitreSprint();
            titre.setVisible(false);
        }
    }
    private void displayComboTitreSprint()
    {
        titreCombo.setVisible(true);
        titreCombo.getItems().clear();
        for (TypeSprint value : TypeSprint.values()) {
            titreCombo.getItems().add(value.getText());
        }
       titreCombo.getSelectionModel().selectFirst();
        
    }
    private void displayTextTitreSprint()
    {
        titre.setVisible(true);
        titre.setText("");
    }
    private String getTitre()
    {
        String titreS = titreCombo.getValue();
        if(autreTitre.isSelected())
            titreS = titre.getText();
        return titreS;
    }

    /* modification Debut */
    @FXML
    void clearObjectifs(ActionEvent event) {
        listObjectifs.getItems().clear();
    }

    @FXML
    void ajouterOjectif(ActionEvent event) {
        String ob = objectif.getText();
        if (!ob.isEmpty()) {
            listObjectifs.getItems().add(new Objectif(ob));
            listObjectifs.setCellFactory((param) -> {
                return new TextFieldListCell<Objectif>(new StringConverterWithFormat<Objectif>() {
                    @Override
                    public String toString(Objectif object) {
                        return object.getObjectif();
                    }

                    @Override
                    public Objectif fromString(String string) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });
            });
            objectif.setText("");
        } else {
            alert("Vous n'avez pas saisie d'objectif");
        }

    }

    @FXML
    void ajouterCoordonateur(ActionEvent event) {
        displayModalHorizontally(affichageInfo1);
        buttonAjouterMulti.setOnMouseClicked((ev) -> {

            coordonateur.setText("");
            UserFx u = null;
            for (int i = 0; i < t1.getRoot().getChildren().size(); i++) {
                if (t1.getRoot().getChildren().get(i).getValue().getSelected()) {
                    u = t1.getRoot().getChildren().get(i).getValue();
                    break;
                }

            }
            if (u != null) {
                coordonateur.setText(u.getNom() + " " + u.getPrenom());
            }
            hideModalHorizontally(affichageInfo1);
        });
    }

    @FXML
    void ajouterParticipants(ActionEvent event) {
        displayModalHorizontally(affichageInfo1);
        buttonAjouterMulti.setOnMouseClicked((ev) -> {
            ArrayList<User> list = new ArrayList<User>();
            for (int i = 0; i < t1.getRoot().getChildren().size(); i++) {
                if (t1.getRoot().getChildren().get(i).getValue().getSelected()) {
                    list.add(t1.getRoot().getChildren().get(i).getValue().getUser());
                }
            }
            if (!list.isEmpty()) {
                initTableModif(tableViewPart, list);
            }
            hideModalHorizontally(affichageInfo1);
        });
    }

    @FXML
    void pogrammerReunion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../../view/activites/Activities.fxml"));
        AnchorPane root = loader.load();
        ActivitiesController controller = loader.getController();
        controller.setHomeController(homeController);
        loader.setController(controller);
        homeController.getbody().getChildren().setAll(root);
    }

    @FXML
    void supprimerParticipants(ActionEvent event) {
        if (tableViewPart.getRoot() != null) {
            ArrayList<TreeItem<UserFx>> list = new ArrayList<>(tableViewPart.getRoot().getChildren().stream().filter((TreeItem<UserFx> t) -> {
                return t.getValue().getSelected();
            }).collect(Collectors.toList()));
            tableViewPart.getRoot().getChildren().removeAll(list);
            if (tableViewPart.getRoot().getChildren().isEmpty()) {
                tableViewPart.getColumns().clear();
            }
        } else {
            alert("aucun participants dans la liste");
        }

    }

    @FXML
    void programmerReunion(ActionEvent event) {
        ArrayList<String> erreurs = controleDeSaisie();
        if (erreurs.isEmpty()) {
            String titre = getTitre();
            String theme = themeDuJour.getText();
            Projet projet = this.projet.getValue();
            Sprint sprint = this.sprint.getValue();
            String description = descriptionText.getText();
            TypeImportance importance = this.importance.getValue();
            LocalDateTime dateDebut = LocalDateTime.of(this.dateDebut.getValue(), heureDebut.getValue());
            LocalDateTime dateFin = LocalDateTime.of(this.dateFin.getValue(), heureFin.getValue());
            String nom = "";
            String prenom = "";
            if (!coordonateur.getText().isEmpty()) {
                String[] nomPrenom = coordonateur.getText().split(" ");
                nom = nomPrenom[0];
                prenom = nomPrenom[1];
            }
            User coordonateur = us.displayUser(nom, prenom);
            int tempsRapppel = tempsRappel.getValue();
            String uniteTemps = this.uniteTemps.getValue();
            LocalDateTime dateRappel = null;
            if (rappelCheckBox.isSelected()) {
                dateRappel = Functions.dateRappel(dateDebut, tempsRapppel, uniteTemps);
            }
            ArrayList<User> listParticipants = new ArrayList<>();
            if (tableViewPart.getRoot() != null) {
                tableViewPart.getRoot().getChildren().forEach((treeItem) -> {
                    listParticipants.add(treeItem.getValue().getUser());
                });
                String corps = description + "\n Debut : " + dateDebut + " Fin : " + dateFin;
                //SendMail.sendMail(listParticipants, titre, corps);
            }

            ArrayList<Objectif> listObjectifs = new ArrayList<>();
            this.listObjectifs.getItems().forEach((item) -> {
                listObjectifs.add(item);
            });

            r.setTitre(titre);
            r.setDateDebut(dateDebut);
            r.setDateFin(dateFin);
            r.setDateRappel(dateRappel);
            r.setDescription(description);
            r.setImportance(importance);
            r.setThemeDuJour(theme);
            r.setSprint(sprint);
            r.setCoordonateur(coordonateur);
            r.setObjectifs(listObjectifs);
            rs.update(r);
            int idReunion = r.getId();
            rs.deleteAllUsersToReunion(idReunion);
            listParticipants.forEach((participant) -> {
                rs.insertUserToReunion(idReunion, participant, 1);
            });
            initTable(listReunion, rs.displayAll());
            displayModalVertivally(afficaheModif);
            insertDetailsValues(r);
            alert("Modifier avec succés");
        } else {
            alert(erreurs.toString().replace('[', ' ').replace(']', ' '));
        }

    }

    @FXML
    void rappelCheckBoxOnClick(ActionEvent event) {
        if (rappelCheckBox.isSelected()) {
            uniteTemps.setDisable(false);
            tempsRappel.setDisable(false);
        } else {
            uniteTemps.setDisable(true);
            tempsRappel.setDisable(true);
        }

    }

    @FXML
    void closeModal(ActionEvent event) {
        hideModalHorizontally(affichageInfo1);
    }

    @FXML
    void uniteTemps(ActionEvent event) {

    }

    @FXML
    void modifierReunion(ActionEvent event) {
        autreTitre.setSelected(true);
        displayModalVertivally(afficaheModif);
        if (r != null) {
            insertModifValues(r);
        }

    }

    @FXML
    void supprimerReunion(ActionEvent event) {
        rs.delete(r.getId());
        initTable(listReunion, rs.displayAll());
        r = getSelectedRow(listReunion);
        insertDetailsValues(r);
        if (r != null) {
            participants = (HashMap<User, Integer>) rs.displayUsersReunion(r.getId());
            insertDetailsValues(r.getReunion());
            displayModalHorizontally(detailsPane);
        } else {
            hideModalHorizontally(detailsPane);
        }
    }

    @FXML
    void hideModal(ActionEvent event) {
        displayModalVertivally(afficaheModif);
    }

    @FXML
    void hideModalPart(ActionEvent event) {
        displayModalVertivally(affichageDetailsPart);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        affichageDetailsPart.setVisible(false);
        affichageDetailsPart.toBack();
        afficaheModif.setVisible(false);
        afficaheModif.toBack();
        detailsPane.setVisible(false);
        listReunion.getStylesheets().add("/FXML/css/fullpackstyling.css");

        rs = new ReunionService();
        ss = new SprintService();
        ps = new ProjetService();
        us = new UserService();

        initTable(listReunion, rs.displayAll());

        listReunion.setOnMouseClicked((event) -> {
            r = getSelectedRow(listReunion);
            if (r != null) {
                participants = (HashMap<User, Integer>) rs.displayUsersReunion(r.getId());
                System.err.println(r);
                insertDetailsValues(r.getReunion());
                displayModalHorizontally(detailsPane);
            } else {
                displayModalHorizontally(detailsPane);
            }

        });

    }

    private void insertModifValues(Reunion r) {
        affichageInfo1.setVisible(false);
        coordonateur.setEditable(false);
        uniteTemps.setDisable(true);
        tempsRappel.setDisable(true);

        titre.setText(r.getTitre());
        themeDuJour.setText(r.getThemeDuJour());
        if (r.getCoordonateur() != null) {
            coordonateur.setText(r.getCoordonateur().getNom() + " " + r.getCoordonateur().getPrenom());
        }
        initProjectModif();
        iniSprintModif(r.getSprint());
        descriptionText.setText(r.getDescription());
        initImporatnceModif(r.getImportance());
        dateDebut.setValue(r.getDateDebut().toLocalDate());
        dateFin.setValue(r.getDateFin().toLocalDate());
        heureDebut.setValue(r.getDateDebut().toLocalTime());
        heureFin.setValue(r.getDateFin().toLocalTime());

        ArrayList<User> part = new ArrayList<>();
        for (Map.Entry<User, Integer> entry : participants.entrySet()) {
            User key = entry.getKey();
            Integer value = entry.getValue();
            part.add(key);
        }
        initTableModif(tableViewPart, part);

        listObjectifs.getItems().setAll(r.getObjectifs());
        listObjectifs.setCellFactory((param) -> {
            return new TextFieldListCell<Objectif>(new StringConverterWithFormat<Objectif>() {
                @Override
                public String toString(Objectif object) {
                    return object.getObjectif();
                }

                @Override
                public Objectif fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        });
        objectif.setText("");
        stackPane.toBack();
        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
            stackPane.toBack();
        });
        mainPage.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
            stackPane.toBack();
        });

        horiareRappel();
        initTableModif(t1, us.displayUsersProjet(projet.getSelectionModel().getSelectedItem().getIdProjet()));

        filtre.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteTable(t1, us.displayUsersProjet(projet.getSelectionModel().getSelectedItem().getIdProjet()), newValue);
        });
    }

    private void initProjectModif() {
        
        projet.setCellFactory((param) -> {
            return new ListCell<Projet>() {
                @Override
                protected void updateItem(Projet t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        setText(t.getNom());
                    }
                }
            };
        });
        projet.setConverter(new StringConverter<Projet>() {

            @Override
            public String toString(Projet object) {
                return object.getNom().toUpperCase();
            }

            @Override
            public Projet fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        if(projet.getItems().isEmpty())
        {
            for (Projet projet1 : ps.displayAll()) {
                projet.getItems().add(projet1);
            }
        }
        
        projet.getSelectionModel().selectFirst();
        projet.valueProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<UserFx> root = new TreeItem<>(new UserFx(new User()));
            for (User displayUser : us.displayUsersProjet(projet.getSelectionModel().getSelectedItem().getIdProjet())) {
                root.getChildren().add(new TreeItem<UserFx>(new UserFx(displayUser)));
            }
            t1.setRoot(root);
            sprint.getItems().clear();
            for (Sprint sprint1 : ss.displaySprintProjet(newValue.getIdProjet())) {
                sprint.getItems().add(sprint1);
            }
            sprint.getSelectionModel().selectFirst();
        });
    }

    private void filteTable(TreeTableView t1, ArrayList<User> users, String newValue) {
        ArrayList<User> data = new ArrayList<>(users.stream().filter((user) -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String inputLower = newValue.toLowerCase();

            if (user.getNom().toLowerCase().indexOf(inputLower) != -1) {
                return true;
            }
            if (user.getPrenom().toLowerCase().indexOf(inputLower) != -1) {
                return true;
            }
            return false;
        }).collect(Collectors.toList()));
        initTableModif(t1, data);
    }

    private void initTableModif(TreeTableView t1, ArrayList<User> data) {

        TreeItem<UserFx> root = new TreeItem<>(new UserFx(new User()));
        for (User displayUser : data) {
            root.getChildren().add(new TreeItem<UserFx>(new UserFx(displayUser)));
        }
        c1.setCellValueFactory((param) -> {
            TreeItem<UserFx> treeItem = param.getValue();
            UserFx emp = treeItem.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(emp.getSelected());

            booleanProp.addListener(new ChangeListener<Boolean>() {

                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                        Boolean newValue) {
                    emp.setSelected(newValue);
                }
            });

            return booleanProp;
        });

        c1.setCellFactory((param) -> {
            CheckBoxTreeTableCell<UserFx, Boolean> cell = new CheckBoxTreeTableCell<UserFx, Boolean>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        c2.setCellValueFactory(new TreeItemPropertyValueFactory<UserFx, String>("nom"));
        c3.setCellValueFactory(new TreeItemPropertyValueFactory<UserFx, String>("prenom"));
        c4.setCellValueFactory(new TreeItemPropertyValueFactory<UserFx, String>("email"));
        t1.getColumns().setAll(c1, c2, c3, c4);
        t1.setRoot(root);
        t1.setShowRoot(false);

    }

    private void iniSprintModif(Sprint defaultSprint) {
        sprint.getItems().clear();
        for (Sprint sprint1 : ss.displaySprintProjet(projet.getSelectionModel().getSelectedItem().getIdProjet())) {
            sprint.getItems().add(sprint1);
        }
        
        sprint.setCellFactory((ListView<Sprint> Sprint) -> {
            return new ListCell<Sprint>() {
                @Override
                protected void updateItem(Sprint t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        setText(t.getNomSprint());
                    }
                }
            };
        });
        sprint.setConverter(new StringConverter<Sprint>() {
            @Override
            public String toString(Sprint object) {
                return object.getNomSprint();
            }

            @Override
            public Sprint fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        sprint.getSelectionModel().select(defaultSprint);
    }

    private void initImporatnceModif(TypeImportance defaultTypeImportance) {
        importance.getItems().clear();
        for (TypeImportance t : TypeImportance.values()) {
            importance.getItems().add(t);
        }
        importance.getSelectionModel().select(defaultTypeImportance);
        importance.setConverter(new StringConverter<TypeImportance>() {

            @Override
            public String toString(TypeImportance object) {
                return object.getText();
            }

            @Override
            public TypeImportance fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void horiareRappel() {
        uniteTemps.getItems().setAll(FXCollections.observableArrayList("heures", "minutes", "jours"));

        uniteTemps.setValue("heures");
        uniteTemps.valueProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Integer> intervalle = getIntervalle(newValue);
            tempsRappel.setValueFactory(setIntervalle(intervalle.get(0), intervalle.get(1)));
        });

        tempsRappel.setValueFactory(setIntervalle(1, 24));
    }

    private ArrayList<Integer> getIntervalle(String uniteTemps) {
        ArrayList<Integer> list = new ArrayList<>();
        if (uniteTemps == "minutes") {
            list.add(1);
            list.add(60);
        } else if (uniteTemps == "jours") {
            list.add(1);
            list.add(10);
        } else {
            list.add(1);
            list.add(24);
        }

        return list;
    }

    private void insertDetailsValues(Reunion r) {
        titreDetails.setText(r.getTitre());
        themeDuJourDetails.setText(r.getThemeDuJour());
        if (r.getCoordonateur() != null) {
            coordonateurDetails.setText(r.getCoordonateur().getNom() + " " + r.getCoordonateur().getPrenom());
        }
        if (r.getSprint() != null) {
            sprintDetails.setText(r.getSprint().getNomSprint());
        }
        Projet p = ps.getProjetByID(r.getSprint().getIdProjet());
        projetDetails.setText(p.getNom().toUpperCase());
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
            nomUserD.setCellValueFactory(new TreeItemPropertyValueFactory<UserP, String>("nom"));
            prenomUserD.setCellValueFactory(new TreeItemPropertyValueFactory<UserP, String>("prenom"));
            presenceUserD.setCellValueFactory(new TreeItemPropertyValueFactory<UserP, String>("presence"));
            TreeItem<UserP> root = new TreeItem<UserP>();
            for (Map.Entry<User, Integer> entry : participants.entrySet()) {
                root.getChildren().add(new TreeItem<UserP>(new UserP(entry)));
            }
            listPartDetails.setRoot(root);
            listPartDetails.setShowRoot(false);
        }

    }

    private SpinnerValueFactory<Integer> setIntervalle(int debut, int fin) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(debut, fin, 1, 1);
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

    public void initTable(TreeTableView t1, ArrayList<Reunion> data) {

        titreCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, String>("titre"));
        dateDebutCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, String>("dateD"));
        dateFinCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, String>("dateF"));
        selectedCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, Boolean>("Selected"));
        etatCol.setCellValueFactory(new TreeItemPropertyValueFactory<ReunionFx, String>("etat"));

        selectedCol.setCellValueFactory((param) -> {
            TreeItem<ReunionFx> treeItem = param.getValue();
            ReunionFx emp = treeItem.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(emp.getSelected());

            booleanProp.addListener(new ChangeListener<Boolean>() {

                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                        Boolean newValue) {
                    emp.setSelected(newValue);
                }
            });

            return booleanProp;
        });

        selectedCol.setCellFactory((param) -> {
            CheckBoxTreeTableCell<ReunionFx, Boolean> cell = new CheckBoxTreeTableCell<ReunionFx, Boolean>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        TreeItem<ReunionFx> root = new TreeItem<>(new ReunionFx(new Reunion()));
        for (Reunion reunion : data) {
            root.getChildren().add(new TreeItem<ReunionFx>(new ReunionFx(reunion)));
        }

        t1.setRoot(root);
        t1.setShowRoot(false);
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

    public void setHomeController(HomeController h) {
        this.homeController = h;
    }

    private ArrayList<String> controleDeSaisie() {
        ArrayList<String> erreurs = new ArrayList<>();

        String titre = getTitre();
        String theme = themeDuJour.getText();
        LocalDateTime dateDebut = null;
        if (this.dateDebut.getValue() == null || heureDebut.getValue() == null) {
            erreurs.add("Vous devez insérer une date de début \n");
        } else {
            dateDebut = LocalDateTime.of(this.dateDebut.getValue(), heureDebut.getValue());
            if (dateDebut.isBefore(LocalDateTime.now())) {
                erreurs.add("Votre date de début est inférieure à la date du jour \n");
            }
            if (this.dateFin.getValue() == null || heureFin.getValue() == null) {
                erreurs.add("Vous devez insérer une date de fin");
            } else {
                LocalDateTime dateFin = LocalDateTime.of(this.dateFin.getValue(), heureFin.getValue());
                if (dateFin.isBefore(LocalDateTime.now()) || dateFin.isBefore(dateDebut)) {
                    erreurs.add("Veuillez insérer une date supérieur à celle d'aujourd'hui et à la date de début \n");
                }
            }
        }

        if (titre.isEmpty()) {
            erreurs.add("Veuillez inserer un titre \n");
        }
        if (titre.length() > 200) {
            erreurs.add("Vous devez entrer un titre de moins de 20 charactéres \n");
        }
        if (theme.isEmpty()) {
            erreurs.add("Veuillez inserer un théme du jour \n");
        }
        if (theme.length() > 200) {
            erreurs.add("Vous devez entrer un titre de moins de 30 charactéres \n");
        }

        return erreurs;
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

    private void alert(String text) {
        mainPage.toFront();
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(mainPage, dialogLayout, JFXDialog.DialogTransition.NONE);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
            dialog.close();
            mainPage.toBack();
            dialogLayout.addEventHandler(MouseEvent.MOUSE_CLICKED, (eve) -> {

                mainPage.toBack();
            });
            dialog.addEventHandler(MouseEvent.MOUSE_CLICKED, (eve) -> {

                mainPage.toBack();
            });
        });
        JFXTextArea tx = new JFXTextArea(text);
        tx.setWrapText(true);
        tx.setEditable(false);
        dialogLayout.setBody(tx);
        dialogLayout.setActions(buttonOk);
        dialog.show();
    }

}
