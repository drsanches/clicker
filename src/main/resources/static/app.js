var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#clicks").show();
    }
    else {
        $("#clicks").hide();
    }
    $("#application").html("");
}

function connect() {
    var socket = new SockJS('/clicks-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/clicks', function (response) {
            showResponse(JSON.parse(response.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendClicks() {
    stompClient.send("/websocket/click", {}, JSON.stringify({'clicks': $("#clicks").val()}));
}

function showResponse(message) {
    $("#application").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on("submit", function (e) { e.preventDefault(); });
    $("#connect").click(function() { connect(); });
    $("#disconnect").click(function() { disconnect(); });
    $("#send").click(function() { sendClicks(); });
});
