var canvas;
var ctx;
$(function () {

    canvas = jQuery("#canvas")[0];
    ctx = canvas.getContext('2d');
    var msg = document.getElementById('msg');
    var ranger = document.getElementById('ranger');
    var colors = document.getElementById('colors');

    var input = document.getElementById('input-msg');
    var users = document.getElementById('div-users');
    // var btnIn = document.getElementById('btn-in');
    var info = document.getElementById('info');
    // btnIn.inAct = function () {
    //     this.innerText = '准备';
    //     this.in = true;
    // };
    // btnIn.outAct = function () {
    //     this.innerText = '取消准备';
    //     this.in = false;
    //     this.disabled = false;
    // };

    // info.time = info.querySelector('#time')
    // info.player = info.querySelector('#player')
    // info.word = info.querySelector('#word')

// btnIn.addEventListener('click',function () {
//     var t = this.in;
//     if(this.t) clearTimeout(this.t);
//     this.t = setTimeout(function () {
//         stompClient.send("/user/action", {},!t?'in':'out');
//     },400);
// })
    $("#clickisMe").click(function () {
        canvas.isMe = true;
    });

    Ctl.init();

    // input.onkeydown = function (e) {
    //     if (e.keyCode === 13 && this.value != '') {
    //         if (canvas.isMe) {
    //             alert('绘图者不能够发送消息！');
    //             return;
    //         }
    //         this.value = '';
    //     }
    // }
    document.querySelector('#btns').addEventListener('click', function (e) {
        if (e.target.classList.contains('btn-active-able')) {
            if (this.prevBtn) {
                this.prevBtn.classList.remove('active')
            }
            e.target.classList.add('active')
            this.prevBtn = e.target;
        }
    }, true);


    canvas.addEventListener('mousemove', function (e) {
        var w = 20, h = 20;
        if (canvas.isMe) {
            var x = e.offsetX, y = e.offsetY;
            if (e.buttons === 1) {
                if (!this.erase) {
                    Ctl.addPos(x, y);
                    Ctl.drawPts(ctx, this.pts);
                    stompClient.send("/draw/paint", {}, JSON.stringify(new Path(this.pts)));
                    // socket.emit('paint',JSON.stringify({data:new Path(this.pts),status:'ing'}))
                } else {
                    var rect = new Rect(x - (w >>> 1), y - (h >>> 1), w, h);
                    rect.clearOn(ctx);
                    stompClient.send("/draw/erase", {}, rect.x, rect.y, rect.w, rect.h);
                    // socket.emit('erase',rect.x,rect.y,rect.w,rect.h);
                }
            }
        }
    });

    canvas.addEventListener('mouseup', function (e) {
        if (!canvas.isMe || this.erase) return;
        var x = e.offsetX, y = e.offsetY;
        Ctl.addPos(x, y);
        Ctl.addPath(this.pts);
        stompClient.send("/draw/paint", {}, JSON.stringify({data: new Path(this.pts), status: 'end'}));
        // socket.emit('paint',JSON.stringify({data:new Path(this.pts),status:'end'}));
        Ctl.clearPos();

    })

    canvas.addEventListener('mousedown', function (e) {
        if (!this.isMe) return;
        if (this.erase) {
            var w = 20, h = 20;
            var rect = new Rect(x - (w >>> 1), y - (h >>> 1), w, h);
            rect.clearOn(ctx);
            stompClient.send("/draw/erase", {}, rect.x, rect.y, rect.w, rect.h);
            // socket.emit('erase',rect.x,rect.y,rect.w,rect.h);
            return;
        }
        var x = e.offsetX, y = e.offsetY;
        Ctl.clearPos();
        Ctl.addPos(x, y);
    });
    colors.addEventListener('click', function (e) {
        var t = e.target;
        if (t.classList.contains('rect')) {
            Array.prototype.slice.call(this.getElementsByClassName('active'))
                .forEach(function (v) {
                    v.classList.remove('active')
                });
            t.classList.add('active');
            Ctl.setColor(t.style.backgroundColor);
        }
    });
    ranger.addEventListener('change', function (e) {
        this.nextElementSibling.innerText = this.value;
        Ctl.setLw(this.value);
    });
});
// Controller
    Ctl = {
        drawPts: function (ctx, pts) {
            if (pts instanceof Path || pts.pts) {
                var color = pts.color, lw = pts.lw;
                pts = pts.pts;
            }
            var p1 = pts[0];
            ctx.save();
            ctx.beginPath();
            ctx.moveTo(p1.x, p1.y);
            pts.slice(1).forEach(function (v) {
                ctx.lineTo(v.x, v.y);
            });
            ctx.lineWidth = lw || canvas.lw
            ctx.strokeStyle = color || canvas.color;
            ctx.stroke();
            ctx.restore();
        },
        init: function () {
            canvas.paths = [];
            canvas.pts = [];
            canvas.color = 'black';
            canvas.lw = 1;
            for (var i = 0; i < 20; i++)
                this.addColor();
        },
        setLw: function (lw) {
            canvas.lw = lw;
        },
        setColor: function (c) {
            canvas.color = c;
        },
        addPath: function (pts) {
            canvas.paths.push(new Path(pts, canvas.lw, canvas.color));
        },
        addPos: function (x, y) {
            canvas.pts.push(new Pos(x, y));
        },
        clearPos: function () {
            canvas.pts = []
        },
        addColor: function (active) {
            var rect = document.createElement('div'), r = this.random;
            rect.className = 'rect';
            if (active)
                rect.className += ' active';
            rect.style.backgroundColor = 'rgb(' + [r(256), r(256), r(256)].join(',') + ')';
            colors.appendChild(rect);
        },
        random: function (b) {
            return Math.floor(Math.random() * b);
        }
    };

// model

    function Pos(x, y) {
        this.x = x;
        this.y = y;
    }

    function Path(pts, lw, color) {
        this.pts = pts;
        this.lw = lw || canvas.lw;
        this.color = color || canvas.color;
    }

    function Rect(x, y, w, h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }


    Rect.prototype.clearOn = function (ctx) {
        ctx.clearRect(this.x, this.y, this.w, this.h);
    }
