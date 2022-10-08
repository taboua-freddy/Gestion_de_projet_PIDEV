/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.tache;
import javafx.scene.control.ComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import service.tacheService.ServiceTache;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Predicate;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.cell.TextFieldTableCell;
import javax.swing.JOptionPane;
import entity.tache.TacheT;
import java.text.ParseException;
import javafx.scene.input.InputMethodEvent;

/**
 * FXML Controller class
 *
 * @author maysa
 */
public class NouveaucrudController implements Initializable {

    @FXML
    private TextField NomID;

    @FXML
    private TextField PrioriteID;

    @FXML
    private DatePicker DateFinID;

    @FXML
    private DatePicker DatedebutID;

    @FXML
    private TextArea DESCRIPTIONID;

    @FXML
    private TableView<TacheT> TableView;

    @FXML
    private ComboBox<String> UtilisateurID;

    @FXML
    private ComboBox<Integer> FonctionnaliteID;

    @FXML
    private ComboBox<String> EtatID;

    private TextField rechercheID;
    @FXML
    private Button Ajouter;

    @FXML
    private TableColumn<TacheT, Integer> colNum;

    @FXML
    private TableColumn<TacheT, Integer> colFonctionnalite;

    @FXML
    private TableColumn<TacheT, String> colNom;

    @FXML
    private TableColumn<TacheT, Integer> colUtilisateur;

    @FXML
    private TableColumn<TacheT, Date> colDatedebut;

    @FXML
    private TableColumn<TacheT, Date> colDatefin;

    @FXML
    private TableColumn<TacheT, Integer> colPriorite;

    @FXML
    private TableColumn<TacheT, String> colEtat;

    @FXML
    private TableColumn<TacheT, String> colDescription;

    private TableColumn colAction;
    private String statusCode, statusClick;
    ObservableList<TacheT> cls;

    private Connection cnx;
    private Statement st;
    private Statement ste;

    private PreparedStatement pst;
    private ResultSet rs;
    ServiceTache ser = new ServiceTache();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableView.setEditable(true);
        colNom.setCellFactory(TextFieldTableCell.forTableColumn());
//       colFonctionnalite.setCellFactory(TextFieldTableCell.forTableColumn());
        ServiceTache ser = new ServiceTache();
        EtatID.getItems().removeAll(EtatID.getItems());
        EtatID.getItems().addAll("todo", "doing", "done");

        //ser.Findtachescrummaster(Integer.parseInt(rechercheID.getText())).stream().forEach(System.out::println);
        List<TacheT> lista = ser.displayAll();
        cls = FXCollections.observableArrayList();
        for (TacheT aux : lista) {

            cls.add(new TacheT(aux.getIdTache(), aux.getIdStory(), aux.getIdUser(), aux.getDateDebut(), aux.getDateFin(), aux.getPriorite(), aux.getNom(), aux.getEtat(), aux.getDescription()));

            TableView.setItems(cls);
        }

        colNum.setCellValueFactory(new PropertyValueFactory<>("IdTache"));
        colFonctionnalite.setCellValueFactory(new PropertyValueFactory<>("idStory"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colUtilisateur.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colDatedebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDatefin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colPriorite.setCellValueFactory(new PropertyValueFactory<>("priorite"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableView.setItems(cls);
    }
/*
    @FXML
    public void Ajouter(ActionEvent event) throws SQLException, ParseException {
        String etats[] = {"todo", "doing", "done"};
        int users[] = {1};

        /*EtatID.setItems(FXCollections 
                             .observableArrayList(etats));*/
  /*      UtilisateurID.setItems(FXCollections
                .observableArrayList(users));
        String nom = NomID.getText();
//String etat = (String)EtatID.getValue().toString() ; 

        String description = DESCRIPTIONID.getText();
        String str;
        str = DatedebutID.getValue().toString();
        Date dt = Date.valueOf(str);
        String str1 = DateFinID.getValue().toString();
        Date dt1 = Date.valueOf(str1);

        /*LocalDate date = DatedebutID.getValue();
       


java.util.Date date1 = Date.valueOf(date);
 java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
 LocalDate date11 = DateFinID.getValue();
       


java.util.Date date2 = Date.valueOf(date);
 java.sql.Date sqlDate1 = new java.sql.Date(date2.getTime());*/
//String etat = EtatID.getText() ; 
        /* LocalDate value = DatedebutID.getValue();
                Date date = (Date) Date.from(value.atStartOfDay(defaultZoneId).toInstant());


        ///Float ancienprix = Float.parseFloat(ancienPrixID.getText()) ; 
       // Integer reduction = Integer.parseInt(reductionID.getText()) ; 
          //Date   date       = format.parse ( "2009-12-31" );
        Date   date2      = format.parse ( "2009-12-31" );
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        
               // Date   date    format.parse    = ( date_debut );

      //date TacheT(Date dateDebut, Date dateFin, int priorite, String nom, TypeEtat etat, String description)*/
        // TacheT  t = new TacheT(date,date, 0,"doing", nom,description);
 /*       ServiceTache ser = new ServiceTache();

        ser.insertPST(new TacheT(dt, dt, 0, nom, "doing", description));

        refresh();
    }
*/
    void refresh() {

        NomID.setText("");
        PrioriteID.setText("");
        DESCRIPTIONID.setText("");
        //  tfplace.setText("");
        ServiceTache sa = new ServiceTache();
        cls.clear();
        cls.addAll(sa.displayAll());
    }

    @FXML
    private void Supprimer(ActionEvent event) throws SQLException {

        ServiceTache sa = new ServiceTache();
        sa.delete(colNum.getCellData(TableView.getSelectionModel().getSelectedIndex()));
        refresh();
        JOptionPane.showMessageDialog(null, "TacheT Supprimée");
    }

    private void RechercheT() {
        rechercheID.setEditable(true);

        //int  nom = Integer.parseInt(rechercheID.getText()) ;
        ServiceTache ser = new ServiceTache();
        // List<TacheT> lista= ser.Findtachescrummaster(nom);
        FilteredList<TacheT> filteredData = new FilteredList<>(cls, e -> true);
        rechercheID.setOnKeyReleased(e -> {
            rechercheID.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super TacheT>) tache -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    String red = newValue;

                    if (tache.getNom().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (tache.getEtat().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (tache.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (tache.getDateFin().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (tache.getDateFin().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });

            SortedList<TacheT> sortedData;
            sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(TableView.comparatorProperty());
            TableView.setItems(sortedData);
        });

    }

    @FXML
    private void updateNom(TableColumn.CellEditEvent bb) throws SQLException {
        ServiceTache ser = new ServiceTache();

        TacheT tacheselected = TableView.getSelectionModel().getSelectedItem();
        tacheselected.setNom(bb.getNewValue().toString());
        ser.update(tacheselected, tacheselected.getIdTache());

    }

    @FXML
    private void updateFonctionnalite(TableColumn.CellEditEvent bb) throws SQLException {
        ServiceTache ser = new ServiceTache();

        TacheT tacheselected = TableView.getSelectionModel().getSelectedItem();
        tacheselected.setIdStory((int) bb.getNewValue());
        ser.update(tacheselected, tacheselected.getIdStory());

    }

    /*@FXML
    private  java.sql.Date Date Ajouter1(ActionEvent event) {
        LocalDate date = DatedebutID.getValue();
       


java.util.Date date1 = Date.valueOf(date);
 java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
 return sqlDate;
    }
    
     */

    @FXML
    private void Ajouter(InputMethodEvent event) {
    }
}


/* private boolean saisie()
        {
           private boolean controle_number() throws SQLException
    {
                
        for(int i=0;i<tfnumber.getText().length();i++){
        
            if(!Character.isDigit(tfnumber.getText().charAt(i))){
               
            JOptionPane.showMessageDialog(null, "Reference must be a number", "Attention", JOptionPane.ERROR_MESSAGE);
                return false;
            }
             ServiceAnimal sa = new ServiceAnimal();
      List<Animal> listA = sa.readAll();
            for (Animal listA1 : listA) {
                if(listA1.getIdA()==Integer.parseInt(tfnumber.getText())){
                    JOptionPane.showMessageDialog(null, "Reference already used", "Attention", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
              
    }
        return true;
    }
 */