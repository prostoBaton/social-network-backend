package com.example.publicationservice.controller;

import com.example.publicationservice.exception.PublicationNotFoundException;
import com.example.publicationservice.model.Comment;
import com.example.publicationservice.model.Publication;
import com.example.publicationservice.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publication")
public class PublicationController {
    private final PublicationService publicationService;

    @Autowired
    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping
    public Page<Publication> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return publicationService.findAll(page);
    }

    @GetMapping("/{id}")
    public Publication findById(@PathVariable String id){
        return publicationService.findById(id).orElseThrow(() -> new PublicationNotFoundException("Publication not found"));
    }

    @PatchMapping("/{id}/like")
    public String pressLike(@PathVariable String id,
                            @RequestHeader("Authorization") String authHeader){
        return publicationService.like(id, authHeader);
    }

    @PatchMapping("/{id}/comment")
    public String comment(@PathVariable String id,
                          @RequestBody Comment comment,
                          @RequestHeader("Authorization") String authHeader){ //TODO edit/delete comments
        return publicationService.comment(id, comment, authHeader);
    }

    @GetMapping("/user/{userId}")
    public List<Publication> findAllByUserId(@PathVariable int userId){
        return publicationService.findAllByUserId(userId);
    }

    @PostMapping("/new")
    public String newPublication(@RequestBody Publication publication,
                                 @RequestHeader("Authorization") String authHeader){
        return publicationService.create(publication,authHeader);
    }

    @PatchMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @RequestBody Publication publication,
                         @RequestHeader("Authorization") String authHeader){
         publicationService.update(id,publication,authHeader);
        return "Publication has been updated";
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable String id,
                         @RequestHeader("Authorization") String authHeader){
        return publicationService.delete(id,authHeader);
    }
}
