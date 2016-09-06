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
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        Account account = em.find(Account.class, id);
        em.close();
        return account;
    }

    @Override
    public List<Account> getAccounts(String name) {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
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
        } finally {
            em.close();
        }
        return accounts;
    }

    @Override
    public Account addAccount(Account a) {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.persist(a);
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
        } finally {
            em.close();
        }
        return a;
    }

    @Override
    public Account updateAccount(Account a) {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            a = em.merge(a);
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
        } finally {
            em.close();
        }
        return a;
    }

    @Override
    public Account deleteAccount(Account a) {
        EntityManager em = EMFSingleton.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.remove(em.merge(a));
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
        } finally {
            em.close();
        }
        return a;
    }

}
