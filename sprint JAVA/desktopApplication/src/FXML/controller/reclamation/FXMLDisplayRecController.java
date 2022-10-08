/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.reclamation;


import FXML.controller.HomeController;
import connexiondb.StaticValue;
import entity.reclamation.Reclamation;
import entity.reclamation.TypeReclamation;
import entity.user.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import org.controlsfx.control.Notifications;
import service.reclamation.ReclamationServices;


/**
 * FXML Controller class
 *
 * @author Legion
 */
public class FXMLDisplayRecController implements Initializable {

    public HomeController homeController;
    @FXML
    private Label Name;
    @FXML
    private VBox vb;
    @FXML
    private RadioButton eventt;
    @FXML
    private RadioButton meeting;
    
    @FXML
    private Label s4;
    @FXML
    private TextArea txt;
    @FXML
    private Button s6;
    
    /**
     * Initializes the controller class.
     */
    final ToggleGroup group = new ToggleGroup();
    @FXML
    private Label c8;
    @FXML
    private Button s7;
    @FXML
    private TextField idrec;
    @FXML
    private ImageView notif1;
    @FXML
    private TextArea notiftxt;
    @FXML
    private Label recEvn;
 
    
    @FXML
    private Label recdate;
    @FXML
    private Label searchdate;
    @FXML
    private DatePicker daate;
    @FXML
    private TableView<Reclamation> tabRec;
    @FXML
    private TableColumn<Reclamation, Integer> IdRec;
    @FXML
    private TableColumn<Reclamation, Integer> IdUser;
    @FXML
    private TableColumn<Reclamation, String> Descrip;
    @FXML
    private TableColumn<Reclamation, Date> Date_rec;
    @FXML
    private TableColumn<Reclamation, String> Reponse;
    @FXML
    private TableColumn<Reclamation, String> TypeRec;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        eventt.setVisible(false);
        meeting.setVisible(false);
        c8.setVisible(false);
        s4.setVisible(false);
        txt.setVisible(false);
        s6.setVisible(false);
        s7.setVisible(false);
        idrec.setVisible(false);
        notif1.setVisible(false);
        notiftxt.setVisible(false);
        tabRec.setVisible(false);
      
        recdate.setVisible(false);
        daate.setVisible(false);
        recEvn.setVisible(true);
        searchdate.setVisible(false);
        
        eventt.setToggleGroup(group);
        meeting.setToggleGroup(group);
        
        Name.setText(StaticValue.utilisateur.getNom());
        ReclamationServices rs = new ReclamationServices();
        if(StaticValue.utilisateur.getNom().equals("admin")){
        ObservableList<Reclamation> oblist = rs.displayAll();
        
        for(Reclamation rec : oblist){
            HBox hb = new HBox();
            VBox vb1 = new VBox();
           if(rec.getEtat()==0)
                 vb1.getChildren().add(new Label("Reclamation non traitée" ));
            else
                 vb1.getChildren().add(new Label("Reclamation traitée "));
            
            vb1.getChildren().add(new Label("L'id du reclamation: - "+rec.getIdRec()));
            vb1.getChildren().add(new Label(""));
            vb1.getChildren().add(new Label("Description: - "+rec.getDescription()));
            vb1.getChildren().add(new Label(""));
            vb1.getChildren().add(new Label("Date reclamation: - "+rec.getDateRec().toString()));
            vb1.getChildren().add(new Label(""));
            vb1.getChildren().add(new Label("réponse reclamation :- "+rec.getReponse()));
            vb1.getChildren().add(new Label(""));
             vb1.getChildren().add(new Label(" Type reclamation : - "+rec.getTr()));
              vb1.getChildren().add(new Separator(Orientation.HORIZONTAL));
             
            VBox vb2 = new VBox();
            
            HBox hb1 = new HBox();
            
            Button del = new Button("Delete");
           
            hb1.getChildren().add(del);
            
            if(rec.getEtat()==0)
            {
                notif1.setVisible(true);
            }
           
            //HBox hb2 = new HBox();
          //  Button del = new Button("Delete");
            //hb2.getChildren().add(del);
            
            vb2.getChildren().add(hb1);
          //  vb2.getChildren().add(hb2);
            
            hb.getChildren().add(vb1);
            hb.getChildren().add(vb2);
            
            vb.getChildren().add(hb);
            EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {
                     Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK)
                     rs.deleteReclamation(rec.getIdRec());
                     Parent parentInscit = null;
                     try {
                         parentInscit = FXMLLoader.load(getClass().getResource("/FXML/view/reclamation/FXMLDisplayRec.fxml"));
                     } catch (IOException ex) {
                         Logger.getLogger(FXMLDisplayRecController.class.getName()).log(Level.SEVERE, null, ex);
                     }
       /*  Scene sceneInscit = new Scene(parentInscit);
              Stage stageInscit  = (Stage)((Node)event.getSource()).getScene().getWindow();
       
             //stageInscit .hide();
        
             stageInscit .setScene(sceneInscit );
         */    
       //stageInscit .show();
         homeController.getbody().getChildren().setAll(parentInscit);
                 }
             };
                del.setOnAction(buttonHandler);
                
        }
        
    }  
        else
        {
         ObservableList<Reclamation> oblist = rs.findReclamById(StaticValue.utilisateur.getIdUser());
        
        for(Reclamation rec : oblist){
            HBox hb = new HBox();
            VBox vb1 = new VBox();
            if(rec.getEtat()==0)
                 vb1.getChildren().add(new Label("Reclamation non traitée" ));
            else
                 vb1.getChildren().add(new Label("Reclamation traitée "));
                
            vb1.getChildren().add(new Label("Nom Utilisateur: - "+StaticValue.utilisateur.getNom()));
            vb1.getChildren().add(new Label(""));
            vb1.getChildren().add(new Label("Description: - "+rec.getDescription()));
            vb1.getChildren().add(new Label(""));
            vb1.getChildren().add(new Label("Date reclamation: - "+rec.getDateRec().toString()));
            vb1.getChildren().add(new Label(""));
             vb1.getChildren().add(new Label("réponse reclamation :- "+rec.getReponse()));
             vb1.getChildren().add(new Label(""));
             vb1.getChildren().add(new Label(" Type reclamation : - "+rec.getTr()));
              vb1.getChildren().add(new Separator(Orientation.HORIZONTAL));
             
            VBox vb2 = new VBox();
            
            HBox hb1 = new HBox();
           
            Button del = new Button("Delete");
          
            hb1.getChildren().add(del);
            
            if(rec.getEtat()==1)
            {
                
                
                notif1.setVisible(true);
            }
           
            //HBox hb2 = new HBox();
          //  Button del = new Button("Delete");
            //hb2.getChildren().add(del);
            
            vb2.getChildren().add(hb1);
          //  vb2.getChildren().add(hb2);
            
            hb.getChildren().add(vb1);
            hb.getChildren().add(vb2);
            
            vb.getChildren().add(hb);
            EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {
                     Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK)
                     rs.deleteReclamation(rec.getIdRec());
                     Parent parentInscit = null;
                     try {
                         parentInscit = FXMLLoader.load(getClass().getResource("FXMLDisplayRec.fxml"));
                     } catch (IOException ex) {
                         Logger.getLogger(FXMLDisplayRecController.class.getName()).log(Level.SEVERE, null, ex);
                     }
         Scene sceneInscit = new Scene(parentInscit);
              Stage stageInscit  = (Stage)((Node)event.getSource()).getScene().getWindow();
       
             //stageInscit .hide();
        
             stageInscit .setScene(sceneInscit );
             stageInscit .show();
                 }
             };
                del.setOnAction(buttonHandler);
                
        }
        }
       }

   

   

    @FXML
    private void Addrecl(ActionEvent event) {
        Notifications info = Notifications.create().title("not1").text("add sucess").graphic(null).hideAfter(Duration.seconds(5))
        .position(Pos.TOP_LEFT);
        info.show();
      if(StaticValue.utilisateur.getNom().equals("admin")){
        
       
        c8.setVisible(true);
        txt.setVisible(true);
        s7.setVisible(true);
        idrec.setVisible(true);
        
        daate.setVisible(true);
        searchdate.setVisible(true);
        recEvn.setVisible(true);
        recdate.setVisible(false);
        
      }
      else
      {
        eventt.setVisible(true);
        meeting.setVisible(true);
       
        s4.setVisible(true);
        txt.setVisible(true);
        s6.setVisible(true);
          
      }
    }
 Reclamation rec = new Reclamation();
    User u = new User(StaticValue.utilisateur.getIdUser());
    @FXML
    private void event(ActionEvent event) {
        eventt.setText("event");
    }

    @FXML
    private void meeting(ActionEvent event) {
        meeting.setText("meetin");
    }
    

    @FXML
    private void Rec(ActionEvent event) {
        //ServiceMail sm = new ServiceMail();

            final String fromEmail = "amir.benmustapha@esprit.tn"; //requires valid gmail id
            final String password = "183JMT3418"; // correct password for gmail id
            final String toEmail = "mohamedahmed.driss@esprit.tn"; // can be any email id 
            final String mail = "une nouvelle reclamation a été envoyés ";

            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };
            Session session = Session.getInstance(props, auth);

            //ServiceMail.sendEmail(session, toEmail, "Ahawaaa", mail);   


        Reclamation rec = new Reclamation();
        
        rec.setUser(u);
        RadioButton selectedRadioButton;
        selectedRadioButton = (RadioButton) group.getSelectedToggle();
        String toogleGroupValue = selectedRadioButton.getText();
        
        if(toogleGroupValue.equals("event"))
            rec.setTr(TypeReclamation.EVENT);
        else
                   rec.setTr(TypeReclamation.MEETING);

        if(txt.getText().equals(""))
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Remplissage de champ");
		alert.setHeaderText(null);
		alert.setContentText("Vous devez inserer une description ");
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK);
        }
        else
        {
       rec.setDescription(txt.getText()); 
        rec.setEtat(0);
        rec.setReponse("");
        String str="2020-02-19";  
        Date dateRec=Date.valueOf(str);
        rec.setDateRec(dateRec);
        ReclamationServices  rs = new ReclamationServices();
        Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to add new reclamation ?");
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK)
        rs.addReclation(rec);
        Parent parentInscit = null;
                        try {
                            parentInscit = FXMLLoader.load(getClass().getResource("/FXML/view/reclamation/FXMLDisplayRec.fxml"));
                     } catch (IOException ex) {
                         Logger.getLogger(FXMLDisplayRecController.class.getName()).log(Level.SEVERE, null, ex);
                     }
        /* Scene sceneInscit = new Scene(parentInscit);
              Stage stageInscit  = (Stage)((Node)event.getSource()).getScene().getWindow();
       
             //stageInscit .hide();
        
             stageInscit .setScene(sceneInscit );
            // stageInscit .show();*/
            homeController.getbody().getChildren().setAll(parentInscit);
        }
    }

    @FXML
    private void reply(ActionEvent event) {
       ReclamationServices rs = new ReclamationServices();
       rec.setEtat(1);
       int i = Integer.parseInt(idrec.getText());
       rec.setReponse(txt.getText());
       
                     rs.updateReclamation(rec, i);
                     rs.updateEtat(rec, i);
                     Parent parentInscit = null;
                     try {
                         parentInscit = FXMLLoader.load(getClass().getResource("/FXML/view/reclamation/FXMLDisplayRec.fxml"));
                     } catch (IOException ex) {
                         Logger.getLogger(FXMLDisplayRecController.class.getName()).log(Level.SEVERE, null, ex);
                     }
       
                   homeController.getbody().getChildren().setAll(parentInscit);
                 }

    @FXML
    private void shownotif(MouseEvent event) {
        if(StaticValue.utilisateur.getNom().equals("admin")){
        notiftxt.setVisible(true);
        notiftxt.setText("Nouvelle reclamation envoyé");
        }
        else
        {
            notiftxt.setVisible(true);
             notiftxt.setText("Nouvelle reclamation résolu");
        }
    }

    @FXML
    private void disshow(MouseEvent event) {
         notiftxt.setVisible(false);
    }

    @FXML
    private void showbydate(ActionEvent event) {
        recEvn.setVisible(false);
        recdate.setVisible(true);
        vb.setVisible(false);
        tabRec.setVisible(true);
        
        
      
         ReclamationServices rs = new ReclamationServices();
         User u = new User();
         int i=u.getIdUser();
        String str = daate.getValue().toString();
        Date dt=Date.valueOf(str);
        ObservableList<Reclamation> oblist = rs.rechercherRec(dt);
        // lenna
        IdRec.setCellValueFactory(new PropertyValueFactory<Reclamation,Integer>("idRec"));
        IdUser.setCellValueFactory(new PropertyValueFactory<Reclamation,Integer>("idUser"));
        Descrip.setCellValueFactory(new PropertyValueFactory<Reclamation,String>("description"));
        Date_rec.setCellValueFactory(new PropertyValueFactory<Reclamation,Date>("dateRec"));
        Reponse.setCellValueFactory(new PropertyValueFactory<Reclamation,String>("reponse"));
        TypeRec.setCellValueFactory(new PropertyValueFactory<Reclamation,String>("tr"));
        
        tabRec.setItems(oblist);
        
    }  
       public void setHomeController(HomeController homeController)
       {
           this.homeController=homeController;
       }
        
    }
        
    

    

