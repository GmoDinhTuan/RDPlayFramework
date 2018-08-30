@(name: String, url: String)
var $messages = $("#messages");
var $send = $("#send");
var  $message = $("#message");
var  $myFile = $("#myFile");
var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
var connection = new WS("@url");
$send.prop("disabled", true);
var send = function (name) {
    var text = name + $message.val();
    $message.val("");
    connection.send(text);
};

var sendFile = function () {
    var file = $myFile.val();
    $myFile.val("");
    connection.send(file);
};
connection.onopen = function () {
    $send.prop("disabled", false);
    $messages.prepend($("<li style='font-size: 1.5em; background: #f0f0f0;'>Connected</li>"));
    $send.on('click', send);
    $message.keypress(function(event){
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            send('@name :');
        }
    });
    $send.click(function(event){

    	var data = new FormData();
    	jQuery.each(jQuery('#myFile')[0].files, function(i, file) {
    	    data.append('files', file);
    	});
    	$.ajax({
    	    url: '/writeFile',
    	    data: data,
    	    cache: false,
    	    contentType: false,
    	    processData: false,
    	    method: 'POST',
    	    type: 'POST',
    	    success: function(data){
    	    	sendFile();
    	    },
    	    error:function(e){
            	console.log(e);
    	    }
    	});
    });
};
connection.onerror = function (error) {
    console.log('WebSocket Error ', error);
};
connection.onmessage = function (event) {
	if(connection.url == '@url') {
		console.log(event.currentTarget);
		var data = event.data.toString();
	    if(data != "") {
	    	if(data.indexOf("C:") != -1) {
	            $messages.append($("<a href='/file/" + data.substr(12, 100) + "'><li style='font-size: 1em; color: #aad4ff'>" + data.substr(12, 100) + "</li></a>"))
	        } else {
	            $messages.append($("<li style='font-size: 1em; color: #000000'>" + data + "</li>"))
	        }
	    }
	}
}