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

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * FXML Controller class
 *
 * @author David
 */
public class ImportExportController implements Initializable {

    @FXML
    private Button importBtn;
    @FXML
    private Button exportBtn;
    @FXML
    private Text fileName;
    
    @FXML
    private void launchFileChooser(ActionEvent event) throws Exception{
        FileChooser fc = new FileChooser();
        File selectedFile = null;
        if(event.getSource()==importBtn){
            selectedFile = fc.showOpenDialog(null);
        }else if(event.getSource()==exportBtn){
            fc.setInitialFileName("StudentManagerDB.sql");
            fc.getExtensionFilters().addAll(new ExtensionFilter("SQL Files", "*.sql"));
            selectedFile = fc.showSaveDialog(null);
        }

        if(selectedFile != null){
            fileName.setText("Selected: " + selectedFile.getName());
        }else{
            fileName.setText("No file selected");
        }
    }
    
    @FXML
    private void importExport(ActionEvent event) throws Exception{
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
