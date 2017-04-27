package com.sectong.service;

import com.google.common.collect.Lists;
import com.sectong.domain.Question;
import com.sectong.domain.Room;
import com.sectong.repository.QuestionRepository;
import com.sectong.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by huangliangliang on 2/19/17.
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private RoomRepository roomRepository;

    private int score=3;
    public List<Question> getRandomQuesitons(String roomId,int num){
        Room room=roomRepository.findOne(roomId);
        List<Question> quesitons= Lists.newArrayList(questionRepository.findAll());
        List<Question> roomQuestions=room.getQuestions();
        quesitons.removeAll(roomQuestions);
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

    public boolean addQuestion(String name,String key1,String key2){
        if(questionRepository.findQuestionByNameAndKey1Key2(name,key1,key2)!=null){
            return false;
        }
        Question question=new Question();
        question.setId(UUID.randomUUID().toString().replaceAll("-",""));
        question.setQuestion(name);
        question.setKeyword1(key1);
        question.setKeyword2(key2);
        question.setScore(score);
        questionRepository.save(question);
        return true;
    }
}
