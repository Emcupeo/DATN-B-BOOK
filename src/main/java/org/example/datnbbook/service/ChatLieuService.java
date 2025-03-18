package org.example.datnbbook.service;

import org.example.datnbbook.dto.ChatLieuDTO;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.repository.ChatLieuRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatLieuService {
    @Autowired
    private ChatLieuRepository chatLieuRepository;


    public List<ChatLieu> getAll() {
        return chatLieuRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public ChatLieu getById(Integer id) {
        return chatLieuRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Chất liệu không tồn tại với id: " + id));
    }

    public ChatLieu create(ChatLieu sp) {
        if (sp.getMaChatLieu() == null || sp.getMaChatLieu().isEmpty()) {
            String nextMaChatLieu = chatLieuRepository.getNextSequenceValue();
            sp.setMaChatLieu(nextMaChatLieu);
        }
        sp.setDeleted(false); // Đảm bảo Chất liệu mới không bị đánh dấu xóa
        return chatLieuRepository.save(sp);
    }

    public ChatLieuDTO update(Integer id, ChatLieuDTO chatLieuDTO) {
        ChatLieu chatLieu = chatLieuRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Chất liệu không tồn tại với id: " + id));

        BeanUtils.copyProperties(chatLieuDTO, chatLieu ,"maChatLieu","id", "deleted");
        chatLieu = chatLieuRepository.save(chatLieu);
        return convertToDTO(chatLieu);
    }

    public void delete(Integer id) {
        ChatLieu chatLieu = chatLieuRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Chất liệu không tồn tại với id: " + id));
        chatLieu.setDeleted(true);
        chatLieuRepository.save(chatLieu);
    }

    public List<ChatLieu> search(String keyword) {
        return chatLieuRepository.search(keyword);
    }

    private ChatLieuDTO convertToDTO(ChatLieu chatLieu) {
        ChatLieuDTO chatLieuDTO = new ChatLieuDTO();
        BeanUtils.copyProperties(chatLieu, chatLieuDTO);
        return chatLieuDTO;
    }
}
