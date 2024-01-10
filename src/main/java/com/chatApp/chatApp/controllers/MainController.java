package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.MessagePageDTO;
import com.chatApp.chatApp.models.User;
import com.chatApp.chatApp.repositories.UserDetailsRepo;
import com.chatApp.chatApp.services.MessageService;
import com.chatApp.chatApp.utils.AuthHelper;
import com.chatApp.chatApp.views.Views;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
@Slf4j
public class MainController {

    private final MessageService messageService;
    private final UserDetailsRepo userDetailsRepo;
    private final ObjectWriter messageWriter;
    private final ObjectWriter profileWriter;

    @Autowired
    public MainController(ObjectMapper mapper, MessageService messageService, UserDetailsRepo userDetailsRepo) {
        this.messageService = messageService;
        this.userDetailsRepo = userDetailsRepo;

        ObjectMapper objectMapper =
                mapper.setConfig(mapper.getSerializationConfig());

        this.messageWriter = objectMapper
                .writerWithView(Views.FullMessage.class);
        this.profileWriter = objectMapper
                .writerWithView(Views.FullProfile.class);
    }

    //crt alt v
    @GetMapping
    public String main(Model model) throws JsonProcessingException {

        User user = userDetailsRepo.findById(AuthHelper.getAuthUserId()).orElse(null);
        HashMap<Object, Object> data = new HashMap<>();

        if (user != null){
            String serializedProfile = profileWriter.writeValueAsString(user);
            model.addAttribute("profile", serializedProfile);

            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0,MessageController.MESSAGES_PER_PAGE, sort);
            MessagePageDTO messagePageDTO = messageService.findForUser(pageRequest);

            String messages = messageWriter.writeValueAsString(messagePageDTO.getMessages());

            model.addAttribute("messages", messages);
            data.put("currentPage", messagePageDTO.getCurrentPage());
            data.put("totalPages", messagePageDTO.getTotalPages());
        }else {
            model.addAttribute("messages", "[]");
            model.addAttribute("profile", "null");
        }


        model.addAttribute("frontendData", data);

        return "index";
    }

}
