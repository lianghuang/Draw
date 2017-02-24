package com.sectong.service;

import com.google.common.collect.Lists;
import com.sectong.domain.Question;
import com.sectong.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by huangliangliang on 2/19/17.
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getRandomQuesitons(int num){
        List<Question> quesitons= Lists.newArrayList(questionRepository.findAll());
        if(quesitons.size()<=num){
            return quesitons;
        }else{
            Collections.shuffle(quesitons);
            return quesitons.subList(0,num);
        }
    }

    public Question getQuestionById(String questionId){
        return questionRepository.findOne(questionId);
    }
}
