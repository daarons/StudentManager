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

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author David
 */
public class NotesController implements Initializable {

    @FXML
    private TilePane tilePane;
    
    @FXML
    private void handleTabAction(KeyEvent event) throws Exception{
        if(event.getCode()==KeyCode.TAB){
            TextArea text = (TextArea) event.getSource();
            TextAreaSkin skin = (TextAreaSkin) text.getSkin();
            if(skin.getBehavior() instanceof TextAreaBehavior){
                TextAreaBehavior behavior = (TextAreaBehavior) skin.getBehavior();
                behavior.callAction("TraverseNext");
                event.consume();
            }
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        tilePane.setPrefTileHeight(screenSize.getHeight() / 4.5);
        tilePane.setPrefTileWidth(screenSize.getWidth() / 6.5);
    }    
    
}
