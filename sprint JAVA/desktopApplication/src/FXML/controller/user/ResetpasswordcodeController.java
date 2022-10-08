/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.user;

import connexiondb.StaticValue;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import entity.user.User;
import java.sql.SQLException;
import service.userService.UserService;

/**
 * FXML Controller class
 *
 * @author SEY.KO
 */
public class ResetpasswordcodeController implements Initializable {

    @FXML
    private TextField codetf;
    @FXML
    private Label msglbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void resetpassbtn(ActionEvent event) throws IOException, SQLException {
        
        if(""==codetf.getText())
        {
            msglbl.setText("enter Code");
        }
        else{
            String code=codetf.getText();
            int cd=(Integer.parseInt(code));
        
        UserService sr=new UserService();
        User u=StaticValue.utilisateur;
       int cdd= u.generateCode();
       String sh=String.valueOf(cdd);
        msglbl.setText(sh);
        if(sr.checkcode(u, cd) )
        {
        
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/user/updatepass.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Feather/Reset Password");
            stage.setScene(new Scene(root1));
            stage.show(); 
        
        }else{
            msglbl.setText("Wrong Code!");
        }
        }
    }
    }
    

