package com.sectong.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by huangliangliang on 2/19/17.
 */
@Entity
public class Question {
    /**
     * 问题主键ID
     */
    @Id
    private String id;

    /**
     * 分数
     */
    private int score;
    /**
     * 问题答案
     */
    private String question;

    /**
     * 提示信息1
     */
    private String keyword1;

    /**
     * 提示信息2
     */
    private String keyword2;

    /**
     * 提示信息3
     */
    private String keyword3;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }

    public String getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }

    public String getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return id != null ? id.equals(question.id) : question.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
