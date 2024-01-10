package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.EventType;
import com.chatApp.chatApp.dto.ObjectType;
import com.chatApp.chatApp.exceptions.NotFoundException;
import com.chatApp.chatApp.models.Comment;
import com.chatApp.chatApp.models.User;
import com.chatApp.chatApp.repositories.CommentRepo;
import com.chatApp.chatApp.repositories.UserDetailsRepo;
import com.chatApp.chatApp.utils.AuthHelper;
import com.chatApp.chatApp.utils.WsSender;
import com.chatApp.chatApp.views.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserDetailsRepo userDetailsRepo;
    private final BiConsumer<EventType, Comment> wsSender;

    @Autowired
    public CommentService(CommentRepo commentRepo, UserDetailsRepo userDetailsRepo, WsSender wsSender) {
        this.commentRepo = commentRepo;
        this.userDetailsRepo = userDetailsRepo;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
    }

    public Comment create(Comment comment){

        User user = userDetailsRepo.
                findById(AuthHelper.getAuthUserId()).orElseThrow(NotFoundException::new);

        comment.setUser(user);
        Comment commentFromDb = commentRepo.save(comment);

        wsSender.accept(EventType.CREATE, commentFromDb);

        return commentFromDb;
    }
}
