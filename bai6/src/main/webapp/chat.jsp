<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Simple WebSocket Chat</title>
    <style>
        #chatbox { width: 400px; height: 300px; border: 1px solid #ccc; overflow-y: scroll; margin-bottom: 10px; }
        #message { width: 300px; }
    </style>
</head>
<body>
<h2>WebSocket Chat</h2>
<div id="chatbox"></div>
<input type="text" id="message" placeholder="Type a message...">
<button onclick="sendMessage()">Send</button>
<script>
    let ws = new WebSocket((location.protocol === 'https:' ? 'wss://' : 'ws://') + location.host + '/bai6/chat');
    let chatbox = document.getElementById('chatbox');
    ws.onopen = function() {
        let msg = document.createElement('div');
        msg.style.color = 'green';
        msg.textContent = '[WebSocket connected]';
        chatbox.appendChild(msg);
    };
    ws.onerror = function(event) {
        let msg = document.createElement('div');
        msg.style.color = 'red';
        msg.textContent = '[WebSocket error: check server logs and endpoint URL]';
        chatbox.appendChild(msg);
    };
    ws.onclose = function() {
        let msg = document.createElement('div');
        msg.style.color = 'orange';
        msg.textContent = '[WebSocket closed]';
        chatbox.appendChild(msg);
    };
    ws.onmessage = function(event) {
        let msg = document.createElement('div');
        msg.textContent = event.data;
        chatbox.appendChild(msg);
        chatbox.scrollTop = chatbox.scrollHeight;
    };
    function sendMessage() {
        let input = document.getElementById('message');
        if (input.value.trim() !== '') {
            ws.send(input.value);
            input.value = '';
        }
    }
    document.getElementById('message').addEventListener('keydown', function(e) {
        if (e.key === 'Enter') sendMessage();
    });
</script>
</body>
</html>
