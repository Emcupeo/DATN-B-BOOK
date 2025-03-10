package org.example.datnbbook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "chat_lieu")
public class ChatLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'CL'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[CLSeq]), 5)")
    @Column(name = "ma_chat_lieu", length = 10, nullable = false)
    private String maChatLieu;

    @Nationalized
    @Column(name = "ten_chat_lieu", length = 100)
    private String tenChatLieu;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;
//
//    @OneToMany(mappedBy = "idChatLieu")
//    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();

}