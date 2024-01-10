package com.chatApp.chatApp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Message")
@Data
@ToString(of = {"id", "text"})
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "date", updatable = false)
    private LocalDateTime creationDate;

    @Column
    private String link;

    @Column
    private String linkTitle;

    @Column
    private String linkDescription;

    @Column
    private String linkCover;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;

    @OneToMany(mappedBy = "message", orphanRemoval = true)
    private List<Comment> comments;



}
