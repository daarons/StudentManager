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

/**
 *
 * @author David
 */
public class Review {
    private long id;
    private Session session;
    private ReviewSection fluencyAndCoherence;
    private ReviewSection vocabulary;
    private ReviewSection grammar;
    private ReviewSection pronunciation;
    private ReviewSection interactionAndEngagement;
    private ReviewSection communicationSkills;
    
    public Review(Session session, ReviewSection fluencyAndCoherence,
            ReviewSection vocabulary, ReviewSection grammar, 
            ReviewSection pronunciation, ReviewSection interactionAndEngagement,
            ReviewSection communicationSkills){
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
