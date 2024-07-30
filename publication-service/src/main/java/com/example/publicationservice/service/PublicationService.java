package com.example.publicationservice.service;

import com.example.publicationservice.client.SecurityServiceClient;
import com.example.publicationservice.dto.RabbitDto;
import com.example.publicationservice.exception.PublicationNotFoundException;
import com.example.publicationservice.exception.UnableToUpdateException;
import com.example.publicationservice.model.Comment;
import com.example.publicationservice.model.Publication;
import com.example.publicationservice.repository.PublicationRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Service
public class PublicationService {
    private final PublicationRepository publicationRepository;
    private final SecurityServiceClient securityServiceClient;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public PublicationService(PublicationRepository publicationRepository, SecurityServiceClient securityServiceClient, AmqpTemplate amqpTemplate) {
        this.publicationRepository = publicationRepository;
        this.securityServiceClient = securityServiceClient;
        this.amqpTemplate = amqpTemplate;
    }

    @Cacheable(value = "Publication",
            key = "#id",
            condition = "#publicationService.getLikesById(id) > 5000"
                )
    public Optional<Publication> findById(String id) {
        return publicationRepository.findById(id);
    }

    public List<Publication> findAll(){
        return publicationRepository.findAll();
    }

    public List<Publication> findAllByUserId(int userId){
        return publicationRepository.findAllByUserId(userId);
    }

    @Transactional
    public String create(Publication publication, String authHeader){
        publication.setUserId(Integer.parseInt(securityServiceClient.getIdByToken(authHeader)));
        publication.setCreatedAt(LocalDateTime.now());
        publicationRepository.save(publication);

        List<String> emails = securityServiceClient.getSubscribersByToken(authHeader);
        for (String email : emails) {
            RabbitDto rabbitDto = new RabbitDto(email, securityServiceClient.getUsernameByToken(authHeader) + " uploaded new publication with id="+publication.getId());
            amqpTemplate.convertAndSend("publications_exchange","publications_routing", rabbitDto);
        }

        return"Publication has been created";
    }

    @Transactional
    @CachePut(value = "Publication", key = "#id")
    public Publication update(String id, Publication updPublication, String authHeader) {
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        if (publication.getUserId() != Integer.parseInt(securityServiceClient.getIdByToken(authHeader)))
            throw new UnableToUpdateException("You can't change this publication");
        publication.setText(updPublication.getText());
        //TODO blob update
        publicationRepository.save(publication);
        return publication;
    }

    @Transactional
    @CacheEvict(value = "Publication", key = "#id")
    public String delete(String id, String authHeader){
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        if (publication.getUserId() != Integer.parseInt(securityServiceClient.getIdByToken(authHeader)))
            return "You can't delete this publication";
        publicationRepository.delete(publication);
        return "Publication has been deleted";
    }

    @Transactional
    @CachePut(value = "Publication", key = "#id")
    public String like(String id, String authHeader) {
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        Set<Integer> likes = publication.getLikes().orElse(new HashSet<>());

        int userId = Integer.parseInt(securityServiceClient.getIdByToken(authHeader));

        if (likes.contains(userId)) {
            likes.remove(userId);
            publication.setLikes(likes);
            publicationRepository.save(publication);
            return "Like has been removed";
        }
        likes.add(userId);
        publication.setLikes(likes);
        publicationRepository.save(publication);
        return "Like has been added";

    }

    @Transactional
    @CachePut(value = "Publication", key = "#id")
    public String comment(String id, Comment comment, String authHeader) {
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        List<Comment> comments = publication.getComments().orElse(new ArrayList<>());

        comment.setUsername(securityServiceClient.getUsernameByToken(authHeader));
        comments.add(comment);

        publication.setComments(comments);
        publicationRepository.save(publication);
        return "comment has been created";
    }

    public int getLikesById(String id){
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        return publication.getLikes().orElse(new HashSet<>()).size();
    }
}
