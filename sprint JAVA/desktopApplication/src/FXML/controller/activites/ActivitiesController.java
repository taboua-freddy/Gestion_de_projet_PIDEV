/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.activites;

import FXML.controller.HomeController;
import com.jfoenix.controls.JFXButton;
import enumeration.TypeImportance;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
import enumeration.TypeSprint;
import functions.Functions;
import functions.SendMail;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
public class ActivitiesController implements Initializable {

    private ReunionService rs;
    private UserService us;
    private SprintService ss;
    private ProjetService ps;
    
    private HomeController homeController;

    @FXML
    private JFXButton buttonAjouterMulti;
    @FXML
    private AnchorPane mainPage;
    @FXML
    private ComboBox<TypeImportance> importance;
    @FXML
    private ComboBox<String> titreCombo;
    @FXML
    private ComboBox<String> uniteTemps;
    @FXML
    private JFXTextArea objectif;
    @FXML
    private JFXListView<Objectif> listObjectifs;
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
    private JFXCheckBox autreTitre;

    @FXML
    private Spinner<Integer> tempsRappel;

    @FXML
    private JFXTextField titre;

    @FXML
    private TextArea descriptionText;

    @FXML
    private JFXTextField themeDuJour;

    @FXML
    private JFXComboBox<Sprint> sprint;

    @FXML
    private JFXComboBox<Projet> projet;

    @FXML
    private JFXTextField coordonateur;

    @FXML
    private TableView tableView;
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField filtre;

    @FXML
    private TreeTableView<UserFx> tableViewPart;

    @FXML
    private AnchorPane affichageInfo;

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
    void clearObjectifs(ActionEvent event) {
        listObjectifs.getItems().clear();
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
        displayModal(affichageInfo, 0.0);
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
            displayModal(affichageInfo, 900.0);
        });
    }

    @FXML
    void ajouterParticipants(ActionEvent event) {
        displayModal(affichageInfo, 0.0);
        buttonAjouterMulti.setOnMouseClicked((ev) -> {
            ArrayList<User> list = new ArrayList<User>();
            for (int i = 0; i < t1.getRoot().getChildren().size(); i++) {
                if (t1.getRoot().getChildren().get(i).getValue().getSelected()) {
                    list.add(t1.getRoot().getChildren().get(i).getValue().getUser());      
                }
            }
            if (!list.isEmpty()) {
                initTable(tableViewPart,list);
            }
            displayModal(affichageInfo, 900.0);
        });
    }
    
    @FXML
    void supprimerParticipants(ActionEvent event) {
        if(tableViewPart.getRoot()!=null)
        {
            ArrayList<TreeItem<UserFx>> list = new ArrayList<>(tableViewPart.getRoot().getChildren().stream().filter((TreeItem<UserFx> t) -> {
                    return t.getValue().getSelected(); 
                }).collect(Collectors.toList()));
            tableViewPart.getRoot().getChildren().removeAll(list);
            if (tableViewPart.getRoot().getChildren().isEmpty()) {
                tableViewPart.getColumns().clear();
            }
        }
        else
            alert("aucun participants dans la liste");
              
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
                String corps = description + "\n Debut : "+ dateDebut + " Fin : "+dateFin;
                SendMail.sendMail(listParticipants, titre, corps);
            }

            ArrayList<Objectif> listObjectifs = new ArrayList<>();
            this.listObjectifs.getItems().forEach((item) -> {
                listObjectifs.add(item);
            });

            Reunion r = new Reunion();
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
            int idReunion = rs.insert(r);
            listParticipants.forEach((participant) -> {
                rs.insertUserToReunion(idReunion, participant, 1);
            });
            //
            alert("Ajouté avec succés");
        } else {
            alert(erreurs.toString().replace('[',' ').replace(']', ' '));
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
        displayModal(affichageInfo, 900.0);
    }

    @FXML
    void uniteTemps(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stackPane.toBack();
        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
            stackPane.toBack();
        });
        mainPage.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
            stackPane.toBack();
        });
        affichageInfo.setVisible(false);
        displayModal(affichageInfo, 900.0);
        coordonateur.setEditable(false);
        uniteTemps.setDisable(true);
        tempsRappel.setDisable(true);
        titre.setVisible(false);

        rs = new ReunionService();
        ss = new SprintService();
        ps = new ProjetService();
        us = new UserService();
        


        displayComboTitreSprint();
        initProject();
        iniSprint();
        initImporatnce();
        horiareRappel();
        initTable(t1,us.displayUsersProjet(projet.getSelectionModel().getSelectedItem().getIdProjet()));
        
        filtre.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteTable(t1,us.displayUsersProjet(projet.getSelectionModel().getSelectedItem().getIdProjet()),newValue);
        });
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
            if(Functions.dateRappel(dateDebut, tempsRappel.getValue(), uniteTemps.getValue()).isBefore(LocalDateTime.now()) && rappelCheckBox
                    .isSelected())
            {
                erreurs.add("Le temps de rappel ne doit pas etre inferieure à la date courante \n");
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

    private void alert(String text) {
        stackPane.toFront();
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.NONE);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
            dialog.close();
            stackPane.toBack();
            dialogLayout.addEventHandler(MouseEvent.MOUSE_CLICKED, (eve) -> {

                stackPane.toBack();
            });
            dialog.addEventHandler(MouseEvent.MOUSE_CLICKED, (eve) -> {

                stackPane.toBack();
            });
        });
        JFXTextArea tx = new JFXTextArea(text);
        tx.setWrapText(true);
        tx.setEditable(false);
        dialogLayout.setBody(tx);
        dialogLayout.setActions(buttonOk);
        dialog.show();
    }

    private void initProject() {
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

        for (Projet projet1 : ps.displayAll()) {
            projet.getItems().add(projet1);
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
    
    private void filteTable(TreeTableView t1,ArrayList<User> users,String newValue)
    {   
        ArrayList<User> data = new ArrayList<>(users.stream().filter((user) -> {
                if(newValue==null || newValue.isEmpty())
                    return true;
                
                String inputLower = newValue.toLowerCase();
                
                if(user.getNom().toLowerCase().indexOf(inputLower)!= -1)
                    return true;
                if(user.getPrenom().toLowerCase().indexOf(inputLower)!= -1)
                    return true;
                return false;
            }).collect(Collectors.toList()));
        initTable(t1, data);
    }
    
    private void initTable(TreeTableView t1, ArrayList<User> data) {
 
        /*c1.prefWidthProperty().bind(t1.widthProperty().divide(6));
        c2.prefWidthProperty().bind(t1.widthProperty().divide(4));
        c3.prefWidthProperty().bind(t1.widthProperty().divide(4));
        c4.prefWidthProperty().bind(t1.widthProperty().divide(4 ));
        t1.getColumns().clear();
        t1.getColumns().addAll(c1, c2,c3,c4);*/
        
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
        t1.getColumns().setAll(c1,c2,c3,c4);
        t1.setRoot(root);
        t1.setShowRoot(false);
        
    }

    private void iniSprint() {
        for (Sprint sprint1 : ss.displaySprintProjet(projet.getSelectionModel().getSelectedItem().getIdProjet())) {
            sprint.getItems().add(sprint1);
        }
        sprint.setCellFactory((Sprint) -> {
            return new ListCell<Sprint>() {
                @Override
                protected void updateItem(Sprint t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        setText(String.valueOf(t.getNomSprint()));
                    }
                }
            };
        });
        sprint.setConverter(new StringConverter<Sprint>() {
            @Override
            public String toString(Sprint object) {
                return String.valueOf(object.getNomSprint());
            }

            @Override
            public Sprint fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        sprint.getSelectionModel().selectFirst();
    }

    private void initImporatnce() {
        for (TypeImportance t : TypeImportance.values()) {
            importance.getItems().add(t);
        }
        importance.setValue(TypeImportance.GRANDE);
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
        uniteTemps.getItems().addAll(FXCollections.observableArrayList("heures", "minutes", "jours"));

        uniteTemps.setValue("heures");
        uniteTemps.valueProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<Integer> intervalle = getIntervalle(newValue);
            tempsRappel.setValueFactory(setIntervalle(intervalle.get(0), intervalle.get(1)));
        });

        tempsRappel.setValueFactory(setIntervalle(1, 24));
    }

    private void displayModal(AnchorPane modal, Double positionX) {
        if(positionX<900.0)
            affichageInfo.setVisible(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), modal);
        transition.setToX(positionX);
        modal.toFront();
        transition.play();
    }

    private SpinnerValueFactory<Integer> setIntervalle(int debut, int fin) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(debut, fin, 1, 1);
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
    public void setHomeController(HomeController h)
    {
        this.homeController=h;
    }

    class UserFx extends User {

        private Boolean Selected;
        private User user;

        public UserFx(User u) {
            super(u.getIdUser(), u.getNom(), u.getPrenom(), u.getEmail());
            user = new User(u.getIdUser(), u.getNom(), u.getPrenom(), u.getEmail());
            this.Selected = false;
        }

        public UserFx(User u, boolean selected) {
            super(u.getIdUser(), u.getNom(), u.getPrenom(), u.getEmail());
            user = new User(u.getIdUser(), u.getNom(), u.getPrenom(), u.getEmail());
            this.Selected = selected;
        }

        public Boolean getSelected() {
            return Selected;
        }

        public void setSelected(Boolean Selected) {
            this.Selected = Selected;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
    
}
