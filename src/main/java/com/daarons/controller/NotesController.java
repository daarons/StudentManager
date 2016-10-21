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
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.*;
import jfxtras.scene.control.CalendarTimePicker;
import org.apache.logging.log4j.*;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author David
 */
public class NotesController implements Initializable {

    private static final Logger log = LogManager.getLogger(NotesController.class);
    private final AccountDAO dao = DAOFactory.getAccountDAO("hibernate");
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
    
    public NotesController(Stage stage){
        this.stage = stage;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        saveBtn.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                saveNotes(event);
            }           
        });
        
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        tilePane.prefTileHeightProperty().bind(stage.widthProperty().divide(3.5));
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
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
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
    
    private void saveNotes(MouseEvent event) {
        if (event.getSource() == saveBtn
                && !accountField.getText().trim().isEmpty()
                && !studentField.getText().trim().isEmpty()
                && Validator.isNumber(sessionIdField.getText())) {
            List<Account> accounts = dao.getAccountsLike(accountField.getText());
            List<Student> students = accounts != null ? accounts.stream()
                    .filter(a -> a.getStudents() != null && !a.getStudents().isEmpty())
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
                for (Student s : account.getStudents()) {
                    if (s.getId() == student.getId()) {
                        session = new Session(s, new Timestamp(timePicker.getCalendar().getTimeInMillis()));
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
            
            //update the db and get the updated objects
            Account updatedAccount = dao.updateAccount(account);
            students = updatedAccount.getStudents().stream()
                    .filter(s
                            -> s.getEnglishName().equalsIgnoreCase(studentField.getText())
                            || s.getChineseName().equalsIgnoreCase(studentField.getText()))
                    .collect(Collectors.toList());
            student = students.get(0);
            for(Session sess : student.getSessions()){
                if(sess.getSessionId() == session.getSessionId()){
                    session = sess;
                }
            }
            
            viewSession(session);
        } else {
            Alert saveAlert = new Alert(AlertType.ERROR, "Please make sure that "
                    + "the account, student, and session ID text fields are "
                    + "filled in correctly before saving.");
            saveAlert.showAndWait();
        }
    }

    private void viewSession(Session session) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/session.fxml"));
        SessionController sessionController = new SessionController(session);
        fxmlLoader.setController(sessionController);
        Stage stage = (Stage) ((Node) tilePane).getScene().getWindow();
        Scene scene = null;
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (Exception ex) {
            log.error("Couldn't load session", ex);
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());
        stage.show();
    }
}
