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
let chatMode = null; // "page" | "popup"
let messageSubscription = null;

let loggedInUsername = document.body.dataset.username;

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
    chatClient.debug = console.log;

    chatClient.connect({}, function (frame) {

        console.log("CONNECTED OK", frame);

        //  SUBSCRIBE ONCE
        if (!messageSubscription) {
            messageSubscription = chatClient.subscribe(
                '/user/queue/messages',
                onMessageReceived
            );
        }

        // Online users (broadcast)
        chatClient.subscribe('/topic/online-users', msg => {
            renderOnlineUsers(JSON.parse(msg.body));
        });

        // Initial sync
        chatClient.subscribe('/user/queue/online-users', msg => {
            renderOnlineUsers(JSON.parse(msg.body));
        });

    }, error => console.error(error));
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

function onMessageReceived(message) {

    const msg = JSON.parse(message.body);
    const sender = msg.sender || msg.senderUsername;

    // Show message ONLY in active chat
    if (chatMode === "popup" && sender === currentReceiverUsername) {
        appendMessage('popupChatMessages', msg);
    }

    if (chatMode === "page" && sender === currentReceiverUsername) {
        appendMessage('chatMessages', msg);
    }

    //  DO NOT auto-open popup here
}


/* =========================
   POPUP CHAT
========================= */
function openChatPopup(username) {

    chatMode = "popup";
    currentReceiverUsername = username;

    document.getElementById('chatPopup')?.remove();

    const container = document.getElementById('chatPopupContainer');
    if (!container) return;

    const popup = document.createElement('div');
    popup.id = 'chatPopup';
    popup.className = 'chat-popup';

    popup.innerHTML = `
        <div class="chat-header">
            <span>Chat with <b>${username}</b></span>
            <div class="chat-actions">
                <button class="chat-minimize">−</button>
                <button class="chat-close">×</button>
            </div>
        </div>
        <div class="chat-body">
            <div class="chat-messages" id="popupChatMessages"></div>
            <div class="chat-input">
                <input id="popupChatInput" placeholder="Type a message...">
                <button id="popupChatSendBtn">Send</button>
            </div>
        </div>
    `;

    container.appendChild(popup);

    popup.querySelector('.chat-close').onclick = () => popup.remove();
    popup.querySelector('.chat-minimize').onclick = () => popup.classList.toggle('minimized');

    loadChatHistory(username, 'popupChatMessages');
}


/* =========================
   SEND MESSAGE
========================= */
function sendMessage(source) {

    if (!chatClient?.connected) return;

    const inputId = source === 'popup'
        ? 'popupChatInput'
        : 'chatInput';

    const input = document.getElementById(inputId);
    if (!input) return;

    const content = input.value.trim();
    if (!content) return;

    chatClient.send('/app/chat.send', {}, JSON.stringify({
        receiver: currentReceiverUsername,
        content: content
    }));

    appendMessage(
        source === 'popup' ? 'popupChatMessages' : 'chatMessages',
        { sender: loggedInUsername, content }
    );

    input.value = '';

    document.addEventListener('click', e => {
        if (e.target.id === 'popupChatSendBtn') sendMessage('popup');
        if (e.target.id === 'chatSendBtn') sendMessage('page');
    });
}


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

function appendMessage(containerId, message) {

    const box = document.getElementById(containerId);
    if (!box) return;

    const div = document.createElement('div');
    const sender = message.sender || message.senderUsername;

    div.className = sender === loggedInUsername ? 'sent' : 'received';
    div.textContent = `${sender}: ${message.content}`;

    box.appendChild(div);
    box.scrollTop = box.scrollHeight;
}

/* =========================
   LOAD CHAT HISTORY
========================= */
function loadChatHistory(username, targetId) {

    const box = document.getElementById(targetId);
    if (!box) return;

    box.innerHTML = '';

    $.get('/chat/history/' + username, messages => {
        messages.forEach(m => appendMessage(targetId, m));
    });
}


/* =========================
   IN-PAGE CHAT
========================= */

function openChatInPage(username) {

    chatMode = "page";
    currentReceiverUsername = username;

    document.getElementById('chatWith').textContent = username;
    document.getElementById('chatInput').disabled = false;
    document.getElementById('chatSendBtn').disabled = false;

    loadChatHistory(username, 'chatMessages');
}


