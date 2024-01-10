package com.chatApp.chatApp.dto;

import com.chatApp.chatApp.views.Views;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonView(Views.Id.class)
public class WsSenderDTO {
    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String body;
}
