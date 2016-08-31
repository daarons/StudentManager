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
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author David
 */
@Entity
public class Student implements Serializable {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name="account")
    private Account account;
    private String chineseName;
    private String englishName;
    private int age;
    private String location;
    private String hobbies;
    private String whyLearnEnglish;
    private String otherNotes;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="student", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Session> sessions;
    
    public Student(){}
    
    public Student(Account account, String chineseName, String englishName,
            int age, String location, String hobbies, String whyLearnEnglish,
            String otherNotes){
        this.account = account;
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.age = age;
        this.location = location;
        this.hobbies = hobbies;
        this.whyLearnEnglish = whyLearnEnglish;
        this.otherNotes = otherNotes;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return the chineseName
     */
    public String getChineseName() {
        return chineseName;
    }

    /**
     * @param chineseName the chineseName to set
     */
    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    /**
     * @return the englishName
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * @param englishName the englishName to set
     */
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the hobbies
     */
    public String getHobbies() {
        return hobbies;
    }

    /**
     * @param hobbies the hobbies to set
     */
    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    /**
     * @return the whyLearnEnglish
     */
    public String getWhyLearnEnglish() {
        return whyLearnEnglish;
    }

    /**
     * @param whyLearnEnglish the whyLearnEnglish to set
     */
    public void setWhyLearnEnglish(String whyLearnEnglish) {
        this.whyLearnEnglish = whyLearnEnglish;
    }

    /**
     * @return the otherNotes
     */
    public String getOtherNotes() {
        return otherNotes;
    }

    /**
     * @param otherNotes the otherNotes to set
     */
    public void setOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }

    /**
     * @return the sessions
     */
    public List<Session> getSessions() {
        return sessions;
    }

    /**
     * @param sessions the sessions to set
     */
    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
