package com.chatApp.chatApp.services;

import com.chatApp.chatApp.exceptions.NotFoundException;
import com.chatApp.chatApp.models.User;
import com.chatApp.chatApp.models.UserSubscription;
import com.chatApp.chatApp.repositories.UserDetailsRepo;
import com.chatApp.chatApp.repositories.UserSubscriptionRepo;
import com.chatApp.chatApp.utils.AuthHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileService {
    private final UserDetailsRepo userDetailsRepo;
    private final UserSubscriptionRepo userSubscriptionRepo;

    public User getProfile(String id){
        return userDetailsRepo.findById(id).orElseThrow(NotFoundException::new);
    }

    public User changeSubscription(String channelId){
        User subscriber = userDetailsRepo.
                findById(AuthHelper.getAuthUserId()).orElseThrow(NotFoundException::new);

        User channel = userDetailsRepo.findById(channelId).orElseThrow(NotFoundException::new);


        if (channel.equals(channel)){
            return channel;
        }else {
            List<UserSubscription> subscriptions = channel.getSubscribers()
                    .stream()
                    .filter(subscription -> subscription.getSubscriber().equals(subscriber))
                    .toList();

            if (subscriptions.isEmpty()){
                UserSubscription subscription = new UserSubscription(channel, subscriber);
                channel.getSubscribers().add(subscription);
            }else {
                channel.getSubscribers().removeAll(subscriptions);
            }
        }

        return userDetailsRepo.save(channel);

    }


    public List<UserSubscription> getSubscribers(User channel) {
        return userSubscriptionRepo.findByChannel(channel);
    }

    public UserSubscription changeSubscriptionStatus(User subscriber) {
        User channel = userDetailsRepo.
                findById(AuthHelper.getAuthUserId()).orElseThrow(NotFoundException::new);


        UserSubscription subscription = userSubscriptionRepo.
                findByChannelAndSubscriber(channel, subscriber);

        subscription.setActive(!subscription.isActive());

        return userSubscriptionRepo.save(subscription);
    }
}
