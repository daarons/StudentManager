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
import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FXML Controller class
 *
 * @author David
 */
public class AccountsController implements Initializable {
    
    private static final Logger log = LogManager.getLogger(AccountsController.class);
    @Autowired
    private AccountDAO dao;
    private TreeView accountsView;

    @FXML
    private GridPane gridPane;
    @FXML
    private TextField searchAccountsField;

    @FXML
    private void searchAccounts(KeyEvent event) throws Exception {
        if (event.getSource() == searchAccountsField) {
            accountsView.setRoot(createTree(null));
            if (!searchAccountsField.getText().isEmpty()) {
                List<Account> accounts = dao.getAccountsLike(searchAccountsField.getText());
                if (accounts != null) {
                    accountsView.setRoot(createTree(accounts));
                }
            }
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
        accountsView.setCellFactory(tv -> new TextFieldTreeCell());
        accountsView.setRoot(createTree(null));

        MenuItem addAccount = new MenuItem("Add Account");
        addAccount.setOnAction((ActionEvent t) -> {
            Account newAccount = new Account("New Account");
            newAccount = dao.addAccount(newAccount);
            accountsView.getRoot().getChildren().add(new AccountTreeItem(newAccount));
        });
        
        accountsView.setContextMenu(new ContextMenu(addAccount));

        gridPane.add(accountsView, 0, 1);
    }

    public class AccountTreeItem extends AbstractTreeItem {

        private Account account;

        public AccountTreeItem(Account account) {
            setValue(account);
            this.account = account;
        }

        @Override
        public ContextMenu getContextMenu() {
            MenuItem addStudent = new MenuItem("Add Student");
            addStudent.setOnAction((ActionEvent t) -> {
                Student newStudent = new Student(account, "", "New Student", 0,
                        "", "", "", "");
                account.getStudents().add(newStudent);
                account = dao.updateAccount(account);
                newStudent = account.getStudents().get(account.getStudents().size() - 1);
                this.getChildren().add(new StudentTreeItem(newStudent));
                List<AbstractTreeItem> children = this.getChildren();
                for (AbstractTreeItem ati : children) {
                    ((Student) ati.getObject()).setAccount(account);
                }
            });

            MenuItem deleteAccount = new MenuItem("Delete Account");
            deleteAccount.setOnAction((ActionEvent t) -> {
                Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Are you "
                        + "sure that you want to delete account "
                        + account.getName() + " ?");
                deleteAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dao.deleteAccount(account);
                        this.getParent().getChildren().remove(this);
                    }
                });
            });

            return new ContextMenu(addStudent, deleteAccount);
        }

        @Override
        public Object getObject() {
            return account;
        }

        @Override
        public void setObject(Object o) {
            if (o instanceof Account) {
                this.account = (Account) o;
            }
        }
    }

    public class StudentTreeItem extends AbstractTreeItem {

        private Student student;

        public StudentTreeItem(Student s) {
            setValue(s);
            this.student = s;
        }

        @Override
        public ContextMenu getContextMenu() {
            MenuItem viewStudent = new MenuItem("View Student");
            viewStudent.setOnAction((ActionEvent event) -> {
                NavigationController.viewStudent(student);
            });
            
            MenuItem deleteStudent = new MenuItem("Delete Student");
            deleteStudent.setOnAction((ActionEvent t) -> {
                Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Are you "
                        + "sure that you want to delete student "
                        + student.toString() + " ?");
                deleteAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        int index = this.getParent().getChildren().indexOf(this);
                        Account parentAccount = ((Account) ((AbstractTreeItem) this.getParent()).getObject());
                        parentAccount.getStudents().remove(index);
                        Account newParentAccount = dao.updateAccount(parentAccount);
                        ((AbstractTreeItem) this.getParent()).setObject(newParentAccount);
                        List<AbstractTreeItem> children = this.getParent().getChildren();
                        for (AbstractTreeItem ati : children) {
                            ((Student) ati.getObject()).setAccount(newParentAccount);
                        }
                        this.getParent().getChildren().remove(this);
                    }
                });
            });
            
            return new ContextMenu(viewStudent, deleteStudent);
        }

        @Override
        public Object getObject() {
            return student;
        }

        @Override
        public void setObject(Object o) {
            if (o instanceof Student) {
                this.student = (Student) o;
            }
        }
    }

    public final class TextFieldTreeCell extends TreeCell {

        private TextField textField;
        private ContextMenu menu;

        @Override
        public void startEdit() {
            super.startEdit();
            if (textField == null) {
                createTextField();
            }
            textField.setText(getString());
            setText(null);
            setGraphic(textField);
            textField.requestFocus();
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getString());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (isEditing() && textField != null) {
                textField.setText(getString());
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
                setContextMenu(((AbstractTreeItem) getTreeItem()).getContextMenu());
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.focusedProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        if (oldValue) {
                            cancelEdit();
                        }
                    });
            textField.setOnKeyReleased((KeyEvent t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    AbstractTreeItem ti = (AbstractTreeItem) getTreeItem();
                    Object o = ti.getObject();
                    Account a = null;
                    if (o instanceof Account) {
                        a = (Account) o;
                        a.setName(textField.getText().replaceAll("`", ""));
                        ti.setValue(a);
                        ti.setObject(a);
                    } else if (o instanceof Student) {
                        Student s = (Student) o;
                        a = s.getAccount();
                        if (containsChinese(textField.getText())) {
                            s.setChineseName(textField.getText().replaceAll("`", ""));
                        } else {
                            s.setEnglishName(textField.getText().replaceAll("`", ""));
                        }
                        int index = getTreeItem().getParent().getChildren().indexOf(ti);
                        a.getStudents().set(index, s);
                        ti.setValue(s);
                        ti.setObject(s);
                    }
                    dao.updateAccount(a);
                    commitEdit(textField.getText().replaceAll("`", ""));
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    private TreeItem createTree(List<Account> accounts) {
        TreeItem root = new TreeItem();
        if (accounts != null) {
            for (Account a : accounts) {
                AccountTreeItem item = new AccountTreeItem(a);
                item.setExpanded(true);
                a.getStudents().forEach(student -> {
                    item.getChildren().add(new StudentTreeItem(student));
                });
                root.getChildren().add(item);
            }
        }
        root.setExpanded(true);
        return root;
    }

    private static boolean containsChinese(String text) {
        return text.codePoints().anyMatch(codepoint
                -> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }
}
