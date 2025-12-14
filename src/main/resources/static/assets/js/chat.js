let chatClient  = null;
let currentReceiver = null;
console.log("CHAT JS LOADED");

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.chatBtn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            openChatPopup(btn.getAttribute('data-id'));
        });
    });

    connectWebSocket();
});

function connectWebSocket() {
    console.log("CHAT WEBSOCKET CONNECTING...");
    const socket = new SockJS('/ws');
    chatClient  = Stomp.over(socket);
    chatClient .debug = null;

    chatClient .connect({}, function(frame) {
        console.log('Connected: ' + frame);

        chatClient .subscribe('/user/queue/notifications', function(messageOutput) {
            console.log("MESSAGE ARRIVED:", messageOutput.body);  // DEBUG
            const msg = JSON.parse(messageOutput.body);

            // If no popup is open → open with sender’s ID
            if (!document.getElementById('chatPopup')) {
                openChatPopup(msg.sender);
            }

            // If popup is open but chatting with someone else → switch chat
            if (currentReceiver !== msg.sender) {
                openChatPopup(msg.sender);
            }

            // Show the message
            displayMessage(msg);
        });
    });
}

function openChatPopup(receiverId) {
    if (currentReceiver === receiverId && document.getElementById('chatPopup')) {
        return; // Already open with this user
    }

    currentReceiver = receiverId;

    // Remove old popup if switching to a new user
    const existingPopup = document.getElementById('chatPopup');
    if (existingPopup) existingPopup.remove();

    const container = document.getElementById('chatPopupContainer');

    const popup = document.createElement('div');
    popup.id = 'chatPopup';
    popup.classList.add('chat-popup');

    popup.innerHTML = `
        <div class="chat-header">Chat with ${receiverId}</div>
        <div class="chat-messages" id="chatMessages"></div>
        <div class="chat-input">
            <input type="text" id="chatInput" placeholder="Type a message...">
            <button id="chatSendBtn">Send</button>
        </div>
    `;

    container.appendChild(popup);

    // Send button and Enter key
    document.getElementById('chatSendBtn').addEventListener('click', sendMessage);
    document.getElementById('chatInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') sendMessage();
    });

    // ⬅ Load chat history from DB (optional)
    loadChatHistory(receiverId);
}

function sendMessage() {
    const input = document.getElementById('chatInput');
    const content = input.value.trim();
    if (!content || !currentReceiver) return;

    const message = {
        receiverId: currentReceiver,
        content: content
    };

    chatClient.send('/app/chat.send', {}, JSON.stringify(message));
    displayMessage({ sender: 'me', content });

    input.value = '';
}

function displayMessage(message) {
    const messagesDiv = document.getElementById('chatMessages');
    if (!messagesDiv) return;

    const msgDiv = document.createElement('div');
    msgDiv.classList.add(message.sender === 'me' ? 'sent' : 'received');
    msgDiv.textContent = `${message.sender}: ${message.content}`;
    messagesDiv.appendChild(msgDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function loadChatHistory(receiverId) {

    const messagesDiv = document.getElementById('chatMessages');
    if (!messagesDiv) return;

    messagesDiv.innerHTML = '';

    $.get('/chat/history/' + receiverId, function (messages) {

        messages.forEach(function (msg) {

            const isMe = msg.senderId === loggedUserId;
            const msgDiv = document.createElement('div');

            msgDiv.classList.add(isMe ? 'sent' : 'received');
            msgDiv.innerHTML = `
                <b>${msg.senderUsername}</b><br/>
                ${msg.content}
            `;

            messagesDiv.appendChild(msgDiv);
        });

        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    });
}


