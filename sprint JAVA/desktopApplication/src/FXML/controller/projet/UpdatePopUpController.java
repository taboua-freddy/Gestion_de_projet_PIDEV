/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.projet;

import entity.pojet.Projet;
import entity.pojet.Projet.status;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.groupeService.GroupeService;
import service.projetService.ProjetService;

/**
 * FXML Controller class
 *
 * @author Aymen Touihri
 */
public class UpdatePopUpController implements Initializable {

    ObservableList observableList = FXCollections.observableArrayList();

    @FXML
    private TextField upname;
    @FXML
    private ComboBox<String> upgroup, upst;
    @FXML
    private DatePicker upstd, upend;
    @FXML
    private TextArea updesc;
    @FXML
    private Button dis, rem, up;

    private ProjetController.Projet projetSelected;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    public void Dismiss(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void Update(ActionEvent event) {
        GroupeService gs = new GroupeService();
        ProjetService ps = new ProjetService();
        entity.pojet.Projet p = new entity.pojet.Projet();
        if (upname.getText().isEmpty()) {
            ShowAlert();
        } else if (upstd.getValue() == null) {
            ShowAlert();
        } else if (upend.getValue() == null) {
            ShowAlert();
        } else if (updesc.getText().isEmpty()) {
            ShowAlert();
        }else if ( upstd.getValue().isAfter(upend.getValue()) || upstd.getValue().equals(upend.getValue())) {
            ShowAlertDate();
        } 
        else {

            java.sql.Date start = java.sql.Date.valueOf(upstd.getValue());
            java.sql.Date enda = java.sql.Date.valueOf(upend.getValue());
            p.setDateDebut(start);
            p.setDateFin(enda);
            p.setDescription(updesc.getText());
            p.setNom(upname.getText());
            // Changer le gs.getGroupeByID = ByName
            //p.setGroupe(gs.getGroupeById(1));
            p.setIdProjet(projetSelected.getId().getValue());
            if (projetSelected.getStatus().getValue().equals(status.enumToString(status.NOTSTARTED)))
            {
                p.setStatus(status.NOTSTARTED);
            }
            else{
               p.setStatus(status.stringToEnum(upst.getSelectionModel().getSelectedItem().toUpperCase())); 
            }
            
//            System.out.println(status.stringToEnum(upst.getSelectionModel().getSelectedItem().toUpperCase()));
            
            ps.update(p);
            ShowAlertSuccess(p.getNom());
            this.Dismiss(event);
        }
    }

    public void Reset(ProjetController.Projet pr) {
        observableList.removeAll(observableList);
        observableList.add(status.enumToString(status.INPROGRESS));
        observableList.add(status.enumToString(status.ONHOLD));

        //System.out.println(ProjetController.getSelectedProject().getNom().getValue().isEmpty());
        updesc.setText(pr.getDescription().getValue());
        upname.setText(pr.getNom().getValue());
        upstd.setValue(LocalDate.parse(pr.getStart().getValue()));
        upstd.setDisable(true);
        upend.setValue(LocalDate.parse(pr.getEnd().getValue()));
        upst.setItems(observableList);
        if (pr.getStatus().getValue().equals(status.enumToString(status.INPROGRESS))) {
            upst.getSelectionModel().selectFirst();
            upst.setDisable(false);
        } else if (pr.getStatus().getValue().equals(status.enumToString(status.ONHOLD))) {
            upst.getSelectionModel().selectLast();
            upst.setDisable(false);
        }else{
            upst.getSelectionModel().select(-1);
            upst.setDisable(true);
            upstd.setDisable(false);
        }
        this.projetSelected = pr;
    }

    @FXML
    public void ResetAll(ActionEvent event) {

        observableList.removeAll(observableList);
        observableList.add(status.enumToString(status.INPROGRESS));
        observableList.add(status.enumToString(status.ONHOLD));

        updesc.setText(projetSelected.getDescription().getValue());
        upname.setText(projetSelected.getNom().getValue());
        upstd.setValue(LocalDate.parse(projetSelected.getStart().getValue()));
        upstd.setDisable(true);
        upend.setValue(LocalDate.parse(projetSelected.getEnd().getValue()));
        upst.setItems(observableList);
        if (projetSelected.getStatus().getValue().equals(status.enumToString(status.INPROGRESS))) {
            upst.getSelectionModel().selectFirst();
            upst.setDisable(false);
        } else if (projetSelected.getStatus().getValue().equals(status.enumToString(status.ONHOLD))){
            upst.getSelectionModel().selectLast();
            upst.setDisable(false);
        }else{
                upst.getSelectionModel().select(-1);
                 upst.setDisable(true);
                 upstd.setDisable(false);
                }
        this.projetSelected = projetSelected;
    }

    private void ShowAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Données Invalide");
        alert.setHeaderText("Champs de name Vide ou Erronné !");
        alert.setContentText("Veuillez verifié vos données.");
        ButtonType buttonTypeYes = new ButtonType("OK");
        alert.getButtonTypes().setAll(buttonTypeYes);
        alert.showAndWait();
    }

    private void ShowAlertSuccess(String nom) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edition Successful!");
        alert.setHeaderText("Done");
        alert.setContentText(nom + " edited with success.");
        ButtonType buttonTypeYes = new ButtonType("OK");
        alert.getButtonTypes().setAll(buttonTypeYes);
        alert.showAndWait();

    }

    public void ShowAlertDate() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Dates Invalides");
        alert.setHeaderText("Champs de Dates invalides !");
        alert.setContentText("Veuillez verifié vos données.");
        ButtonType buttonTypeYes = new ButtonType("OK");
        alert.getButtonTypes().setAll(buttonTypeYes);
        alert.showAndWait();
    }
}
