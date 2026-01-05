/*document.addEventListener('DOMContentLoaded', () => {

    console.log("CHAT TEST START");

    const socket = new SockJS('/ws');
    const stomp = Stomp.over(socket);
    stomp.debug = console.log;

    stomp.connect({}, frame => {
        console.log("CONNECTED OK", frame);

        stomp.subscribe('/user/queue/messages', msg => {
            console.log("MESSAGE RECEIVED", msg.body);
        });
    });

});*/


let chatClient = null;
let currentReceiverUsername = null;
let loggedInUsername = document.body.dataset.username; // IMPORTANT
console.log("LOGGED IN USER:", loggedInUsername);
console.log("CHAT JS LOADED");


document.addEventListener('click', function (e) {
    if (e.target && (e.target.id === 'popupChatSendBtn' ||
        e.target.id === 'chatSendBtn')) {
        sendMessage();
    }
});


document.addEventListener('DOMContentLoaded', function () {

    console.log("CHAT TEST START");

    document.querySelectorAll('.chatBtn').forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            openChatPopup(btn.dataset.username);
        });
    });

    connectWebSocket();
});

/* =========================
   WEBSOCKET CONNECTION (FIXED)
========================= */
function connectWebSocket() {

    const socket = new SockJS('/ws');
    chatClient = Stomp.over(socket);

    // DEBUG ENABLED (same as your working test)
    chatClient.debug = console.log;

    chatClient.connect({}, function (frame) {

        console.log("CONNECTED OK", frame);

        /* =========================
           PRIVATE CHAT SUBSCRIPTION
        ========================= */
        chatClient.subscribe('/user/queue/messages', function (message) {
            console.log("MESSAGE RECEIVED", message.body);

            const msg = JSON.parse(message.body);

            if (!document.getElementById('chatPopup')
                || currentReceiverUsername !== msg.sender) {
                openChatPopup(msg.sender);
            }

            displayMessage(msg);
        });

        /* =========================
           ONLINE USERS (BROADCAST)
        ========================= */
        chatClient.subscribe('/topic/online-users', function (message) {
            renderOnlineUsers(JSON.parse(message.body));
        });

    }, function (error) {
        console.error("STOMP CONNECTION ERROR", error);
    });
}

/* =========================
   ONLINE USERS
========================= */
function renderOnlineUsers(users) {

    const list = document.getElementById('onlineUsersList');
    if (!list) return;

    list.innerHTML = "";

    users.forEach(username => {

        if (username === loggedInUsername) return;

        const li = document.createElement("li");
        li.className = "list-group-item chatUser";
        li.innerHTML = `<span class="online-dot"></span>${username}`;

        li.onclick = () => openChatInPage(username);

        list.appendChild(li);
    });
}

/* =========================
   POPUP CHAT
========================= */
function openChatPopup(receiverUsername) {

    if (currentReceiverUsername === receiverUsername &&
        document.getElementById('chatPopup')) return;

    currentReceiverUsername = receiverUsername;

    document.getElementById('chatPopup')?.remove();

    const container = document.getElementById('chatPopupContainer');
    if (!container) return;

    const popup = document.createElement('div');
    popup.id = 'chatPopup';
    popup.className = 'chat-popup';

    popup.innerHTML = `
        <div class="chat-header">Chat with <b>${receiverUsername}</b></div>
        <div class="chat-messages" id="popupChatMessages"></div>
        <div class="chat-input">
            <input type="text" id="popupChatInput" placeholder="Type a message...">
            <button id="popupChatSendBtn">Send</button>
        </div>
    `;

    container.appendChild(popup);


    loadChatHistory(receiverUsername);
}

/* =========================
   SEND MESSAGE
========================= */
function sendMessage() {
   // alert("Hi");
    console.log("SEND BUTTON CLICKED");
    if (!chatClient || !chatClient.connected) {
        console.error("STOMP NOT CONNECTED");
        return;
    }

    const input =
        document.getElementById('popupChatInput') ||
        document.getElementById('chatInput');

    if (!input) {
        console.error("chatInput NOT FOUND");
        return;
    }

    const content = input.value.trim();
    if (!content || !currentReceiverUsername) {
        console.error("Missing content or receiver");
        return;
    }

    chatClient.send('/app/chat.send', {}, JSON.stringify({
        receiver: currentReceiverUsername,
        content: content
    }));
    console.log("MESSAGE SENT TO SERVER");

    displayMessage({
        sender: loggedInUsername,
        content: content
    });

    input.value = '';
}

/* =========================
   DISPLAY MESSAGE
========================= */
/*function displayMessage(message) {

    const messagesDiv =
        document.getElementById('popupChatMessages') ||
        document.getElementById('chatMessages');

    if (!messagesDiv) return;

    const div = document.createElement('div');
    const sender = message.sender || message.senderUsername;

    div.className = sender === loggedInUsername ? 'sent' : 'received';
    div.textContent = `${sender}: ${message.content}`;

    messagesDiv.appendChild(div);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}
*/
function displayMessage(message) {
    ['popupChatMessages', 'chatMessages'].forEach(id => {
        const box = document.getElementById(id);
        if (!box) return;

        const div = document.createElement('div');
        const sender = message.sender || message.senderUsername;

        div.className = sender === loggedInUsername ? 'sent' : 'received';
        div.textContent = `${sender}: ${message.content}`;

        box.appendChild(div);
        box.scrollTop = box.scrollHeight;
    });
}

/* =========================
   LOAD CHAT HISTORY
========================= */
function loadChatHistory(username) {

    $('#chatMessages').empty();

    $.get('/chat/history/' + username, function (messages) {
        messages.forEach(displayMessage);
    });
}

/* =========================
   IN-PAGE CHAT
========================= */
function openChatInPage(receiverUsername) {
   // alert("OPEN PAGE CHAT");
    currentReceiverUsername = receiverUsername;
    document.getElementById('chatWith').textContent = receiverUsername;

    $('#chatMessages').empty();

    document.getElementById('chatInput').disabled = false;
    document.getElementById('chatSendBtn').disabled = false;

    loadChatHistory(receiverUsername);
}

