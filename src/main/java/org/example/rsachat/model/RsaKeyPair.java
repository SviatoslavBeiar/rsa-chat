package org.example.rsachat.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rsa_key_pairs")
public class RsaKeyPair {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne @JoinColumn(name = "chat_id")
    private Chat chat;

    @Lob
    private String publicKeyBase64;

    @Lob
    private String privateKeyEncryptedBase64;

    @Lob
    private String aesGcmIvBase64;

    public RsaKeyPair(Long id, Chat chat, String publicKeyBase64, String privateKeyEncryptedBase64, String aesGcmIvBase64) {
        this.id = id;
        this.chat = chat;
        this.publicKeyBase64 = publicKeyBase64;
        this.privateKeyEncryptedBase64 = privateKeyEncryptedBase64;
        this.aesGcmIvBase64 = aesGcmIvBase64;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getPrivateKeyEncryptedBase64() {
        return privateKeyEncryptedBase64;
    }

    public void setPrivateKeyEncryptedBase64(String privateKeyEncryptedBase64) {
        this.privateKeyEncryptedBase64 = privateKeyEncryptedBase64;
    }

    public String getAesGcmIvBase64() {
        return aesGcmIvBase64;
    }

    public void setAesGcmIvBase64(String aesGcmIvBase64) {
        this.aesGcmIvBase64 = aesGcmIvBase64;
    }

    public RsaKeyPair() {
    }
// геттер/сеттер...
}
