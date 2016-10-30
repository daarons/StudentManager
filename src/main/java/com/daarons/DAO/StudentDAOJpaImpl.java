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

import com.daarons.model.Student;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author David
 */
@Repository
public class StudentDAOJpaImpl implements StudentDAO{
    private static final Logger log = LogManager.getLogger(StudentDAOJpaImpl.class);   
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly=true)
    public Student getStudentWithSessions(long id) {
        Student student = em.find(Student.class, id);
        student.getSessions().size();
        return student;
    }

    @Override
    @Transactional
    public Student updateStudent(Student student) {
        try{
            student = em.merge(student);
        }catch(Exception e){
            log.error("Couldn't update student", e);
        }
        return student;
    }
    
}
