package com.daarons.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.*;

public class HomeController implements Initializable {

    @FXML
    private Button accountsBtn;
    @FXML
    private Button notesBtn;
    @FXML
    private Button importExportBtn;
    @FXML
    private Button infoBtn;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = null;
        Parent root = null;
        if (event.getSource() == accountsBtn) {
            System.out.println("accounts");
        } else if (event.getSource() == notesBtn) {
            System.out.println("notes");
        } else if (event.getSource() == importExportBtn) {
            System.out.println("importexport");
        } else if (event.getSource() == infoBtn) {
            root = FXMLLoader.load(getClass().getResource("/view/info.fxml"));
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
