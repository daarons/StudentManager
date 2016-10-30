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
import com.daarons.control.AbstractTreeItem;
import com.daarons.model.*;
import com.daarons.util.*;
import extfx.scene.chart.DateAxis;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.chart.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FXML Controller class
 *
 * @author David
 */
public class StudentController implements Initializable {

    private static final Logger log = LogManager.getLogger(StudentController.class);
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private SessionDAO sessionDAO;
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
    private GridPane gridPaneCenter;
    @FXML
    private Button trashBtn;

    public StudentController(Student student) {
        this.student = student;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initGridPanes();
        initTextFieldsAndAreas();
        installSaveBtnEventHandler();
        installTrashBtnEventHandler();
        installHandleTabEventHandler();
        
        initSessionTableView();
        gridPaneCenter.add(sessionTableView, 0, 0);

        LineChart<Date, Number> lineChart = initLineChart();
        gridPaneCenter.add(lineChart, 0, 1);
    }

    private LineChart<Date, Number> initLineChart() {
        DateAxis xAxis = new DateAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis("Score", 6, 30, 2);
        LineChart<Date, Number> lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle(student.toString() + "'s scores");
        lineChart.setLegendVisible(false);
        XYChart.Series<Date, Number> series = new XYChart.Series();
        series.setName("Scores");
        for (Session session : student.getSessions()) {
            Date xTimestamp = session.getTimestamp();
            int yTotalScore = session.getReview().getTotalGrade();
            series.getData().add(new XYChart.Data(xTimestamp, yTotalScore));
        }
        lineChart.getData().add(series);
        
        setTooltipDuration(lineChart);
        return lineChart;
    }

    private void setTooltipDuration(LineChart<Date, Number> lineChart) {
        for (XYChart.Series<Date, Number> dataSeries : lineChart.getData()) {
            for (XYChart.Data<Date, Number> data : dataSeries.getData()) {
                Tooltip tooltip = new Tooltip(data.getXValue().toString() + "\nScore: " + data.getYValue());
                try {
                    Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
                    fieldBehavior.setAccessible(true);
                    Class[] classes = Tooltip.class.getDeclaredClasses();
                    for (Class clazz : classes) {
                        if (clazz.getName().equals("javafx.scene.control.Tooltip$TooltipBehavior")) {
                            Constructor ctor = clazz.getDeclaredConstructor(
                                    Duration.class,
                                    Duration.class,
                                    Duration.class,
                                    boolean.class);
                            ctor.setAccessible(true);
                            Object tooltipBehavior = ctor.newInstance(
                                    new Duration(0),
                                    new Duration(Double.POSITIVE_INFINITY),
                                    new Duration(0),
                                    false);
                            fieldBehavior.set(null, tooltipBehavior);
                            break;
                        }
                    }
                } catch (Exception ex) {
                    log.error("Couldn't set duration of tooltip", ex);
                }
                Tooltip.install(data.getNode(), tooltip);

                data.getNode().setOnMouseEntered(event -> data.getNode().getStyleClass().add("onHover"));
                data.getNode().setOnMouseExited(event -> data.getNode().getStyleClass().remove("onHover"));
            }
        }
    }

    private void initSessionTableView() {
        sessionTableView = new TreeTableView();
        sessionTableView.setMaxHeight(Double.MAX_VALUE);
        sessionTableView.setMaxWidth(Double.MAX_VALUE);
        
        TreeTableColumn<Session, Number> sessionColumn = new TreeTableColumn("Session ID");
        sessionColumn.setCellValueFactory(column
                -> new ReadOnlyLongWrapper(column.getValue().getValue().getSessionId()));
        
        TreeTableColumn<Session, String> dateColumn = new TreeTableColumn("Date");
        dateColumn.setCellValueFactory(column
                -> new ReadOnlyStringWrapper(column.getValue().getValue().getTimestamp().toString()));
        
        sessionTableView.getColumns().addAll(sessionColumn, dateColumn);
        sessionTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        
        sessionTableView.setRowFactory(treeTableView -> {
            TreeTableRow row = new TreeTableRow() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        setContextMenu(((AbstractTreeItem) getTreeItem()).getContextMenu());
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    SessionTreeItem sessionTreeItem = (SessionTreeItem) sessionTableView.getSelectionModel().getSelectedItem();
                    Session session = sessionTreeItem.getSession();
                    session = sessionDAO.getSessionWithNoteAndReview(session.getId());
                    NavigationController.viewSession(session);
                }
            });
            return row;
        });
        
        sessionTableView.setRoot(createTree(student.getSessions()));
        sessionTableView.setShowRoot(false);
    }

    private void installHandleTabEventHandler() {
        hobbiesArea.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        motivesArea.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
        notesArea.addEventHandler(KeyEvent.KEY_PRESSED, new HandleTab());
    }

    private void installTrashBtnEventHandler() {
        trashBtn.setOnMouseClicked((MouseEvent event) -> {
            Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Are you "
                    + "sure that you want to delete student "
                    + this.student.toString() + " ?");
            deleteAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Account account = this.student.getAccount();
                    for (Student student : account.getStudents()) {
                        if (student.getId() == this.student.getId()) {
                            account.getStudents().remove(student);
                            break;
                        }
                    }
                    accountDAO.addOrUpdateAccount(account);
                    NavigationController.viewAccount();
                }
            });
        });
    }

    private void installSaveBtnEventHandler() {
        saveBtn.setOnMouseClicked((MouseEvent event) -> {
            if ((!englishNameField.getText().trim().isEmpty()
                    || !chineseNameField.getText().trim().isEmpty())
                    && Validator.isNumber(ageField.getText())) {
                student.setEnglishName(englishNameField.getText().replaceAll("`", ""));
                student.setChineseName(chineseNameField.getText().replaceAll("`", ""));
                student.setAge(Integer.parseInt(ageField.getText()));
                student.setLocation(locationField.getText().replaceAll("`", ""));
                student.setHobbies(hobbiesArea.getText().replaceAll("`", ""));
                student.setMotive(motivesArea.getText().replaceAll("`", ""));
                student.setOtherInfo(notesArea.getText().replaceAll("`", ""));
                
                //get account and replace the old student with the new student
                Account account = this.student.getAccount();
                for (Student student : account.getStudents()) {
                    if (student.getId() == this.student.getId()) {
                        student = this.student;
                    }
                }
                
                accountDAO.addOrUpdateAccount(account);
            } else {
                Alert saveAlert = new Alert(AlertType.ERROR, "Please make sure that "
                        + "at least one name field is filled in and the age is a "
                        + "number before saving.");
                saveAlert.showAndWait();
            }
        });
    }

    private void initTextFieldsAndAreas() {
        englishNameField.setText(student.getEnglishName());
        chineseNameField.setText(student.getChineseName());
        ageField.setText(String.valueOf(student.getAge()));
        locationField.setText(student.getLocation());
        hobbiesArea.setText(student.getHobbies());
        motivesArea.setText(student.getMotive());
        notesArea.setText(student.getOtherInfo());
    }

    private void initGridPanes() {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        gridPaneLeft.setMaxWidth(screenSize.getWidth() / 2);
        gridPaneCenter.setMaxWidth(screenSize.getWidth() / 2);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        gridPaneCenter.getRowConstraints().setAll(row1, row2);
    }

    public class SessionTreeItem extends AbstractTreeItem {

        private Session session;

        public SessionTreeItem(Session session) {
            this.session = session;
            setValue(session);
            createContextMenu();
        }

        @Override
        public ContextMenu getContextMenu() {
            return contextMenu;
        }

        public Session getSession() {
            return session;
        }

        public void setSession(Session session) {
            this.session = session;
        }

        @Override
        protected void createContextMenu() {
            MenuItem viewSession = new MenuItem("View Session");
            viewSession.setOnAction((ActionEvent event) -> {
                session = sessionDAO.getSessionWithNoteAndReview(session.getId());
                NavigationController.viewSession(session);
            });

            MenuItem deleteSession = new MenuItem("Delete Session");
            deleteSession.setOnAction((ActionEvent event) -> {
                Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Are you "
                        + "sure that you want to delete session "
                        + session.getSessionId() + " ?");
                deleteAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        //get account and remove session from it
                        Account account = session.getStudent().getAccount();
                        for (Student s : account.getStudents()) {
                            if (s.getId() == student.getId()) {
                                for (Session sess : s.getSessions()) {
                                    if (sess.getId() == session.getId()) {
                                        s.getSessions().remove(sess);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        Account updatedAccount = accountDAO.addOrUpdateAccount(account);
                        Student updatedStudent = updatedAccount
                                .getStudents()
                                .stream()
                                .filter(s -> s.getId() == student.getId())
                                .findFirst()
                                .get();
                        student = updatedStudent;
                        List<Session> updatedSessions = updatedStudent.getSessions();
                        List<SessionTreeItem> oldSessions = this.getParent().getChildren();
                        oldSessions.remove(this);
                        if (updatedSessions.size() > 0) {
                            for (int i = 0; i < updatedSessions.size(); i++) {
                                //automatically removes this SessionTreeItem from view
                                oldSessions.get(i).setSession(updatedSessions.get(i));
                            }
                        }
                        
                        //reloads view to show updated linechart
                        initialize(null, null);
                    }
                });
            });

            contextMenu = new ContextMenu(viewSession, deleteSession);
        }

    }

    private TreeItem createTree(List<Session> sessions) {
        TreeItem root = new TreeItem();
        if (sessions != null) {
            for (Session session : sessions) {
                SessionTreeItem item = new SessionTreeItem(session);
                item.setExpanded(true);
                root.getChildren().add(item);
            }
        }
        root.setExpanded(true);
        return root;
    }
}
