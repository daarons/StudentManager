package com.daarons.controller;

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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author David
 */
public class InfoController implements Initializable {
    
    @FXML
    private TextArea legalText;
    @FXML
    private TextArea attrText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Path licensePath = Paths.get("src/main/resources/text/license.txt");
        File licenseFile = licensePath.toFile();
        Path attrPath = Paths.get("src/main/resources/text/attributions.txt");
        File attrFile = attrPath.toFile();
        try{
            BufferedReader in = new BufferedReader(new FileReader(licenseFile));
            String line = in.readLine();
            while (line != null) {
                legalText.appendText(line + "\n");
                line = in.readLine();
            }
            legalText.setScrollTop(Double.MAX_VALUE);
            in.close();
            in = null;
            
            in = new BufferedReader(new FileReader(attrFile));
            line = in.readLine();
            while (line != null) {
                attrText.appendText(line + "\n");
                line = in.readLine();
            }
            attrText.setScrollTop(Double.MAX_VALUE);
            in.close();
            in = null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InfoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
