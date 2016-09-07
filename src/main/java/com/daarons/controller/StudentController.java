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
import com.daarons.model.Account;
import com.daarons.model.Student;
import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author David
 */
public class StudentController implements Initializable {

    private final AccountDAO dao = DAOFactory.getAccountDAO("derby");
    private Student student;

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
    }

}
