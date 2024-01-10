package com.chatApp.chatApp.dto.messageMapper;

import com.chatApp.chatApp.dto.MessageDTO;
import com.chatApp.chatApp.models.Message;
import org.springframework.stereotype.Component;


import java.util.function.Function;


@Component
public class MessageDTOMapper implements Function<Message, MessageDTO> {
    @Override
    public MessageDTO apply(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getText(),
                message.getCreationDate(),
                message.getLink(),
                message.getLinkTitle(),
                message.getLinkDescription(),
                message.getLinkCover(),
                message.getAuthor(),
                message.getComments()
        );
    }
}
