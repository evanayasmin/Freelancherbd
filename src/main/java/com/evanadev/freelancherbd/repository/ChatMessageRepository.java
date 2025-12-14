package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    @Query("""
        SELECT c FROM ChatMessageEntity c
        WHERE (c.senderId = :user1 AND c.receiverId = :user2)
           OR (c.senderId = :user2 AND c.receiverId = :user1)
        ORDER BY c.createdAt ASC
    """)
    List<ChatMessageEntity> findConversation(
            @Param("user1") Long user1,
            @Param("user2") Long user2
    );

    @Query("""
       SELECT m FROM ChatMessageEntity m
       WHERE (m.senderId = :user1 AND m.receiverId = :user2)
          OR (m.senderId = :user2 AND m.receiverId = :user1)
       ORDER BY m.createdAt ASC
    """)
    List<ChatMessageEntity> findChatHistory(
                    Long user1,
                    Long user2
            );


}
