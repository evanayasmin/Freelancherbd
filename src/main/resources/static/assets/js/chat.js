    let stompClient = null;
    let currentReceiver = null;

    document.addEventListener('DOMContentLoaded', function() {
    const chatButtons = document.querySelectorAll('.chatBtn');
    chatButtons.forEach(btn => {
    btn.addEventListener('click', function(e) {
    e.preventDefault();
    const receiverId = btn.getAttribute('data-id');
    openChatPopup(receiverId);
});
});

    connectWebSocket();
});

    function connectWebSocket() {
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);

    stompClient.subscribe('/user/queue/messages', function(messageOutput) {
    const msg = JSON.parse(messageOutput.body);
    if (msg.sender === currentReceiver || msg.receiver === currentReceiver) {
    displayMessage(msg);
}
});
});
}

    function openChatPopup(receiverId) {
    currentReceiver = receiverId;

    // Avoid creating duplicate popup
    if(document.getElementById('chatPopup')) return;

    const container = document.getElementById('chatPopupContainer');

    const popup = document.createElement('div');
    popup.id = 'chatPopup';
    popup.classList.add('chat-popup');

    popup.innerHTML = `
        <div class="chat-header">Chat</div>
        <div class="chat-messages" id="chatMessages"></div>
        <div class="chat-input">
            <input type="text" id="chatInput" placeholder="Type a message...">
            <button id="chatSendBtn">Send</button>
        </div>
    `;

    container.appendChild(popup);

    const sendBtn = document.getElementById('chatSendBtn');
    sendBtn.addEventListener('click', sendMessage);
    const input = document.getElementById('chatInput');
    input.addEventListener('keypress', function(e) {
    if(e.key === 'Enter') sendMessage();
});
}

    function sendMessage() {
    const input = document.getElementById('chatInput');
    const content = input.value.trim();
    if (!content || !currentReceiver) return;

    const message = {
    sender: '', // backend uses session principal
    receiver: currentReceiver,
    content: content,
    timestamp: new Date().toISOString()
};

    stompClient.send('/app/chat.send', {}, JSON.stringify(message));
    displayMessage(message);
    input.value = '';
}

    function displayMessage(message) {
    const messagesDiv = document.getElementById('chatMessages');
    if(!messagesDiv) return;

    const msgDiv = document.createElement('div');
    msgDiv.textContent = `${message.sender}: ${message.content}`;
    messagesDiv.appendChild(msgDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

