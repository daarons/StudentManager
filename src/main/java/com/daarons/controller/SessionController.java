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

import com.daarons.model.Session;
import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author David
 */
public class SessionController implements Initializable {
    private Session session;
    
    public SessionController(Session session){
        this.session = session;
    }
    
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPaneLeft;
    @FXML
    private GridPane gridPaneRight;
    @FXML
    private ChoiceBox fluencyCoherenceBox;
    @FXML
    private ChoiceBox vocabularyBox;
    @FXML
    private ChoiceBox grammarBox;
    @FXML
    private ChoiceBox pronunciationBox;
    @FXML
    private ChoiceBox interactEngageBox;
    @FXML
    private ChoiceBox commSkillsBox;
    @FXML
    private TextArea fluencyCoherenceReview;
    @FXML
    private TextArea vocabularyReview;
    @FXML
    private TextArea grammarReview;
    @FXML
    private TextArea pronunciationReview;
    @FXML
    private TextArea interactEngageReview;
    @FXML
    private TextArea commSkillsReview;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Integer> cbList = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        ArrayList<Node> nodes = getAllNodes(borderPane);
        for(Node node : nodes){
            if(node instanceof ChoiceBox){
                ((ChoiceBox)node).setItems(cbList);
            }else if(node instanceof TextArea){
                ((TextArea)node).setOnKeyPressed(handleTabAction);
            }
        }
    }  
    
    private static ArrayList<Node> getAllNodes(Parent root){
        ArrayList<Node> nodes = new ArrayList();
        addAllDescendents(root, nodes);
        return nodes;
    }
    
    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes){
        for(Node node : parent.getChildrenUnmodifiable()){
            nodes.add(node);
            if(node instanceof Parent){
                addAllDescendents((Parent)node, nodes);
            }
        }
    }
    
    EventHandler<KeyEvent> handleTabAction = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.TAB) {
                TextArea text = (TextArea) event.getSource();
                TextAreaSkin skin = (TextAreaSkin) text.getSkin();
                if (skin.getBehavior() instanceof TextAreaBehavior) {
                    TextAreaBehavior behavior = (TextAreaBehavior) skin.getBehavior();
                    behavior.callAction("TraverseNext");
                    event.consume();
                }
            }
        }
    };

}
