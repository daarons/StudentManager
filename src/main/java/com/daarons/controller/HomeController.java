package com.daarons.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
    private void handleButtonAction(ActionEvent event) {
        Stage stage;
        Parent root;
        if(event.getSource()==accountsBtn){
            System.out.println("accounts");
        }else if(event.getSource()==notesBtn){
            System.out.println("notes");
        }else if(event.getSource()==importExportBtn){
            System.out.println("importexport");
        }else if(event.getSource()==infoBtn){
            System.out.println("info");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
