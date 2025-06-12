package org.example.rsachat.model;

import jakarta.persistence.*;

import java.time.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne @JoinColumn(name = "sender_id")
    private User sender;

    @Lob
    private String encryptedContentBase64;

    private LocalDateTime sentAt;

    public Message(Long id, User sender, Chat chat, String encryptedContentBase64, LocalDateTime sentAt) {
        this.id = id;
        this.sender = sender;
        this.chat = chat;
        this.encryptedContentBase64 = encryptedContentBase64;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getEncryptedContentBase64() {
        return encryptedContentBase64;
    }

    public void setEncryptedContentBase64(String encryptedContentBase64) {
        this.encryptedContentBase64 = encryptedContentBase64;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Message() {
    }
}
