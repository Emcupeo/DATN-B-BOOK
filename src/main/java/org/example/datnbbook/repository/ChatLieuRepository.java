package org.example.datnbbook.repository;

import org.example.datnbbook.model.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> {
}
