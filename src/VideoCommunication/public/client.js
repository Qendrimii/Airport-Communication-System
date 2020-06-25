// marrja e DOM elementeve 
var divSelectRoom = document.getElementById("selectRoom");
var divConsultingRoom = document.getElementById("consultingRoom");
var inputRoomNumber = document.getElementById("roomNumber");
var btnGoRoom = document.getElementById("goRoom");
var btnStopCall = document.getElementById("stopCall");
var localVideo = document.getElementById("localVideo");
var remoteVideo = document.getElementById("remoteVideo");

// variablat per ruajtje
var roomNumber;
var localStream;
var remoteStream;
var rtcPeerConnection;

//specifikimi i nje STUN serveri per tu siguruar
//qe lidhja ne P2P do te behet
var iceServers = {
    'iceServers': [
        { 'urls': 'stun:stun.services.mozilla.com' },
        { 'urls': 'stun:stun.l.google.com:19302' }
    ]
}

var streamConstraints = { audio: true, video: true };
var isCaller;

// Thirrja e librarise per komunikim ne web ne kohe reale
var socket = io();

//mbajtesi i eventit te klikimit te butonit
btnGoRoom.onclick = function() {
    if (inputRoomNumber.value === '') {
        alert("Shtyp numrin e dhomes")
    } else {
        roomNumber = inputRoomNumber.value;
        socket.emit('create or join', roomNumber);
        divSelectRoom.style = "display: none;";
        divConsultingRoom.style = "display: block;";
    }
};

// mbajtesit e mesazheve

//kur serveri lejon krijimin e dhomes
socket.on('created', function(room) {
    navigator.mediaDevices.getUserMedia(streamConstraints).then(function(stream) {
        localStream = stream;
        localVideo.srcObject = stream;
        isCaller = true;
    }).catch(function(err) {
        console.log('An error ocurred when accessing media devices', err);
    });
});

//kur serveri lejon nje join event tek klienti
socket.on('joined', function(room) {
    navigator.mediaDevices.getUserMedia(streamConstraints).then(function(stream) {
        localStream = stream;
        localVideo.srcObject = stream;
        socket.emit('ready', roomNumber);
    }).catch(function(err) {
        console.log('An error ocurred when accessing media devices', err);
    });
});

//pasi pranohet mesazhi ready dergohet objekti tek
//kandidatet per stream
socket.on('candidate', function(event) {
    var candidate = new RTCIceCandidate({
        sdpMLineIndex: event.label,
        candidate: event.candidate
    });
    rtcPeerConnection.addIceCandidate(candidate);
});
//pasi qe klienti te behet gati nga hapi -joined-
//dergon tek serveri mesazhin ready
socket.on('ready', function() {
    if (isCaller) {
        rtcPeerConnection = new RTCPeerConnection(iceServers);
        rtcPeerConnection.onicecandidate = onIceCandidate;
        rtcPeerConnection.ontrack = onAddStream;
        rtcPeerConnection.addTrack(localStream.getTracks()[0], localStream);
        rtcPeerConnection.addTrack(localStream.getTracks()[1], localStream);
        rtcPeerConnection.createOffer()
            .then(sessionDescription => {
                rtcPeerConnection.setLocalDescription(sessionDescription);
                socket.emit('offer', {
                    type: 'offer',
                    sdp: sessionDescription,
                    room: roomNumber
                });
            })
            .catch(error => {
                console.log(error)
            })
    }
});

//ritransmetimi i ofertes(per stream) tek kandidati
//i dyte
socket.on('offer', function(event) {
    if (!isCaller) {
        rtcPeerConnection = new RTCPeerConnection(iceServers);
        rtcPeerConnection.onicecandidate = onIceCandidate;
        rtcPeerConnection.ontrack = onAddStream;
        rtcPeerConnection.addTrack(localStream.getTracks()[0], localStream);
        rtcPeerConnection.addTrack(localStream.getTracks()[1], localStream);
        rtcPeerConnection.setRemoteDescription(new RTCSessionDescription(event));
        rtcPeerConnection.createAnswer()
            .then(sessionDescription => {
                rtcPeerConnection.setLocalDescription(sessionDescription);
                socket.emit('answer', {
                    type: 'answer',
                    sdp: sessionDescription,
                    room: roomNumber
                });
            })
            .catch(error => {
                console.log(error)
            })
    }
});

//kthimi i pergjigjes nga serveri
socket.on('answer', function(event) {
    rtcPeerConnection.setRemoteDescription(new RTCSessionDescription(event));
})

//EventHandler qe specifikon nje funksion qe duhet
//thirrur kur eventi ndodh
function onIceCandidate(event) {
    if (event.candidate) {
        console.log('sending ice candidate');
        socket.emit('candidate', {
            type: 'candidate',
            label: event.candidate.sdpMLineIndex,
            id: event.candidate.sdpMid,
            candidate: event.candidate.candidate,
            room: roomNumber
        })
    }
}

//kur cdo gje behet gati secila nyje pranon nje
//remotestream dhe fubksioni onAddStream e paraqet
//ate ne dritare
function onAddStream(event) {
    remoteVideo.srcObject = event.streams[0];
    remoteStream = event.stream;
}

//ruan oferten dhe e dergon mesazhin te serveri
function setLocalAndOffer(sessionDescription) {
    rtcPeerConnection.setLocalDescription(sessionDescription);
    socket.emit('offer', {
        type: 'offer',
        sdp: sessionDescription,
        room: roomNumber

    });
}

//ruan pergjigjen dhe e dergon mesazhin te serveri
function setLoalAndAnswer(sessionDescription) {
    rtcPeerConnection.setLocalDescription(sessionDescription);
    socket.emit('answer', {
        type: 'answer',
        sdp: sessionDescription,
        room: roomNumber
    });
}