package com.example.publicationservice.repository;

import com.example.publicationservice.model.Publication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends MongoRepository<Publication,String> {
    List<Publication> findAllByUserId(int userId);
}
