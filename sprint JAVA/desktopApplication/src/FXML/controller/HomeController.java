/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller;

import FXML.controller.activites.ActivitiesController;
//import FXML.controller.activites.AfficherEventsController;
import FXML.controller.activites.AfficherReunionController;
import FXML.controller.activites.AfficherReunionEmpController;
import FXML.controller.activites.PublierEventController;
import FXML.controller.projet.ProjetController;
import FXML.controller.projet.ProjetDevSMController;
import FXML.controller.reclamation.FXMLDisplayRecController;
import FXML.controller.sprint.HomeControllerSprint;
import FXML.controller.tache.NouveaucrudController;
import FXML.controller.tache.TacheController;
import FXML.controller.user.ProfileController;
import animatefx.animation.BounceIn;
import animatefx.animation.BounceOut;
import animatefx.animation.FadeInLeftBig;
import animatefx.animation.FadeOutLeftBig;
import animatefx.animation.Swing;
import com.jfoenix.controls.JFXListView;
import connexiondb.StaticValue;
import entity.activite.Reunion;
import entity.user.User;
import functions.Functions;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.activiteService.ReunionService;
import service.projetService.ProjetService;
import service.sprintService.SprintService;
import service.userService.UserService;

/**
 *
 * @author Too
 */
public class HomeController implements Initializable {
    
    private Stage window;
    private User user;
    private HomeController homeController;
    private ReunionService rs;
    private UserService us;
    private SprintService ss;
    private ProjetService ps;
    
    @FXML
    private MediaView media;
    @FXML
    private AnchorPane body;
    @FXML
    private VBox menuLogOut;
    
    @FXML
    private AnchorPane mainPage;
    @FXML
    private Text labelNom;
    @FXML
    private Pane buttonMenuLogOut;
    @FXML
    private VBox menu;

    @FXML
    private VBox sousMenuActivites;
    @FXML
    private VBox sousMenuTaches;
    
    @FXML
    private ImageView imageNotificaction;
    
    @FXML
    private Text nombreNotification;
    @FXML
    private JFXListView<String> listNotification;
    public String certeficate;


    /*
        Sprints
    */
    
    @FXML
    void gestionDesSprints(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/sprint/home.fxml"));
            Parent root = loader.load();
            HomeControllerSprint controller = loader.getController();
            loader.setController(controller);   
            body.getChildren().setAll(root);
    }
    /*
        taches
    */
    
    @FXML
    void gestionTache(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //StaticValue.utilisateur;  
        if(true)
        {
            loader.setLocation(getClass().getResource("../view/tache/nouveaucrud.fxml"));
            Parent root = loader.load();
            NouveaucrudController controller = loader.getController();
            loader.setController(controller);   
            body.getChildren().setAll(root);
        }
        else
        {
            loader.setLocation(getClass().getResource("../view/tache/tache.fxml"));
            Parent root = loader.load();
            TacheController controller = loader.getController();
            loader.setController(controller);   
            body.getChildren().setAll(root);
        }
        
    }
    /*
        projet
    */
    
    @FXML
    void afficherProjet(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
       if(StaticValue.utilisateur.getTypeUser().equals("scrum master"))
        {
            loader.setLocation(getClass().getResource("../view/projet/Projet.fxml"));
            Parent root = loader.load();
            ProjetController controller = loader.getController();
            controller.setHomeController(homeController);
            System.err.println();
            loader.setController(controller);   
            body.getChildren().setAll(root);
        }
        else
        {
            loader.setLocation(getClass().getResource("../view/projet/ProjetDevSM.fxml"));
            Parent root = loader.load();
            ProjetDevSMController controller = loader.getController();
            controller.setHomeController(homeController);
            loader.setController(controller);   
            body.getChildren().setAll(root);
        }  

    }
    /*
        reclamation
    */
        @FXML
    void afficherReclamation(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/reclamation/FXMLDisplayRec.fxml"));
            Parent root = loader.load();
            FXMLDisplayRecController controller = loader.getController();
            controller.setHomeController(homeController);
            loader.setController(controller);   
            body.getChildren().setAll(root);
    }
    /*
        activite
    */
    @FXML
    void afficherReunions(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/activites/AfficherReunion.fxml"));
        StackPane root = loader.load();
        AfficherReunionController controller = loader.getController();
        controller.setHomeController(homeController);
        loader.setController(controller);   
        body.getChildren().setAll(root);
    }

    @FXML
    void programmerReunion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/activites/Activities.fxml"));
        AnchorPane root = loader.load();
        ActivitiesController controller = loader.getController();
        controller.setHomeController(homeController);
        loader.setController(controller);   
        body.getChildren().setAll(root);
    }
    @FXML
    void programmerEvent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/activites/publierEvent.fxml"));
        StackPane root = loader.load();
        PublierEventController controller = loader.getController();
        //controller.setHomeController(homeController);
        loader.setController(controller);   
        body.getChildren().setAll(root);
    }
    @FXML
    void AfficherEvent(ActionEvent event) throws IOException {
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/activites/AfficherEvents.fxml"));
        StackPane root = loader.load();
        AfficherEventsController controller = loader.getController();
        loader.setController(controller);   
        body.getChildren().setAll(root);*/
    }
    @FXML
    void gestionActivitesOnClick(ActionEvent event) throws IOException {
        if(StaticValue.utilisateur.getTypeUser().equals("scrum master"))
        {
            displaySubmenu(sousMenuActivites);
        }
        else
            afficherReunionEmp();
        
    }
    private void afficherReunionEmp() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/activites/AfficherReunionEmp.fxml"));
        StackPane root = loader.load();
        AfficherReunionEmpController controller = loader.getController();
        loader.setController(controller);   
        body.getChildren().setAll(root);
    }
    /*
        user
    */
    @FXML
    void profilOnclick(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/user/profile.fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();
        loader.setController(controller);   
        body.getChildren().setAll(root);
    }
    /*
        interface home
    */
    @FXML
    void buttonMenuLogOut(MouseEvent event) {
        if(menuLogOut.isVisible())
            menuLogOut.setVisible(false);
        else
            menuLogOut.setVisible(true);
        
    }
    
    @FXML
    void logOutOnClick(ActionEvent event) throws IOException {
        StaticValue.utilisateur=null;
        sousMenuActivites.setVisible(false);
        Parent root = (new FXMLLoader(getClass().getResource("../view/login.fxml"))).load();
        Parent rootMain = window.getScene().getRoot();
        
        root.setOnMouseDragged(rootMain.getOnMouseDragged());
        root.setOnMousePressed(rootMain.getOnMousePressed());
        Scene login = new Scene(root);
        window.setScene(login);
        
    }
    
    @FXML
    void buttonExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void pageOnClick(MouseEvent event) {
        Scene sc = menuLogOut.getScene();
        hideSubmenu(sousMenuActivites);
        
        if(event.getSceneX()<buttonMenuLogOut.getLayoutX() || event.getSceneX()>buttonMenuLogOut.getLayoutX()+buttonMenuLogOut.getWidth() || event.getSceneY()<buttonMenuLogOut.getLayoutY()|| event.getSceneY()>buttonMenuLogOut.getLayoutY()+buttonMenuLogOut.getHeight())
        {
            if(event.getSceneX()<sc.getX() || event.getSceneX()>sc.getX()+menuLogOut.getWidth() || event.getSceneY()<sc.getY() || event.getSceneY()>sc.getY()+menuLogOut.getHeight())
                menuLogOut.setVisible(false);
        }
        
    }
    @FXML
    void afficherNotification(MouseEvent event) {
        displayNotif(listNotification);
    }
    
    @FXML
    void backToMenu(ActionEvent event){
        hideSubmenu(sousMenuActivites);
    }
    
    
    
    private void displaySubmenu(Node node)
    {
        if(!node.isVisible())
        {
            node.setVisible(true);
            node.toFront();
            new FadeInLeftBig(node).play();
        }
    }
    private void hideSubmenu(Node node)
    {
        if(node.isVisible())
        {
            FadeOutLeftBig animation = new FadeOutLeftBig(node);
            animation.setDelay(Duration.ONE);
            animation.setOnFinished((event) -> {
                node.setVisible(false);
                node.toBack();
            });
            animation.play();
        }
    }
    
    private void displayNotif(Node node)
    {
        if(!node.isVisible())
        {
            node.setVisible(true);
            node.toFront();
            new BounceIn(node).play();
        }
        else
        {
            BounceOut animation = new BounceOut(node);
            animation.setDelay(Duration.ONE);
            animation.setOnFinished((event) -> {
                node.setVisible(false);
                node.toBack();
            });
            animation.play();
        }
    }
    private void hideNotif(Node node)
    {
        if(node.isVisible())
        {
            BounceOut animation = new BounceOut(node);
            animation.setDelay(Duration.ONE);
            animation.setOnFinished((event) -> {
                node.setVisible(false);
                node.toBack();
            });
            animation.play();
        }
    }
    
    private void initList(JFXListView<String> listView ,ArrayList<String> data)
    {
        String n = String.valueOf((data.isEmpty()?0:data.size()));
        nombreNotification.setText(n);
        listView.getItems().setAll(data);
    }
    
    public void setUser(User u)
    {
        this.user = u;
    }

    public User getUser() {
        return this.user;
    }
    
    public void setWindow(Stage s)
    {
        this.window = s;
    }
    
    public AnchorPane getbody()
    {
        return body;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuLogOut.setVisible(false);
        Swing animation = new Swing(imageNotificaction);
        animation.setCycleCount(1000000).setResetOnFinished(true).play();
        rs = new ReunionService();
        ss = new SprintService();
        ps = new ProjetService();
        us = new UserService();
        //media = new MediaView(new MediaPlayer(new Media("/E:/f/COURS/3e A/piDEV/Gestion de projet PIDEV 3A/Feather/sprint JAVA/desktopApplication/src/FXML/icons.mkv")));
        homeController = this;
        if(StaticValue.utilisateur!=null)
        {
            user = StaticValue.utilisateur;
            labelNom.setText(user.getNom().toUpperCase() +"  "+Functions.capitalize(user.getPrenom()));
        }
        
        //entity.activite.Scheduler  s = new entity.activite.Scheduler("");
        
        //s.planJob((new testJob()), 5000l );
      
    } 

    public class testJob extends TimerTask{

        @Override
        public void run() {
            ArrayList<String> data = new ArrayList<>();
            ArrayList<Reunion> listReunion = rs.displayReunionsUser(user.getIdUser());
            if(user.getTypeUser().equals("scrum master"))
            {
                listReunion = rs.displayAll();
            }

            
            for (Reunion reunion : listReunion) {
                if(reunion.getDateRappel()!=null)
                {
                    if(Functions.matchDate(reunion.getDateRappel(), reunion.getDateDebut(), LocalDateTime.now()))
                    {
                        String date = reunion.getDateDebut().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.FRENCH));
                        String heure = reunion.getDateDebut().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(Locale.FRENCH));
                        System.err.println(listReunion);
                        data.add("Vous avez une réunion le " + date + " à " +heure );
                    }
                }
            }
            
            initList(listNotification, data);
        }
    }

    
    
    
}
