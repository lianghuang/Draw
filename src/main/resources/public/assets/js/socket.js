/**
 * Created by huangliangliang on 2/3/17.
 */
var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('http://112.74.174.121:8080/ws');
    stompClient = Stomp.over(socket);
    var myDate = new Date();
    stompClient.connect({token:"token","user-name":myDate.toLocaleTimeString()}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        // stompClient.subscribe('/topic/draw/paths', function (paths) {
        //     paths = JSON.parse(paths);
        //     ctx.clearRect(0,0,canvas.width,canvas.height);
        //     for(var k in paths) {
        //         if(paths[k].tag==='pts')
        //             Ctl.drawPts(ctx, paths[k]);
        //         else{
        //             new Rect(paths[k].x,paths[k].y,paths[k].w,paths[k].h).clearOn(ctx);
        //         }
        //     }
        // });
        stompClient.subscribe('/topic/draw/pts', function (pts) {
            pts = JSON.parse(pts.body);
            if(!pts) return;
            Ctl.drawPts(ctx, pts);
        });
        stompClient.subscribe('/topic/user.login', function (login) {
            var loginEvent=JSON.parse(login.body);
            alert(loginEvent.username+"加入进来了");
        });
        stompClient.subscribe('/topic/user.logout', function (logout) {
            var logoutEvent=JSON.parse(logout.body);
            alert(logoutEvent.username+"离开了");
        });

    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});

//
//
// socket.on('paint paths',function (paths) {
//     paths = JSON.parse(paths);
//     ctx.clearRect(0,0,canvas.width,canvas.height);
//     for(var k in paths) {
//         if(paths[k].tag==='pts')
//             Ctl.drawPts(ctx, paths[k]);
//         else{
//             new Rect(paths[k].x,paths[k].y,paths[k].w,paths[k].h).clearOn(ctx);
//         }
//     }
// });
// socket.on('paint pts',function (pts) {
//     //canvas.paths = paths;
//     pts = JSON.parse(pts)
//     if(!pts) return;
//     Ctl.drawPts(ctx, pts);
// });
//
//
// socket.on('erase',function (x,y,w,h) {
//     new Rect(x,y,w,h).clearOn(ctx);
// })
// socket.on('new in user',function (data) {
//     users.appendChild(utils.makeUserP(JSON.parse(data)));
// });
// socket.on('out user',function (id) {
//     var x = users.querySelector('#p'+id);
//     if(x) x.outerHTML='';
// })
// socket.on('in',function (data) {
//     users.appendChild(utils.makeUserP(JSON.parse(data)));
//     users.scrollTop = users.scrollHeight;
//     btnIn.inAct();
// });
// socket.on('out',function (id) {
//     var x = users.querySelector('#p'+id);
//     if(x){
//         x.outerHTML='';
//         btnIn.outAct();
//     }
// });
//
// socket.on('mytime',function (data) {
//     data = JSON.parse(data);// name,word:,time
//     btnIn.disabled = true;
//     info.player.innerText = data.name + '(自己)';
//     info.time.innerText = data.time +'s';
//     info.word.innerText = data.word;
//     canvas.isMe = true;
// });
// socket.on('othertime',function (data) {
//     data = JSON.parse(data);// name,word:,time
//     info.player.innerText = data.name;
//     info.time.innerText = data.time +'s';
//     canvas.isMe = false;
// });
// socket.on('update time',function (data) {
//     data = JSON.parse(data);
//     info.player.innerText = data.name;
//     info.time.innerText = data.time +'s';
//     info.word.innerText = data.word;
// });
// socket.on('update my time',function (data) {
//     data = JSON.parse(data);
//     info.time.innerText = data.time +'s';
// });
// socket.on('mytimeout',function (id) {
//     var t = users.querySelector('#p'+id);
//     if(t) t.outerHTML='';
//     info.time.innerText = '时间到了！';
//     canvas.isMe = false;
//     btnIn.outAct();
// });
// socket.on('timeout',function (d) {
//     d = JSON.parse(d);
//     var t = users.querySelector('#p'+d.id);
//     if(t) t.outerHTML='';
//     info.time.innerText = '时间到了！';
//     info.word.innerText = '正确答案为：'+d.word;
// });
// socket.on('clear paint',function () {
//     ctx.clearRect(0,0,ctx.canvas.width,ctx.canvas.height);
// });
// socket.on('tops',function (d) {
//     d = JSON.parse(d);
//     tops.innerHTML = '';
//     var temp = tops.template;
//     d.forEach((x,i)=>{
//         temp.id = x.id;
//     temp.children[0].firstElementChild.innerText = 'No'+(i+1);
//     temp.children[1].firstElementChild.innerText = x.name;
//     temp.children[2].firstElementChild.innerText = x.v+'次';
//
//     var node = tops.template.cloneNode(true);
//     node.removeAttribute('role');
//     tops.appendChild(node);
// });
// })
//
// utils = {
//     makeUserP : function (x) {
//         var p = document.createElement('p'); p.id = 'p'+x.id;
//         p.innerText = x.name;
//         return p;
//     }
// }
