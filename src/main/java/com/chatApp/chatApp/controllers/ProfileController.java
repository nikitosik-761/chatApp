package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.models.User;
import com.chatApp.chatApp.models.UserSubscription;
import com.chatApp.chatApp.services.ProfileService;
import com.chatApp.chatApp.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{id}")
    @JsonView(Views.FullProfile.class)
    public User get(
            @PathVariable("id") String id
    ){
        return profileService.getProfile(id);
    }

    @PostMapping("/change-subscription/{channelId}")
    @JsonView(Views.FullProfile.class)
    public User changeSubscription(
            @PathVariable("channelId") String channelId
    ){
        return profileService.changeSubscription(channelId);
    }

    @GetMapping("/get-subscribers/{channelId}")
    @JsonView({Views.IdName.class})
    public List<UserSubscription> subscribers(
            @PathVariable("channelId") User channel
    )
    {
        return profileService.getSubscribers(channel);
    }

    @PostMapping("/change-status/{subscriberId}")
    @JsonView(Views.IdName.class)
    public UserSubscription changeSubscriptionStatus(
            @PathVariable("subscriberId") User subscriber
    ){
        return profileService.changeSubscriptionStatus(subscriber);
    }
}
