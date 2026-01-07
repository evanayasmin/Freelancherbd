package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.dto.ChatMessageRequest;
import com.evanadev.freelancherbd.dto.ChatMessageResponse;
import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.ChatMessageEntity;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.ChatMessageRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.UserService;
import com.evanadev.freelancherbd.util.AESUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;


    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatMessageRepository chatMessageRepository, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
    }

    @GetMapping("/chat")
    public String chat_open(HttpServletRequest request, Model model){
        model.addAttribute("currentPath", request.getRequestURI());
        return "chat_page";
    }


//    @MessageMapping("/chat.send")
//    public void sendPrivateMessage(ChatMessageRequest request, Principal principal) {
//
//        String senderUsername = principal.getName();
//        String receiverUsername = request.getReceiver(); // FIXED
//
//        if (receiverUsername == null) return;
//
//        User sender = userService.findByUsername(senderUsername);
//        User receiver = userService.findByUsername(receiverUsername);
//
//        if (receiver == null) return;
//
//        ChatMessageEntity entity = new ChatMessageEntity();
//        entity.setSenderUsername(senderUsername);
//        entity.setSenderId(sender.getId());
//        entity.setReceiverId(receiver.getId());
//        entity.setReceiverUsername(receiverUsername);
//        entity.setContent(request.getContent());
//
//        chatMessageRepository.save(entity);
//
//        ChatMessageResponse response = toResponse(entity);
//
//        messagingTemplate.convertAndSendToUser(
//                receiverUsername,
//                "/queue/messages",
//                response
//        );
//
//        messagingTemplate.convertAndSendToUser(
//                senderUsername,
//                "/queue/messages",
//                response
//        );
//
//        System.out.println("FROM " + senderUsername + " TO " + receiverUsername);
//    }

    @MessageMapping("/chat.send")
    public void sendPrivateMessage(@Payload ChatMessageRequest request,
                                   Principal principal) {

        // 1 Safety check
        if (principal == null) return;

        String senderUsername = principal.getName();
        String receiverUsername = request.getReceiver();

        if (receiverUsername == null || receiverUsername.isBlank()) return;

        // 2 Load users safely
        User sender = userService.findByUsername(senderUsername);
        User receiver = userService.findByUsername(receiverUsername);

        if (sender == null || receiver == null) return;

        // 3 Persist message
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSenderId(sender.getId());
        entity.setSenderUsername(senderUsername);
        entity.setReceiverId(receiver.getId());
        entity.setReceiverUsername(receiverUsername);
        entity.setContent(request.getContent());

        chatMessageRepository.save(entity);

        // 4 Prepare response
        ChatMessageResponse response = new ChatMessageResponse(
                senderUsername,
                receiverUsername,
                entity.getContent(),
                entity.getCreatedAt()   // optional if you have it
        );

        // 5 Send to RECEIVER (MAIN delivery)
        messagingTemplate.convertAndSendToUser(
                receiverUsername,
                "/queue/messages",
                response
        );

        // 6 Send to SENDER (echo back)
        messagingTemplate.convertAndSendToUser(
                senderUsername,
                "/queue/messages",
                response
        );

        System.out.println("CHAT SENT: " + senderUsername + " → " + receiverUsername);
    }


    @GetMapping("/chat/history/{username}")
    @ResponseBody
    public List<ChatMessageResponse> loadChatHistory(@PathVariable String username, Principal principal) {

        String me = principal.getName();

        return chatMessageRepository
                .findConversation(me, username)
                .stream()
                .map(m -> new ChatMessageResponse(
                        m.getSenderUsername(),
                        m.getReceiverUsername(),
                        m.getContent(),
                        m.getCreatedAt()
                ))
                .toList();
    }

    // Helper mapper
    private ChatMessageResponse toResponse(ChatMessageEntity entity) {

        ChatMessageResponse dto = new ChatMessageResponse();
        dto.setSender(entity.getSenderUsername());
        dto.setContent(entity.getContent());
        //dto.setTimestamp(entity.getCreatedAt().toString());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // Resolve username safely
    private String getUsernameById(Long userId) {
        // fetch from UserRepository
        User user = userService.findUserDetailById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getUsername();
    }
}

