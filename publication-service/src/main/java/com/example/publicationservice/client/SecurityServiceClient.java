package com.example.publicationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("${security_url}")
public interface SecurityServiceClient {
    @GetMapping("/auth/id")
    String getIdByToken(@RequestParam("token") String token);


    @GetMapping("/auth/username")
    String getUsernameByToken(@RequestParam("token") String token);
}
