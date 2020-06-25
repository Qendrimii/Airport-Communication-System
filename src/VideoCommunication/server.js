//requires
const express = require('express');
const app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);

const port = process.env.PORT || 3000;

// express routing
app.use(express.static('public'));


// sinjalizimi
io.on('connection', function(socket) {
    console.log('Nje linje airore u lidh');
    //ky funksion shikon se nese dhoma eshte plot ose sa klienta ka
    socket.on('create or join', function(room) {
        console.log('create or join to room ', room);

        var myRoom = io.sockets.adapter.rooms[room] || { length: 0 };
        var numClients = myRoom.length;

        console.log(room, ' ka ', numClients, ' klient');

        //nese klienti eshte i pari dmth ai eka krijuar lidhjen
        //keshtu i dergohet nje event 'created'
        if (numClients == 0) {
            socket.join(room);
            socket.emit('created', room);
        } else if (numClients == 1) {
            socket.join(room); //nese ka nje klient ne dhome, klienti tjeter vetem shtohet ne dhome
            socket.emit('joined', room);
        } else {
            socket.emit('full', room);
        }
    });

    socket.on('ready', function(room) {
        socket.broadcast.to(room).emit('ready');
    });

    socket.on('candidate', function(event) {
        socket.broadcast.to(event.room).emit('candidate', event);
    });

    socket.on('offer', function(event) {
        socket.broadcast.to(event.room).emit('offer', event.sdp);
    });

    socket.on('answer', function(event) {
        socket.broadcast.to(event.room).emit('answer', event.sdp);
    });

    socket.on('stop', function(event) {
        socket.broadcast.to(event.room).emit('stop', event.sdp);
    });
});

// listeneri
http.listen(port || 3000, function() {
    console.log('Hap localhost:', port);
});