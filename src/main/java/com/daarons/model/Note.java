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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Type;

/**
 *
 * @author David
 */
@Entity
public class Note implements Serializable {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name="session", nullable=false)
    private Session session;
    @Type(type="text")
    private String fluencyAndCoherence;
    @Type(type="text")
    private String vocabulary;
    @Type(type="text")
    private String grammar;
    @Type(type="text")
    private String pronunciation;
    @Type(type="text")
    private String interactionAndEngagement;
    @Type(type="text")
    private String communicationSkills;
    
    public Note(){
        this.session = null;
        this.fluencyAndCoherence = "";
        this.vocabulary = "";
        this.grammar = "";
        this.pronunciation = "";
        this.interactionAndEngagement = "";
        this.communicationSkills = "";
    }
    
    public Note(Session session, String fluencyAndCoherence, String vocabulary,
            String grammar, String pronunciation, String interactionAndEngagement,
            String communicationSkills){
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
    public String getFluencyAndCoherence() {
        return fluencyAndCoherence;
    }

    /**
     * @param fluencyAndCoherence the fluencyAndCoherence to set
     */
    public void setFluencyAndCoherence(String fluencyAndCoherence) {
        this.fluencyAndCoherence = fluencyAndCoherence;
    }

    /**
     * @return the vocabulary
     */
    public String getVocabulary() {
        return vocabulary;
    }

    /**
     * @param vocabulary the vocabulary to set
     */
    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    /**
     * @return the grammar
     */
    public String getGrammar() {
        return grammar;
    }

    /**
     * @param grammar the grammar to set
     */
    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }

    /**
     * @return the pronunciation
     */
    public String getPronunciation() {
        return pronunciation;
    }

    /**
     * @param pronunciation the pronunciation to set
     */
    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    /**
     * @return the interactionAndEngagement
     */
    public String getInteractionAndEngagement() {
        return interactionAndEngagement;
    }

    /**
     * @param interactionAndEngagement the interactionAndEngagement to set
     */
    public void setInteractionAndEngagement(String interactionAndEngagement) {
        this.interactionAndEngagement = interactionAndEngagement;
    }

    /**
     * @return the communicationSkills
     */
    public String getCommunicationSkills() {
        return communicationSkills;
    }

    /**
     * @param communicationSkills the communicationSkills to set
     */
    public void setCommunicationSkills(String communicationSkills) {
        this.communicationSkills = communicationSkills;
    }
}
