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
package com.daarons.config;

import com.daarons.DAO.AccountDAO;
import com.daarons.DAO.AccountDAOHibernateImpl;
import com.daarons.DAO.ImportExportDAO;
import com.daarons.DAO.ImportExportDAOHibernateImpl;
import com.daarons.controller.AccountsController;
import com.daarons.controller.ImportExportController;
import com.daarons.controller.InfoController;
import com.daarons.controller.NavigationController;
import com.daarons.controller.NotesController;
import com.daarons.controller.SessionController;
import com.daarons.controller.StudentController;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 *
 * @author David
 */
@Configuration
@Lazy
@ComponentScan("com.daarons.DAO")
public class SpringConfig {
    private static Stage stage;
    private static ApplicationContext applicationContext;
    
    public static void setStage(Stage injectedStage){
        stage = injectedStage;
    }
    
    public static Stage getStage(){
        return stage;
    }
    
    public static void setApplicationContext(ApplicationContext injectedApplicationContext){
        applicationContext = injectedApplicationContext;
    }
    
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
    
    @Bean
    public AccountDAO accountDAOHibernateImpl(){
        return new AccountDAOHibernateImpl();
    }
    
    @Bean
    public ImportExportDAO importExportDAOHibernateImpl(){
        return new ImportExportDAOHibernateImpl();
    }
    
    @Bean
    public NavigationController navigationController(){
        return new NavigationController();
    }
    
    @Bean
    public AccountsController accountsController(){
        return new AccountsController();
    }
    
    @Bean
    public ImportExportController importExportController(){
        return new ImportExportController();
    }
    
    @Bean
    public InfoController infoController(){
        return new InfoController();
    }
    
    @Bean
    public NotesController notesController(){
        return new NotesController(getStage());
    }
    
//    @Bean
//    public SessionController sessionController(){
//        return new SessionController();
//    }
//    
//    @Bean
//    public StudentController studentController(){
//        return new StudentController();
//    }
}
