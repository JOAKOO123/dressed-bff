package cl.dressed.bff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final WebClient backendClient;

    @PostMapping
    public ResponseEntity contact(@RequestBody Map body) {
        try {
            backendClient.post()
                .uri("/api/contact")
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .block();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}