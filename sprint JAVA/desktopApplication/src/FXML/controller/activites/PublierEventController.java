/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.activites;

import animatefx.animation.FadeInRightBig;
import animatefx.animation.FadeOutRightBig;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import entity.activite.Evenement;
import entity.activite.Objectif;
import entity.activite.Reunion;
import entity.pojet.Projet;
import entity.user.User;
import enumeration.TypeImportance;
import functions.Functions;
import functions.SendMail;
import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import service.activiteService.EventService;
import service.activiteService.ReunionService;
import service.projetService.ProjetService;
import service.sprintService.SprintService;
import service.userService.UserService;

/**
 * FXML Controller class
 *
 * @author Freddy
 */
public class PublierEventController implements Initializable {

    private ReunionService rs;
    private UserService us;
    private EventService es;
    private SprintService ss;
    private ProjetService ps;
    private Evenement evenement= null;
    private FileChooser fileChooser;
    private File file;
    private FileInputStream fis;
    private Desktop desktop = Desktop.getDesktop();
    @FXML
    private StackPane stackpane;

    @FXML
    private AnchorPane affichageInfo;

    @FXML
    private JFXTextField filtre;
    
    @FXML
    private JFXTextField lieu;

    @FXML
    private TreeTableView<ProjetFx> t1;

    @FXML
    private TreeTableColumn<ProjetFx,Boolean> c1;

    @FXML
    private TreeTableColumn<ProjetFx,String> c2;

    @FXML
    private JFXDatePicker dateDebut;

    @FXML
    private JFXTimePicker heureDebut;

    @FXML
    private JFXDatePicker dateFin;

    @FXML
    private JFXTimePicker heureFin;

    @FXML
    private JFXTextField titre;

    @FXML
    private TextArea descriptionText;

    @FXML
    private TreeTableView<ProjetFx> tableViewPart;
    
    @FXML
    private JFXButton browser;
    @FXML
    private Pane affiche;
    
    @FXML
    private JFXButton supprimerAffiche;

    @FXML
    void ajouterProjet(ActionEvent event) {
        ArrayList<Projet> list = new ArrayList<Projet>();
            for (int i = 0; i < t1.getRoot().getChildren().size(); i++) {
                if (t1.getRoot().getChildren().get(i).getValue().getSelected()) {
                    list.add(t1.getRoot().getChildren().get(i).getValue().getProjet());      
                }
            }
            if (!list.isEmpty()) {
                initTable(tableViewPart,list);
            }
        hideModalHorizontally(affichageInfo);
    }

    @FXML
    void closeModal(ActionEvent event) {
        hideModalHorizontally(affichageInfo);
    }

    @FXML
    void enregistrerEvent(ActionEvent event) {
        ArrayList<String> erreurs = controleDeSaisie();
        if (erreurs.isEmpty()) {
            String titre = this.titre.getText();
            String lieu = this.lieu.getText();
            String description = descriptionText.getText();
            LocalDateTime dateDebut = LocalDateTime.of(this.dateDebut.getValue(), heureDebut.getValue());
            LocalDateTime dateFin = LocalDateTime.of(this.dateFin.getValue(), heureFin.getValue());

            ArrayList<Projet> listProjets = new ArrayList<>();
            if (tableViewPart.getRoot() != null) {
                tableViewPart.getRoot().getChildren().forEach((treeItem) -> {
                    listProjets.add(treeItem.getValue().getProjet());
                });
                
            }
            this.evenement = new Evenement();
            evenement.setTitre(titre);
            evenement.setDateDebut(dateDebut);
            evenement.setDateFin(dateFin);
            evenement.setDateRappel(null);
            evenement.setDescription(description);
            evenement.setLieu(lieu);
            
            evenement.setAffiche(fis);
            
            int idReunion = es.insertIdReturn(evenement,file);
            listProjets.forEach((projet) -> {
                es.insertProjetToEvent(projet.getIdProjet(), idReunion);
            });
            
            alert("Ajouté avec succés");
        } else {
            alert(erreurs.toString().replace('[',' ').replace(']', ' '));
            
        }
    }

    @FXML
    void publierEvent(ActionEvent event) {
        
    }

    @FXML
    void supprimerProjets(ActionEvent event) {
        if(tableViewPart.getRoot()!=null)
        {
            ArrayList<TreeItem<ProjetFx>> list = new ArrayList<>(tableViewPart.getRoot().getChildren().stream().filter((TreeItem<ProjetFx> t) -> {
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
    void deroulerAjouterProjet(ActionEvent event) {
        
        initTable(t1, (ArrayList<Projet>) ps.displayAll());
        displayModalHorizontally(affichageInfo);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        affichageInfo.setVisible(false);
        affichageInfo.toBack();
        supprimerAffiche.setVisible(false);
        rs = new ReunionService();
        ss = new SprintService();
        ps = new ProjetService();
        us = new UserService();
        es = new EventService();
        fileChooser =  new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files",Arrays.asList(".*png","*.jpeg","*.jpg")));
        
        hideModalHorizontally(affichageInfo);
        browser.setOnAction((event) -> {
            
            file = fileChooser.showOpenDialog(stackpane.getScene().getWindow());
            if(file!=null)
            {
                supprimerAffiche.setVisible(true);
                String path = file.getAbsolutePath();
                Image image = new Image(file.toURI().toString(),100,150,true,true);
                System.err.println(file.toURI().toString());
                ImageView imageAffiche = new ImageView(image);
                imageAffiche.setFitWidth(594);
                imageAffiche.setFitHeight(252);
                imageAffiche.setPreserveRatio(true);
                affiche.getChildren().setAll(imageAffiche);
                
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PublierEventController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
    }  
    @FXML
    void supprimerAffiche(ActionEvent event) {
        file=null;
        supprimerAffiche.setVisible(false);
        affiche.getChildren().clear();
    }
    private ArrayList<String> controleDeSaisie() {
        ArrayList<String> erreurs = new ArrayList<>();

        String titre = this.titre.getText();
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

        return erreurs;
    }

    private void initTable(TreeTableView t1, ArrayList<Projet> data) {
        
        TreeItem<ProjetFx> root = new TreeItem<>(new ProjetFx(new Projet())); 
       for (Projet projet : data) {
            root.getChildren().add(new TreeItem<ProjetFx>(new ProjetFx(projet)));
        }
        c1.setCellValueFactory((param) -> {
            TreeItem<ProjetFx> treeItem = param.getValue();
            ProjetFx emp = treeItem.getValue();
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
            CheckBoxTreeTableCell<ProjetFx, Boolean> cell = new CheckBoxTreeTableCell<ProjetFx, Boolean>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
           
        c2.setCellValueFactory(new TreeItemPropertyValueFactory<ProjetFx, String>("nom"));
        t1.getColumns().setAll(c1,c2);
        t1.setRoot(root);
        t1.setShowRoot(false);
        
    }

    private void displayModalHorizontally(Node node)
    {
        if(!node.isVisible())
        {
            node.setVisible(true);
            node.toFront();
            new FadeInRightBig(node).setDelay(Duration.ONE).play();
        }
    }
    private void hideModalHorizontally(Node node)
    {
        if(node.isVisible())
        {
            FadeOutRightBig animation = new FadeOutRightBig(node);
            animation.setDelay(Duration.ONE);
            animation.setOnFinished((event) -> {
                node.setVisible(false);
                node.toBack();
            });
            animation.play();
        }
    }

    private void upload()
    {
        
    }
    private void alert(String text) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(stackpane, dialogLayout, JFXDialog.DialogTransition.NONE);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {
            dialog.close();
        });
        JFXTextArea tx = new JFXTextArea(text);
        tx.setWrapText(true);
        tx.setEditable(false);
        dialogLayout.setBody(tx);
        dialogLayout.setActions(buttonOk);
        dialog.show();    
    }
    
    
    
}
