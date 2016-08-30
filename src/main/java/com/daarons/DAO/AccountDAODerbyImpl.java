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
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author David
 */
public class AccountDAODerbyImpl implements AccountDAO {

    @Override
    public Account getAccount(long id) {
        EntityManager em = EMSingleton.getEntityManager();
        Account account = em.find(Account.class, id);
        return account;
    }

    @Override
    public List<Account> getAccounts(String name) {
        EntityManager em = EMSingleton.getEntityManager();
        String qString = "SELECT a FROM Account a WHERE a.name LIKE :name";
        TypedQuery<Account> q = em.createQuery(qString, Account.class);
        q.setParameter("name", name + "%");

        List<Account> accounts = null;
        try {
            accounts = q.getResultList();
            if (accounts == null || accounts.isEmpty()) {
                accounts = null;
            }
        } catch (NoResultException e) {
            System.out.println(e);
        }
        return accounts;
    }

    @Override
    public boolean addAccount(Account a) {
        EntityManager em = EMSingleton.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.persist(a);
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateAccount(Account a) {
        EntityManager em = EMSingleton.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.merge(a);
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteAccount(Account a) {
        EntityManager em = EMSingleton.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.remove(em.merge(a));
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
            return false;
        }
        return true;
    }

}
