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

import com.daarons.DAO.*;
import com.daarons.model.*;
import com.daarons.util.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.fxml.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.*;
import jfxtras.scene.control.CalendarTimePicker;
import org.apache.logging.log4j.*;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FXML Controller class
 *
 * @author David
 */
public class NotesController implements Initializable {

    private static final Logger log = LogManager.getLogger(NotesController.class);
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private SessionDAO sessionDAO;
    private Stage stage;

    @FXML
    private BorderPane borderPane;
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

    public NotesController(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        installSaveBtnEventHandler();

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        setTilePaneSize(screenSize);

        initTimePicker(screenSize);

        initBorderPaneBottomTextFields();

        initTextAreas();
    }

    private void initTextAreas() {
        fluencyCoherenceNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        vocabularyNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        grammarNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        pronunciationNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        interactEngageNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        commSkillsNote.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
    }

    private void initBorderPaneBottomTextFields() {
        TextFields.bindAutoCompletion(accountField, iSuggestionRequest -> {
            return accountDAO.getAccountsLike(iSuggestionRequest.getUserText());
        });
        TextFields.bindAutoCompletion(studentField, iSuggestionRequest -> {
            List<Account> accounts = accountDAO.getAccountsLike(accountField.getText());
            return accounts.stream().filter(a -> a.getStudents() != null)
                    .flatMap(a -> a.getStudents().stream()).collect(Collectors.toList());
        });

        studentField.disableProperty().bind(
                Bindings.isEmpty(accountField.textProperty()));
    }

    private void initTimePicker(Rectangle2D screenSize) {
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
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        timePicker.setCalendar(cal);
    }

    private void setTilePaneSize(Rectangle2D screenSize) {
        tilePane.prefTileHeightProperty().bind(stage.widthProperty().divide(3.5));
        tilePane.setPrefTileWidth(screenSize.getWidth() / 6.5);
    }

    private void installSaveBtnEventHandler() {
        saveBtn.setOnMouseClicked((MouseEvent event) -> {
            if (event.getSource() == saveBtn
                    && !accountField.getText().trim().isEmpty()
                    && !studentField.getText().trim().isEmpty()
                    && Validator.isNumber(sessionIdField.getText())) {
                List<Account> accounts = accountDAO.getAccountsLike(accountField.getText());
                //get students from the list of accounts that have the same name the user entered
                List<Student> students = accounts != null ? accounts.stream()
                        .filter(account -> account.getStudents() != null && !account.getStudents()
                                .isEmpty()).flatMap((com.daarons.model.Account a1)
                                -> a1.getStudents().stream().filter(student
                                        -> student.getEnglishName().equalsIgnoreCase(studentField.getText())
                                        || student.getChineseName().equalsIgnoreCase(studentField.getText())))
                        .collect(Collectors.toList()) : null;
                Account account = null;
                Student student = null;
                Session session = null;
                Note note = null;
                if (accounts == null) {
                    //create new account and student
                    account = new Account(accountField.getText());
                    student = new Student();
                    student.setEnglishName(studentField.getText());
                    student.setAccount(account);
                    session = new Session(student, new Timestamp(timePicker.getCalendar().getTimeInMillis()));
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
                    //create new student and add to account
                    account = accounts.get(0);
                    student = new Student();
                    student.setEnglishName(studentField.getText());
                    student.setAccount(account);
                    session = new Session(student, new Timestamp(timePicker.getCalendar().getTimeInMillis()));
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
                    for (Student loopedStudent : account.getStudents()) {
                        if (loopedStudent.getId() == student.getId()) {
                            session = new Session(loopedStudent, new Timestamp(timePicker.getCalendar().getTimeInMillis()));
                            note = new Note(session, fluencyCoherenceNote.getText().replaceAll("`", ""),
                                    vocabularyNote.getText().replaceAll("`", ""),
                                    grammarNote.getText().replaceAll("`", ""),
                                    pronunciationNote.getText().replaceAll("`", ""),
                                    interactEngageNote.getText().replaceAll("`", ""),
                                    commSkillsNote.getText().replaceAll("`", ""));
                            session.setNote(note);
                            session.setSessionId(Long.parseLong(sessionIdField.getText()));
                            loopedStudent = studentDAO.getStudentWithSessions(loopedStudent.getId());
                            loopedStudent.getSessions().add(session);
                        }
                    }
                }
                //update the db, get the updated objects, view the new session
                Account updatedAccount = accountDAO.addOrUpdateAccount(account);
                students = updatedAccount.getStudents().stream()
                        .filter(filteredStudent -> 
                                filteredStudent.getEnglishName().equalsIgnoreCase(studentField.getText()) 
                                        || filteredStudent.getChineseName().equalsIgnoreCase(studentField.getText()))
                        .collect(Collectors.toList());
                student = students.get(0);
                student = studentDAO.getStudentWithSessions(student.getId());
                for (Session sess : student.getSessions()) {
                    if (sess.getSessionId() == session.getSessionId()) {
                        session = sess;
                    }
                }               
                NavigationController.viewSession(session);
            } else {
                Alert saveAlert = new Alert(AlertType.ERROR, "Please make sure that "
                        + "the account, student, and session ID text fields are "
                        + "filled in correctly before saving.");
                saveAlert.showAndWait();
            }
        });
    }

}
