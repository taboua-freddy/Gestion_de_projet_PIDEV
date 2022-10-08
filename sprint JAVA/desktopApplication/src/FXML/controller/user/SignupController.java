/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.user;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import entity.user.User;
import entity.groupe.Groupe;
import java.sql.SQLException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.userService.UserService;

/**
 * FXML Controller class
 *
 * @author SEY.KO
 */
public class SignupController implements Initializable {

    @FXML
    private TextField nomtf;
    @FXML
    private TextField prenomtf;
    @FXML
    private TextField emailtf;
    @FXML
    private TextField Groupetf;
    @FXML
    private TextField roletf;
    @FXML
    private TextField passwordtf;
    @FXML
    private TextField certeficatetf;
    @FXML
    private CheckBox checkboxx;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void signupp(ActionEvent event) throws SQLException, IOException {
        String nom = nomtf.getText();
        String prenom = prenomtf.getText();
        String email = emailtf.getText();
        String role = roletf.getText();
        String password = passwordtf.getText();
        String cert = certeficatetf.getText();
        //boolean check= checkboxx.
        User u = new User(nom, prenom, role, email, password);
        UserService su = new UserService();
        su.ajouter1(u);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/login.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Feather");
        stage.setScene(new Scene(root1));
        stage.show();
    }

}
