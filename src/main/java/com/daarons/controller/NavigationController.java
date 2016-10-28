package com.daarons.controller;

import com.daarons.config.SpringConfig;
import com.daarons.model.Account;
import com.daarons.model.Session;
import com.daarons.model.Student;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.*;
import org.apache.logging.log4j.*;
import org.springframework.context.ApplicationContext;

public class NavigationController implements Initializable {

    private static final Logger log = LogManager.getLogger(NavigationController.class);

    @FXML
    private Button homeBtn;
    @FXML
    private Button accountsBtn;
    @FXML
    private Button notesBtn;
    @FXML
    private Button importExportBtn;
    @FXML
    private Button infoBtn;

    @FXML
    private void navigate(ActionEvent event) {
        //this is only for navigating with the main buttons on the 1st page and button bar
        ApplicationContext applicationContext = SpringConfig.getApplicationContext();
        Stage stage = SpringConfig.getStage();
        Scene scene = null;
        Parent root = null;
        FXMLLoader fxmlLoader = null;
        try {
            if (event.getSource() == homeBtn) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            } else if (event.getSource() == accountsBtn) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/accounts.fxml"));
            } else if (event.getSource() == notesBtn) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/notes.fxml"));
            } else if (event.getSource() == importExportBtn) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/importExport.fxml"));
            } else if (event.getSource() == infoBtn) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/info.fxml"));
            }
        } catch (Exception ex) {
            log.error("Couldn't navigate", ex);
        }
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            log.error(ex);
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    public static void viewAccount(){
        FXMLLoader fxmlLoader = new FXMLLoader(NavigationController.class.getResource("/view/accounts.fxml"));
        ApplicationContext applicationContext = SpringConfig.getApplicationContext();
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Stage stage = SpringConfig.getStage();
        Scene scene = null;
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception ex) {
            log.error("Couldn't load accounts.fxml", ex);
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());
        stage.show();
    }
    
    public static void viewStudent(Student student){
        FXMLLoader fxmlLoader = new FXMLLoader(NavigationController.class.getResource("/view/student.fxml"));
        StudentController studentController = (StudentController) SpringConfig.getApplicationContext()
                .getBean("studentController", student);
        fxmlLoader.setController(studentController);
        Stage stage = SpringConfig.getStage();
        Scene scene = null;
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception ex) {
            log.error("Couldn't load student.fxml", ex);
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());
        stage.show();
    }
    
    public static void viewSession(Session session){
        FXMLLoader fxmlLoader = new FXMLLoader(NavigationController.class.getResource("/view/session.fxml"));
        SessionController sessionController = (SessionController) SpringConfig.getApplicationContext()
                .getBean("sessionController", session);
        fxmlLoader.setController(sessionController);
        Stage stage = SpringConfig.getStage();
        Scene scene = null;
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception ex) {
            log.error("Couldn't load session.fxml", ex);
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(stage.getHeight());
        stage.setWidth(stage.getWidth());
        stage.show();
    }
}
