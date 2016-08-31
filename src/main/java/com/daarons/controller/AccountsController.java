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

import com.daarons.model.Account;
import com.daarons.model.Student;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author David
 */
public class AccountsController implements Initializable {
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private void searchAccounts(ActionEvent event) throws Exception{
        
    }
    @FXML
    private void editAccount(ActionEvent event) throws Exception{
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        //create table
        TreeTableView accountsTable = new TreeTableView();
        TreeTableColumn accountsCol = new TreeTableColumn("Account");
        accountsCol.setCellValueFactory((Object obj) -> {
            Object dataObj = ((TreeTableColumn.CellDataFeatures)obj).getValue().getValue();
            if(dataObj instanceof Account){
                return new ReadOnlyStringWrapper(String.valueOf(((Account)dataObj).getName()));
            }else if(dataObj instanceof Student){
                Student s = (Student) dataObj;
                if(s.getEnglishName() != null && !s.getEnglishName().equals(""))
                    return new ReadOnlyStringWrapper(String.valueOf(s.getEnglishName()));
                else if(s.getChineseName() != null && !s.getChineseName().equals(""))
                    return new ReadOnlyStringWrapper(String.valueOf(s.getChineseName()));
            }
            return null;
        });
        //accountsTable.setRoot(createTree(INSERT_ARRAYLIST_FROM_DB));
        accountsTable.setShowRoot(false);
        accountsTable.getColumns().add(accountsCol);
        accountsCol.prefWidthProperty().bind(accountsTable.widthProperty());
        
        gridPane.add(accountsTable, 0, 1);
    }    
    
    private TreeItem createTree(List<Account> a){
        TreeItem root = new TreeItem();
        for(Account acc : a){
            TreeItem item = new TreeItem(acc);
            item.setExpanded(true);
            acc.getStudents().stream().forEach(student ->{
               item.getChildren().add(new TreeItem(student));
            });
            root.getChildren().add(item);
        }
        root.setExpanded(true);
        return root;
    }
}
