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

import com.daarons.config.SpringConfig;
import com.daarons.model.Account;
import java.io.File;
import java.sql.*;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.logging.log4j.*;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public class ImportExportDAOJpaHibernateImpl implements ImportExportDAO {

    private static final Logger log = LogManager.getLogger(ImportExportDAOJpaHibernateImpl.class);
    private EntityManager em;
    private Session session;   
    @Autowired
    AccountDAO dao;

    @Override
    public boolean importDB(File folder) {
        try {
            //all accounts must be deleted before importing due to derby's 
            //import procedure
            deleteAccounts();

            em = SpringConfig.getApplicationContext()
                    .getBean(LocalContainerEntityManagerFactoryBean.class)
                    .getObject()
                    .createEntityManager();
            session = em.unwrap(Session.class);
            session.doWork((Connection connection) -> {
                String[] tables = {"ACCOUNT", "STUDENT", "SESSION",
                    "NOTE", "REVIEW"};

                try {
                    for (int i = 0; i < 5; i++) {
                        PreparedStatement ps = connection.prepareStatement(
                                "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE (?,?,?,?,?,?,?)");
                        ps.setString(1, null);
                        ps.setString(2, tables[i]);
                        ps.setString(3, folder.getPath() + File.separator + tables[i] + ".dat");
                        ps.setString(4, "`");
                        ps.setString(5, null);
                        ps.setString(6, "UTF-8");
                        ps.setString(7, "1");
                        ps.execute();
                    }
                } catch (Exception e) {
                    throw new HibernateException("Import failed.", e);
                } finally {
                    connection.close();
                }
            });
        } catch (Exception e) {
            log.error("Import failed", e);
            return false;
        } finally {
            em.close();
        }
        return true;
    }

    @Override
    public boolean exportDB(File folder) {
        try {
            em = SpringConfig.getApplicationContext()
                    .getBean(LocalContainerEntityManagerFactoryBean.class)
                    .getObject()
                    .createEntityManager();
            session = em.unwrap(Session.class);
            session.doWork((Connection connection) -> {
                String[] tables = {"ACCOUNT", "STUDENT", "SESSION",
                    "NOTE", "REVIEW"};
                try {
                    for (int i = 0; i < tables.length; i++) {
                        PreparedStatement ps = connection.prepareStatement(
                                "CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE (?,?,?,?,?,?)");
                        ps.setString(1, null);
                        ps.setString(2, tables[i]);
                        ps.setString(3, folder.getPath() + File.separator + tables[i] + ".dat");
                        ps.setString(4, "`");
                        ps.setString(5, null);
                        ps.setString(6, "UTF-8");
                        ps.execute();
                    }
                } catch (Exception e) {
                    throw new HibernateException("Export failed", e);
                }finally{
                    connection.close();
                }
            });
        } catch (Exception e) {
            log.error("Couldn't export", e);
            return false;
        } finally {
            em.close();
        }
        return true;
    }

    private void deleteAccounts() {
        List<Account> accounts = dao.getAccountsLike("*");
        if (accounts != null) {
            accounts.forEach(account -> dao.deleteAccount(account));
        }
    }
}
