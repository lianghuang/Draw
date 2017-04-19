var SERVER_HOST = "http://112.74.174.121:8080";
var room={};
$(document).ready(function(){
    var username="usher";

});

/**
 * into a random room.
 * @param username
 */
function getRandomRoom(username) {
    var url = SERVER_HOST + "/api/v1/room/into";
    $.get(url, {
        username: username
    }, function (data) {
        if(data.code==0){
            //TODO handle error
            return;
        }
        room = data.content;
        return room;
    });
}
/**
 * leave room
 * @param username
 * @param type  0游客退出,1是创建新房间
 * @param roomId
 */
function leaveRoom(username,type,roomId){
    var url = SERVER_HOST + "/api/v1/room/leave";
    $.get(url, {
        username: username
    }, function (data) {
        
    });
}