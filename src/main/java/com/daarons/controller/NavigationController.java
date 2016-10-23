package com.daarons.controller;

import com.daarons.config.SpringConfig;
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
        ApplicationContext applicationContext = SpringConfig.getApplicationContext();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
}
