/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import entity.user.User;
import functions.SendMail;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.userService.UserService;

/**
 * FXML Controller class
 *
 * @author Freddy
 */
public class LoginController implements Initializable {

    private Stage window;
    private UserService us = new UserService();
    private HomeController homeController;
    @FXML
    private JFXTextField inputEmail;

    @FXML
    private JFXPasswordField inputPassword;

    @FXML
    void seConnecter(ActionEvent event) throws IOException {

        String nom = inputEmail.getText();
        String prenom = inputPassword.getText();
        User u = us.existUser(nom, "");
        if (u != null) {
            us.connect(u);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/Home.fxml"));
            Parent root = loader.load();

            homeController = loader.getController();
            homeController.setUser(u);

            loader.setController(homeController);

            window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent rootMain = window.getScene().getRoot();

            root.setOnMouseDragged(rootMain.getOnMouseDragged());
            root.setOnMousePressed(rootMain.getOnMousePressed());

            Scene scene = new Scene(root);
            homeController.setWindow(window);

            window.setScene(scene);
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("invalid Username or Password!");
            errorAlert.showAndWait();
        }

    }

    @FXML
    private void resetpasswrdlnk(ActionEvent event) throws IOException {
        String em = inputEmail.getText();
        ArrayList<User> list = new ArrayList();
        User u = new User("tabouaf@gmail.com", "");
        list.add(u);
        SendMail.sendMail(list, "Reset Password", "Nous avons reçu une demande de réinitialisation du mot de passe pour votre compte Feather.\n"
                + "\n"
                + "559687\n"
                + "Saisissez ce code pour terminer la réinitialisation.\n"
                + "\n"
                + "Merci de nous aider à préserver la sécurité de votre compte.\n"
                + "\n"
                + "L’équipe Feather");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/user/resetpasswordcode.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Feather/Reset Password");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    private void signuplink(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/user/signup.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Feather");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void buttonExitOnClick(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
