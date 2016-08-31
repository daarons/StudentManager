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
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author David
 */
@Entity
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "session", nullable = false)
    private Session session;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "grade", column = @Column(name = "fluencyAndCoherenceGrade")),
        @AttributeOverride(name = "comment", column = @Column(name = "fluencyAndCoherenceComment"))
    })
    private ReviewSection fluencyAndCoherence;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "grade", column = @Column(name = "vocabularyGrade")),
        @AttributeOverride(name = "comment", column = @Column(name = "vocabularyComment"))
    })
    private ReviewSection vocabulary;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "grade", column = @Column(name = "grammarGrade")),
        @AttributeOverride(name = "comment", column = @Column(name = "grammarComment"))
    })
    private ReviewSection grammar;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "grade", column = @Column(name = "pronunciationGrade")),
        @AttributeOverride(name = "comment", column = @Column(name = "pronunciationComment"))
    })
    private ReviewSection pronunciation;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "grade", column = @Column(name = "interactionAndEngagementGrade")),
        @AttributeOverride(name = "comment", column = @Column(name = "interactionAndEngagementComment"))
    })
    private ReviewSection interactionAndEngagement;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "grade", column = @Column(name = "communicationSkillsGrade")),
        @AttributeOverride(name = "comment", column = @Column(name = "communicationSkillsComment"))
    })
    private ReviewSection communicationSkills;
    
    public Review(){}

    public Review(Session session, ReviewSection fluencyAndCoherence,
            ReviewSection vocabulary, ReviewSection grammar,
            ReviewSection pronunciation, ReviewSection interactionAndEngagement,
            ReviewSection communicationSkills) {
        this.session = session;
        this.fluencyAndCoherence = fluencyAndCoherence;
        this.vocabulary = vocabulary;
        this.grammar = grammar;
        this.pronunciation = pronunciation;
        this.interactionAndEngagement = interactionAndEngagement;
        this.communicationSkills = communicationSkills;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * @return the fluencyAndCoherence
     */
    public ReviewSection getFluencyAndCoherence() {
        return fluencyAndCoherence;
    }

    /**
     * @param fluencyAndCoherence the fluencyAndCoherence to set
     */
    public void setFluencyAndCoherence(ReviewSection fluencyAndCoherence) {
        this.fluencyAndCoherence = fluencyAndCoherence;
    }

    /**
     * @return the vocabulary
     */
    public ReviewSection getVocabulary() {
        return vocabulary;
    }

    /**
     * @param vocabulary the vocabulary to set
     */
    public void setVocabulary(ReviewSection vocabulary) {
        this.vocabulary = vocabulary;
    }

    /**
     * @return the grammar
     */
    public ReviewSection getGrammar() {
        return grammar;
    }

    /**
     * @param grammar the grammar to set
     */
    public void setGrammar(ReviewSection grammar) {
        this.grammar = grammar;
    }

    /**
     * @return the pronunciation
     */
    public ReviewSection getPronunciation() {
        return pronunciation;
    }

    /**
     * @param pronunciation the pronunciation to set
     */
    public void setPronunciation(ReviewSection pronunciation) {
        this.pronunciation = pronunciation;
    }

    /**
     * @return the interactionAndEngagement
     */
    public ReviewSection getInteractionAndEngagement() {
        return interactionAndEngagement;
    }

    /**
     * @param interactionAndEngagement the interactionAndEngagement to set
     */
    public void setInteractionAndEngagement(ReviewSection interactionAndEngagement) {
        this.interactionAndEngagement = interactionAndEngagement;
    }

    /**
     * @return the communicationSkills
     */
    public ReviewSection getCommunicationSkills() {
        return communicationSkills;
    }

    /**
     * @param communicationSkills the communicationSkills to set
     */
    public void setCommunicationSkills(ReviewSection communicationSkills) {
        this.communicationSkills = communicationSkills;
    }
}
