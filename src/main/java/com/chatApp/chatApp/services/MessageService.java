package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.*;
import com.chatApp.chatApp.dto.messageMapper.MessageDTOMapper;
import com.chatApp.chatApp.exceptions.NotFoundException;
import com.chatApp.chatApp.models.Message;
import com.chatApp.chatApp.models.User;
import com.chatApp.chatApp.models.UserSubscription;
import com.chatApp.chatApp.repositories.MessageRepository;
import com.chatApp.chatApp.repositories.UserDetailsRepo;
import com.chatApp.chatApp.repositories.UserSubscriptionRepo;
import com.chatApp.chatApp.utils.AuthHelper;
import com.chatApp.chatApp.utils.WsSender;
import com.chatApp.chatApp.views.Views;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";
    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMAGE_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepository messageRepository;
    private final MessageDTOMapper messageDTOMapper;
    private final UserDetailsRepo userDetailsRepo;
    private final UserSubscriptionRepo userSubscriptionRepo;

    private final BiConsumer<EventType, MessageDTO> wsSender;


    @Autowired
    public MessageService(MessageRepository messageRepository, MessageDTOMapper messageDTOMapper, UserDetailsRepo userDetailsRepo, UserSubscriptionRepo userSubscriptionRepo, WsSender wsSender) {
        this.messageRepository = messageRepository;
        this.messageDTOMapper = messageDTOMapper;
        this.userDetailsRepo = userDetailsRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);
    }

    public MessagePageDTO findForUser(Pageable pageable){

        User user = userDetailsRepo.
                findById(AuthHelper.getAuthUserId()).orElseThrow(NotFoundException::new);

        List<User> channels = userSubscriptionRepo.findBySubscriber(user)
                .stream()
                .filter(UserSubscription::isActive)
                .map(UserSubscription::getChannel)
                .toList();

        channels.add(user);

        Page<Message> page = messageRepository.findByAuthorIn(channels,pageable);

        List<MessageDTO> messageDTOS = page.getContent().stream().map(messageDTOMapper).collect(Collectors.toList());

        return new MessagePageDTO(
                messageDTOS,
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    public MessageDTO findOne(Long id){
        return messageRepository.findById(id)
                .map(messageDTOMapper)
                .orElseThrow(NotFoundException::new);
    }

    public MessageDTO createMessage(MessageCreationRequest messageRequest) throws IOException {


        User user = userDetailsRepo.
                findById(AuthHelper.getAuthUserId()).orElseThrow(NotFoundException::new);


        Message message = new Message();



        message.setText(messageRequest.text());

        message.setCreationDate(LocalDateTime.now());

        fillMeta(message);

        message.setAuthor(user);

        messageRepository.save(message);

        MessageDTO messageDTO = messageDTOMapper.apply(message);

        wsSender.accept(EventType.CREATE, messageDTO);

        return messageDTO;
    }

    public MessageDTO updateMessage(Long id, MessageCreationRequest messageRequest) throws IOException {
        Message messageFromDb =
                messageRepository
                        .findById(id)
                        .orElseThrow(
                                NotFoundException::new
                        );



        //BeanUtils.copyProperties(message, messageFromDb,"id");

        messageFromDb.setText(messageRequest.text());

        fillMeta(messageFromDb);

        messageRepository.save(messageFromDb);

        MessageDTO messageDTO = messageDTOMapper.apply(messageFromDb);

        wsSender.accept(EventType.UPDATE, messageDTO);

        return messageDTO;
    }

    public void deleteMessage(Long id){
        Message message = messageRepository.findById(id).orElseThrow(NotFoundException::new);
        wsSender.accept(EventType.REMOVE, messageDTOMapper.apply(message));
        messageRepository.deleteById(id);
    }

    private void fillMeta(Message message) throws IOException {


        String text = message.getText();


        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()){
            String url = text.substring(matcher.start(),matcher.end());

            matcher = IMAGE_REGEX.matcher(url);

            message.setLink(url);

            if (matcher.find()){
                message.setLinkCover(url);
            } else if (!url.contains("youtu")) {

                MetaDTO meta = getMeta(url);


                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
            }
        }
    }

    private MetaDTO getMeta(String url) throws IOException{
        Document doc = Jsoup.connect(url).get();

        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDTO(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element){
        return element == null ? "" : element.attr("content");
    }

}
