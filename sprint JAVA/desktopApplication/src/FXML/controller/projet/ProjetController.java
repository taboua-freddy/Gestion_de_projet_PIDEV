/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.projet;


import FXML.controller.HomeController;
import entity.pojet.Projet.status;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import service.groupeService.GroupeService;
import service.projetService.ProjetService;
import java.util.Base64;
import java.io.*;

/**
 * FXML Controller class
 *
 * @author Aymen Touihri
 */
public class ProjetController implements Initializable {

    ObservableList observableList = FXCollections.observableArrayList();

    public HomeController homeController;
    ProjetService ps = new ProjetService();
    GroupeService gs = new GroupeService();

    @FXML
    private TreeTableView<Projet> tv;

    @FXML
    private TreeTableColumn<Projet, Number> col0;
    @FXML
    private TreeTableColumn<Projet, String> col1;
    @FXML
    private TreeTableColumn<Projet, String> col2;
    @FXML
    private TreeTableColumn<Projet, String> col3;
    @FXML
    private TreeTableColumn<Projet, String> col4;
    @FXML
    private TreeTableColumn<Projet, String> col5;
    @FXML
    private TreeTableColumn<Projet, String> col6;
    @FXML
    private TextField pname, src;

    @FXML
    private ComboBox<String> group;
    @FXML
    private DatePicker std, end;
    @FXML
    private TextArea desc;
    @FXML
    private Button addbtn, resbtn, rem, up, fin;

    public static Projet SelectedProject;

    public static Projet getSelectedProject() {
        return SelectedProject;
    }
    public void setHomeController(HomeController h)
    {
        this.homeController=h;
    }

    public static void setSelectedProject(Projet SelectedProject) {
        ProjetController.SelectedProject = SelectedProject;
    }

    @FXML
    public void DataTableUpdateSearch() {
        SearchByKeyWord(src.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Reset();
       
       loadData();
        tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Projet>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Projet>> observable, TreeItem<Projet> oldValue, TreeItem<Projet> newValue) {

                if (newValue != null) {
                    if (newValue.getValue().getStatus().getValue().equals(status.FINISHED.enumToString())) {
                        fin.setDisable(true);
                    } else {
                        fin.setDisable(false);
                    }
                }
            }
        });

    }

    @FXML
    private void delete(ActionEvent event) {
        if (tv.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selected Project");
            alert.setHeaderText("Error 403 !");
            alert.setContentText("Select a project to delete.");
            ButtonType buttonTypeYes = new ButtonType("OK");
            alert.getButtonTypes().setAll(buttonTypeYes);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Are you sure to delete?");
            alert.setHeaderText("Confirmation!");
            alert.setContentText("You're attempting to delete a project.");
            ButtonType buttonTypeYes = new ButtonType("OK");
            ButtonType buttonTypeNo = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                ps.delete(tv.getSelectionModel().getSelectedItem().getValue().getId().getValue());
                loadData();
            }

        }
    }

    @FXML
    private void PopUpUpdate(ActionEvent event) throws IOException {

        if (tv.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selected Project");
            alert.setHeaderText("Error 403 !");
            alert.setContentText("Select a project to update.");
            ButtonType buttonTypeYes = new ButtonType("OK");
            alert.getButtonTypes().setAll(buttonTypeYes);
            alert.showAndWait();
        } else {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXML/view/projet/UpdatePopUp.fxml"));
            Parent settingsParent = loader.load();

            UpdatePopUpController controller = loader.getController();
            controller.Reset(tv.getSelectionModel().getSelectedItem().getValue());
            Scene settingsScene = new Scene(settingsParent);
            Stage popup = new Stage();
            setSelectedProject(tv.getSelectionModel().getSelectedItem().getValue());

            popup.initStyle(StageStyle.TRANSPARENT);
            popup.setScene(settingsScene);
            popup.initModality(Modality.WINDOW_MODAL);
            popup.showAndWait();
            loadData();

        }

    }

    @FXML
    private void insert(ActionEvent event) throws IOException {
        entity.pojet.Projet p = new entity.pojet.Projet();
        if (pname.getText().isEmpty()) {
            ShowAlert();
        } else if (std.getValue() == null) {
            ShowAlert();
        } else if (end.getValue() == null) {
            ShowAlert();
        } else if (desc.getText().isEmpty()) {
            ShowAlert();
        }else if ( std.getValue().isAfter(end.getValue()) || std.getValue().equals(end.getValue())) {
            ShowAlertDate();
        } else {

            java.sql.Date start = java.sql.Date.valueOf(std.getValue());
            java.sql.Date enda = java.sql.Date.valueOf(end.getValue());
            p.setDateDebut(start);
            p.setDateFin(enda);
            p.setDescription(desc.getText());
            p.setNom(pname.getText());
            // Changer le gs.getGroupeByID = ByName
            //p.setGroupe(gs.getGroupeById(1));
            p.setIdProjet(ps.getLastProjet().getIdProjet() + 1);

            ps.insert(p);
            ShowAlertSuccess(p.getNom());
            Reset();
            loadData();
            sms();
            

        }

    }

    private void loadData() {
        observableList.removeAll(observableList);
        List<entity.pojet.Projet> myprojects = new ArrayList<>();
        List<TreeItem> list = new ArrayList<>();
        myprojects.addAll(ps.displayAll());
        SetupTable(myprojects);

    }

    private void SearchByKeyWord(String key) {
        System.err.println(homeController.getUser());
        observableList.removeAll(observableList);
        List<entity.pojet.Projet> myprojects = new ArrayList<>();
        myprojects.addAll(ps.getProjetsByKey(key));
        SetupTable(myprojects);
    }

    private void SetupTable(List<entity.pojet.Projet> myprojects) {

        List<TreeItem> list = new ArrayList<>();
        int i = 0;
        TreeItem root = new TreeItem<>(new Projet(0, 0, "nom", "descrpition", "grp_name", java.sql.Date.valueOf("2019-09-09"), java.sql.Date.valueOf("2019-09-09"), status.enumToString(entity.pojet.Projet.status.INPROGRESS)));
        for (i = 0; i < myprojects.size(); i++) {
            Projet p = new Projet();
            p.setNbr(i + 1);
            p.setId(myprojects.get(i).getIdProjet());
            p.setNom(myprojects.get(i).getNom());
            p.setDescription(myprojects.get(i).getDescription());
            p.setGrp_name("Group 1");
            p.setEnd(myprojects.get(i).getDateFin().toString());
            //System.out.println(myprojects.get(i).getDateFin().getDay());
            p.setStart(myprojects.get(i).getDateDebut().toString());
            p.setStatus(status.enumToString(myprojects.get(i).getStatus()));
            
            /*
            Change State Automatic From Not Started To In Progress
            And Update Database
            */
            if (myprojects.get(i).getStatus().equals(status.NOTSTARTED))
            {
                if ((myprojects.get(i).getDateDebut().equals(getNow())||(myprojects.get(i).getDateDebut().before(getNow()))))
                {
                    myprojects.get(i).setStatus(status.INPROGRESS);
                    ps.update(myprojects.get(i));
                    p.setStatus(status.enumToString(status.INPROGRESS));
                }
            }
            
            TreeItem<Projet> t = new TreeItem<>(p);
            list.add(t);

        }
        root.getChildren().addAll(list);

        col0.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Projet, Number>, javafx.beans.value.ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Projet, Number> param) {
                return param.getValue().getValue().getNbr();
            }
        });
        col1.setCellValueFactory((TreeTableColumn.CellDataFeatures<Projet, String> param) -> param.getValue().getValue().getNom());
        col2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Projet, String> param) -> param.getValue().getValue().getGrp_name());
        col3.setCellValueFactory((TreeTableColumn.CellDataFeatures<Projet, String> param) -> param.getValue().getValue().getStart());
        col4.setCellValueFactory((TreeTableColumn.CellDataFeatures<Projet, String> param) -> param.getValue().getValue().getEnd());
        col5.setCellValueFactory((TreeTableColumn.CellDataFeatures<Projet, String> param) -> param.getValue().getValue().getDescription());
        col6.setCellValueFactory((TreeTableColumn.CellDataFeatures<Projet, String> param) -> param.getValue().getValue().getStatus());
        col6.setCellFactory((TreeTableColumn<Projet, String> param) -> {
            TreeTableCell cell = new TreeTableCell<Projet, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<Projet> ttr = getTreeTableRow();
                    if (!empty) {
                        setText(item.toString());
                        setTextFill(Paint.valueOf(States(entity.pojet.Projet.status.stringToEnum(item))));

                    }

                }
            };
            return cell;
        });
        tv.setRoot(root);
        tv.setShowRoot(false);
    }

    public String States(entity.pojet.Projet.status State) {
        switch (State) {
            case INPROGRESS:
                return "orange";

            case FINISHED:
                return "green";

            case ONHOLD:
                return "red";

            case NOTSTARTED:
                return "blue";
            default:
                return "black";

        }
    }

    private void ShowAlertSuccess(String a) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Insertion Successful!");
        alert.setHeaderText("Done");
        alert.setContentText(a + " added with success.");
        ButtonType buttonTypeYes = new ButtonType("OK");
        alert.getButtonTypes().setAll(buttonTypeYes);
        alert.showAndWait();
        loadData();
    }

    private void ShowFinishedSuccess(String a) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Updating!");
        alert.setHeaderText("Congratulations");
        alert.setContentText(a + " finished with success.");
        ButtonType buttonTypeYes = new ButtonType("OK");
        alert.getButtonTypes().setAll(buttonTypeYes);
        alert.showAndWait();
        loadData();
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
    

    class Projet {

        SimpleIntegerProperty nbr;
        SimpleIntegerProperty id;
        SimpleStringProperty nom;
        SimpleStringProperty description;
        SimpleStringProperty grp_name;
        SimpleStringProperty start, end, status;

        public Projet() {
        }

        public Projet(int nbr, int id, String nom, String description, String grp_name, Date start, Date end, String status) {
            this.nbr = new SimpleIntegerProperty(nbr);
            this.id = new SimpleIntegerProperty(id);
            this.nom = new SimpleStringProperty(nom);
            this.description = new SimpleStringProperty(description);
            this.grp_name = new SimpleStringProperty(grp_name);
            this.start = new SimpleStringProperty(start.toLocalDate().toString());
            this.end = new SimpleStringProperty(end.toLocalDate().toString());
            this.status = new SimpleStringProperty(status);
        }

        public SimpleStringProperty getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = new SimpleStringProperty(status);
        }

        public SimpleIntegerProperty getId() {
            return id;
        }

        public void setId(int id) {
            this.id = new SimpleIntegerProperty(id);
        }

        public SimpleIntegerProperty getNbr() {
            return nbr;
        }

        public SimpleStringProperty getNom() {
            return nom;
        }

        public SimpleStringProperty getDescription() {
            return description;
        }

        public SimpleStringProperty getGrp_name() {
            return grp_name;
        }

        public SimpleStringProperty getStart() {
            return start;
        }

        public SimpleStringProperty getEnd() {
            return end;
        }

        public void setNbr(int nbr) {
            this.nbr = new SimpleIntegerProperty(nbr);;
        }

        public void setNom(String nom) {
            this.nom = new SimpleStringProperty(nom);;
        }

        public void setDescription(String description) {
            this.description = new SimpleStringProperty(description);
        }

        public void setGrp_name(String grp_name) {
            this.grp_name = new SimpleStringProperty(grp_name);
        }

        public void setStart(String start) {
            this.start = new SimpleStringProperty(start);
        }

        public void setEnd(String end) {
            this.end = new SimpleStringProperty(end);
        }

    }

    @FXML
    public void FinishProject(ActionEvent event) {
        if (tv.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selected Project");
            alert.setHeaderText("Error 402 !");
            alert.setContentText("Select a project to finish.");
            ButtonType buttonTypeYes = new ButtonType("OK");
            alert.getButtonTypes().setAll(buttonTypeYes);
            alert.showAndWait();
        } else {
            entity.pojet.Projet p = new entity.pojet.Projet();
            Projet pr = tv.getSelectionModel().getSelectedItem().getValue();
            p.setStatus(status.FINISHED);
            p.setDateFin(getNow());
            p.setDateDebut(Date.valueOf(pr.getStart().getValue()));
            p.setDescription(pr.getDescription().getValue());
            p.setNom(pr.getNom().getValue());
            // Changer le gs.getGroupeByID = ByName
            //p.setGroupe(gs.getGroupeById(1));
            p.setIdProjet(pr.getId().getValue());

            ps.update(p);
            ShowFinishedSuccess(p.getNom());
            loadData();

        }
    }

    public Date getNow()
    {
        return new Date(LocalDate.now().getYear() - 1900, LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getDayOfMonth());
    }
    @FXML
    public void Reset() {
        pname.setText("");
        desc.setText("");
        end.setValue(LocalDate.now());
        std.setValue(LocalDate.now());
    }

    public void ShowAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Données Invalide");
        alert.setHeaderText("Champs de name Vide ou Erronné !");
        alert.setContentText("Veuillez verifié vos données.");
        ButtonType buttonTypeYes = new ButtonType("OK");
        alert.getButtonTypes().setAll(buttonTypeYes);
        alert.showAndWait();
    }
    
    
    
    //getUsersByGroupByProjet
    
    public void sms() throws MalformedURLException, IOException{
        String myURI = "https://api.bulksms.com/v1/messages";

    String myUsername = "aymentouihri";
    String myPassword = "richou20.";

    String myData = "{to: \"+21653191446\", encoding: \"UNICODE\", body: \"Un nouveau projet a été ajouté à votre groupe de travail\"}";

   

    URL url = new URL(myURI);
    HttpURLConnection request = (HttpURLConnection) url.openConnection();
    request.setDoOutput(true);

    String authStr = myUsername + ":" + myPassword;
    String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());
    request.setRequestProperty("Authorization", "Basic " + authEncoded);

    request.setRequestMethod("POST");
    request.setRequestProperty( "Content-Type", "application/json");

    OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
    out.write(myData);
    out.close();

    try {
      InputStream response = request.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(response));
      String replyText;
      while ((replyText = in.readLine()) != null) {
        System.out.println(replyText);
      }
      in.close();
    } catch (IOException ex) {
      System.out.println("An error occurred:" + ex.getMessage());
      BufferedReader in = new BufferedReader(new InputStreamReader(request.getErrorStream()));
      String replyText;
      while ((replyText = in.readLine()) != null) {
        System.out.println(replyText);
      }
      in.close();
    }
    request.disconnect();
  }
}
    
   

