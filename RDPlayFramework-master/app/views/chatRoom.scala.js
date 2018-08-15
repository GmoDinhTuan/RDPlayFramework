@(name: String, url: String)
var $messages = $("#messages");
var $send = $("#send");
var  $message = $("#message");
var connection = new WebSocket("@url");
$send.prop("disabled", true);
var send = function () {
    var text = $message.val();
    $message.val("");
    connection.send(text);
};
connection.onopen = function () {
    $send.prop("disabled", false);
    $messages.prepend($("<li style='font-size: 1.5em; background: #f0f0f0;'>Connected</li>"));
    $send.on('click', send);
    $message.keypress(function(event){
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            send();
        }
    });
};
connection.onerror = function (error) {
    console.log('WebSocket Error ', error);
};
connection.onmessage = function (event) {
    $messages.append($("<li style='font-size: 1.5em'>" + "@name" + " : " + event.data + "</li>"))
}