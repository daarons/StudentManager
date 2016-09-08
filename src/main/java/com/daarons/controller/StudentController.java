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

import com.daarons.DAO.AccountDAO;
import com.daarons.DAO.DAOFactory;
import com.daarons.model.AbstractTreeItem;
import com.daarons.model.Account;
import com.daarons.model.Session;
import com.daarons.model.Student;
import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author David
 */
public class StudentController implements Initializable {

    private final AccountDAO dao = DAOFactory.getAccountDAO("derby");
    private Student student;
    private TreeTableView sessionTableView;

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField englishNameField;
    @FXML
    private TextField chineseNameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField locationField;
    @FXML
    private TextArea hobbiesArea;
    @FXML
    private TextArea motivesArea;
    @FXML
    private TextArea notesArea;
    @FXML
    private Button saveBtn;
    @FXML
    private GridPane gridPaneLeft;
    @FXML
    private GridPane gridPaneRight;

    public StudentController(Student student) {
        this.student = student;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        gridPaneLeft.setMaxSize(screenSize.getWidth() / 2, screenSize.getHeight() / 2);
        gridPaneRight.setMaxSize(screenSize.getWidth() / 2, screenSize.getHeight() / 2);

        englishNameField.setText(student.getEnglishName());
        chineseNameField.setText(student.getChineseName());
        ageField.setText(String.valueOf(student.getAge()));
        locationField.setText(student.getLocation());
        hobbiesArea.setText(student.getHobbies());
        motivesArea.setText(student.getWhyLearnEnglish());
        notesArea.setText(student.getOtherNotes());

        saveBtn.setOnAction((ActionEvent event) -> {
            student.setEnglishName(englishNameField.getText());
            student.setChineseName(chineseNameField.getText());
            Integer age = Integer.getInteger(ageField.getText());
            student.setAge(age == null ? 0 : age);
            student.setLocation(locationField.getText());
            student.setHobbies(hobbiesArea.getText());
            student.setWhyLearnEnglish(motivesArea.getText());
            student.setOtherNotes(notesArea.getText());
            Account account = student.getAccount();
            for (Student s : account.getStudents()) {
                if (s.getId() == student.getId()) {
                    s = student;
                }
            }
            dao.updateAccount(account);
        });

        hobbiesArea.setOnKeyPressed(handleTabAction);
        motivesArea.setOnKeyPressed(handleTabAction);
        notesArea.setOnKeyPressed(handleTabAction);

        sessionTableView = new TreeTableView();
        sessionTableView.setMaxHeight(Double.MAX_VALUE);
        sessionTableView.setMaxWidth(Double.MAX_VALUE);
        
        TreeTableColumn<Session, String> sessionCol = new TreeTableColumn("Session ID");
        sessionCol.setCellValueFactory(ti -> 
                new ReadOnlyStringWrapper(String.valueOf(ti.getValue().getValue().getId())));
        
        TreeTableColumn<Session, String> dateCol = new TreeTableColumn("Date");
        dateCol.setCellValueFactory(ti -> 
                new ReadOnlyStringWrapper(ti.getValue().getValue().getTimestamp().toString()));
        
        sessionTableView.getColumns().addAll(sessionCol, dateCol);
        sessionTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        
        sessionTableView.setRowFactory(ttv -> {
            TreeTableRow row = new TreeTableRow(){
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty); 
                    if(empty){
                        setContextMenu(null);
                    }else{
                        setContextMenu(((AbstractTreeItem) getTreeItem()).getContextMenu());
                    }
                }
            };
            row.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(event.getClickCount()==2){
                        //go to session view
                    }
                }              
            });
            return row;
        });
        
        sessionTableView.setRoot(createTree(student.getSessions()));
        sessionTableView.setShowRoot(false);

        gridPaneRight.add(sessionTableView, 0, 0);
    }

    public class SessionTreeItem extends AbstractTreeItem{
        private Session session;
        
        public SessionTreeItem (Session session){
            this.session = session;
            setValue(session);
        }

        @Override
        public ContextMenu getContextMenu() {
            MenuItem viewSession = new MenuItem("View Session");
            //viewSession.setOnAction....go to session view
            return new ContextMenu(viewSession);
        }

        @Override
        public Object getObject() {
            return session;
        }

        @Override
        public void setObject(Object o) {
            if(o instanceof Session){
                session = (Session) o;
            }
        }
        
    }

    private TreeItem createTree(List<Session> sessions) {
        TreeItem root = new TreeItem();
        if (sessions != null) {
            for (Session s : sessions) {
                SessionTreeItem item = new SessionTreeItem(s);
                item.setExpanded(true);
                root.getChildren().add(item);
            }
        }
        root.setExpanded(true);
        return root;
    }
}
