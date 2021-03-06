package com.sectong.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangliangliang on 2/15/17.
 */
@Entity
@Table(name = "rooms", uniqueConstraints = { @UniqueConstraint(columnNames = { "roomId" }) })
public class Room {
    public enum Stage{
        /**
         * 房间初始状态，游戏未开始前的状态
         */
        Ready,
        /**
         * 自定义房间第二个场景的状态
         */
        Start,
        /**
         * 代表第二个房间开始游戏的状态
         */
        Gaming
    }
    public enum RoomType{
        /**
         * 随机房间类型
         */
        Random,
        /**
         * 自定义房间类型
         */
        Specified
    }
    //最大用户数，未传递给前台
    public static final int maxUserNum=6;
    //最小游戏开始人数，未传递给前台
    public static final  int  minGameUserNum=3;
    //默认等待开始时间,未传递给前台
    public static final int timeToStart=10;

    /**
     * 房间ID
     */
    @Id
    private String roomId;

    /**
     * 房间类型，见RoomType
     */
    @Enumerated(EnumType.STRING)
    private RoomType type=RoomType.Random;

    /**
     * 房间阶段，见Stage
     */
    @Enumerated(EnumType.STRING)
    private Stage stage=Stage.Ready;

    /**
     * 房主的username
     */
    private String roomOwnerName;

    /**
     * 房间内目前人数
     */
    private Integer nowUserNum=0;

    /**
     * 每一局的问题数量
     */
    private Integer questionNum=5;

    /**
     * 房间内用户列表
     */
    @OneToMany
    @JoinTable(
            name = "ref_room_user",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> addedUserList;//已加入用户列表

    /**
     * 房间内的问题列表
     */
    @ManyToMany
    @JoinTable(
            name = "ref_room_question",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions;//问题列表

    /**
     * 当前正在玩的问题
     */
    @OneToOne
    @JoinColumn(name="current_question", referencedColumnName="id")
    private Question currentQuestion;

    /**
     * 数据库乐观锁，请忽略
     */
    @Version
    private Integer version;

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getNowUserNum() {
        return nowUserNum;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public List<User> getAddedUserList() {
        return addedUserList;
    }

    public void setAddedUserList(List<User> addedUserList) {
        this.addedUserList = addedUserList;
    }

    public String getRoomOwnerName() {
        return roomOwnerName;
    }

    public void setRoomOwnerName(String roomOwnerName) {
        this.roomOwnerName = roomOwnerName;
    }

    public void addUser(User user){
        if(CollectionUtils.isEmpty(addedUserList)){
            addedUserList=new ArrayList<>();
            roomOwnerName=user.getUsername();
        }
        if(addedUserList.size()<maxUserNum){
            addedUserList.add(user);
            nowUserNum=addedUserList.size();

        }
    }

    public void removeUser(User user){
        if(CollectionUtils.isEmpty(addedUserList)){
            return;
        }
        if(addedUserList.contains(user)){
            addedUserList.remove(user);
            nowUserNum=addedUserList.size();
        }
        if(roomOwnerName.equals(user.getUsername())){
            if(!CollectionUtils.isEmpty(addedUserList)){
                roomOwnerName=addedUserList.get(0).getUsername();
            }else{
                roomOwnerName=null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        return roomId.equals(room.roomId);
    }

    @Override
    public int hashCode() {
        return roomId.hashCode();
    }
}
