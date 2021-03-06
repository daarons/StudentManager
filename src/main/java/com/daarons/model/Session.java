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
package com.daarons.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author David
 */
@Entity
public class Session implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long sessionId;
    @ManyToOne
    @JoinColumn(name="student")
    private Student student;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @OneToOne(fetch=FetchType.LAZY, mappedBy="session", cascade=CascadeType.ALL, orphanRemoval=true)
    private Note note;
    @OneToOne(fetch=FetchType.LAZY, mappedBy="session", cascade=CascadeType.ALL, orphanRemoval=true)
    private Review review;
    
    public Session(){
        this.student = null;
        this.timestamp = null;
        this.note = new Note();
        this.review = new Review();
        this.note.setSession(this);
        this.review.setSession(this);
    }
    
    public Session(Student student, Date timestamp){
        this.student = student;
        this.timestamp = timestamp;
        this.note = new Note();
        this.review = new Review();
        this.note.setSession(this);
        this.review.setSession(this);
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the note
     */
    public Note getNote() {
        return note;
    }

    /**
     * @param note the notes to set
     */
    public void setNote(Note note) {
        this.note = note;
    }

    /**
     * @return the review
     */
    public Review getReview() {
        return review;
    }

    /**
     * @param review the review to set
     */
    public void setReview(Review review) {
        this.review = review;
    }

    /**
     * @return the sessionId
     */
    public long getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
}
