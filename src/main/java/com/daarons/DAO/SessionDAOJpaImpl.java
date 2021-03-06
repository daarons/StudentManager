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

import com.daarons.model.Session;
import javax.persistence.*;
import org.apache.logging.log4j.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David
 */
@Repository
public class SessionDAOJpaImpl implements SessionDAO{
    private static final Logger log = LogManager.getLogger(SessionDAOJpaImpl.class);   
    @PersistenceContext
    private EntityManager em;
    
    @Override
    @Transactional(readOnly=true)
    public Session getSessionWithNoteAndReview(long id) {
        Session session = em.find(Session.class, id);
        session.getNote();
        session.getReview();
        return session;
    }

    @Override
    @Transactional
    public Session updateSession(Session session) {
        try{
            session = em.merge(session);
        }catch(Exception e){
            log.error("Couldn't update session", e);
        }
        return session;
    }
    
}
