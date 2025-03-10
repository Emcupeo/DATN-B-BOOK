package org.example.datnbbook.controller;

import org.example.datnbbook.dto.ChatLieuDTO;
import org.example.datnbbook.dto.SanPhamDTO;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/{id}")
    public ResponseEntity<ChatLieu> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(chatLieuService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ChatLieu> create(@RequestBody ChatLieu chatLieu) {
        return ResponseEntity.ok(chatLieuService.create(chatLieu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatLieuDTO> update(@PathVariable Integer id, @RequestBody ChatLieuDTO chatLieu) {
        return ResponseEntity.ok(chatLieuService.update(id, chatLieu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        chatLieuService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChatLieu>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(chatLieuService.search(keyword));
    }



}
