package com.a2z.backend.controller;

import com.a2z.backend.entity.User;
import com.a2z.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User savedUser = userService.register(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user,
                                      HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                );

        Authentication authentication =
                authenticationManager.authenticate(authToken);

//        System.out.println("Authorities: " + authentication.getAuthorities());

        // Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Force creation of session
        var session = request.getSession(true);

        // Explicitly bind SecurityContext to the session
        session.setAttribute(
                org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        return ResponseEntity.ok(loggedInUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        // Get the current session (if any)
        var session = request.getSession(false);

        if (session != null) {
            // Invalidate session → removes SecurityContext + JSESSIONID
            session.invalidate();
        }

        // Also clear SecurityContext (thread-local)
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully");
    }


}
