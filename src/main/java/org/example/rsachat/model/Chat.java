package org.example.rsachat.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "chats")
public class Chat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Двоє учасників
    @ManyToOne @JoinColumn(name = "user_a_id", nullable = false)
    private User userA;

    @ManyToOne @JoinColumn(name = "user_b_id", nullable = false)
    private User userB;

    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private RsaKeyPair keyPair;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sentAt ASC")
    private List<Message> messages = new ArrayList<>();

    public Chat(List<Message> messages, User userA, RsaKeyPair keyPair, User userB, Long id) {
        this.messages = messages;
        this.userA = userA;
        this.keyPair = keyPair;
        this.userB = userB;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public RsaKeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(RsaKeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public Chat() {
    }
// геттер/сеттер...
}
