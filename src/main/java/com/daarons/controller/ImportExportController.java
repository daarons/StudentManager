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
import com.daarons.DAO.EMFSingleton;
import com.daarons.model.Account;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * FXML Controller class
 *
 * @author David
 */
public class ImportExportController implements Initializable {

    private File file;
    private boolean importDb;

    @FXML
    private Button importBtn;
    @FXML
    private Button exportBtn;
    @FXML
    private Text fileName;

    @FXML
    private void launchFileChooser(ActionEvent event) throws Exception {
        DirectoryChooser dc = new DirectoryChooser();
        if (event.getSource() == importBtn) {
            importDb = true;
            file = dc.showDialog(null);
        } else if (event.getSource() == exportBtn) {
            importDb = false;
            file = dc.showDialog(null);
        }

        if (file != null) {
            fileName.setText("Selected: " + file.getPath());
        } else {
            fileName.setText("No file selected");
        }
    }

    @FXML
    private void importExport(ActionEvent event) {
        EntityManager em = null;
        Session session = null;
        if (!importDb) {
            try {
                em = EMFSingleton.getEntityManagerFactory()
                        .createEntityManager();
                em.getTransaction().begin();
                session = em.unwrap(Session.class);
                session.doWork(new Work() {
                    @Override
                    public void execute(Connection connection) throws SQLException {
                        String[] tables = {"ACCOUNT", "STUDENT", "SESSION",
                            "NOTE", "REVIEW"};
                        for (int i = 0; i < tables.length; i++) {
                            PreparedStatement ps = connection.prepareStatement(
                                    "CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
                            ps.setString(1, null);
                            ps.setString(2, tables[i]);
                            ps.setString(3, file.getPath() + File.separator + tables[i] + ".dat");
                            ps.setString(4, "`");
                            ps.setString(5, null);
                            ps.setString(6, null);
                            ps.execute();
                        }
                        connection.close();
                    }
                });
            } catch (Exception e) {
                //if user attempts to overwrite previously written .dat files
                //an error is thrown and the files are not overwritten
                //warn user that this happened
                e.printStackTrace();
            } finally {
                em.close();
            }
        } else if (importDb) {
            try {
                //importing must delete all accounts
                AccountDAO dao = DAOFactory.getAccountDAO("derby");
                List<Account> accounts = dao.getAccountsLike("*");
                accounts.forEach(account -> dao.deleteAccount(account));
                em = EMFSingleton.getEntityManagerFactory()
                        .createEntityManager();
                em.getTransaction().begin();
                session = em.unwrap(Session.class);
                session.doWork(new Work() {
                    @Override
                    public void execute(Connection connection) throws SQLException {
                        String[] tables = {"ACCOUNT", "STUDENT", "SESSION",
                            "NOTE", "REVIEW"};
                        for (int i = 0; i < tables.length; i++) {
                            PreparedStatement ps = connection.prepareStatement(
                                    "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (?,?,?,?,?,?,?)");
                            ps.setString(1, null);
                            ps.setString(2, tables[i]);
                            ps.setString(3, file.getPath() + File.separator + tables[i] + ".dat");
                            ps.setString(4, "`");
                            ps.setString(5, null);
                            ps.setString(6, null);
                            ps.setString(7, "1");
                            ps.execute();
                        }
                        connection.close();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                em.close();
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
