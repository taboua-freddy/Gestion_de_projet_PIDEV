/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.user;

import connexiondb.StaticValue;
import entity.user.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.userService.UserService;

/**
 * FXML Controller class
 *
 * @author SEY.KO
 */
public class UpdatepassController implements Initializable {

    
    @FXML
    private PasswordField newpasswrdtf;
    @FXML
    private PasswordField conpasstf;
    @FXML
    private Label errorlb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void updatepass(ActionEvent event) throws IOException, SQLException {
        String pass=newpasswrdtf.getText();
        String passcon=conpasstf.getText();
   
        if(pass!=passcon)
        {
            errorlb.setText("Verify password!");
        }
        else
        {
            errorlb.setText("");
            User u= StaticValue.utilisateur;
            UserService b=new UserService();
            b.updatepassword(u);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../view/login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Feather/Reset Password");
            stage.setScene(new Scene(root1));
            stage.show(); 
        }
        
    
    }
    
}
