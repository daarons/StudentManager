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
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.CalendarTimePicker;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FXML Controller class
 *
 * @author David
 */
public class SessionController implements Initializable {

    private static final Logger log = LogManager.getLogger(SessionController.class);
    @Autowired
    private AccountDAO dao;
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private SessionDAO sessionDAO;
    private Session session;

    public SessionController(Session session) {
        this.session = session;
    }

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextArea fluencyCoherenceNotes;
    @FXML
    private TextArea vocabularyNotes;
    @FXML
    private TextArea grammarNotes;
    @FXML
    private TextArea pronunciationNotes;
    @FXML
    private TextArea interactEngageNotes;
    @FXML
    private TextArea commSkillsNotes;
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
    @FXML
    private TextField sessionIdField;
    @FXML
    private CalendarPicker calendarPicker;
    @FXML
    private CalendarTimePicker timePicker;
    @FXML
    private Button saveBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button trashBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initChoiceBoxesAndTextAreas();

        sessionIdField.setText(String.valueOf(session.getSessionId()));
        initCalendar();
        initNotes();
        initReview();

        installSaveBtnEventHandler();
        installBackBtnEventHandler();
        installTrashBtnEventHandler();
    }

    private void installTrashBtnEventHandler() {
        trashBtn.setOnMouseClicked((MouseEvent event) -> {
            Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Are you "
                    + "sure that you want to delete session "
                    + session.getSessionId() + " ?");
            deleteAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Student student = session.getStudent();
                    student = studentDAO.getStudentWithSessions(student.getId());
                    for (Session sess : student.getSessions()) {
                        if (sess.getId() == session.getId()) {
                            student.getSessions().remove(sess);
                            break;
                        }
                    }
                    student = studentDAO.updateStudent(student);
                    NavigationController.viewStudent(student);
                }
            });
        });
    }

    private void installBackBtnEventHandler() {
        backBtn.setOnMouseClicked((MouseEvent event) -> {
            Student student = session.getStudent();
            student = studentDAO.getStudentWithSessions(student.getId());
            NavigationController.viewStudent(student);
        });
    }

    private void installSaveBtnEventHandler() {
        saveBtn.setOnMouseClicked((MouseEvent event) -> {
            if (Validator.isNumber(sessionIdField.getText())) {
                long sessionId = Long.parseLong(sessionIdField.getText());
                session.setSessionId(sessionId);

                Calendar newCalendar = calendarPicker.getCalendar();
                Calendar newTimeCalendar = timePicker.getCalendar();
                int day = newCalendar.get(Calendar.DAY_OF_WEEK);
                int month = newCalendar.get(Calendar.MONTH);
                int date = newCalendar.get(Calendar.DATE);
                int year = newCalendar.get(Calendar.YEAR);
                int hour = newTimeCalendar.get(Calendar.HOUR_OF_DAY);
                int min = newTimeCalendar.get(Calendar.MINUTE);
                Calendar c = new GregorianCalendar(year, month, date, hour, min);
                Date newTimestamp = new Timestamp(c.getTimeInMillis());
                session.setTimestamp(newTimestamp);

                Note note = session.getNote();
                note.setFluencyAndCoherence(fluencyCoherenceNotes.getText().replaceAll("`", ""));
                note.setVocabulary(vocabularyNotes.getText().replaceAll("`", ""));
                note.setGrammar(grammarNotes.getText().replaceAll("`", ""));
                note.setPronunciation(pronunciationNotes.getText().replaceAll("`", ""));
                note.setInteractionAndEngagement(interactEngageNotes.getText().replaceAll("`", ""));
                note.setCommunicationSkills(commSkillsNotes.getText().replaceAll("`", ""));
                note.setSession(session);
                session.setNote(note);

                Review review = session.getReview();
                review.setFluencyAndCoherence(
                        new ReviewSection((int) fluencyCoherenceBox.getValue(),
                                fluencyCoherenceReview.getText().replaceAll("`", "")));
                review.setVocabulary(
                        new ReviewSection((int) vocabularyBox.getValue(),
                                vocabularyReview.getText().replaceAll("`", "")));
                review.setGrammar(
                        new ReviewSection((int) grammarBox.getValue(),
                                grammarReview.getText().replaceAll("`", "")));
                review.setPronunciation(
                        new ReviewSection((int) pronunciationBox.getValue(),
                                pronunciationReview.getText().replaceAll("`", "")));
                review.setInteractionAndEngagement(
                        new ReviewSection((int) interactEngageBox.getValue(),
                                interactEngageReview.getText().replaceAll("`", "")));
                review.setCommunicationSkills(
                        new ReviewSection((int) commSkillsBox.getValue(),
                                commSkillsReview.getText().replaceAll("`", "")));
                review.setSession(session);
                session.setReview(review);

                //updates the sessions for persisting and get the student view
                session = sessionDAO.updateSession(session);
                Student studentView = session.getStudent();
                studentView = studentDAO.getStudentWithSessions(studentView.getId());
                NavigationController.viewStudent(studentView);
            } else {
                Alert saveAlert = new Alert(AlertType.ERROR, "The session ID "
                        + "field does not contain a session ID.");
                saveAlert.showAndWait();
            }
        });
    }

    private void initReview() {
        Review sessionReview = session.getReview();
        fluencyCoherenceBox.setValue(sessionReview.getFluencyAndCoherence().getGrade());
        fluencyCoherenceReview.setText(sessionReview.getFluencyAndCoherence().getComment());
        vocabularyBox.setValue(sessionReview.getVocabulary().getGrade());
        vocabularyReview.setText(sessionReview.getVocabulary().getComment());
        grammarBox.setValue(sessionReview.getGrammar().getGrade());
        grammarReview.setText(sessionReview.getGrammar().getComment());
        pronunciationBox.setValue(sessionReview.getPronunciation().getGrade());
        pronunciationReview.setText(sessionReview.getPronunciation().getComment());
        interactEngageBox.setValue(sessionReview.getInteractionAndEngagement().getGrade());
        interactEngageReview.setText(sessionReview.getInteractionAndEngagement().getComment());
        commSkillsBox.setValue(sessionReview.getCommunicationSkills().getGrade());
        commSkillsReview.setText(sessionReview.getCommunicationSkills().getComment());
    }

    private void initNotes() {
        Note sessionNotes = session.getNote();
        fluencyCoherenceNotes.setText(sessionNotes.getFluencyAndCoherence());
        vocabularyNotes.setText(sessionNotes.getVocabulary());
        grammarNotes.setText(sessionNotes.getGrammar());
        pronunciationNotes.setText(sessionNotes.getPronunciation());
        interactEngageNotes.setText(sessionNotes.getInteractionAndEngagement());
        commSkillsNotes.setText(sessionNotes.getCommunicationSkills());
    }

    private void initCalendar() {
        Date timestamp = session.getTimestamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendarPicker.setCalendar(calendar);
        timePicker.setCalendar(calendar);
    }

    private void initChoiceBoxesAndTextAreas() {
        ObservableList<Integer> cbList = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        
        ArrayList<Node> nodes = getAllNodes(borderPane);
        for (Node node : nodes) {
            if (node instanceof ChoiceBox) {
                ((ChoiceBox) node).setItems(cbList);
                ((ChoiceBox) node).getSelectionModel().selectFirst();
            } else if (node instanceof TextArea) {
                ((TextArea) node).setOnKeyPressed(new HandleTab());
            }
        }
    }

    private static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent) {
                addAllDescendents((Parent) node, nodes);
            }
        }
    }
}
