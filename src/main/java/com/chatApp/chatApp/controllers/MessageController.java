package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.MessageCreationRequest;
import com.chatApp.chatApp.dto.MessageDTO;
import com.chatApp.chatApp.dto.MessagePageDTO;
import com.chatApp.chatApp.services.MessageService;
import com.chatApp.chatApp.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/message")
@Slf4j
@AllArgsConstructor
public class MessageController {


    private final MessageService messageService;
    public static final int MESSAGES_PER_PAGE = 3;


    @GetMapping
    @JsonView(Views.FullMessage.class)
    public MessagePageDTO list(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
            ){
        return messageService.findForUser(pageable);
    }

    @GetMapping("/{id}")
    @JsonView(Views.FullMessage.class)
    public MessageDTO getOne(@PathVariable("id") Long id){
        return messageService.findOne(id);
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public MessageDTO create(@RequestBody MessageCreationRequest message) throws IOException {
        return messageService.createMessage(message);
    }

    @PutMapping("/{id}")
    @JsonView(Views.FullMessage.class)
    public MessageDTO update(
            @PathVariable("id") Long id,
            @RequestBody MessageCreationRequest message
    ) throws IOException {

       return messageService.updateMessage(id, message);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id){
        messageService.deleteMessage(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }



    /*
    @MessageMapping("/changeMessage")
    @SendTo("/topic/activity")
    public MessageDTO change(MessageCreationRequest messageDTO){
        return messageService.createMessage(messageDTO);
    }

     */

}
