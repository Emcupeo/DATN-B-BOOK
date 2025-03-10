package org.example.datnbbook.controller;

import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/chat-lieu")
public class ChatLieuController {

    @Autowired
    ChatLieuService chatLieuService;

    @GetMapping
    public List<ChatLieu> getAlChatLieu() {
        return chatLieuService.getAll();
    }



}
