package tn.esprit.spring.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.user.Role;
import tn.esprit.spring.user.User;
import tn.esprit.spring.user.dto.CreateUserRequest;
import tn.esprit.spring.user.dto.UpdateUserRequest;
import tn.esprit.spring.user.dto.UserResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserResponse create(CreateUserRequest req) {

        if (repo.existsByEmail(req.email()))
            throw new RuntimeException("Email already used");

        if (repo.existsByUsername(req.username()))
            throw new RuntimeException("Username already used");

        Role role = (req.role() == null || req.role().isBlank())
                ? Role.USER
                : Role.valueOf(req.role().toUpperCase());

        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .role(role)
                .enabled(true)
                .build();

        user = repo.save(user);
        return mapToResponse(user);
    }

    public List<UserResponse> findAll() {
        return repo.findAll().stream().map(this::mapToResponse).toList();
    }

    public UserResponse findById(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    public UserResponse update(Long id, UpdateUserRequest req) {

        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.username() != null && !req.username().isBlank())
            user.setUsername(req.username());

        if (req.email() != null && !req.email().isBlank())
            user.setEmail(req.email());

        if (req.password() != null && !req.password().isBlank())
            user.setPasswordHash(encoder.encode(req.password()));

        if (req.role() != null && !req.role().isBlank())
            user.setRole(Role.valueOf(req.role().toUpperCase()));

        user = repo.save(user);
        return mapToResponse(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public UserResponse setEnabled(Long id, boolean enabled) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(enabled);
        user = repo.save(user);

        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.isEnabled()
        );
    }
}