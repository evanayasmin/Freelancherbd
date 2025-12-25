let chatClient = null;
let currentReceiverUsername = null;

console.log("CHAT JS LOADED");

document.addEventListener('DOMContentLoaded', function () {

    document.querySelectorAll('.chatBtn').forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            openChatPopup(
                btn.getAttribute('data-username')
            );
        });
    });

    connectWebSocket();
});

function connectWebSocket() {

    const socket = new SockJS('/ws');
    chatClient = Stomp.over(socket);
    chatClient.debug = null;

    chatClient.connect({}, function () {

        chatClient.subscribe('/user/queue/notifications', function (message) {
            const msg = JSON.parse(message.body);

            if (!document.getElementById('chatPopup')
                || currentReceiverUsername !== msg.sender) {
                openChatPopup(msg.sender);
            }

            displayMessage(msg);
        });
    });
}

function openChatPopup(receiverUsername) {

    if (currentReceiverUsername === receiverUsername &&
        document.getElementById('chatPopup')) {
        return;
    }

    currentReceiverUsername = receiverUsername;

    const oldPopup = document.getElementById('chatPopup');
    if (oldPopup) oldPopup.remove();

    const container = document.getElementById('chatPopupContainer');

    const popup = document.createElement('div');
    popup.id = 'chatPopup';
    popup.className = 'chat-popup';

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

    document.getElementById('chatSendBtn').onclick = sendMessage;
    document.getElementById('chatInput').onkeypress = e => {
        if (e.key === 'Enter') sendMessage();
    };

    loadChatHistory(receiverUsername);
}

function sendMessage() {

    const input = document.getElementById('chatInput');
    const content = input.value.trim();
    if (!content) return;

    chatClient.send('/app/chat.send', {}, JSON.stringify({
        receiverUsername: currentReceiverUsername,
        content: content
    }));

    displayMessage({
        sender: 'me',
        content: content
    });

    input.value = '';
}

function displayMessage(message) {

    const messagesDiv = document.getElementById('chatMessages');
    if (!messagesDiv) return;

    const div = document.createElement('div');
    div.className = message.sender === 'me' ? 'sent' : 'received';
    div.textContent = `${message.sender}: ${message.content}`;
    messagesDiv.appendChild(div);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

function loadChatHistory(username) {

    $('#chatMessages').empty();

    $.get('/chat/history/' + username, function (messages) {

        const groupedMessages = groupByDate(messages);

        Object.keys(groupedMessages).forEach(date => {

            // Date separator
            $('#chatMessages').append(`
                <div class="chat-date-separator">
                    <span>${formatDateLabel(date)}</span>
                </div>
            `);

            // Messages of that date
            groupedMessages[date].forEach(displayMessage);
        });
    });
}
function groupByDate(messages) {
    return messages.reduce((group, msg) => {
        const dateTime = msg.createdDate || msg.created_at || msg.createdAt;
        if (!dateTime) {
            console.warn("Message missing created date:", msg);
            return group;
        }

        const date = dateTime.split('T')[0];

        if (!group[date]) {
            group[date] = [];
        }

        group[date].push(msg);
        return group;

    }, {});
}

function formatDateLabel(dateString) {
    const today = new Date();
    const msgDate = new Date(dateString);

    const diffDays = Math.floor(
        (today.setHours(0,0,0,0) - msgDate.setHours(0,0,0,0)) / (1000 * 60 * 60 * 24)
    );

    if (diffDays === 0) return "Today";
    if (diffDays === 1) return "Yesterday";

    return msgDate.toLocaleDateString();
}
