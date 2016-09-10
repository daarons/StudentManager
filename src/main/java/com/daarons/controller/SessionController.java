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
import com.daarons.model.Review;
import com.daarons.model.ReviewSection;
import com.daarons.model.Session;
import com.daarons.model.Student;
import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.CalendarTimePicker;

/**
 * FXML Controller class
 *
 * @author David
 */
public class SessionController implements Initializable {

    private final AccountDAO dao = DAOFactory.getAccountDAO("derby");
    private Session session;
    private ArrayList<Node> nodes;

    public SessionController(Session session) {
        this.session = session;
    }

    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPaneCenterCenter;
    @FXML
    private GridPane gridPaneCenterRight;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Integer> cbList = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        ArrayList<Node> nodes = getAllNodes(borderPane);
        this.nodes = nodes;
        for (Node node : nodes) {
            if (node instanceof ChoiceBox) {
                ((ChoiceBox) node).setItems(cbList);
                ((ChoiceBox) node).getSelectionModel().selectFirst();
            } else if (node instanceof TextArea) {
                ((TextArea) node).setOnKeyPressed(handleTabAction);
            }
        }

        sessionIdField.setText(String.valueOf(session.getSessionId()));

        Date timestamp = session.getTimestamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendarPicker.setCalendar(calendar);
        timePicker.setCalendar(calendar);

        Note sessionNotes = session.getNotes();
        if (sessionNotes != null) {
            fluencyCoherenceNotes.setText(sessionNotes.getFluencyAndCoherence());
            vocabularyNotes.setText(sessionNotes.getVocabulary());
            grammarNotes.setText(sessionNotes.getGrammar());
            pronunciationNotes.setText(sessionNotes.getPronunciation());
            interactEngageNotes.setText(sessionNotes.getInteractionAndEngagement());
            commSkillsNotes.setText(sessionNotes.getCommunicationSkills());
        }

        Review sessionReview = session.getReview();
        if (sessionReview != null) {
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

        saveBtn.setOnAction((ActionEvent event) -> {
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

            Note notes = new Note();
            notes.setFluencyAndCoherence(fluencyCoherenceNotes.getText());
            notes.setVocabulary(vocabularyNotes.getText());
            notes.setGrammar(grammarNotes.getText());
            notes.setPronunciation(pronunciationNotes.getText());
            notes.setInteractionAndEngagement(interactEngageNotes.getText());
            notes.setCommunicationSkills(commSkillsNotes.getText());
            notes.setSession(session);
            session.setNotes(notes);

            Review review = new Review();
            review.setFluencyAndCoherence(
                    new ReviewSection((int) fluencyCoherenceBox.getValue(),
                            fluencyCoherenceReview.getText()));
            review.setVocabulary(
                    new ReviewSection((int) vocabularyBox.getValue(),
                            vocabularyReview.getText()));
            review.setGrammar(
                    new ReviewSection((int) grammarBox.getValue(),
                            grammarReview.getText()));
            review.setPronunciation(
                    new ReviewSection((int) pronunciationBox.getValue(),
                            pronunciationReview.getText()));
            review.setInteractionAndEngagement(
                    new ReviewSection((int) interactEngageBox.getValue(),
                            interactEngageReview.getText()));
            review.setCommunicationSkills(
                    new ReviewSection((int) commSkillsBox.getValue(),
                            commSkillsReview.getText()));
            review.setSession(session);
            session.setReview(review);

            Account account = session.getStudent().getAccount();
            for (Student student : account.getStudents()) {
                if (student.getId() == session.getStudent().getId()) {
                    for (Session s : student.getSessions()) {
                        if (s.getId() == session.getId()) {
                            s = session;
                            break;
                        }
                    }
                }
            }
            dao.updateAccount(account);
        });

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
