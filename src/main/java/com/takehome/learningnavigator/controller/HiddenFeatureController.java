package com.takehome.learningnavigator.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/hidden-feature")
public class HiddenFeatureController {

    private final RestTemplate restTemplate;

    public HiddenFeatureController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{number}")
    public ResponseEntity<String> getNumberFact(@PathVariable int number) {
        // Define the URL to fetch data from the Numbers API
        String url = "http://numbersapi.com/" + number;

        // Make a GET request to the Numbers API
        String response = restTemplate.getForObject(url, String.class);

        // Return the fact as the response
        return ResponseEntity.ok(response);
    }
}

