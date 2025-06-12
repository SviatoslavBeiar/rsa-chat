package org.example.rsachat.controller;


import org.example.rsachat.model.Chat;
import org.example.rsachat.model.Message;
import org.example.rsachat.model.User;
import org.example.rsachat.service.ChatService;
import org.example.rsachat.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.*;

@Controller
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;

    public ChatController(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }


    @GetMapping("/chats")
    public String chats(@AuthenticationPrincipal UserDetails ud, Model m) {
        User me = userService.findByUsername(ud.getUsername()).get();
        m.addAttribute("chats", chatService.listChats(me));
        return "chats";
    }


    @PostMapping("/chats")
    public String newChat(@AuthenticationPrincipal UserDetails ud,
                          @RequestParam String otherUsername) throws GeneralSecurityException {
        User me = userService.findByUsername(ud.getUsername()).get();
        User other = userService.findByUsername(otherUsername)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        chatService.createChat(me, other);
        return "redirect:/chats";
    }


    @GetMapping("/chats/{chatId}")
    public String chat(@PathVariable Long chatId,
                       @AuthenticationPrincipal UserDetails ud,
                       Model m) throws GeneralSecurityException {
        Chat chat = chatService.listChats(
            userService.findByUsername(ud.getUsername()).get()
        ).stream().filter(c -> c.getId().equals(chatId))
         .findFirst().orElseThrow();
        List<Message> raw = chatService.listMessages(chatId);
        List<String> decrypted = new ArrayList<>();
        for (Message msg : raw) {
            decrypted.add(chatService.decryptMessage(msg));
        }
        m.addAttribute("chat", chat);
        m.addAttribute("users", Arrays.asList(chat.getUserA(), chat.getUserB()));
        m.addAttribute("msgsRaw", raw);
        m.addAttribute("msgsDec", decrypted);
        return "chat";
    }


    @PostMapping("/chats/{chatId}/message")
    public String send(@PathVariable Long chatId,
                       @AuthenticationPrincipal UserDetails ud,
                       @RequestParam String text) throws GeneralSecurityException {
        User me = userService.findByUsername(ud.getUsername()).get();
        Chat chat = chatService.listChats(me).stream()
                    .filter(c -> c.getId().equals(chatId))
                    .findFirst().orElseThrow();
        chatService.sendMessage(chat, me, text);
        return "redirect:/chats/" + chatId;
    }


    @PostMapping("/deleteAccount")
    public String deleteAcc(@AuthenticationPrincipal UserDetails ud) {
        User u = userService.findByUsername(ud.getUsername()).get();

        return "redirect:/logout";
    }


    @PostMapping("/chats/{chatId}/regenerateKey")
    public String regenKey(@PathVariable Long chatId,
                           @AuthenticationPrincipal UserDetails ud) throws GeneralSecurityException {
        User me = userService.findByUsername(ud.getUsername()).get();
        Chat chat = chatService.listChats(me).stream()
                    .filter(c -> c.getId().equals(chatId))
                    .findFirst().orElseThrow();

        return "redirect:/chats/" + chatId;
    }
}
