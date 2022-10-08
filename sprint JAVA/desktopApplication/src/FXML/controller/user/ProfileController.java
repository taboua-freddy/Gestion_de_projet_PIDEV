/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.user;

import connexiondb.StaticValue;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import entity.user.User;

/**
 * FXML Controller class
 *
 * @author SEY.KO
 */
public class ProfileController implements Initializable {
    
    
    @FXML
    private Label nametlbf;
    @FXML
    private Label prenomtl;
    @FXML
    private Label emailtl;
    @FXML
    private Label certeficatetl;
    @FXML
    private Label roletl;
    @FXML
    private Label groupetl;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            User u1 = StaticValue.utilisateur;
            nametlbf.setText(u1.getNom());
            prenomtl.setText(u1.getPrenom());
            emailtl.setText(u1.getEmail());
            roletl.setText(u1.getTypeUser());
    }    

  
    
}
