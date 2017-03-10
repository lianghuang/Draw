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

/**
 * Created by huangliangliang on 2/19/17.
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private RoomRepository roomRepository;

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
}
