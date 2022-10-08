/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.tache;

import entity.tache.TacheT;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import service.tacheService.ServiceTache;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;
import java.io.File;
import javax.management.Notification;
import javafx.scene.control.Alert;
import com.itextpdf.text.Document;
//import eu.hansolo.enzo.notification.Notification.Notifier;

import org.controlsfx.control.Notifications;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;

import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

/**
 * * FXML Controller class
 *
 * @author maysa
 */
public class TacheController implements Initializable {

    @FXML
    private TableView<TacheT> TableView;

    @FXML
    private TableColumn<TacheT, Integer> colNum;

    @FXML
    private TableColumn<TacheT, Integer> colFonctionallite;

    @FXML
    private TableColumn<TacheT, Integer> colPriorite;

    @FXML
    private TableColumn<TacheT, String> colNom;

    @FXML
    private TableColumn<TacheT, Date> colDateDebut;

    @FXML
    private TableColumn<TacheT, Date> colDateFin;

    @FXML
    private TableColumn<TacheT, String> colDescription;
    @FXML
    private TableColumn<TacheT, String> colEtat;
    @FXML
    private TableView<TacheT> TableView1;

    @FXML
    private TableColumn<TacheT, Integer> colNum1;

    @FXML
    private TableColumn<TacheT, Integer> colFonctionallite1;

    @FXML
    private TableColumn<TacheT, Integer> colPriorite1;

    @FXML
    private TableColumn<TacheT, String> colNom1;

    @FXML
    private TableColumn<TacheT, Date> colDateDebut1;

    @FXML
    private TableColumn<TacheT, Date> colDateFin1;

    @FXML
    private TableColumn<TacheT, String> colDescription1;

    @FXML
    private TableColumn<TacheT, Integer> colEtat1;
    @FXML
    private TableView<TacheT> TableView11;

    @FXML
    private TableColumn<TacheT, Integer> colNum11;

    @FXML
    private TableColumn<TacheT, Integer> colFonctionallite11;

    @FXML
    private TableColumn<TacheT, Integer> colPriorite11;

    @FXML
    private TableColumn<TacheT, String> colNom11;

    @FXML
    private TableColumn<TacheT, Date> colDateDebut11;

    @FXML
    private TableColumn<TacheT, Date> colDateFin11;

    @FXML
    private TableColumn<TacheT, String> colDescription11;

    @FXML
    private TableColumn<TacheT, Integer> colEtat11;
    @FXML
    private PieChart mypiechart;
    ObservableList<TacheT> cls;
    @FXML
    private Button commencer;
    @FXML
    private Button done;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        affichertodo();
        afficherdoing();
        afficherdone();

        /*List<TacheT> lista2 = ser.displayAlldoing();
         cls = FXCollections.observableArrayList();
        for (TacheT  aux : lista1)
        {
            
          cls.add(new TacheT(aux.getIdTache(),aux.getIdStory(),aux.getDateDebut(),aux.getDateFin(),aux.getPriorite(),aux.getNom(),aux.getEtat(),aux.getDescription()));  
        
          TableView1.setItems(cls);}
        
        colNum1.setCellValueFactory(new PropertyValueFactory<>("IdTache"));
        colFonctionallite1.setCellValueFactory(new PropertyValueFactory<>("idStory"));
        colNom1.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDateDebut1.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin1.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
       // colPriorite.setCellValueFactory(new PropertyValueFactory<>("priorite"));
         colEtat1.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDescription1.setCellValueFactory(new PropertyValueFactory<>("description"));*/
    }

    private void affichertodo() {
        ServiceTache ser = new ServiceTache();
        List<TacheT> lista1 = ser.displayAlltodo();
        cls = FXCollections.observableArrayList();
        for (TacheT aux : lista1) {

            cls.add(new TacheT(aux.getIdTache(), aux.getIdStory(), aux.getDateDebut(), aux.getDateFin(), aux.getPriorite(), aux.getNom(), aux.getEtat(), aux.getDescription()));

            TableView.setItems(cls);
        }

        colNum.setCellValueFactory(new PropertyValueFactory<>("IdTache"));
        colFonctionallite.setCellValueFactory(new PropertyValueFactory<>("idStory"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        // colPriorite.setCellValueFactory(new PropertyValueFactory<>("priorite"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void afficherdoing() {
        ServiceTache ser = new ServiceTache();
        List<TacheT> lista1 = ser.displayAlldoing();
        cls = FXCollections.observableArrayList();
        for (TacheT aux : lista1) {

            cls.add(new TacheT(aux.getIdTache(), aux.getIdStory(), aux.getDateDebut(), aux.getDateFin(), aux.getPriorite(), aux.getNom(), aux.getEtat(), aux.getDescription()));

            TableView1.setItems(cls);
        }

        colNum1.setCellValueFactory(new PropertyValueFactory<>("IdTache"));
        colFonctionallite1.setCellValueFactory(new PropertyValueFactory<>("idStory"));
        colNom1.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDateDebut1.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin1.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        // colPriorite.setCellValueFactory(new PropertyValueFactory<>("priorite"));
        colEtat1.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDescription1.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void afficherdone() {
        ServiceTache ser = new ServiceTache();
        List<TacheT> lista1 = ser.displayAlldone();
        cls = FXCollections.observableArrayList();
        for (TacheT aux : lista1) {

            cls.add(new TacheT(aux.getIdTache(), aux.getIdStory(), aux.getDateDebut(), aux.getDateFin(), aux.getPriorite(), aux.getNom(), aux.getEtat(), aux.getDescription()));

            TableView11.setItems(cls);
        }

        colNum11.setCellValueFactory(new PropertyValueFactory<>("IdTache"));
        colFonctionallite11.setCellValueFactory(new PropertyValueFactory<>("idStory"));
        colNom11.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDateDebut11.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin11.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        // colPriorite.setCellValueFactory(new PropertyValueFactory<>("priorite"));
        colEtat11.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDescription11.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML
    private void Commencerdoing(ActionEvent event) throws SQLException {

        ServiceTache sa = new ServiceTache();
        sa.updateddoing(colNum.getCellData(TableView.getSelectionModel().getSelectedIndex()));
        //  refresh();
        JOptionPane.showMessageDialog(null, "TacheT commencée");
        affichertodo();
        afficherdoing();
        afficherdone();
    }

    @FXML
    private void Commencerdone(ActionEvent event) throws SQLException {

        ServiceTache sa = new ServiceTache();
        sa.updateddone(colNum1.getCellData(TableView1.getSelectionModel().getSelectedIndex()));
        //  refresh();
        JOptionPane.showMessageDialog(null, "TacheT Terminée");
        affichertodo();
        afficherdoing();
        afficherdone();

    }

    @FXML
    private void PieChartSample(ActionEvent event) throws SQLException {
        ServiceTache sa = new ServiceTache();
        int count1 = 0, count2 = 0, count3 = 0;
        count1 = sa.counttodo();
        count2 = sa.countdoing();
        count3 = sa.countdone();

        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("todo", count1),
                        new PieChart.Data("doing", count2),
                        new PieChart.Data("done", count3));

        mypiechart.setData(pieChartData);
    }

    private void Warning() {

        // int b=3; ;
        int a = 0;
        int b = 3;
        if (b >= 2) {
            Notifications notificationBuilder = Notifications.create()
                    .title("notification ")
                    .text("le service des evenements a atteint " + b + "reclamations")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT);
            notificationBuilder.darkStyle();

            notificationBuilder.showWarning();
        }
        if (a >= 2) {

            Notifications notificationBuilder = Notifications.create()
                    .title("notification ")
                    .text("le service d'achat a atteint " + a + "reclamations")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT);
            notificationBuilder.darkStyle();

            notificationBuilder.showWarning();
        }
    }
}
