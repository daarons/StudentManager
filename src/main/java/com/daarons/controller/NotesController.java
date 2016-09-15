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
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Account account;
    private Student student;

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
    private void handleTabAction(KeyEvent event) throws Exception {
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

    @FXML
    private void saveNotes(KeyEvent event) throws Exception {
        if (event.getSource() == saveBtn) {
            //grab account and update
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
            return dao.getAccounts(t.getUserText());
        });

        studentField.disableProperty().bind(
                Bindings.isEmpty(accountField.textProperty()));

    }

}
