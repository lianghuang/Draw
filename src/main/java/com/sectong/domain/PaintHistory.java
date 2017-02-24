package com.sectong.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangliangliang on 2/22/17.
 */
public class PaintHistory {
    private String questionId;//主键
    private List<String> historys=new ArrayList<>();

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public List<String> getHistorys() {
        return historys;
    }

    public void setHistorys(List<String> historys) {
        this.historys = historys;
    }

    public void addHistory(String history){
        historys.add(history);
    }

    public void removeHistory(String history){
        historys.remove(history);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaintHistory that = (PaintHistory) o;

        return questionId != null ? questionId.equals(that.questionId) : that.questionId == null;
    }

    @Override
    public int hashCode() {
        return questionId != null ? questionId.hashCode() : 0;
    }
}
