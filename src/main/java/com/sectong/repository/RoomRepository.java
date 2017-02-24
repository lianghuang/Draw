package com.sectong.repository;

import com.sectong.domain.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by huangliangliang on 2/15/17.
 */
@RestResource(exported = false)
public interface RoomRepository extends CrudRepository<Room, String> {

    @Query(value = "select * from Rooms where now_user_num<?1 and `type`='Random' order by now_user_num desc limit 0,1", nativeQuery = true)
    Room findMostSuitRoom(Integer num);

    @Query(value = "select room.room_id from Rooms room left join ref_room_user ref on room.room_id=ref.room_id left join users u on u.id=ref.user_id where u.username=?1", nativeQuery = true)
    String findRoomIdByUserName(String userName);

    @Query(value = "delete  from ref_room_user ref where ref.room_id=?1 and ref.user_id=?2", nativeQuery = true)
    int removeRoomAndUserConnection(String roomId,Long userId);
}
