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

public class NavigationController implements Initializable {

    @FXML
    private Button homeBtn;
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
        if(event.getSource()==homeBtn){
            root = FXMLLoader.load(getClass().getResource("/view/home.fxml"));
        }else if (event.getSource() == accountsBtn) {
            System.out.println("accounts");
        } else if (event.getSource() == notesBtn) {
            root = FXMLLoader.load(getClass().getResource("/view/notes.fxml"));
        } else if (event.getSource() == importExportBtn) {
            root = FXMLLoader.load(getClass().getResource("/view/importExport.fxml"));
        } else if (event.getSource() == infoBtn) {
            root = FXMLLoader.load(getClass().getResource("/view/info.fxml"));
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
