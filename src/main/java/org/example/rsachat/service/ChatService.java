package org.example.rsachat.service;


import org.example.rsachat.model.Chat;
import org.example.rsachat.model.RsaKeyPair;
import org.example.rsachat.model.User;
import org.example.rsachat.repo.ChatRepository;
import org.example.rsachat.model.Message;
import org.example.rsachat.repo.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepo;
    private final MessageRepository msgRepo;
    private final CryptoService crypto;

    public ChatService(ChatRepository chatRepo, MessageRepository msgRepo, CryptoService crypto) {
        this.chatRepo = chatRepo;
        this.msgRepo = msgRepo;
        this.crypto = crypto;
    }

    @Transactional
    public Chat createChat(User a, User b) throws GeneralSecurityException {
        Chat chat = new Chat();
        chat.setUserA(a);
        chat.setUserB(b);


        KeyPair kp = crypto.generateRsaKeyPair();
        RsaKeyPair pair = new RsaKeyPair();
        pair.setChat(chat);
        pair.setPublicKeyBase64(
            java.util.Base64.getEncoder().encodeToString(kp.getPublic().getEncoded())
        );
        pair.setPrivateKeyEncryptedBase64(
            crypto.encryptPrivateKey(kp.getPrivate())
        );
        chat.setKeyPair(pair);

        return chatRepo.save(chat);
    }

    public List<Chat> listChats(User u) {
        return chatRepo.findByUserAOrUserB(u, u);
    }

    public List<Message> listMessages(Long chatId) {
        return msgRepo.findByChatIdOrderBySentAtAsc(chatId);
    }

    @Transactional
    public Message sendMessage(Chat chat, User sender, String plainText) throws GeneralSecurityException {

        String enc = crypto.encryptWithPublicKey(
            plainText,
            chat.getKeyPair().getPublicKeyBase64()
        );
        Message m = new Message();
        m.setChat(chat);
        m.setSender(sender);
        m.setEncryptedContentBase64(enc);
        m.setSentAt(LocalDateTime.now());
        return msgRepo.save(m);
    }

    public String decryptMessage(Message m) throws GeneralSecurityException {
        PrivateKey priv = crypto.decryptPrivateKey(
            m.getChat().getKeyPair().getPrivateKeyEncryptedBase64()
        );
        return crypto.decryptWithPrivateKey(m.getEncryptedContentBase64(), priv);
    }
}
