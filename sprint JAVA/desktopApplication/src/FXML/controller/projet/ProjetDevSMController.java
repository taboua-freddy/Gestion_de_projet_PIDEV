/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.projet;

import FXML.controller.HomeController;
import entity.pojet.Projet.status;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

import javafx.util.Callback;
import service.groupeService.GroupeService;
import service.projetService.ProjetService;


/**
 * FXML Controller class
 *
 * @author Aymen Touihri
 */
public class ProjetDevSMController implements Initializable {

    ObservableList observableList = FXCollections.observableArrayList();

    public HomeController homeController;
    ProjetService ps = new ProjetService();
    GroupeService gs = new GroupeService();

    @FXML
    private TreeTableView<Projet> tv1;

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
        ProjetDevSMController.SelectedProject = SelectedProject;
    }

    @FXML
    public void DataTableUpdateSearch() {
        SearchByKeyWord(src.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
           
        ;

    }

    private void loadData() {
        observableList.removeAll(observableList);
        List<entity.pojet.Projet> myprojects = new ArrayList<>();
        List<TreeItem> list = new ArrayList<>();
        myprojects.addAll(ps.getProjetByUser(1));
        SetupTable(myprojects);

    }

    private void SearchByKeyWord(String key) {
        observableList.removeAll(observableList);
        List<entity.pojet.Projet> myprojects = new ArrayList<>();
        myprojects.addAll(ps.getUserProjetsByKey(key,1));
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
            System.out.println(myprojects.get(i).getDateFin().getDay());
            p.setStart(myprojects.get(i).getDateDebut().toString());
            p.setStatus(status.enumToString(myprojects.get(i).getStatus()));

            /*
            Change State Automatic From Not Started To In Progress
            And Update Database
             */
            if (myprojects.get(i).getStatus().equals(status.NOTSTARTED)) {
                if ((myprojects.get(i).getDateDebut().equals(getNow()) || (myprojects.get(i).getDateDebut().before(getNow())))) {
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
        tv1.setRoot(root);
        tv1.setShowRoot(false);
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

    public Date getNow() {
        return new Date(LocalDate.now().getYear() - 1900, LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getDayOfMonth());
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

}
