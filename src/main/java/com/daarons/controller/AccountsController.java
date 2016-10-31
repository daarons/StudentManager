/*
 * Copyright 2016 David Aarons.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain account copy of the License at
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
import com.daarons.util.Validator;
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
    private AccountDAO accountDAO;  
    @Autowired
    private StudentDAO studentDAO;
    private TreeView accountsTreeView;

    @FXML
    private GridPane gridPane;
    @FXML
    private TextField searchAccountsField;

    @FXML
    private void displayAccounts(KeyEvent event) throws Exception {
        if (event.getSource() == searchAccountsField) {
            accountsTreeView.setRoot(createTree(null));
            if (!searchAccountsField.getText().isEmpty()) {
                List<Account> accounts = accountDAO.getAccountsLike(searchAccountsField.getText());
                if (accounts != null) {
                    accountsTreeView.setRoot(createTree(accounts));
                }
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountsTreeView = new TreeView();
        accountsTreeView.setShowRoot(false);
        accountsTreeView.setEditable(true);
        accountsTreeView.setCellFactory(tv -> new TextFieldTreeCell());
        accountsTreeView.setRoot(createTree(null));

        MenuItem addAccount = new MenuItem("Add Account");
        addAccount.setOnAction((ActionEvent event) -> {
            Account newAccount = new Account("New Account");
            newAccount = accountDAO.addOrUpdateAccount(newAccount);
            accountsTreeView.getRoot().getChildren().add(new AccountTreeItem(newAccount));
        });
        
        accountsTreeView.setContextMenu(new ContextMenu(addAccount));

        gridPane.add(accountsTreeView, 0, 1);
    }

    private class AccountTreeItem extends AbstractTreeItem {

        private Account account;

        public AccountTreeItem(Account account) {
            setValue(account);
            this.account = account;
            createContextMenu();
        }

        @Override
        public ContextMenu getContextMenu() {
            return contextMenu;
        }

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }

        @Override
        public void createContextMenu() {
            MenuItem addStudent = new MenuItem("Add Student");
            addStudent.setOnAction((ActionEvent event) -> {
                Student newStudent = new Student(account, "", "New Student", 0,
                        "", "", "", "");
                account.getStudents().add(newStudent);
                account = accountDAO.addOrUpdateAccount(account);
                newStudent = account.getStudents().get(account.getStudents().size() - 1);
                this.getChildren().add(new StudentTreeItem(newStudent));
                List<StudentTreeItem> children = this.getChildren();
                for (StudentTreeItem studentTreeItem : children) {
                    studentTreeItem.getStudent().setAccount(account);
                }
            });

            MenuItem deleteAccount = new MenuItem("Delete Account");
            deleteAccount.setOnAction((ActionEvent event) -> {
                Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Are you "
                        + "sure that you want to delete account "
                        + account.getName() + " ?");
                deleteAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        accountDAO.deleteAccount(account);
                        this.getParent().getChildren().remove(this);
                    }
                });
            });

            contextMenu = new ContextMenu(addStudent, deleteAccount);
        }
    }

    private class StudentTreeItem extends AbstractTreeItem {

        private Student student;

        public StudentTreeItem(Student student) {
            setValue(student);
            this.student = student;
            createContextMenu();
        }

        @Override
        public ContextMenu getContextMenu() {
            return contextMenu;
        }

        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        @Override
        public void createContextMenu() {
            MenuItem viewStudent = new MenuItem("View Student");
            viewStudent.setOnAction((ActionEvent event) -> {
                student = studentDAO.getStudentWithSessions(student.getId());
                NavigationController.viewStudent(student);
            });
            
            MenuItem deleteStudent = new MenuItem("Delete Student");
            deleteStudent.setOnAction((ActionEvent event) -> {
                Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Are you "
                        + "sure that you want to delete student "
                        + student.toString() + " ?");
                deleteAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        int index = this.getParent().getChildren().indexOf(this);
                        Account parentAccount = ((AccountTreeItem)this.getParent()).getAccount();
                        parentAccount.getStudents().remove(index);
                        parentAccount = accountDAO.addOrUpdateAccount(parentAccount);
                        ((AccountTreeItem) this.getParent()).setAccount(parentAccount);
                        List<StudentTreeItem> children = this.getParent().getChildren();
                        for (StudentTreeItem studentTreeItem : children) {
                            studentTreeItem.getStudent().setAccount(parentAccount);
                        }
                        this.getParent().getChildren().remove(this);
                    }
                });
            });
            
            contextMenu = new ContextMenu(viewStudent, deleteStudent);
        }
    }

    private final class TextFieldTreeCell extends TreeCell {

        private TextField textField;
        private ContextMenu contextMenu;

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
            textField.setOnKeyReleased((KeyEvent event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    AbstractTreeItem treeItem = (AbstractTreeItem) getTreeItem();
                    Account account = null;
                    if (treeItem instanceof AccountTreeItem) {
                        AccountTreeItem accountTreeItem = (AccountTreeItem) treeItem;
                        account = accountTreeItem.getAccount();
                        account.setName(textField.getText().replaceAll("`", ""));
                        accountTreeItem.setAccount(account);
                        accountTreeItem.setValue(account);
                    } else if (treeItem instanceof StudentTreeItem) {
                        StudentTreeItem studentTreeItem = (StudentTreeItem) treeItem;
                        Student student = studentTreeItem.getStudent();
                        account = student.getAccount();
                        if (Validator.containsChinese(textField.getText())) {
                            student.setChineseName(textField.getText().replaceAll("`", ""));
                        } else {
                            student.setEnglishName(textField.getText().replaceAll("`", ""));
                        }
                        int index = getTreeItem().getParent().getChildren().indexOf(treeItem);
                        account.getStudents().set(index, student);
                        studentTreeItem.setStudent(student);
                        studentTreeItem.setValue(student);
                    }
                    accountDAO.addOrUpdateAccount(account);
                    commitEdit(textField.getText().replaceAll("`", ""));
                } else if (event.getCode() == KeyCode.ESCAPE) {
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
            for (Account account : accounts) {
                AccountTreeItem item = new AccountTreeItem(account);
                item.setExpanded(true);
                account.getStudents().forEach(student -> {
                    item.getChildren().add(new StudentTreeItem(student));
                });
                root.getChildren().add(item);
            }
        }
        root.setExpanded(true);
        return root;
    }
}
