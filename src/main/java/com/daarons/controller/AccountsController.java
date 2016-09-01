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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author David
 */
public class AccountsController implements Initializable {

    private final AccountDAO dao = DAOFactory.getAccountDAO("derby");
    private TreeView accountsView;

    @FXML
    private GridPane gridPane;
    @FXML
    private TextField searchAccountsField;
    @FXML
    private TextField addAccountField;
    @FXML
    private Button addAccountBtn;

    @FXML
    private void searchAccounts(KeyEvent event) throws Exception {
        if (event.getSource() == searchAccountsField) {
            accountsView.setRoot(null);
            if (!searchAccountsField.getText().isEmpty()) {
                List<Account> accounts = dao.getAccounts(searchAccountsField.getText());
                if (accounts != null) {
                    accountsView.setRoot(createTree(accounts));
                }
            }
        }
    }

    @FXML
    private void editAccount(ActionEvent event) throws Exception {

    }

    @FXML
    private void addAccount(InputEvent event) throws Exception {
        if (((event.getSource() == addAccountField
                && event.getEventType().equals(KeyEvent.KEY_RELEASED)
                && ((KeyEvent) event).getCode() == KeyCode.ENTER)
                || (event.getSource() == addAccountBtn
                && event.getEventType().equals(MouseEvent.MOUSE_CLICKED)
                && ((MouseEvent) event).getButton() == MouseButton.PRIMARY))
                && !addAccountField.getText().isEmpty()) {
            dao.addAccount(new Account(addAccountField.getText()));
            addAccountField.setText("");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountsView = new TreeView();
        accountsView.setShowRoot(false);
        accountsView.setEditable(true);
        gridPane.add(accountsView, 0, 1);
    }

    private TreeItem createTree(List<Account> accounts) {
        TreeItem root = new TreeItem();
        for (Account a : accounts) {
            TreeItem item = new TreeItem(a);
            item.setExpanded(true);
            a.getStudents().stream().forEach(student -> {
                item.getChildren().add(new TreeItem(student));
            });
            root.getChildren().add(item);
        }
        root.setExpanded(true);
        return root;
    }
}
