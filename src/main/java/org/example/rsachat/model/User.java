package org.example.rsachat.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    @OneToMany(mappedBy = "userA", cascade = CascadeType.ALL)
    private List<Chat> chatsAsA = new ArrayList<>();

    @OneToMany(mappedBy = "userB", cascade = CascadeType.ALL)
    private List<Chat> chatsAsB = new ArrayList<>();

    public User(Long id, List<Chat> chatsAsB, List<Chat> chatsAsA, String password, String username) {
        this.id = id;
        this.chatsAsB = chatsAsB;
        this.chatsAsA = chatsAsA;
        this.password = password;
        this.username = username;
    }

    public User() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Chat> getChatsAsB() {
        return chatsAsB;
    }

    public void setChatsAsB(List<Chat> chatsAsB) {
        this.chatsAsB = chatsAsB;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Chat> getChatsAsA() {
        return chatsAsA;
    }

    public void setChatsAsA(List<Chat> chatsAsA) {
        this.chatsAsA = chatsAsA;
    }
}
