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

import com.daarons.controller.*;
import com.daarons.model.*;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author David
 */
@Configuration
@EnableTransactionManagement
@Lazy
@ComponentScan("com.daarons.DAO")
public class SpringConfig{
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
    public LocalContainerEntityManagerFactoryBean emf(){
        return new LocalContainerEntityManagerFactoryBean();
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        return new JpaTransactionManager(emf);
    }
    
    @Bean
    @Scope("prototype")
    public NavigationController navigationController(){
        return new NavigationController();
    }
    
    @Bean
    @Scope("prototype")
    public AccountsController accountsController(){
        return new AccountsController();
    }
    
    @Bean
    @Scope("prototype")
    public ImportExportController importExportController(){
        return new ImportExportController();
    }
    
    @Bean
    @Scope("prototype")
    public InfoController infoController(){
        return new InfoController();
    }
    
    @Bean
    @Scope("prototype")
    public NotesController notesController(){
        return new NotesController(getStage());
    }
    
    @Bean
    @Scope("prototype")
    public SessionController sessionController(Session session){
        return new SessionController(session);
    }
    
    @Bean
    @Scope("prototype")
    public StudentController studentController(Student student){
        return new StudentController(student);
    }
}
