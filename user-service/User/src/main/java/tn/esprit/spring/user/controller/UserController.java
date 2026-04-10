package tn.esprit.spring.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.user.dto.CreateUserRequest;
import tn.esprit.spring.user.dto.UpdateUserRequest;
import tn.esprit.spring.user.dto.UserResponse;
import tn.esprit.spring.user.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    // ✅ MANUAL CONSTRUCTOR (fixes your error instantly)
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        UserResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/users/" + created.id())).body(created);
    }

    @GetMapping
    public List<UserResponse> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id,
                               @Valid @RequestBody UpdateUserRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    public UserResponse enable(@PathVariable Long id) {
        return service.setEnabled(id, true);
    }

    @PatchMapping("/{id}/disable")
    public UserResponse disable(@PathVariable Long id) {
        return service.setEnabled(id, false);
    }
}