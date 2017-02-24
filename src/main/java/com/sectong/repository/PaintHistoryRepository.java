package com.sectong.repository;

import com.sectong.domain.PaintHistory;
import com.sectong.domain.Room;
import com.sectong.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangliangliang on 2/19/17.
 */
@Service
public class PaintHistoryRepository {

    private Map<String,List<PaintHistory>> paintHistory =new HashMap<>();

    @Autowired
    private RoomService roomService;

    public void addHistory(String roomId,String history){
        Room room=roomService.findRoomById(roomId);
        String questionId=room.getCurrentQuestion().getId();
        if(paintHistory.containsKey(roomId)){
            List<PaintHistory> paintHistories= paintHistory.get(roomId);
            for(PaintHistory his:paintHistories){
                if(questionId.equals(his.getQuestionId())){
                    his.addHistory(history);
                    return;
                }
            }
            PaintHistory newPaintHistory=new PaintHistory();
            newPaintHistory.addHistory(history);
            newPaintHistory.setQuestionId(questionId);
            paintHistories.add(newPaintHistory);
        }else{
            List<PaintHistory> paintHistories=new ArrayList<>();
            PaintHistory newPaintHistory=new PaintHistory();
            newPaintHistory.addHistory(history);
            newPaintHistory.setQuestionId(questionId);
            paintHistories.add(newPaintHistory);
            paintHistory.put(roomId,paintHistories);
        }
    }

    public void removeHistory(String roomId,String history){
        Room room=roomService.findRoomById(roomId);
        String questionId=room.getCurrentQuestion().getId();
        {
            List<PaintHistory> paintHistories= paintHistory.get(roomId);
            for(PaintHistory his:paintHistories){
                if(questionId.equals(his.getQuestionId())){
                    his.removeHistory(history);
                    return;
                }
            }
        }
    }

    public void destroyRoomHistry(String roomId){
        paintHistory.remove(roomId);
    }

    public List<String> getHistory(String roomId){
        Room room=roomService.findRoomById(roomId);
        String questionId=room.getCurrentQuestion().getId();
        if(paintHistory.containsKey(roomId)){
            List<PaintHistory> paintHistories= paintHistory.get(roomId);
            for(PaintHistory his:paintHistories){
                if(his.getQuestionId().equals(questionId)){
                    return his.getHistorys();
                }
            }
           return  new ArrayList<>();
        }
        return new ArrayList<>();
    }

}
