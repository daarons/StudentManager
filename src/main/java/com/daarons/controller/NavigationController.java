package com.daarons.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.*;
import org.apache.logging.log4j.*;

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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = null;
        Parent root = null;
        try {
            if (event.getSource() == homeBtn) {
                root = FXMLLoader.load(getClass().getResource("/view/home.fxml"));
            } else if (event.getSource() == accountsBtn) {
                root = FXMLLoader.load(getClass().getResource("/view/accounts.fxml"));
            } else if (event.getSource() == notesBtn) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/notes.fxml"));
                NotesController notesController = new NotesController(stage);
                fxmlLoader.setController(notesController);
                root = fxmlLoader.load();
            } else if (event.getSource() == importExportBtn) {
                root = FXMLLoader.load(getClass().getResource("/view/importExport.fxml"));
            } else if (event.getSource() == infoBtn) {
                root = FXMLLoader.load(getClass().getResource("/view/info.fxml"));
            }
        } catch (Exception ex) {
            log.error("Couldn't navigate", ex);
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
