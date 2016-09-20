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
package com.daarons.DAO;

/**
 *
 * @author David
 */
public class DAOFactory {
    public static AccountDAO getAccountDAO(String impl){
        if(impl.equalsIgnoreCase("derby")){
            return new AccountDAODerbyImpl();
        }
        return null;
    }
    
    public static ImportExportDAO getImportExportDAO(String impl){
        if(impl.equalsIgnoreCase("derby")){
            return new ImportExportDAODerbyImpl();
        }
        return null;
    }
}
