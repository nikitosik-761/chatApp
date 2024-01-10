package com.chatApp.chatApp.dto;

import com.chatApp.chatApp.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class MessagePageDTO {
    private List<MessageDTO> messages;
    private int currentPage;
    private int totalPages;
}
