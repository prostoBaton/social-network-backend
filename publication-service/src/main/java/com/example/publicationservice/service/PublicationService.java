package com.example.publicationservice.service;

import com.example.publicationservice.client.SecurityServiceClient;
import com.example.publicationservice.exception.PublicationNotFoundException;
import com.example.publicationservice.model.Comment;
import com.example.publicationservice.model.Publication;
import com.example.publicationservice.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class PublicationService {
    private final PublicationRepository publicationRepository;
    private final SecurityServiceClient securityServiceClient;

    @Autowired
    public PublicationService(PublicationRepository publicationRepository, SecurityServiceClient securityServiceClient) {
        this.publicationRepository = publicationRepository;
        this.securityServiceClient = securityServiceClient;
    }

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
        publicationRepository.save(publication);
        return"Publication has been created";
    }

    @Transactional
    public String update(String id, Publication updPublication, String authHeader) {
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        if (publication.getUserId() != Integer.parseInt(securityServiceClient.getIdByToken(authHeader)))
            return "You can't change this publication";
        publication.setText(updPublication.getText());
        //TODO blob update
        publicationRepository.save(publication);
        return "Publication has been updated";
    }

    @Transactional
    public String delete(String id, String authHeader){
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        if (publication.getUserId() != Integer.parseInt(securityServiceClient.getIdByToken(authHeader)))
            return "You can't delete this publication";
        publicationRepository.delete(publication);
        return "Publication has been deleted";
    }

    @Transactional
    public String like(String id, String authHeader) {
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        Set<Integer> likes = publication.getLikes();
        int userId = Integer.parseInt(securityServiceClient.getIdByToken(authHeader));

        if (likes.contains(userId)) {
            likes.remove(userId);
            publicationRepository.save(publication);
            return "Like has been removed";
        }
        likes.add(userId);
        publicationRepository.save(publication);
        return "Like has been added";

    }

    @Transactional
    public String comment(String id, Comment comment, String authHeader) {
        Publication publication = findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
        List<Comment> comments = publication.getComments();

        comment.setUsername(securityServiceClient.getUsernameByToken(authHeader));
        comments.add(comment);

        publication.setComments(comments);
        publicationRepository.save(publication);
        return "comment has been created";
    }
}
