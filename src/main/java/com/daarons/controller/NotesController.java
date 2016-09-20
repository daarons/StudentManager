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
import com.daarons.model.Note;
import com.daarons.model.Session;
import com.daarons.model.Student;
import com.daarons.util.*;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import jfxtras.scene.control.CalendarTimePicker;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author David
 */
public class NotesController implements Initializable {

    private final AccountDAO dao = DAOFactory.getAccountDAO("derby");

    @FXML
    private TilePane tilePane;
    @FXML
    private TextArea fluencyCoherenceNote;
    @FXML
    private TextArea vocabularyNote;
    @FXML
    private TextArea grammarNote;
    @FXML
    private TextArea pronunciationNote;
    @FXML
    private TextArea interactEngageNote;
    @FXML
    private TextArea commSkillsNote;
    @FXML
    private Button saveBtn;
    @FXML
    private CalendarTimePicker timePicker;
    @FXML
    private TextField accountField;
    @FXML
    private TextField studentField;
    @FXML
    private TextField sessionIdField;

    @FXML
    private void saveNotes(MouseEvent event) {
        if (event.getSource() == saveBtn 
                && !accountField.getText().trim().isEmpty()
                && !studentField.getText().trim().isEmpty()
                && Validator.isNumber(sessionIdField.getText())) {
            List<Account> accounts = dao.getAccountsLike(accountField.getText());
            List<Student> students = accounts != null ? accounts.stream()
                    .filter(a -> a.getStudents() != null)
                    .flatMap(a -> a.getStudents().stream().filter(s
                                    -> s.getEnglishName().equalsIgnoreCase(studentField.getText())
                                    || s.getChineseName().equalsIgnoreCase(studentField.getText())))
                    .collect(Collectors.toList()) : null;
            Account account = null;
            Student student = null;
            Session session = null;
            Note note = null;
            if (accounts == null) {
                account = new Account(accountField.getText());
                student = new Student();
                student.setEnglishName(studentField.getText());
                student.setAccount(account);
                session = new Session(student, timePicker.getCalendar().getTime());
                note = new Note(session, fluencyCoherenceNote.getText().replaceAll("`", ""),
                        vocabularyNote.getText().replaceAll("`", ""),
                        grammarNote.getText().replaceAll("`", ""),
                        pronunciationNote.getText().replaceAll("`", ""),
                        interactEngageNote.getText().replaceAll("`", ""),
                        commSkillsNote.getText().replaceAll("`", ""));
                session.setNote(note);
                session.setSessionId(Long.parseLong(sessionIdField.getText()));
                student.getSessions().add(session);
                account.getStudents().add(student);
            } else if (students == null || students.isEmpty()) {
                account = accounts.get(0);
                student = new Student();
                student.setEnglishName(studentField.getText());
                student.setAccount(account);
                session = new Session(student, timePicker.getCalendar().getTime());
                note = new Note(session, fluencyCoherenceNote.getText().replaceAll("`", ""),
                        vocabularyNote.getText().replaceAll("`", ""),
                        grammarNote.getText().replaceAll("`", ""),
                        pronunciationNote.getText().replaceAll("`", ""),
                        interactEngageNote.getText().replaceAll("`", ""),
                        commSkillsNote.getText().replaceAll("`", ""));
                session.setNote(note);
                session.setSessionId(Long.parseLong(sessionIdField.getText()));
                student.getSessions().add(session);
                account.getStudents().add(student);
            } else {
                account = accounts.get(0);
                student = students.get(0);
                for (Student s : account.getStudents()) {
                    if (s.getId() == student.getId()) {
                        session = new Session(s, timePicker.getCalendar().getTime());
                        note = new Note(session, fluencyCoherenceNote.getText().replaceAll("`", ""),
                                vocabularyNote.getText().replaceAll("`", ""),
                                grammarNote.getText().replaceAll("`", ""),
                                pronunciationNote.getText().replaceAll("`", ""),
                                interactEngageNote.getText().replaceAll("`", ""),
                                commSkillsNote.getText().replaceAll("`", ""));
                        session.setNote(note);
                        session.setSessionId(Long.parseLong(sessionIdField.getText()));
                        s.getSessions().add(session);
                    }
                }
            }
            dao.updateAccount(account);
        }else{
            Alert saveAlert = new Alert(AlertType.ERROR, "Please make sure that "
                    + "the account, student, and session ID text fields are "
                    + "filled in correctly before saving.");
            saveAlert.showAndWait();
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

        timePicker.setPrefSize(screenSize.getWidth() / 5, 60);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (minute > 5 && minute < 35) {
            cal.set(Calendar.MINUTE, 30);
        } else {
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.HOUR_OF_DAY, hour + 1);
        }
        timePicker.setCalendar(cal);

        TextFields.bindAutoCompletion(accountField, t -> {
            return dao.getAccountsLike(t.getUserText());
        });
        TextFields.bindAutoCompletion(studentField, t -> {
            List<Account> accounts = dao.getAccountsLike(accountField.getText());
            return accounts.stream().filter(a -> a.getStudents() != null)
                    .flatMap(a -> a.getStudents().stream()).collect(Collectors.toList());
        });

        studentField.disableProperty().bind(
                Bindings.isEmpty(accountField.textProperty()));

        fluencyCoherenceNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        vocabularyNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        grammarNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        pronunciationNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        interactEngageNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        commSkillsNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
    }

}
