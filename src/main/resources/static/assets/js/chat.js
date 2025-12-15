let chatClient  = null;
let currentReceiver = null;

let currentReceiverKey = null;
let currentReceiverUsername = null;

console.log("CHAT JS LOADED");

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.chatBtn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            openChatPopup(
                btn.getAttribute('data-key'),
                btn.getAttribute('data-username')
            );
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

function openChatPopup(receiverKey, receiverUsername) {

    // Prevent reopening the same chat
    if (
        currentReceiverKey === receiverKey &&
        document.getElementById('chatPopup')
    ) {
        return;
    }

    currentReceiverKey = receiverKey;
    currentReceiverUsername = receiverUsername;

    // Remove old popup if switching users
    const existingPopup = document.getElementById('chatPopup');
    if (existingPopup) {
        existingPopup.remove();
    }

    const container = document.getElementById('chatPopupContainer');

    const popup = document.createElement('div');
    popup.id = 'chatPopup';
    popup.classList.add('chat-popup');

    popup.innerHTML = `
        <div class="chat-header">
            Chat with <b>${receiverUsername}</b>
        </div>
        <div class="chat-messages" id="chatMessages"></div>
        <div class="chat-input">
            <input type="text" id="chatInput" placeholder="Type a message...">
            <button id="chatSendBtn">Send</button>
        </div>
    `;

    container.appendChild(popup);

    // Send button and Enter key
    document.getElementById('chatSendBtn').addEventListener('click', sendMessage);
    document.getElementById('chatInput').addEventListener('keypress', function (e) {
        if (e.key === 'Enter') sendMessage();
    });

    // Load chat history using encrypted key
    loadChatHistory(receiverKey);
}

function sendMessage() {
    const content = document.getElementById('chatInput').value.trim();
    if (!content) return;

    chatClient.send('/app/chat.send', {}, JSON.stringify({
        receiverKey: currentReceiverKey,
        content: content
    }));

    displayMessage({
        sender: 'me',
        content: content
    });
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

function loadChatHistory(receiverKey) {

    $('#chatMessages').empty();

    $.get('/chat/history/' + receiverKey, function (messages) {

        messages.forEach(msg => {
            displayMessage(msg);
        });
    });
}




