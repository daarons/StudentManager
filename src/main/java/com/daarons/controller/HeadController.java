/*
 * Copyright 2016 David Aarons.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.daarons.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author David
 */
public class HeadController implements Initializable {
    
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
            System.out.println("notes");
        } else if (event.getSource() == importExportBtn) {
            System.out.println("importexport");
        } else if (event.getSource() == infoBtn) {
            root = FXMLLoader.load(getClass().getResource("/view/info.fxml"));
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());
        stage.show();

    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
