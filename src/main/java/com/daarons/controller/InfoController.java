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
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.*;

/**
 * FXML Controller class
 *
 * @author David
 */
public class InfoController implements Initializable {
    
    private static final Logger log = LogManager.getLogger(InfoController.class);
    
    @FXML
    private TextArea legalText;
    @FXML
    private TextArea attrText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final String license = "/text/license.txt";
        final String attributions = "/text/attributions.txt";
        try{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(license)));
            String line = in.readLine();
            while (line != null) {
                legalText.appendText(line + "\n");
                line = in.readLine();
            }
            legalText.setScrollTop(Double.MAX_VALUE);
            in.close();
            in = null;
            
            in = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream(attributions)));
            line = in.readLine();
            while (line != null) {
                attrText.appendText(line + "\n");
                line = in.readLine();
            }
            attrText.setScrollTop(Double.MAX_VALUE);
            in.close();
            in = null;
        } catch (FileNotFoundException ex) {
            log.error("Couldn't find file", ex);
        } catch (IOException ex) {
            log.error("IO failed", ex);
        }
    }

}
