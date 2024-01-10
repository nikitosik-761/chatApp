package com.chatApp.chatApp.dto;

import com.chatApp.chatApp.models.Comment;
import com.chatApp.chatApp.models.User;
import com.chatApp.chatApp.views.Views;
import com.fasterxml.jackson.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public record MessageDTO(
        @JsonView(Views.Id.class)
        Long id,
        @JsonView(Views.IdName.class)
        String text,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonView(Views.FullMessage.class)
        LocalDateTime creationDate,

        @JsonView(Views.FullMessage.class)
        String link,
        @JsonView(Views.FullMessage.class)
        String linkTitle,
        @JsonView(Views.FullMessage.class)
        String linkDescription,
        @JsonView(Views.FullMessage.class)
        String linkCover,
        @JsonView(Views.FullMessage.class)
        User author,
        @JsonView(Views.FullMessage.class)
        List<Comment> comments
){

}
