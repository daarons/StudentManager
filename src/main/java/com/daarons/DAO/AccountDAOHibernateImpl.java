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

import com.daarons.model.Account;
import java.util.List;
import javax.persistence.*;
import org.apache.logging.log4j.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David
 */
@Repository
public class AccountDAOHibernateImpl implements AccountDAO {
    private static final Logger log = LogManager.getLogger(AccountDAOHibernateImpl.class);
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public Account getAccount(long id) {
        Account account = em.find(Account.class, id);
        em.close();
        return account;
    }

    @Override
    public List<Account> getAccountsLike(String name) {
        String qStringLike = "SELECT a FROM Account a WHERE a.name LIKE :name";
        String qStringAll = "FROM Account";
        TypedQuery<Account> q;
        if (name.equals("*")) {
            q = em.createQuery(qStringAll, Account.class);
        } else {
            q = em.createQuery(qStringLike, Account.class);
            q.setParameter("name", name + "%");
        }

        List<Account> accounts = null;
        try {
            accounts = q.getResultList();
            if (accounts == null || accounts.isEmpty()) {
                accounts = null;
            }
        } catch (NoResultException e) {
            //no logging needed for NoResultException
        } finally {
            em.close();
        }
        return accounts;
    }

    @Override
    @Transactional
    public Account addAccount(Account a) {
        try {
            em.persist(a);
        } catch (Exception e) {
            log.error("Couldn't add account", e);
        } finally {
            em.close();
        }
        return a;
    }

    @Override
    @Transactional
    public Account updateAccount(Account a) {
        try {
            a = em.merge(a);
        } catch (Exception e) {
            log.error("Couldn't update account", e);
        } finally {
            em.close();
        }
        return a;
    }

    @Override
    @Transactional
    public Account deleteAccount(Account a) {
        try {
            em.remove(em.merge(a));
        } catch (Exception e) {
            log.error("Couldn't delete account", e);
        } finally {
            em.close();
        }
        return a;
    }

}
