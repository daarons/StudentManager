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

import com.daarons.DAO.DAOFactory;
import com.daarons.DAO.ImportExportDAO;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author David
 */
public class ImportExportController implements Initializable {

    private File folder;
    private boolean importDB;
    private boolean exportDB;
    private final ImportExportDAO dao = DAOFactory.getImportExportDAO("hibernate");

    @FXML
    private Button importBtn;
    @FXML
    private Button exportBtn;
    @FXML
    private Text folderName;

    @FXML
    private void chooseFolder(ActionEvent event) throws Exception {
        importDB = false;
        exportDB = false;
        DirectoryChooser dc = new DirectoryChooser();
        if (event.getSource() == importBtn) {
            Alert importAlert = new Alert(AlertType.CONFIRMATION, "Importing a "
                    + "database will delete the current database. Do you want "
                    + "to continue?");
            Optional<ButtonType> result = importAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                importDB = true;
            } else {
                folderName.setText("No folder selected");
                return;
            }
        } else if (event.getSource() == exportBtn) {
            exportDB = true;
        }

        folder = dc.showDialog(null);

        if (folder == null) {
            folderName.setText("No folder selected");
        } else {
            folderName.setText("Selected: " + folder.getPath());
        }
    }

    @FXML
    private void importExport(ActionEvent event) {
        if (folder == null) {
            Alert folderAlert = new Alert(AlertType.ERROR, "No folder selected!");
            folderAlert.showAndWait();
        } else if (importDB) {
            Alert importAlert = new Alert(AlertType.CONFIRMATION, "Importing a database"
                    + " will delete the current database. Do you want to continue?");
            Optional<ButtonType> result = importAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isImported = dao.importDB(folder);
                if(isImported){
                    folderName.setText("Import successful!");
                }else{
                    importAlert = new Alert(AlertType.ERROR, "Import failed!\n"
                            + "Make sure the database is working properly.");
                    importAlert.showAndWait();
                    folderName.setText("Import failed!");
                }
            }
        }else if(exportDB){
            boolean isExported = dao.exportDB(folder);
            if(isExported){
                folderName.setText("Export successful!");
            }else{
                Alert exportAlert = new Alert(AlertType.ERROR, "Export failed!\n"
                        + "Make sure that the database is working properly.\n"
                        + "If you are trying to overwrite previously exported files, "
                        + "the export will fail. Make sure that you are not exporting "
                        + "to a folder that already contains exported files.");
                exportAlert.showAndWait();
                folderName.setText("Export failed!");
            }
        }
        folder = null;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        folderName.setText("No folder selected");
    }

}
